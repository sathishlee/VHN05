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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.PNHBNCMotherListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNHBNCListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface, MotherListAdapter.ContactsAdapterListener {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;

    private TextView textView;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    PNHBNCMotherListRealmModel pnhbncMotherListRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.pnhbnc_mothers_list_activity);
        Log.d(PNHBNCListActivity.class.getSimpleName(), "oncrreate");

        showActionBar();
        initUI();


    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN/HBNC Mothers");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(PNHBNCListActivity.this, this, realm);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherRecordsList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);

        textView = (TextView) findViewById(R.id.txt_no_records_found);

        mAdapter = new MotherListAdapter(mResult, PNHBNCListActivity.this, "PN", this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PNHBNCListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
        if (isoffline) {
            showOffline();
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
        Log.e(PNHBNCListActivity.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersList");

                RealmQuery<PNHBNCMotherListRealmModel> pnhbncDueListRealmModels = realm.where(PNHBNCMotherListRealmModel.class);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNHBNCMotherListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
//                    mother_recycler_view.setVisibility(View.VISIBLE);
//                    textView.setVisibility(View.GONE);
                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pnhbncMotherListRealmModel = realm.createObject(PNHBNCMotherListRealmModel.class);

                        mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                       /* mresponseResult.setMid(jsonObject.getString("mid"));
                        mresponseResult.setMName(jsonObject.getString("mName"));
                        mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                        mresponseResult.setPnId(jsonObject.getString("pnId"));
                        mresponseResult.setMotherType(jsonObject.getString("motherType"));
                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                        mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
                        mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                        mResult.add(mresponseResult);
                        mAdapter.notifyDataSetChanged();*/

                        pnhbncMotherListRealmModel.setMid(jsonObject.getString("mid"));
                        pnhbncMotherListRealmModel.setmName(jsonObject.getString("mName"));
                        pnhbncMotherListRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
                        pnhbncMotherListRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
//                        pnhbncMotherListRealmModel.pni(jsonObject.getString("pnId"));
                        pnhbncMotherListRealmModel.setMotherType(jsonObject.getString("motherType"));
                        pnhbncMotherListRealmModel.setVhnId(jsonObject.getString("vhnId"));
                        pnhbncMotherListRealmModel.setmLatitude(jsonObject.getString("mLatitude"));
                        pnhbncMotherListRealmModel.setmLongitude(jsonObject.getString("mLongitude"));
                    }
                    realm.commitTransaction();
                } else {
//                    mother_recycler_view.setVisibility(View.GONE);
//                    textView.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();

    }

    private void setValueToUI() {


        Log.d(PNHBNCDueListActivity.class.getSimpleName(), "ON LINE");

        realm.beginTransaction();
        RealmResults<PNHBNCMotherListRealmModel> pnhbncMotherListRealmModelRealmResults = realm.where(PNHBNCMotherListRealmModel.class).findAll();
        if (pnhbncMotherListRealmModelRealmResults.size() == 0) {
//            mother_recycler_view.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < pnhbncMotherListRealmModelRealmResults.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            PNHBNCMotherListRealmModel model = pnhbncMotherListRealmModelRealmResults.get(i);
            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setMPicmeId(model.getmPicmeId());
            mresponseResult.setmAge(model.getmAge());
//    mresponseResult.setPnId(jsonObject.getString("pnId"));
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getmLatitude());
            mresponseResult.setMLongitude(model.getmLongitude());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();

    }


    private void showOffline() {

        Log.d(PNHBNCDueListActivity.class.getSimpleName(), "off Line");

        realm.beginTransaction();
        RealmResults<PNHBNCMotherListRealmModel> pnhbncMotherListRealmModelRealmResults = realm.where(PNHBNCMotherListRealmModel.class).findAll();
        if (pnhbncMotherListRealmModelRealmResults.size() == 0) {
//           mother_recycler_view.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < pnhbncMotherListRealmModelRealmResults.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            PNHBNCMotherListRealmModel model = pnhbncMotherListRealmModelRealmResults.get(i);
            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setMPicmeId(model.getmPicmeId());
//    mresponseResult.setPnId(jsonObject.getString("pnId"));
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getmLatitude());
            mresponseResult.setMLongitude(model.getmLongitude());
            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }

    @Override
    public void showLoginError(String response) {
        Log.e(PNHBNCListActivity.class.getSimpleName(), "Response Error" + response);
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
    public void onContactSelected(PNMotherListResponse.VhnAN_Mothers_List contact) {

    }
}
