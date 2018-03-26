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
import com.unicef.vhn.adapter.TremAndPreTremAdapter;
import com.unicef.vhn.model.TremAndPreTremResponseModel;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreamPreTreamListActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<TremAndPreTremResponseModel.DelveryInfo> mResult;
    TremAndPreTremResponseModel.DelveryInfo mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private TremAndPreTremAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tream_pre_tream_list);
        showActionBar();
        initUI();
    }

    private void initUI() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(TreamPreTreamListActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        pnMotherListPresenter.getTremAndPreTremMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);

        mAdapter = new TremAndPreTremAdapter(mResult, TreamPreTreamListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TreamPreTreamListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
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
            String status =mJsnobject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("delveryInfo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    mresponseResult = new TremAndPreTremResponseModel.DelveryInfo();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mresponseResult.setMid(jsonObject.getString("mid"));
                    mresponseResult.setDInfantId(jsonObject.getString("dInfantId"));
                    mresponseResult.setDdatetime(jsonObject.getString("ddatetime"));
                    mresponseResult.setDtime(jsonObject.getString("dtime"));
                    mresponseResult.setDBirthDetails(jsonObject.getString("dBirthDetails"));

                    mResult.add(mresponseResult);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
