package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.PNMotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.PNMothenListAdapter;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.PNMotherResponseModel;
import com.unicef.vhn.view.PNMotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PNMotherVisitListActivity extends AppCompatActivity implements PNMotherListsViews {
    ProgressDialog pDialog;
    PNMotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    private RecyclerView recyclerView;
    private PNMothenListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnmother_visit_list);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("PN Mother Visits");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new PNMotherListPresenter(PNMotherVisitListActivity.this,this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        pnMotherListPresenter.getPNMotherList(preferenceData.getVhnCode(),preferenceData.getVhnId());
        mResult = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rec_immunization);

        mAdapter = new PNMothenListAdapter(mResult, PNMotherVisitListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PNMotherVisitListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        CardView pn_visit = (CardView) findViewById(R.id.pn_visit);

        /*pn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PNMotherVisitListActivity.this, PNMotherVisitListDetailsActivity.class);
                startActivity(i);
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(PNMotherVisitListActivity.this, MainActivity.class);
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
