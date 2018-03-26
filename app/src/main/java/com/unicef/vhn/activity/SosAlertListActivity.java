package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.SOSListAdapter;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.SOSListResponse;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SosAlertListActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<SOSListResponse.VhnAN_Mothers_List> mResult ;
    SOSListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private SOSListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent intent = new Intent(SosAlertListActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(SosAlertListActivity.this,this);
        pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_SOS_MOTHER_LIST,preferenceData.getVhnCode(),preferenceData.getVhnId());

        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        mAdapter = new SOSListAdapter(mResult, SosAlertListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SosAlertListActivity.this);
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
        Log.e(SosAlertListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
            for (int i = 0; i < jsonArray.length(); i++) {

                mresponseResult =new SOSListResponse.VhnAN_Mothers_List();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mresponseResult.setMid(jsonObject.getString("mid"));
                mresponseResult.setMName(jsonObject.getString("mName"));
                mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                mresponseResult.setSosId(jsonObject.getString("sosId"));
                mresponseResult.setMRiskStatus(jsonObject.getString("mRiskStatus"));
                mresponseResult.setSosStatus(jsonObject.getString("sosStatus"));
                mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                mresponseResult.setMotherType(jsonObject.getString("motherType"));

                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

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


}
