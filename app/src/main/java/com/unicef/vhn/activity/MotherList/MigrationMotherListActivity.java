package com.unicef.vhn.activity.MotherList;

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
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.adapter.MotherMigrationAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.MotherMigrationResponseModel;
import com.unicef.vhn.realmDbModel.MotherMigrationRealmModel;
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

public class MigrationMotherListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<MotherMigrationResponseModel.Vhn_migrated_mothers> vhn_migrated_mothers;

    MotherMigrationResponseModel.Vhn_migrated_mothers getVhn_migrated_mothers;

    boolean isDataUpdate = true;

    private RecyclerView recyclerView;
    private TextView textView;
    private MotherMigrationAdapter motherMigrationAdapter;


    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherMigrationRealmModel motherMigrationRealmModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_mother_migration);
        showActionBar();
        initUI();

    }

    public void initUI() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(MigrationMotherListActivity.this, this, realm);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getMigratedMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }

        vhn_migrated_mothers = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.mother_migration_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);

        motherMigrationAdapter = new MotherMigrationAdapter(vhn_migrated_mothers, MigrationMotherListActivity.this, "", this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MigrationMotherListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(motherMigrationAdapter);
        if (isoffline) {
            showOfflineData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }


    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mother Migration List");
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
        pDialog.hide();
    }

    @Override
    public void showLoginSuccess(String response) {

        Log.e(MigrationMotherListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {

                JSONArray jsonArray = mJsnobject.getJSONArray("vhn_migrated_mothers");

                RealmResults<MotherMigrationRealmModel> motherListAdapterRealmModel = realm.where(MotherMigrationRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MotherMigrationRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    realm.beginTransaction();       //create or open

                    for (int i = 0; i < jsonArray.length(); i++) {

                        motherMigrationRealmModel = realm.createObject(MotherMigrationRealmModel.class);

                        getVhn_migrated_mothers = new MotherMigrationResponseModel.Vhn_migrated_mothers();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                 /*   getVhn_migrated_mothers.setMid(jsonObject.getString("mid"));
                    getVhn_migrated_mothers.setMName(jsonObject.getString("mName"));
                    getVhn_migrated_mothers.setMPicmeId(jsonObject.getString("mPicmeId"));
                    getVhn_migrated_mothers.setMtype(jsonObject.getString("mtype"));
                    getVhn_migrated_mothers.setSubject(jsonObject.getString("subject"));
//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                    vhn_migrated_mothers.add(getVhn_migrated_mothers);
                    motherMigrationAdapter.notifyDataSetChanged();*/

                        motherMigrationRealmModel.setMid(jsonObject.getString("mid"));
                        motherMigrationRealmModel.setMName(jsonObject.getString("mName"));
                        motherMigrationRealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        motherMigrationRealmModel.setMtype(jsonObject.getString("mtype"));
                        motherMigrationRealmModel.setSubject(jsonObject.getString("subject"));
                        motherMigrationRealmModel.setMMotherMobile(jsonObject.getString("mMotherMobile"));
//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));

                    }

                    realm.commitTransaction();       //create or open

                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();

    }

    private void setValueToUI() {
        Log.d(MigrationMotherListActivity.class.getSimpleName(), "oneline");

        realm.beginTransaction();

        RealmResults<MotherMigrationRealmModel> motherMigrationrealmResults = realm.where(MotherMigrationRealmModel.class).findAll();
        for (int i = 0; i < motherMigrationrealmResults.size(); i++) {
            getVhn_migrated_mothers = new MotherMigrationResponseModel.Vhn_migrated_mothers();

            MotherMigrationRealmModel model = motherMigrationrealmResults.get(i);

            getVhn_migrated_mothers.setMid(model.getMid());
            getVhn_migrated_mothers.setMName(model.getMName());
            getVhn_migrated_mothers.setMPicmeId(model.getMPicmeId());
            getVhn_migrated_mothers.setMtype(model.getMtype());
            getVhn_migrated_mothers.setSubject(model.getSubject());
            getVhn_migrated_mothers.setMMotherMobile(model.getMMotherMobile());

//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
            if (model.getMtype().equalsIgnoreCase("AN")) {
                vhn_migrated_mothers.add(getVhn_migrated_mothers);
                motherMigrationAdapter.notifyDataSetChanged();
            }

        }
        realm.commitTransaction();
    }


    private void showOfflineData() {

        Log.d(MigrationMotherListActivity.class.getSimpleName(), "off line");

        realm.beginTransaction();

        RealmResults<MotherMigrationRealmModel> motherMigrationrealmResults = realm.where(MotherMigrationRealmModel.class).findAll();
        for (int i = 0; i < motherMigrationrealmResults.size(); i++) {
            getVhn_migrated_mothers = new MotherMigrationResponseModel.Vhn_migrated_mothers();

            MotherMigrationRealmModel model = motherMigrationrealmResults.get(i);

            getVhn_migrated_mothers.setMid(model.getMid());
            getVhn_migrated_mothers.setMName(model.getMName());
            getVhn_migrated_mothers.setMPicmeId(model.getMPicmeId());
            getVhn_migrated_mothers.setMtype(model.getMtype());
            getVhn_migrated_mothers.setSubject(model.getSubject());
            getVhn_migrated_mothers.setMMotherMobile(model.getMMotherMobile());

//                    mresponseResult.setMotherType(jsonObject.getString("motherType"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
            vhn_migrated_mothers.add(getVhn_migrated_mothers);
            motherMigrationAdapter.notifyDataSetChanged();


        }
        realm.commitTransaction();

    }


    @Override
    public void showLoginError(String string) {
        Log.e(MigrationMotherListActivity.class.getSimpleName(), "Response Error" + string);
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
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
        Log.i(MigrationMotherListActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
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
