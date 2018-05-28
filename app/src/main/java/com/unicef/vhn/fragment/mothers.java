package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.MotherListRealm;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;


//import com.unicef.vhn.adapter.MoviesAdapter;


/**
 * Created by priyan on 2/3/2018.
 */

public class mothers extends Fragment implements MotherListsViews, MakeCallInterface {

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    //
//     List<MotherListAdapterRealmModel> mRealmResult ;
//    MotherListAdapterRealmModel motherListAdapterRealmModel;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;

//    private MotherListAdapter_RM mAdapter_rm;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherListRealm dashBoardRealmModel;

    //    CardView mother_card;
    public static mothers newInstance() {
        mothers fragment = new mothers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mothers, container, false);

        realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"
        getActivity().setTitle("Mothers List");
        initUI(view);
        return view;


    }

    private void initUI(View view) {
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        pnMotherListPresenter = new MotherListPresenter(getActivity(), this);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }

        mResult = new ArrayList<>();
//        mRealmResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) view.findViewById(R.id.mother_recycler_view);
        mAdapter = new MotherListAdapter(mResult, getActivity(), "", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

        if (isoffline) {
            showOfflineData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }




    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {


        Log.e(mothers.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject res_mJsnobject = new JSONObject(response);
            String status = res_mJsnobject.getString("status");
            String message = res_mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = res_mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<MotherListRealm> motherListAdapterRealmModel = realm.where(MotherListRealm.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MotherListRealm.class);
                    }
                });
//                realm.commitTransaction();
              /*  if (DashBoardRealmResult.size() != 0) {

//
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(DashBoardRealmModel.class);
                        }
                    });
                } else {
                    Log.e("Realm size  ---->", DashBoardRealmResult.size() + "");
                }*/
                Log.e("After Realm size  ---->", motherListAdapterRealmModel.size() + "");

                //create new realm Table
                realm.beginTransaction();       //create or open


                for (int i = 0; i < jsonArray.length(); i++) {
                     dashBoardRealmModel = realm.createObject(MotherListRealm.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                    JSONObject mJsnobject = jsonArray.getJSONObject(i);
            /*        Log.e("position", i+"");
                    Log.e("mName", mJsnobject.getString("mName"));
                    Log.e("PICMEID", mJsnobject.getString("mPicmeId"));
                    Log.e("PICMEID", mJsnobject.getString("mid"));
                    Log.e("mMotherMobile", mJsnobject.getString("mMotherMobile"));
                    Log.e("vhnId", mJsnobject.getString("vhnId"));*/

                    dashBoardRealmModel.setMName(mJsnobject.getString("mName"));
                    dashBoardRealmModel.setMPicmeId(mJsnobject.getString("mPicmeId"));
                    dashBoardRealmModel.setMid(mJsnobject.getString("mid"));
                    dashBoardRealmModel.setmMotherMobile(mJsnobject.getString("mMotherMobile"));
                    dashBoardRealmModel.setVhnId(mJsnobject.getString("vhnId"));
                    dashBoardRealmModel.setMLatitude(mJsnobject.getString("mLatitude"));
                    dashBoardRealmModel.setMLongitude(mJsnobject.getString("mLongitude"));
                    dashBoardRealmModel.setMotherType(mJsnobject.getString("motherType"));
                    dashBoardRealmModel.setmPhoto(mJsnobject.getString("mPhoto"));
                }

                realm.commitTransaction(); //close table
            } else {
                Log.e(mothers.class.getSimpleName(), message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();
    }

    private void setValueToUI() {
        Log.e("ON LINE ->",  "on line");

        realm.beginTransaction();
        RealmResults<MotherListRealm> userInfoRealmResult = realm.where(MotherListRealm.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherListRealm model = userInfoRealmResult.get(i);
           /* Log.e("Mother list size ->", model+ "");
            Log.e("getMid", model.getMid() + "");
            Log.e("getMName", model.getMName() + "");
            Log.e("getMPicmeId", model.getMPicmeId() + "");
            Log.e("getmMotherMobile", model.getmMotherMobile() + "");
            Log.e("getVhnId", model.getVhnId() + "");
            Log.e("getvLatitude", model.getvLatitude() + "");
            Log.e("getvLongitude", model.getvLongitude() + "");
            Log.e("getMotherType", model.getMotherType() + "");
            Log.e("getmPhoto", model.getmPhoto() + "");*/


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
    }

    private void showOfflineData() {
        Log.e("off ->",  "offline");

        realm.beginTransaction();
          RealmResults<MotherListRealm> realmResults = realm.where(MotherListRealm.class).findAll();
        Log.e("Mother list size ->", realmResults.size() + "");
        for (int i = 0; i < realmResults.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherListRealm model = realmResults.get(i);
            Log.e("off list size ->", model+ "");
            Log.e("offline  getMid", model.getMid() + "");
            Log.e("offlinegetMName", model.getMName() + "");
            Log.e("offlinegetMPicmeId", model.getMPicmeId() + "");
            Log.e("offlinegetmMotherMobile", model.getmMotherMobile() + "");
            Log.e("offlinegetVhnId", model.getVhnId() + "");
            Log.e("offlinegetvLatitude", model.getvLatitude() + "");
            Log.e("offlinegetvLongitude", model.getvLongitude() + "");
            Log.e("offlinegetMotherType", model.getMotherType() + "");
            Log.e("offlinegetmPhoto", model.getmPhoto() + "");


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
    }


    @Override
    public void showLoginError(String response) {
        Log.e(mothers.class.getSimpleName(), "Response success" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }


    @Override
    public void makeCall(String mMotherMobile) {
        isDataUpdate = false;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getActivity(), "Displaying Call permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getActivity(), "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
