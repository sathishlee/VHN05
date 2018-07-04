package com.unicef.vhn.activity.MotherVisitReport;

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
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANViewReportsActivity;
import com.unicef.vhn.activity.PNMotherDeliveryReportActivity;
import com.unicef.vhn.adapter.PNHBNCVisitRecordsAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.PnHbncVisitRecordsModel;
import com.unicef.vhn.realmDbModel.PNMVisitRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/*This screen values form realm DB, Api call in mother list*/
public class PNMotherVisitReportActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PreferenceData preferenceData;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    Button btn_delivery_reports, btn_visit_reports;
    PnHbncVisitRecordsModel.PnMothersVisit mPnHbncVisitRecordsModel;
    ArrayList<PnHbncVisitRecordsModel.PnMothersVisit> mPnHbncVisitRecordsList;
    PNHBNCVisitRecordsAdapter pnhbncVisitRecordsAdapter;
    TextView txt_no_records_found, txt_no_internet;
    CheckNetwork checkNetwork;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_pnmother_visit_report);
        Toast.makeText(getApplicationContext(), PNMotherVisitReportActivity.class.getSimpleName(), Toast.LENGTH_LONG).show();
        initUI();
        showActionBar();
        onClickListner();
    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        mPnHbncVisitRecordsList = new ArrayList<>();
        checkNetwork = new CheckNetwork(this);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_internet.setVisibility(View.GONE);
        if (checkNetwork.isNetworkAvailable()) {
            txt_no_internet.setVisibility(View.GONE);
        } else {
            txt_no_internet.setVisibility(View.VISIBLE);
        }
        viewPager = (ViewPager) findViewById(R.id.pn_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.pn_tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btn_delivery_reports = (Button) findViewById(R.id.btn_delivery_reports);
        btn_visit_reports = (Button) findViewById(R.id.btn_visit_reports);
        getValuefromRealm();

    }

    private void getValuefromRealm() {
        realm.beginTransaction();
        RealmResults<PNMVisitRealmModel> userInfoRealmResult = realm.where(PNMVisitRealmModel.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        if (!(userInfoRealmResult.size() == 0)) {
            for (int i = 0; i < userInfoRealmResult.size(); i++) {
                PNMVisitRealmModel model = userInfoRealmResult.get(i);
                mPnHbncVisitRecordsModel = new PnHbncVisitRecordsModel.PnMothersVisit();

    mPnHbncVisitRecordsModel.setPnId(model.getPnId());
    mPnHbncVisitRecordsModel.setMid(model.getMid());
    mPnHbncVisitRecordsModel.setPicmeId(model.getPicmeId());
    mPnHbncVisitRecordsModel.setPnVisitNo(model.getPnVisitNo());
    mPnHbncVisitRecordsModel.setPnDueDate(model.getPnDueDate());
    mPnHbncVisitRecordsModel.setPnCareProvidedDate(model.getPnCareProvidedDate());
    mPnHbncVisitRecordsModel.setPnPlace(model.getPnPlace());
    mPnHbncVisitRecordsModel.setPnAnyComplaints(model.getPnAnyComplaints());
    mPnHbncVisitRecordsModel.setPnBPSystolic(model.getPnBPSystolic());
    mPnHbncVisitRecordsModel.setPnPulseRate(model.getPnPulseRate());
    mPnHbncVisitRecordsModel.setPnTemp(model.getPnTemp());
    mPnHbncVisitRecordsModel.setPnEpistomyTear(model.getPnEpistomyTear());
    mPnHbncVisitRecordsModel.setPnPVDischarge(model.getPnPVDischarge());
    mPnHbncVisitRecordsModel.setPnBreastFeedingReason(model.getPnBreastFeedingReason());
    mPnHbncVisitRecordsModel.setPnBreastExamination(model.getPnBreastExamination());
    mPnHbncVisitRecordsModel.setPnOutCome(model.getPnOutCome());
    mPnHbncVisitRecordsModel.setCWeight(model.getCWeight());
    mPnHbncVisitRecordsModel.setCTemp(model.getCTemp());
    mPnHbncVisitRecordsModel.setCUmbilicalStump(model.getCUmbilicalStump());
    mPnHbncVisitRecordsModel.setCCry(model.getCCry());
    mPnHbncVisitRecordsModel.setCEyes(model.getCEyes());
    mPnHbncVisitRecordsModel.setCSkin(model.getCSkin());
    mPnHbncVisitRecordsModel.setCBreastFeeding(model.getCBreastFeeding());
    mPnHbncVisitRecordsModel.setCBreastFeedingReason(model.getCBreastFeedingReason());
    mPnHbncVisitRecordsModel.setCOutCome(model.getCOutCome());
    mPnHbncVisitRecordsList.add(mPnHbncVisitRecordsModel);

                    pnhbncVisitRecordsAdapter.notifyDataSetChanged();

            }

        } else {
            txt_no_records_found.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
        realm.commitTransaction();
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
                startActivity(new Intent(getApplicationContext(), PNMotherDeliveryReportActivity.class));
            }
        });
        btn_visit_reports.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        pnhbncVisitRecordsAdapter = new PNHBNCVisitRecordsAdapter(this, mPnHbncVisitRecordsList);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_delivery_reports:
                startActivity(new Intent(getApplicationContext(), PNMotherDeliveryReportActivity.class));
                break;
            case R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}