package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.unicef.vhn.adapter.MothenListAdapter;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MotherListActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
//    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MothenListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mothers_list_activity);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("PN Mother Visits");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MotherListActivity.this,this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        pnMotherListPresenter.getPNMotherList(preferenceData.getVhnCode(),preferenceData.getVhnId());
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);

        mAdapter = new MothenListAdapter(mResult, MotherListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MotherListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

//        CardView pn_visit = (CardView) findViewById(R.id.pn_visit);

        /*pn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MotherListActivity.this, PNMotherVisitListDetailsActivity.class);
                startActivity(i);
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MotherListActivity.this, MainActivity.class);
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



        Log.e(ImmunizationActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
            for (int i = 0; i < jsonArray.length(); i++) {
                mresponseResult =new PNMotherListResponse.VhnAN_Mothers_List();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mresponseResult.setMid(jsonObject.getString("mid"));

                mresponseResult.setMName(jsonObject.getString("mName"));
                mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));


                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showLoginError(String string) {

    }
}
