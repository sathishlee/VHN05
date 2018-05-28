package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
//import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.MotherListRealm;
import com.unicef.vhn.realmDbModel.MotherRiskListRealm;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by priyan on 2/3/2018.
 */

public class risk extends Fragment implements MotherListsViews, MakeCallInterface {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;


    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherRiskListRealm motherRiskListRealm;

    public static risk newInstance() {
        risk fragment = new risk();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_risk, container, false);
        realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"
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

            pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_MOTHERS_RISK, preferenceData.getVhnCode(), preferenceData.getVhnId());
        }else {
            isoffline = true;
        }
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new MotherListAdapter(mResult, getActivity(), "Risk", this);
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

    private void showOfflineData() {

        Log.e("off ->",  "offline");

        realm.beginTransaction();
        RealmResults<MotherRiskListRealm> realmResults = realm.where(MotherRiskListRealm.class).findAll();
        Log.e("Mother list size ->", realmResults.size() + "");
        for (int i = 0; i < realmResults.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherRiskListRealm model = realmResults.get(i);
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
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {

        Log.e(risk.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<MotherListRealm> motherListAdapterRealmModel = realm.where(MotherListRealm.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MotherListRealm.class);
                    }
                });

                realm.beginTransaction();       //create or open
                for (int i = 0; i < jsonArray.length(); i++) {
                 /*   mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mresponseResult.setMid(jsonObject.getString("mid"));
                    mresponseResult.setMName(jsonObject.getString("mName"));
                    mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                    mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                    mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
                    mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                    mresponseResult.setmPhoto(jsonObject.getString("mPhoto"));
                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
                    mResult.add(mresponseResult);
                    mAdapter.notifyDataSetChanged();*/


                    motherRiskListRealm = realm.createObject(MotherRiskListRealm.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                    JSONObject mJsnobject = jsonArray.getJSONObject(i);
            /*        Log.e("position", i+"");
                    Log.e("mName", mJsnobject.getString("mName"));
                    Log.e("PICMEID", mJsnobject.getString("mPicmeId"));
                    Log.e("PICMEID", mJsnobject.getString("mid"));
                    Log.e("mMotherMobile", mJsnobject.getString("mMotherMobile"));
                    Log.e("vhnId", mJsnobject.getString("vhnId"));*/

                    motherRiskListRealm.setMName(mJsnobject.getString("mName"));
                    motherRiskListRealm.setMPicmeId(mJsnobject.getString("mPicmeId"));
                    motherRiskListRealm.setMid(mJsnobject.getString("mid"));
                    motherRiskListRealm.setmMotherMobile(mJsnobject.getString("mMotherMobile"));
                    motherRiskListRealm.setVhnId(mJsnobject.getString("vhnId"));
                    motherRiskListRealm.setMLatitude(mJsnobject.getString("mLatitude"));
                    motherRiskListRealm.setMLongitude(mJsnobject.getString("mLongitude"));
                    motherRiskListRealm.setMotherType(mJsnobject.getString("motherType"));
                    motherRiskListRealm.setmPhoto(mJsnobject.getString("mPhoto"));

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
        RealmResults<MotherRiskListRealm> userInfoRealmResult = realm.where(MotherRiskListRealm.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherRiskListRealm model = userInfoRealmResult.get(i);
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

    @Override
    public void showLoginError(String response) {
        Log.e(risk.class.getSimpleName(), "Response success" + response);

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
