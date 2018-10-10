package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.TremAndPreTremAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.TremAndPreTremResponseModel;
import com.unicef.vhn.realmDbModel.TreamPreTreamListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TreamPreTreamListActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<TremAndPreTremResponseModel.DelveryInfo> mResult;
    TremAndPreTremResponseModel.DelveryInfo mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found;
    private TremAndPreTremAdapter mAdapter;
    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    TreamPreTreamListRealmModel treamPreTreamListRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_tream_pre_tream_list);
        showActionBar();
        initUI();
    }

    private void initUI() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(TreamPreTreamListActivity.this, this, realm);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getTremAndPreTremMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        mAdapter = new TremAndPreTremAdapter(mResult, TreamPreTreamListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TreamPreTreamListActivity.this);
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


    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trem/PreTream List");
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

        Log.e(TreamPreTreamListActivity.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("delveryInfo");

                RealmResults<TreamPreTreamListRealmModel> motherListAdapterRealmModel = realm.where(TreamPreTreamListRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(TreamPreTreamListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
                    realm.beginTransaction();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        treamPreTreamListRealmModel = realm.createObject(TreamPreTreamListRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database


                        mresponseResult = new TremAndPreTremResponseModel.DelveryInfo();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        treamPreTreamListRealmModel.setMid(jsonObject.getString("mid"));
                        treamPreTreamListRealmModel.setDInfantId(jsonObject.getString("dInfantId"));
                        treamPreTreamListRealmModel.setDdatetime(jsonObject.getString("ddatetime"));
                        treamPreTreamListRealmModel.setDtime(jsonObject.getString("dtime"));
                        treamPreTreamListRealmModel.setmName(jsonObject.getString("mName"));
                        treamPreTreamListRealmModel.setDBirthDetails(jsonObject.getString("dBirthDetails"));


                        /* mresponseResult.setMid(jsonObject.getString("mid"));
                        mresponseResult.setDInfantId(jsonObject.getString("dInfantId"));
                        mresponseResult.setDdatetime(jsonObject.getString("ddatetime"));
                        mresponseResult.setDtime(jsonObject.getString("dtime"));
                        mresponseResult.setmName(jsonObject.getString("mName"));
                        mresponseResult.setDBirthDetails(jsonObject.getString("dBirthDetails"));

                        mResult.add(mresponseResult);
                        mAdapter.notifyDataSetChanged();*/
                    }
                    realm.commitTransaction();
                } else {
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();

    }

    private void setValueToUI() {

        Log.e(TreamPreTreamListActivity.class.getSimpleName(), "ON LINE ");

        realm.beginTransaction();
        RealmResults<TreamPreTreamListRealmModel> antt1listRealmResult = realm.where(TreamPreTreamListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", antt1listRealmResult.size() + "");
        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            mresponseResult = new TremAndPreTremResponseModel.DelveryInfo();

            TreamPreTreamListRealmModel model = antt1listRealmResult.get(i);


            mresponseResult.setMid(model.getMid());
            mresponseResult.setDInfantId(model.getDInfantId());
            mresponseResult.setDdatetime(model.getDdatetime());
            mresponseResult.setDtime(model.getDtime());
            mresponseResult.setmName(model.getmName());
            mresponseResult.setDBirthDetails(model.getDBirthDetails());

            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }


    private void showOfflineData() {
        Log.e(TreamPreTreamListActivity.class.getSimpleName(), "ON LINE ");

        realm.beginTransaction();
        RealmResults<TreamPreTreamListRealmModel> antt1listRealmResult = realm.where(TreamPreTreamListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", antt1listRealmResult.size() + "");
        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            mresponseResult = new TremAndPreTremResponseModel.DelveryInfo();

            TreamPreTreamListRealmModel model = antt1listRealmResult.get(i);


            mresponseResult.setMid(model.getMid());
            mresponseResult.setDInfantId(model.getDInfantId());
            mresponseResult.setDdatetime(model.getDdatetime());
            mresponseResult.setDtime(model.getDtime());
            mresponseResult.setmName(model.getmName());
            mresponseResult.setDBirthDetails(model.getDBirthDetails());

            mResult.add(mresponseResult);
            mAdapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }

    @Override
    public void showLoginError(String response) {
        Log.e(TreamPreTreamListActivity.class.getSimpleName(), "Response Error" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
