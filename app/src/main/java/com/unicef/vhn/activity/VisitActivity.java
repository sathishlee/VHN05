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
import com.unicef.vhn.adapter.TremAndPreTremAdapter;
import com.unicef.vhn.adapter.VisitListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.TremAndPreTremResponseModel;
import com.unicef.vhn.model.VisitListResponseModel;
import com.unicef.vhn.realmDbModel.TreamPreTreamListRealmModel;
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

/**
 * Created by Suthishan on 20/1/2018.
 */

public class VisitActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<VisitListResponseModel.Vhn_current_visits> mResult;
    VisitListResponseModel.Vhn_current_visits mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private VisitListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    VisitListRealmModel visitListRealmMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_layout_visit);
        showActionBar();
        initUI();
        onClickListner();
    }

    public void initUI() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(VisitActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
//        pnMotherListPresenter.getTremAndPreTremMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.CURRENT_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        mAdapter = new VisitListAdapter(VisitActivity.this, mResult, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisitActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

        if (isoffline) {
            showOfflineData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }


    private void onClickListner() {

    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today Visits List");
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
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {

        Log.e(VisitActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhn_today_visit_count");
            RealmResults<VisitListRealmModel> motherListAdapterRealmModel = realm.where(VisitListRealmModel.class).findAll();
            Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(VisitListRealmModel.class);
                }
            });
            if (jsonArray.length() != 0) {
                mother_recycler_view.setVisibility(View.VISIBLE);
                txt_no_records_found.setVisibility(View.GONE);
                realm.beginTransaction();
                for (int i = 0; i < jsonArray.length(); i++) {
                    visitListRealmMode = realm.createObject(VisitListRealmModel.class);

                    mresponseResult = new VisitListResponseModel.Vhn_current_visits();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    visitListRealmMode.setMid(jsonObject.getString("mid"));
                    visitListRealmMode.setMName(jsonObject.getString("mName"));
                    visitListRealmMode.setPicmeId(jsonObject.getString("picmeId"));
                    visitListRealmMode.setVhnId(jsonObject.getString("vhnId"));
                    visitListRealmMode.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                    visitListRealmMode.setMtype(jsonObject.getString("mtype"));
                    visitListRealmMode.setNextVisit(jsonObject.getString("nextVisit"));

                    visitListRealmMode.setMLatitude(jsonObject.getString("mLatitude"));
                    visitListRealmMode.setMLongitude(jsonObject.getString("mLongitude"));

                    /*mresponseResult.setMid(jsonObject.getString("mid"));
                    mresponseResult.setMName(jsonObject.getString("mName"));
                    mresponseResult.setPicmeId(jsonObject.getString("picmeId"));
                    mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                    mresponseResult.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                    mresponseResult.setMtype(jsonObject.getString("mtype"));
                    mresponseResult.setNextVisit(jsonObject.getString("nextVisit"));
//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                    mResult.add(mresponseResult);
                    mAdapter.notifyDataSetChanged();*/
                }
                realm.commitTransaction();
            } else {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValuetoUI();
    }

    private void setValuetoUI() {
        Log.e(VisitActivity.class.getSimpleName(), "ON LINE ");

        realm.beginTransaction();
        RealmResults<VisitListRealmModel> motherListAdapterRealmModel = realm.where(VisitListRealmModel.class).findAll();

        for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
            mresponseResult = new VisitListResponseModel.Vhn_current_visits();

            VisitListRealmModel model = motherListAdapterRealmModel.get(i);


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setPicmeId(model.getPicmeId());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMMotherMobile(model.getMMotherMobile());
            mresponseResult.setMtype(model.getMtype());
            mresponseResult.setNextVisit(model.getNextVisit());

            mresponseResult.setMLatitude(model.getMLatitude());
            mresponseResult.setMLongitude(model.getMLongitude());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }


    private void showOfflineData() {

        Log.e(VisitActivity.class.getSimpleName(), "ON LINE ");

        realm.beginTransaction();
        RealmResults<VisitListRealmModel> motherListAdapterRealmModel = realm.where(VisitListRealmModel.class).findAll();

        for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
            mresponseResult = new VisitListResponseModel.Vhn_current_visits();

            VisitListRealmModel model = motherListAdapterRealmModel.get(i);


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setPicmeId(model.getPicmeId());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMMotherMobile(model.getMMotherMobile());
            mresponseResult.setMtype(model.getMtype());
            mresponseResult.setNextVisit(model.getNextVisit());

            mresponseResult.setMLatitude(model.getMLatitude());
            mresponseResult.setMLongitude(model.getMLongitude());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();

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
}
