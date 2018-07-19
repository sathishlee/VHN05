/*
package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.RemainderVisitListAdapter;
import com.unicef.vhn.adapter.VisitListAdapter;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.RemainderVisitResponseModel;
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

 public class AlertActivity  extends AppCompatActivity implements MakeCallInterface, MotherListsViews {

    String TAG =VisitActivity.class.getSimpleName();
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<RemainderVisitResponseModel.Remaindermothers> mResult;
    RemainderVisitResponseModel.Remaindermothers mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private RemainderVisitListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;


    CheckNetwork checkNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_alert);
        showActionBar();
        initUI();
        onClickListner();
    }

    public void initUI(){

        Log.e(TAG,"VisitActivity INIT");

        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(AlertActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
//        pnMotherListPresenter.getTremAndPreTremMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        if (checkNetwork.isNetworkAvailable()) {
            Log.e(TAG, Apiconstants.REMAINDER_VISIT_LIST+" api called");
            pnMotherListPresenter.getPNMotherList(Apiconstants.REMAINDER_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
//            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        mAdapter = new RemainderVisitListAdapter(AlertActivity.this, mResult, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AlertActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
    }

    private void onClickListner() {

    }

    public void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Alert List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void makeCall(String mMotherMobile) {
//        isDataUpdate = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getApplicationContext(), "Displaying Call permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
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

        Log.e(TAG,Apiconstants.CURRENT_VISIT_LIST +"api response"+response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String msg = mJsnobject.getString("message");

            JSONArray jsonArray = mJsnobject.getJSONArray("remaindermothers");


            if (status.equalsIgnoreCase("1")) {

                if (jsonArray.length() != 0) {
                    Log.e(TAG, jsonArray.length() + " of json array not null");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        mresponseResult = new RemainderVisitResponseModel.Remaindermothers();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        mresponseResult.setNoteId(jsonObject.getString("noteId"));
                        mresponseResult.setMasterId(jsonObject.getString("masterId"));
                        mresponseResult.setMid(jsonObject.getString("mid"));
                        mresponseResult.setMName(jsonObject.getString("mName"));
                        mresponseResult.setPicmeId(jsonObject.getString("picmeId"));
                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                        mresponseResult.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                        mresponseResult.setMtype(jsonObject.getString("mtype"));
//                        mresponseResult.setNextVisit(jsonObject.getString("nextvisit"));
//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                        mResult.add(mresponseResult);
                        mAdapter.notifyDataSetChanged();
                    }

                }else{

                }
            }else {
                Log.e(TAG,jsonArray.length()+" of json array  null");

                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        setValuetoUI();

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
}
*/
