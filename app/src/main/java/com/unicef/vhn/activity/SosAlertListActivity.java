package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.adapter.SOSListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.SOSListResponse;
import com.unicef.vhn.realmDbModel.ANTT1RealmModel;
import com.unicef.vhn.realmDbModel.DashBoardRealmModel;
import com.unicef.vhn.realmDbModel.SosListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SosAlertListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<SOSListResponse.VhnAN_Mothers_List> mResult ;
    SOSListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private SOSListAdapter mAdapter;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate=true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    SosListRealmModel sosListRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_sos_alert_list);
        initUI();
        showActionBar();
        onClickListner();


    }

    private void onClickListner() {

    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sos Mother List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        checkNetwork =   new CheckNetwork(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(SosAlertListActivity.this,this);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_SOS_MOTHER_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        }
        else{
            isoffline=true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found  =(TextView)findViewById(R.id.txt_no_records_found);
        mAdapter = new SOSListAdapter(mResult, SosAlertListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SosAlertListActivity.this);
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
        Log.e(SosAlertListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");

            RealmResults<SosListRealmModel> motherListAdapterRealmModel = realm.where(SosListRealmModel.class).findAll();
            Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(SosListRealmModel.class);
                }
            });
            if (jsonArray.length()!=0) {
                mother_recycler_view .setVisibility(View.VISIBLE);
                txt_no_records_found  .setVisibility(View.GONE);
                realm.beginTransaction();
                for (int i = 0; i < jsonArray.length(); i++) {
                    sosListRealmModel = realm.createObject(SosListRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                    mresponseResult = new SOSListResponse.VhnAN_Mothers_List();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    /*mresponseResult.setMid(jsonObject.getString("mid"));
                    mresponseResult.setMName(jsonObject.getString("mName"));
                    mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                    mresponseResult.setSosId(jsonObject.getString("sosId"));
                    mresponseResult.setMRiskStatus(jsonObject.getString("mRiskStatus"));
                    mresponseResult.setSosStatus(jsonObject.getString("sosStatus"));
                    mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                    mresponseResult.setMotherType(jsonObject.getString("motherType"));

                    mresponseResult.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
                    mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
                    mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                    mresponseResult.setmPhoto(jsonObject.getString("mPhoto"));
                    mResult.add(mresponseResult);
                    mAdapter.notifyDataSetChanged();*/

                    sosListRealmModel.setMid(jsonObject.getString("mid"));
                    sosListRealmModel.setMName(jsonObject.getString("mName"));
                    sosListRealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                    sosListRealmModel.setSosId(jsonObject.getString("sosId"));
                    sosListRealmModel.setMRiskStatus(jsonObject.getString("mRiskStatus"));
                    sosListRealmModel.setSosStatus(jsonObject.getString("sosStatus"));
                    sosListRealmModel.setVhnId(jsonObject.getString("vhnId"));
                    sosListRealmModel.setMotherType(jsonObject.getString("motherType"));

                    sosListRealmModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                    sosListRealmModel.setMotherType(jsonObject.getString("motherType"));
                    sosListRealmModel.setMLatitude(jsonObject.getString("mLatitude"));
                    sosListRealmModel.setMLongitude(jsonObject.getString("mLongitude"));
                    sosListRealmModel.setmPhoto(jsonObject.getString("mPhoto"));

                }
                realm.commitTransaction();
            }
            else{
                mother_recycler_view .setVisibility(View.GONE);
                txt_no_records_found  .setVisibility(View.VISIBLE);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();



    }

    private void setValueToUI() {
        Log.e(SosAlertListActivity.class.getSimpleName(), "ON LINE ");

        realm.beginTransaction();
        RealmResults<SosListRealmModel> antt1listRealmResult = realm.where(SosListRealmModel.class).findAll();
        if (antt1listRealmResult.size()==0){
            mother_recycler_view .setVisibility(View.GONE);
            txt_no_records_found  .setVisibility(View.VISIBLE);
        }
        Log.e("ANTT1 list size ->", antt1listRealmResult.size() + "");
        for (int i = 0; i < antt1listRealmResult.size(); i++) {


            mresponseResult = new SOSListResponse.VhnAN_Mothers_List();

            SosListRealmModel model = antt1listRealmResult.get(i);

            Log.e("Sos Alert list ->", i+ model.getSosStatus());


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setSosId(model.getSosId());
            mresponseResult.setMRiskStatus(model.getMRiskStatus());
            mresponseResult.setSosStatus(model.getSosStatus());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMotherType(model.getMotherType());

            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setMLatitude(model.getMLatitude());
            mresponseResult.setMLongitude(model.getMLongitude());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }
    private void showOfflineData() {

        Log.e(SosAlertListActivity.class.getSimpleName(), "OFF LINE ");

        realm.beginTransaction();
        RealmResults<SosListRealmModel> antt1listRealmResult = realm.where(SosListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", antt1listRealmResult.size() + "");
        if (antt1listRealmResult.size()==0){
            mother_recycler_view .setVisibility(View.VISIBLE);
            txt_no_records_found  .setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            mresponseResult = new SOSListResponse.VhnAN_Mothers_List();

            SosListRealmModel model = antt1listRealmResult.get(i);


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setSosId(model.getSosId());
            mresponseResult.setMRiskStatus(model.getMRiskStatus());
            mresponseResult.setSosStatus(model.getSosStatus());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMotherType(model.getMotherType());

            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setMLatitude(model.getMLatitude());
            mresponseResult.setMLongitude(model.getMLongitude());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();

    }
    @Override
    public void showLoginError(String response) {
        Log.e(SosAlertListActivity.class.getSimpleName(), "Response Error" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }

    @Override
    public void makeCall(String mMotherMobile) {
        isDataUpdate=false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+"+mMotherMobile)));
        }
    }
    private void requestCallPermission() {
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getApplicationContext(),"Displaying Call permission rationale to provide additional context.",Toast.LENGTH_SHORT).show();
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
