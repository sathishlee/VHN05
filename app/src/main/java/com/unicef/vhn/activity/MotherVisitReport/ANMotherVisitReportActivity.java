package com.unicef.vhn.activity.MotherVisitReport;

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
import com.unicef.vhn.activity.MotherList.AllMotherListActivity;
import com.unicef.vhn.activity.MothersPrimaryRecordsActivity;
import com.unicef.vhn.adapter.ANVisitAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ANMotherVisitResponseModel;
import com.unicef.vhn.realmDbModel.ANMVisitRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;

import java.util.ArrayList;

import com.unicef.vhn.view.VisitANMotherViews;
import io.realm.Realm;
import io.realm.RealmResults;

public class ANMotherVisitReportActivity extends AppCompatActivity implements View.OnClickListener, VisitANMotherViews {


    private ProgressDialog pDialog;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PreferenceData preferenceData;

    private ArrayList<ANMotherVisitResponseModel.VhnAN_Mothers_List> mhealthRecordList;

    ANMotherVisitResponseModel.VhnAN_Mothers_List mhealthRecordResponseModel;

    ANVisitAdapter hAdapter;

    Button btn_primary_report, btn_view_report;
    TextView txt_no_records_found;

    Realm realm;

    CheckNetwork checkNetwork;
    TextView txt_no_internet;

    GetVisitANMotherPresenter getVisitANMotherPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_anvisit_report);

        Toast.makeText(getApplicationContext(),"AN Mother visit MID"+AppConstants.SELECTED_MID ,Toast.LENGTH_SHORT).show();

        Log.d(ANMotherVisitReportActivity.class.getSimpleName(), "Activity Created123");
        initUI();
        onClickListner();
        showActionBar();
    }

    private void initUI() {
        Log.d(ANMotherVisitReportActivity.class.getSimpleName(), "Activity initUI");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        checkNetwork =new CheckNetwork(this);
        txt_no_internet.setVisibility(View.GONE);
        getVisitANMotherPresenter = new GetVisitANMotherPresenter(ANMotherVisitReportActivity.this, this,realm);

        if (checkNetwork.isNetworkAvailable())
        {
            getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);

            txt_no_internet.setVisibility(View.GONE);
        }else{
            txt_no_internet.setVisibility(View.VISIBLE);

        }
        mhealthRecordList = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.hre_tabs);
        viewPager = (ViewPager) findViewById(R.id.hre_viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btn_primary_report = (Button) findViewById(R.id.btn_primary_report);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
        getValuefromRealm();
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
                startActivity(new Intent(getApplicationContext(), MothersPrimaryRecordsActivity.class)); break;
            case  R.id.btn_view_report:
//                 startActivity(new Intent(getApplicationContext(),ANViewReportsActivity.class)); break;
                 startActivity(new Intent(getApplicationContext(),ANViewReportsActivity.class)); break;
        }
    }

    private void getValuefromRealm() {

        Log.d(ANMotherVisitReportActivity.class.getSimpleName(), "getValue formRealm");
        Log.e(ANMotherVisitReportActivity.class.getSimpleName(), "showe report of mid  "+ AppConstants.SELECTED_MID);

        realm.beginTransaction();
        RealmResults<ANMVisitRealmModel> userInfoRealmResult = realm.where(ANMVisitRealmModel.class)
                .equalTo("mid", AppConstants.SELECTED_MID).findAll();
        Log.e(ANMotherVisitReportActivity.class.getSimpleName(), userInfoRealmResult.size()+"");
        if (!(userInfoRealmResult.size() == 0)) {
            for (int i = 0; i < userInfoRealmResult.size(); i++) {
                mhealthRecordResponseModel = new ANMotherVisitResponseModel.VhnAN_Mothers_List();
                ANMVisitRealmModel model = userInfoRealmResult.get(i);

                mhealthRecordResponseModel.setVDate(model.getVDate());
                mhealthRecordResponseModel.setVFacility(model.getVFacility());
//                mhealthRecordResponseModel.setMLongitude(jsonObject.getString("mLongitude"));
//                mhealthRecordResponseModel.setMLatitude(jsonObject.getString("mLatitude"));
                mhealthRecordResponseModel.setMotherStatus(model.getMotherStatus());
                mhealthRecordResponseModel.setMotherCloseDate(model.getMotherCloseDate());
                mhealthRecordResponseModel.setMRiskStatus(model.getMRiskStatus());
                mhealthRecordResponseModel.setMEDD(model.getMEDD());
                mhealthRecordResponseModel.setMLMP(model.getMLMP());
                mhealthRecordResponseModel.setPhcId(model.getPhcId());
                mhealthRecordResponseModel.setAwwId(model.getAwwId());
                mhealthRecordResponseModel.setVhnId(model.getVhnId());
                mhealthRecordResponseModel.setMasterId(model.getMasterId());
                mhealthRecordResponseModel.setVTSH(model.getVTSH());
                mhealthRecordResponseModel.setUsgPlacenta(model.getUsgPlacenta());
                mhealthRecordResponseModel.setUsgLiquor(model.getUsgLiquor());
                mhealthRecordResponseModel.setUsgGestationSac(model.getUsgGestationSac());
                mhealthRecordResponseModel.setUsgFetus(model.getUsgFetus());
                mhealthRecordResponseModel.setVAlbumin(model.getVAlbumin());
                mhealthRecordResponseModel.setVUrinSugar(model.getVUrinSugar());
                mhealthRecordResponseModel.setVGTT(model.getVGTT());
                mhealthRecordResponseModel.setVPPBS(model.getVPPBS());
                mhealthRecordResponseModel.setVFBS(model.getVFBS());
                mhealthRecordResponseModel.setVRBS(model.getVRBS());
                mhealthRecordResponseModel.setVFHS(model.getVFHS());
                mhealthRecordResponseModel.setVHemoglobin(model.getVHemoglobin());
                mhealthRecordResponseModel.setVBodyTemp(model.getVBodyTemp());
                mhealthRecordResponseModel.setVPedalEdemaPresent(model.getVPedalEdemaPresent());
                mhealthRecordResponseModel.setVFundalHeight(model.getVFundalHeight());
                mhealthRecordResponseModel.setVEnterWeight(model.getVEnterWeight());
                mhealthRecordResponseModel.setVEnterPulseRate(model.getVEnterPulseRate());
                mhealthRecordResponseModel.setVClinicalBPDiastolic(model.getVClinicalBPDiastolic());
                mhealthRecordResponseModel.setVClinicalBPSystolic(model.getVClinicalBPSystolic());
//                mhealthRecordResponseModel.setVAnyComplaintsOthers(jsonObject.getString("vAnyComplaintsOthers"));
                mhealthRecordResponseModel.setVAnyComplaints(model.getVAnyComplaints());
//                mhealthRecordResponseModel.setVFacilityOthers(jsonObject.getString("vFacilityOthers"));
                mhealthRecordResponseModel.setVtypeOfVisit(model.getVtypeOfVisit());
                mhealthRecordResponseModel.setPicmeId(model.getPicmeId());
                mhealthRecordResponseModel.setMid(model.getMid());
                mhealthRecordResponseModel.setVisitId(model.getVisitId());
                mhealthRecordResponseModel.setVDate(model.getVDate());
                mhealthRecordResponseModel.setVid(model.getVid());
                mhealthRecordList.add(mhealthRecordResponseModel);
                hAdapter.notifyDataSetChanged();
            }
        } else {
            txt_no_records_found.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
        realm.commitTransaction();
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
