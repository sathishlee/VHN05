package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNHBNCListActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnhbnc_mothers_list_activity);
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
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(PNHBNCListActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        pnMotherListPresenter.getPNMotherRecordsList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);

        mAdapter = new MotherListAdapter(mResult, PNHBNCListActivity.this,"PN");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PNHBNCListActivity.this);
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
        Log.e(PNHBNCListActivity.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
           String status =mJsnobject.getString("status");
           if (status.equalsIgnoreCase("1")) {
               JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersList");
               for (int i = 0; i < jsonArray.length(); i++) {
                   mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                   JSONObject jsonObject = jsonArray.getJSONObject(i);
                   mresponseResult.setMid(jsonObject.getString("mid"));
                   mresponseResult.setMName(jsonObject.getString("mName"));
                   mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                   mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                   mresponseResult.setPnId(jsonObject.getString("pnId"));
//                   mresponseResult.setMotherType(jsonObject.getString("motherType"));

//                   mresponseResult.setVhnId(jsonObject.getString("vhnId"));
//                   mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                   mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
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
        Log.e(PNHBNCListActivity.class.getSimpleName(), "Response Error" + response);
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
