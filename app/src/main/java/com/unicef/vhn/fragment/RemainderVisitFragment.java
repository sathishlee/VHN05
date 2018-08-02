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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.RemainderVisitListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.RemainderVisitResponseModel;
import com.unicef.vhn.model.VisitListResponseModel;
import com.unicef.vhn.realmDbModel.RemainderListRealModel;
import com.unicef.vhn.realmDbModel.VisitListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import io.realm.Realm;
import io.realm.RealmResults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemainderVisitFragment extends Fragment implements MotherListsViews, MakeCallInterface {

    String TAG = RemainderVisitFragment.class.getSimpleName();
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<RemainderVisitResponseModel.Remaindermothers> mResult;
    RemainderVisitResponseModel.Remaindermothers mresponseResult;
     private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private RemainderVisitListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;

    Realm realm;


TextView visit_list;
boolean isoffline=false;
private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =null;
        view = inflater.inflate(R.layout.fragment_remainder_visit, container, false);
        initUI(view);
        return  view;
    }

    private void initUI(View view) {
        realm = RealmController.with(this).getRealm();
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        pnMotherListPresenter = new MotherListPresenter(getActivity(), this, realm);
        if (checkNetwork.isNetworkAvailable()) {

            pnMotherListPresenter.getPNMotherList(Apiconstants.REMAINDER_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView)view. findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) view.findViewById(R.id.txt_no_records_found);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        visit_list =(TextView)view.findViewById(R.id.visit_list);
        mAdapter = new RemainderVisitListAdapter(mResult,getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
if (isoffline){
    setValuetoUI();
}else{
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Record Not Found");
    builder.create();
}
swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.REMAINDER_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            swipeRefreshLayout.setRefreshing(false);
            isoffline = true;
        }
    }
});
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
        swipeRefreshLayout.setRefreshing(false);
        mResult.clear();
        Log.e(TAG, Apiconstants.CURRENT_VISIT_LIST +" api response"+response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
              JSONArray jsonArray = mJsnobject.getJSONArray("remaindermothers");
//            RemainderListRealModel
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(RemainderListRealModel.class);
                }
            });
            if (status.equalsIgnoreCase("1")) {
                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);

                    realm.beginTransaction();       //create or open
                    RemainderListRealModel   remainderListRealModel=null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mresponseResult = new RemainderVisitResponseModel.Remaindermothers();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e(TAG,"remaindermothers position -->"+i);
                            remainderListRealModel  = realm.createObject(RemainderListRealModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database


                        Log.e(TAG,"noteId   -->"+i+"-->"+jsonObject.getString("noteId"));
                        Log.e(TAG,"  mid -->"+i+"-->"+jsonObject.getString("masterId"));
                        Log.e(TAG,"  mName -->"+i+"-->"+jsonObject.getString("mName"));
                        Log.e(TAG,"  picmeId -->"+i+"-->"+jsonObject.getString("picmeId"));

                        mresponseResult.setNoteId(jsonObject.getString("noteId"));
                        remainderListRealModel.setNoteId(jsonObject.getString("noteId"));

                        mresponseResult.setMasterId(jsonObject.getString("masterId"));
                        remainderListRealModel.setMasterId(jsonObject.getString("masterId"));

                        mresponseResult.setMid(jsonObject.getString("masterId"));
                        remainderListRealModel.setMid(jsonObject.getString("masterId"));

                        mresponseResult.setMName(jsonObject.getString("mName"));
                        remainderListRealModel.setmName(jsonObject.getString("mName"));

                        mresponseResult.setPicmeId(jsonObject.getString("picmeId"));
                        remainderListRealModel.setPicmeId(jsonObject.getString("picmeId"));

                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                        remainderListRealModel.setVhnId(jsonObject.getString("vhnId"));

                        mresponseResult.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                        remainderListRealModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));

                        mresponseResult.setMtype(jsonObject.getString("mtype"));
                        remainderListRealModel.setMtype(jsonObject.getString("mtype"));

                        mresponseResult.setNextVisit(jsonObject.getString("nextVisit"));
                        remainderListRealModel.setNextVisit(jsonObject.getString("nextVisit"));

//                       mResult.add(mresponseResult);
//                       mAdapter.notifyDataSetChanged();
                    }
                    realm.commitTransaction();
                }else{
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                }
            }else {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
                txt_no_records_found.setText(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValuetoUI();
    }

    private void setValuetoUI() {

        mResult.clear();
        realm.beginTransaction();
        RealmResults<RemainderListRealModel> motherListAdapterRealmModel = realm.where(RemainderListRealModel.class).findAll();
        Log.e(TAG, "Today Visit realm db size" + motherListAdapterRealmModel.size());

        if (motherListAdapterRealmModel.size() != 0) {

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                RemainderListRealModel model = motherListAdapterRealmModel.get(i);

                /*Log.e(TAG, i+ " Note Id" + model.getNoteId());
                Log.e(TAG, i+ " MId" + model.getMid());
                Log.e(TAG, i+ " MASTER Id" + model.getMasterId());
                Log.e(TAG, i+ " NAME" + model.getMName());
                Log.e(TAG, i+ " PICME ID" + model.getPicmeId());
                Log.e(TAG, i+ " VHN ID" + model.getVhnId());
                Log.e(TAG, i+ " MOTHER MOBILE" + model.getMMotherMobile());
                Log.e(TAG, i+ " MOTHER NEXT VISIT" + model.getNextVisit());
                Log.e(TAG, i+ " MOTHER TYPE" + model.getMtype());*/


                mresponseResult = new RemainderVisitResponseModel.Remaindermothers();

                mresponseResult.setNoteId(model.getNoteId());

                mresponseResult.setMasterId(model.getMasterId());

                mresponseResult.setMid(model.getMid());

                mresponseResult.setMName(model.getmName());

                mresponseResult.setPicmeId(model.getPicmeId());

                mresponseResult.setVhnId(model.getVhnId());

                mresponseResult.setMMotherMobile(model.getmMotherMobile());

                mresponseResult.setMtype(model.getMtype());

                mresponseResult.setNextVisit(model.getNextVisit());




                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();


//                mresponseResult = new VisitListResponseModel.Vhn_current_visits();

            /*
                mresponseResult.setNoteId(model.getNoteId());
                mresponseResult.setMasterId(model.getMasterId());
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMName(model.getMName());
                mresponseResult.setPicmeId(model.getPicmeId());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setMMotherMobile(model.getMMotherMobile());
                mresponseResult.setMtype(model.getMtype());
                mresponseResult.setNextVisit(model.getNextVisit());

                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();*/
            }

        }
        realm.commitTransaction();


    }


    @Override
    public void showLoginError(String string) {
        swipeRefreshLayout.setRefreshing(false);

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

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CALL_PHONE)) {
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
