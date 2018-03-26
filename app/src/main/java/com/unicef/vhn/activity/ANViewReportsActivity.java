package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ANVisitAdapter;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ANMotherVisitResponseModel;
import com.unicef.vhn.view.VisitANMotherViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ANViewReportsActivity extends AppCompatActivity implements VisitANMotherViews {
    private  ProgressDialog pDialog;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PreferenceData preferenceData;
    GetVisitANMotherPresenter getVisitANMotherPresenter;
    private ArrayList<ANMotherVisitResponseModel.VhnAN_Mothers_List> mhealthRecordList;
    ANMotherVisitResponseModel.VhnAN_Mothers_List mhealthRecordResponseModel;
    ANVisitAdapter hAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anview_reports);
        initUI();
//        onClickListner();

    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);

        getVisitANMotherPresenter = new GetVisitANMotherPresenter(ANViewReportsActivity.this, this);
//        gVHRecordsPresenteer.getAllVistHeathRecord(Apiconstants.POST_VIST_HEALTH_RECORD_PICME,preferenceData.getPicmeId(), preferenceData.getMId());
        getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);
        mhealthRecordList =new ArrayList<>();
        tabLayout =(TabLayout) findViewById(R.id.hre_tabs);
        viewPager =(ViewPager) findViewById(R.id.hre_viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.e("mhealthRecordList",mhealthRecordList.size()+"");
        hAdapter =new ANVisitAdapter(this,mhealthRecordList);
        viewPager.setOffscreenPageLimit(mhealthRecordList.size());
        viewPager.setAdapter(hAdapter);
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

        Log.d(ANViewReportsActivity.class.getSimpleName(), "--->healthRecordResponseModel" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showVisitRecordsFailiur(String response) {

    }
}