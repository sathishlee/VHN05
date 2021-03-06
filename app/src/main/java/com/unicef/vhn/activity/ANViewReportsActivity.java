package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ANVisitAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ANMotherVisitResponseModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.VisitANMotherViews;

import io.realm.Realm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*AN Mother visit Report have api call working well is not use this project,
use for ANMothervistReportActivity */
public class ANViewReportsActivity extends AppCompatActivity implements VisitANMotherViews, View.OnClickListener {
    String TAG = ANViewReportsActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PreferenceData preferenceData;
    GetVisitANMotherPresenter getVisitANMotherPresenter;
    private ArrayList<ANMotherVisitResponseModel.VhnAN_Mothers_List> mhealthRecordList;
    ANMotherVisitResponseModel.VhnAN_Mothers_List mhealthRecordResponseModel;
    ANVisitAdapter hAdapter;

    Button btn_primary_report, btn_view_report;
    TextView txt_no_records_found, txt_no_internet;
    String strPicmeId;
    Realm realm;
    CheckNetwork checkNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_anview_reports);
        Log.e(TAG, "");

        initUI();
        onClickListner();
        showActionBar();
    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        checkNetwork = new CheckNetwork(this);

        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_internet.setVisibility(View.GONE);

        getVisitANMotherPresenter = new GetVisitANMotherPresenter(ANViewReportsActivity.this, this, realm);
//        gVHRecordsPresenteer.getAllVistHeathRecord(Apiconstants.POST_VIST_HEALTH_RECORD_PICME,preferenceData.getPicmeId(), preferenceData.getMId());
        if (checkNetwork.isNetworkAvailable()) {
            Log.e(TAG, "Api name  --> vDashboardANVisitRecords");

            getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);
        } else {
            txt_no_internet.setVisibility(View.VISIBLE);
        }
        mhealthRecordList = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.hre_tabs);
        viewPager = (ViewPager) findViewById(R.id.hre_viewpager);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_records_found.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btn_primary_report = (Button) findViewById(R.id.btn_primary_report);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.e("mhealthRecordList", mhealthRecordList.size() + "");
        hAdapter = new ANVisitAdapter(this, mhealthRecordList);
        viewPager.setOffscreenPageLimit(mhealthRecordList.size());
        viewPager.setAdapter(hAdapter);
    }

    private void onClickListner() {
        btn_primary_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MothersPrimaryRecordsActivity.class));
            }
        });
        btn_view_report.setOnClickListener(this);
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AN Mother Records");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(MothersDetailsActivity.this, MainActivity.class);
        finish();
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_primary_report:
                startActivity(new Intent(getApplicationContext(), MothersPrimaryRecordsActivity.class));
                break;
            case R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(), com.unicef.vhn.activity.MotherVisitReport.ANViewReportsActivity.class));
                break;
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
    public void showANVisitRecordsSuccess(String response) {

        Log.d(ANViewReportsActivity.class.getSimpleName(), "--->healthRecordResponseModel" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                if (jsonArray.length() != 0) {
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            mhealthRecordResponseModel = new ANMotherVisitResponseModel.VhnAN_Mothers_List();

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                            mhealthRecordResponseModel.setVFacility(jsonObject.getString("vFacility"));
//                mhealthRecordResponseModel.setMLongitude(jsonObject.getString("mLongitude"));
//                mhealthRecordResponseModel.setMLatitude(jsonObject.getString("mLatitude"));
                            mhealthRecordResponseModel.setMotherStatus(jsonObject.getString("motherStatus"));
                            mhealthRecordResponseModel.setMotherCloseDate(jsonObject.getString("motherCloseDate"));
                            mhealthRecordResponseModel.setMRiskStatus(jsonObject.getString("mRiskStatus"));
                            mhealthRecordResponseModel.setMEDD(jsonObject.getString("mEDD"));
                            mhealthRecordResponseModel.setMLMP(jsonObject.getString("mLMP"));
                            mhealthRecordResponseModel.setPhcId(jsonObject.getString("phcId"));
                            mhealthRecordResponseModel.setAwwId(jsonObject.getString("awwId"));
                            mhealthRecordResponseModel.setVhnId(jsonObject.getString("vhnId"));
                            mhealthRecordResponseModel.setMasterId(jsonObject.getString("masterId"));
                            mhealthRecordResponseModel.setVTSH(jsonObject.getString("vTSH"));
                            mhealthRecordResponseModel.setUsgPlacenta(jsonObject.getString("usgPlacenta"));
                            mhealthRecordResponseModel.setUsgLiquor(jsonObject.getString("usgLiquor"));
                            mhealthRecordResponseModel.setUsgGestationSac(jsonObject.getString("usgGestationSac"));
                            mhealthRecordResponseModel.setUsgFetus(jsonObject.getString("usgFetus"));
                            mhealthRecordResponseModel.setVAlbumin(jsonObject.getString("vAlbumin"));
                            mhealthRecordResponseModel.setVUrinSugar(jsonObject.getString("vUrinSugar"));
                            mhealthRecordResponseModel.setVGTT(jsonObject.getString("vGTT"));
                            mhealthRecordResponseModel.setVPPBS(jsonObject.getString("vPPBS"));
                            mhealthRecordResponseModel.setVFBS(jsonObject.getString("vFBS"));
                            mhealthRecordResponseModel.setVRBS(jsonObject.getString("vRBS"));
                            mhealthRecordResponseModel.setVFHS(jsonObject.getString("vFHS"));
                            mhealthRecordResponseModel.setVHemoglobin(jsonObject.getString("vHemoglobin"));
                            mhealthRecordResponseModel.setVBodyTemp(jsonObject.getString("vBodyTemp"));
                            mhealthRecordResponseModel.setVPedalEdemaPresent(jsonObject.getString("vPedalEdemaPresent"));
                            mhealthRecordResponseModel.setVFundalHeight(jsonObject.getString("vFundalHeight"));
                            mhealthRecordResponseModel.setVEnterWeight(jsonObject.getString("vEnterWeight"));
                            mhealthRecordResponseModel.setVEnterPulseRate(jsonObject.getString("vEnterPulseRate"));
                            mhealthRecordResponseModel.setVClinicalBPDiastolic(jsonObject.getString("vClinicalBPDiastolic"));
                            mhealthRecordResponseModel.setVClinicalBPSystolic(jsonObject.getString("vClinicalBPSystolic"));
//                mhealthRecordResponseModel.setVAnyComplaintsOthers(jsonObject.getString("vAnyComplaintsOthers"));
                            mhealthRecordResponseModel.setVAnyComplaints(jsonObject.getString("vAnyComplaints"));
//                mhealthRecordResponseModel.setVFacilityOthers(jsonObject.getString("vFacilityOthers"));
                            mhealthRecordResponseModel.setVtypeOfVisit(jsonObject.getString("vtypeOfVisit"));
                            mhealthRecordResponseModel.setPicmeId(jsonObject.getString("picmeId"));
                            mhealthRecordResponseModel.setMid(jsonObject.getString("mid"));
                            mhealthRecordResponseModel.setVisitId(jsonObject.getString("visitId"));
                            mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                            mhealthRecordResponseModel.setVid(jsonObject.getString("vid"));
                            mhealthRecordList.add(mhealthRecordResponseModel);
                            hAdapter.notifyDataSetChanged();

                            strPicmeId = mhealthRecordResponseModel.getPicmeId();

                            AppConstants.MOTHER_PICME_ID = strPicmeId;
                            AppConstants.SELECTED_MID = mhealthRecordResponseModel.getMid();
                        }
                    } else {
                        txt_no_records_found.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                    }
                }
            } else {
                txt_no_records_found.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showANVisitRecordsFailiur(String response) {

    }

    @Override
    public void showPNVisitRecordsSuccess(String response) {

    }

    @Override
    public void showPNVisitRecordsFailiur(String response) {

    }
}
