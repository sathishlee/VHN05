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
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.activity.VisitActivity;
import com.unicef.vhn.adapter.RemainderVisitListAdapter;
import com.unicef.vhn.adapter.VisitListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.VisitListResponseModel;
import com.unicef.vhn.realmDbModel.VisitListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RemainderVisitFragment extends Fragment implements MotherListsViews, MakeCallInterface {

    String TAG = RemainderVisitFragment.class.getSimpleName();
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<VisitListResponseModel.Vhn_current_visits> mResult;
    VisitListResponseModel.Vhn_current_visits mresponseResult;
     private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private RemainderVisitListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;



TextView visit_list;

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

        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        pnMotherListPresenter = new MotherListPresenter(getActivity(), this);
        if (checkNetwork.isNetworkAvailable()) {

            pnMotherListPresenter.getPNMotherList(Apiconstants.REMAINDER_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
//            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView)view. findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) view.findViewById(R.id.txt_no_records_found);
        visit_list =(TextView)view.findViewById(R.id.visit_list);
//        mAdapter = new RemainderVisitListAdapter(getActivity(), mResult, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);


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
        Log.e(TAG, Apiconstants.CURRENT_VISIT_LIST +" api response"+response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
              JSONArray jsonArray = mJsnobject.getJSONArray("remaindermothers");
            Log.e(TAG,"jsonArray.length -->"+jsonArray.length());

            if (status.equalsIgnoreCase("1")) {
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mresponseResult = new VisitListResponseModel.Vhn_current_visits();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e(TAG,"remaindermothers position -->"+i);

                        Log.e(TAG,"noteId   -->"+i+"-->"+jsonObject.getString("noteId"));
                        Log.e(TAG,"  mid -->"+i+"-->"+jsonObject.getString("masterId"));
                        Log.e(TAG,"  mName -->"+i+"-->"+jsonObject.getString("mName"));
                        Log.e(TAG,"  picmeId -->"+i+"-->"+jsonObject.getString("picmeId"));

                        mresponseResult.setNoteId(jsonObject.getString("noteId"));
                        mresponseResult.setMasterId(jsonObject.getString("masterId"));
                        mresponseResult.setMid(jsonObject.getString("masterId"));
                        mresponseResult.setMName(jsonObject.getString("mName"));
                        mresponseResult.setPicmeId(jsonObject.getString("picmeId"));
                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                        mresponseResult.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                        mresponseResult.setMtype(jsonObject.getString("mtype"));
                        mresponseResult.setMtype("AN");
                        mresponseResult.setNextVisit("2018-08-18");

                       mResult.add(mresponseResult);

                        mAdapter.notifyDataSetChanged();
                    }
                    Log.e(TAG,"mResult size -->"+mResult);
                }
            }else {
                Log.e(TAG,"message   -->"+message);
Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void showLoginError(String string) {

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
