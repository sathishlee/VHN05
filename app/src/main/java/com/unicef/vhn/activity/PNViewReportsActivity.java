package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.PNHBNCVisitRecordsAdapter;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PnHbncVisitRecordsModel;
import com.unicef.vhn.view.VisitANMotherViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PNViewReportsActivity extends AppCompatActivity implements VisitANMotherViews, View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PreferenceData preferenceData;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    GetVisitANMotherPresenter getVisitANMotherPresenter;
    Button btn_delivery_reports, btn_visit_reports;

    //    HealthRecordResponseModel.Visit_Records mhealthRecordResponseModel;
    PnHbncVisitRecordsModel.PnMothersVisit mPnHbncVisitRecordsModel;
    ArrayList<   PnHbncVisitRecordsModel.PnMothersVisit> mPnHbncVisitRecordsList;

    String strpnId, strPicmeId;


    PNHBNCVisitRecordsAdapter pnhbncVisitRecordsAdapter;
    TextView txt_no_records_found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnview_reports);
        initUI();
        showActionBar();
        onClickListner();
    }
    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        getVisitANMotherPresenter = new GetVisitANMotherPresenter(PNViewReportsActivity.this, this);
        getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(),preferenceData.getVhnId(), AppConstants.SELECTED_MID);
//        gVHRecordsPresenteer = new GetVisitHealthRecordsPresenter(getActivity(), this);
//        gVHRecordsPresenteer.getPN_HBNC_VisitRecord(Apiconstants.POST_PN_HBNC_VIST_RECORD,preferenceData.getPicmeId(), preferenceData.getMId());

        mPnHbncVisitRecordsList = new ArrayList<>();
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        viewPager = (ViewPager)findViewById(R.id.pn_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.pn_tabs);
        txt_no_records_found.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btn_delivery_reports = (Button) findViewById(R.id.btn_delivery_reports);
        btn_visit_reports = (Button) findViewById(R.id.btn_visit_reports);

    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN Mother Records");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onClickListner() {
        btn_delivery_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PNMotherDeliveryReportActivity.class));
            }
        });
        btn_visit_reports.setOnClickListener(this);
    }
    private void setupViewPager(ViewPager viewPager) {
        pnhbncVisitRecordsAdapter =new PNHBNCVisitRecordsAdapter(this,mPnHbncVisitRecordsList);
        viewPager.setOffscreenPageLimit(mPnHbncVisitRecordsList.size());
        viewPager.setAdapter(pnhbncVisitRecordsAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(MothersDetailsActivity.this, MainActivity.class);
        finish();
//        startActivity(intent);

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
    public void showVisitRecordsSuccess(String response) {
        Log.e(PNViewReportsActivity.class.getSimpleName(),"Response success"+response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
           /* if (status.equalsIgnoreCase("1")) {
                txt_no_records_found.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);*/
                JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersVisit");
                if (jsonArray.length()!=0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mPnHbncVisitRecordsModel = new PnHbncVisitRecordsModel.PnMothersVisit();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mPnHbncVisitRecordsModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        mPnHbncVisitRecordsModel.setPnId(jsonObject.getString("pnId"));
                        strPicmeId = mPnHbncVisitRecordsModel.getMPicmeId();
                        AppConstants.MOTHER_PICME_ID = strPicmeId;
                        strpnId = mPnHbncVisitRecordsModel.getPnId();
                        if (strpnId.equalsIgnoreCase("null")) {
                            txt_no_records_found.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            tabLayout.setVisibility(View.GONE);
                        } else {
                            txt_no_records_found.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            tabLayout.setVisibility(View.VISIBLE);

                            mPnHbncVisitRecordsModel.setMid(jsonObject.getString("mid"));
                            mPnHbncVisitRecordsModel.setPicmeId(jsonObject.getString("picmeId"));
                            mPnHbncVisitRecordsModel.setPnVisitNo(jsonObject.getString("pnVisitNo"));
                            mPnHbncVisitRecordsModel.setPnDueDate(jsonObject.getString("pnDueDate"));
                            mPnHbncVisitRecordsModel.setPnCareProvidedDate(jsonObject.getString("pnCareProvidedDate"));
                            mPnHbncVisitRecordsModel.setPnPlace(jsonObject.getString("pnPlace"));
                            mPnHbncVisitRecordsModel.setPnAnyComplaints(jsonObject.getString("pnAnyComplaints"));
                            mPnHbncVisitRecordsModel.setPnBPSystolic(jsonObject.getString("pnBPSystolic"));
                            mPnHbncVisitRecordsModel.setPnPulseRate(jsonObject.getString("pnPulseRate"));
                            mPnHbncVisitRecordsModel.setPnTemp(jsonObject.getString("pnTemp"));
                            mPnHbncVisitRecordsModel.setPnEpistomyTear(jsonObject.getString("pnEpistomyTear"));
                            mPnHbncVisitRecordsModel.setPnPVDischarge(jsonObject.getString("pnPVDischarge"));
                            mPnHbncVisitRecordsModel.setPnBreastFeedingReason(jsonObject.getString("pnBreastFeedingReason"));
                            mPnHbncVisitRecordsModel.setPnBreastExamination(jsonObject.getString("pnBreastExamination"));
                            mPnHbncVisitRecordsModel.setPnOutCome(jsonObject.getString("pnOutCome"));
                            mPnHbncVisitRecordsModel.setCWeight(jsonObject.getString("cWeight"));
                            mPnHbncVisitRecordsModel.setCTemp(jsonObject.getString("cTemp"));
                            mPnHbncVisitRecordsModel.setCUmbilicalStump(jsonObject.getString("cUmbilicalStump"));
                            mPnHbncVisitRecordsModel.setCCry(jsonObject.getString("cCry"));
                            mPnHbncVisitRecordsModel.setCEyes(jsonObject.getString("cEyes"));
                            mPnHbncVisitRecordsModel.setCSkin(jsonObject.getString("cSkin"));
                            mPnHbncVisitRecordsModel.setCBreastFeeding(jsonObject.getString("cBreastFeeding"));
                            mPnHbncVisitRecordsModel.setCBreastFeedingReason(jsonObject.getString("cBreastFeedingReason"));
                            mPnHbncVisitRecordsModel.setCOutCome(jsonObject.getString("cOutCome"));

                            mPnHbncVisitRecordsList.add(mPnHbncVisitRecordsModel);
                            pnhbncVisitRecordsAdapter.notifyDataSetChanged();

                            AppConstants.SELECTED_MID = mPnHbncVisitRecordsModel.getMid();
                        }
                    }
                }else{
                    txt_no_records_found.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                }
                /*}else{
                txt_no_records_found.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }*/
            } catch(JSONException e){
                e.printStackTrace();
            }

    }

    @Override
    public void showVisitRecordsFailiur(String response) {
        Log.e(PNViewReportsActivity.class.getSimpleName(),"Response Error"+response);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_delivery_reports:
                 startActivity(new Intent(getApplicationContext(),PNMotherDeliveryReportActivity.class));
                 break;
            case  R.id.btn_view_report:
                  startActivity(new Intent(getApplicationContext(),ANViewReportsActivity.class));
                  break;
        }
    }
}
