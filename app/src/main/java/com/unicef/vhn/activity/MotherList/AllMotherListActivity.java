package com.unicef.vhn.activity.MotherList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.fragment.mothers;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.ANMVisitRealmModel;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.realmDbModel.PNMVisitRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.view.VisitANMotherViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AllMotherListActivity extends AppCompatActivity implements MotherListsViews,
        MakeCallInterface, MotherListAdapter.ContactsAdapterListener, VisitANMotherViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;

    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private TextView txt_no_records_found,txt_no_internet;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    LinearLayout ll_filter;
    final Context context = this;
    TextView txt_filter;
    String str_mPhoto;
    ImageView cardview_image;
    CheckNetwork checkNetwork;
    boolean isOffline;
    Realm realm;
    PNMMotherListRealmModel pnmMotherListRealmModel;


    boolean strHighRisk = false;
    boolean strDescending = false;
    String strVillageName = "";
    String strTrimester = "";
    boolean strMotherType;
    ArrayList<String> vhnVillageList;
    ArrayList<String> termisterlist;
    LinearLayout ll_filter_block;
    boolean isfillter = false;

    GetVisitANMotherPresenter getVisitANMotherPresenter;

    ANMVisitRealmModel mhealthRecordResponseModel;
    PNMVisitRealmModel pnmVisitRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_all_mother_list);

        initUI();
        showActionBar();
    }

    private void showActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(AppConstants.MOTHER_LIST_TITLE);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {
        ll_filter_block = (LinearLayout) findViewById(R.id.ll_filter_block);
        if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
            ll_filter_block.setVisibility(View.GONE);
        }
        else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
            ll_filter_block.setVisibility(View.GONE);
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
            ll_filter_block.setVisibility(View.GONE);
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
            ll_filter_block.setVisibility(View.GONE);
        } else {
            ll_filter_block.setVisibility(View.VISIBLE);

        }
        checkNetwork = new CheckNetwork(this);
        txt_filter = (TextView) findViewById(R.id.txt_filter);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_internet.setVisibility(View.GONE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(AllMotherListActivity.this, this);

        getVisitANMotherPresenter = new GetVisitANMotherPresenter(AllMotherListActivity.this, this);

        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST,
                    preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isOffline = true;
        }
        mResult = new ArrayList<>();

        mAdapter = new MotherListAdapter(mResult,AllMotherListActivity.this,"AN", this,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllMotherListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
        if (isOffline) {
            txt_no_internet.setVisibility(View.VISIBLE);
            setValuetoUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }

        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceData.setFilterStatus(true);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_fragment);
                CheckBox ch_highRisk, ch_desc, ch_anmother, ch_pnmother;
                Spinner sp_village_wise, sp_trimester;
                RadioGroup rg_trim;
                RadioButton rb_trim_all,rb_trim_first,rb_trim_second,rb_trim_thrid;
                rg_trim = (RadioGroup) dialog.findViewById(R.id.rg_trim);
                ch_highRisk = (CheckBox) dialog.findViewById(R.id.ch_high_risk);
                ch_desc = (CheckBox) dialog.findViewById(R.id.ch_desc);
                ch_anmother = (CheckBox) dialog.findViewById(R.id.ch_anmother);
                ch_pnmother = (CheckBox) dialog.findViewById(R.id.ch_pnmother);
                sp_village_wise = (Spinner) dialog.findViewById(R.id.sp_village_wise);
//                sp_trimester = (Spinner) dialog.findViewById(R.id.sp_trimester);
                rb_trim_all = (RadioButton) dialog.findViewById(R.id.rb_trim_all);
                rb_trim_first = (RadioButton) dialog.findViewById(R.id.rb_trim_first);
                rb_trim_second = (RadioButton) dialog.findViewById(R.id.rb_trim_second);
                rb_trim_thrid = (RadioButton) dialog.findViewById(R.id.rb_trim_thrid);
//                rb_trim_all.setChecked(true);

                if (preferenceData.getTermisterPosition()==0){
                    rb_trim_all.setChecked(true);
                }else if (preferenceData.getTermisterPosition()==1){
                    rb_trim_first.setChecked(true);
                }else if (preferenceData.getTermisterPosition()==2){
                    rb_trim_second.setChecked(true);
                }else if (preferenceData.getTermisterPosition()==3){
                    rb_trim_thrid.setChecked(true);


                }
                ch_anmother.setVisibility(View.GONE);
                ch_pnmother.setVisibility(View.GONE);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                if (preferenceData.getHighRiskStatus()) {
                    ch_highRisk.setChecked(true);
                }
                if (preferenceData.getDescendingStatus()) {
                    ch_desc.setChecked(true);
                }

                ch_highRisk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        strHighRisk = isChecked;
                        preferenceData.setHighRiskStatus(isChecked);
                    }
                });
                ch_desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        preferenceData.setDescendingStatus(isChecked);

                    }
                });
                rg_trim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb = (RadioButton) group.findViewById(checkedId);
                        if (null != rb && checkedId > -1) {
//                            Toast.makeText(AllMotherListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                            if (rb.getText().toString().equalsIgnoreCase("All")) {
                                preferenceData.setTermister("All");
                                preferenceData.setTermisterPosition(0);
                            }else if (rb.getText().toString().equalsIgnoreCase("1st")) {
                                preferenceData.setTermister("1-3");
                                preferenceData.setTermisterPosition(1);
                            }if (rb.getText().toString().equalsIgnoreCase("2nd")) {
                                preferenceData.setTermister("4-6");
                                preferenceData.setTermisterPosition(2);
                            }if (rb.getText().toString().equalsIgnoreCase("3rd")) {
                                preferenceData.setTermister("7-10");
                                preferenceData.setTermisterPosition(3);
                            }
                        }
                    }
                });
                vhnVillageList = new ArrayList<>();
                termisterlist = new ArrayList<>();
                termisterlist.add("All");
                termisterlist.add("1-3");
                termisterlist.add("4-6");
                termisterlist.add("7-10");

                realm.beginTransaction();
                RealmResults<PNMMotherListRealmModel> getVillageList=null;
               getVillageList = realm.where(PNMMotherListRealmModel.class).findAll();
                vhnVillageList.add("All");
                for (int i = 0; i < getVillageList.size(); i++) {
                    strVillageName = getVillageList.get(0).getmVillage();
                    vhnVillageList.add(getVillageList.get(i).getmVillage());
                    Log.e("vhnVillageList", getVillageList.get(i).getmVillage());
                }
                realm.commitTransaction();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        vhnVillageList
                );

                sp_village_wise.setAdapter(adapter);
                ArrayAdapter<String> adapterT = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        termisterlist
                );

//                sp_trimester.setAdapter(adapterT);
//                sp_trimester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int pos , long id) {
//                        preferenceData.setTermister(termisterlist.get(pos ));
//                        preferenceData.setTermisterPosition(pos);
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
                sp_village_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        preferenceData.setVillageName(vhnVillageList.get(position));
                        preferenceData.setVillageNamePosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                sp_village_wise.setSelection(preferenceData.getVillageNamePosition());
//                sp_trimester.setSelection(preferenceData.getTermisterPosition());

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), AllMotherListActivity.class));
                        finish();
                    }
                });
                dialog.show();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        pDialog.hide();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void showANVisitRecordsSuccess(String response) {

        Log.d(mothers.class.getSimpleName(),  "ANVisitRecordsSuccess api call success");

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Log.d(mothers.class.getSimpleName(),  "ANVisitRecordsSuccess api call success status"+status);

                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<ANMVisitRealmModel> motherListAdapterRealmModel = realm.where(ANMVisitRealmModel.class).findAll();
                Log.e(mothers.class.getSimpleName(), "already anm visit data available is size"+motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.e(mothers.class.getSimpleName(), "delete ANMother visit realm records");

                        realm.delete(ANMVisitRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel realm talbe create");

                        mhealthRecordResponseModel = realm.createObject(ANMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call"+i+"th visit details"+jsonObject.getString("picmeId"));
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call"+i+"th visit details"+jsonObject.getString("vid"));

                        mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                        mhealthRecordResponseModel.setVFacility(jsonObject.getString("vFacility"));
                        //              mhealthRecordResponseModel.setMLongitude(jsonObject.getString("mLongitude"));
                        //              mhealthRecordResponseModel.setMLatitude(jsonObject.getString("mLatitude"));
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

//                        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(motherListAdapterRealmModel.get(i).getPicmeId());

                    }
                    realm.commitTransaction();
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                } else {
                    Log.e(mothers.class.getSimpleName(), "ANMother visit records NOt Found");

                }
            } else {
                Log.e(mothers.class.getSimpleName(), "ANMother visit records"+message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void showANVisitRecordsFailiur(String response) {
        Log.e(mothers.class.getSimpleName(), "ANMother visit records api failiur");
        }

    @Override
    public void showPNVisitRecordsSuccess(String response) {

        Log.d(mothers.class.getSimpleName(),  "PNVisitRecordsSuccess api call success");

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Log.d(mothers.class.getSimpleName(),  "PNVisitRecordsSuccess api call success status"+status);

                JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersVisit");
                RealmResults<PNMVisitRealmModel> motherListAdapterRealmModel = realm.where(PNMVisitRealmModel.class).findAll();
                Log.d(mothers.class.getSimpleName(),  "PNVisitRecords motherListAdapterRealmModel size "+motherListAdapterRealmModel.size());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(mothers.class.getSimpleName(),  "PNVisitRecords delete");
                        realm.delete(PNMVisitRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pnmVisitRealmModel = realm.createObject(PNMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        Log.e(mothers.class.getSimpleName(), "PNMVisitRealmModel visit realm table created");

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        pnmVisitRealmModel.setMAge(jsonObject.getString("mAge"));
                        pnmVisitRealmModel.setMWeight(jsonObject.getString("mWeight"));
                        pnmVisitRealmModel.setPnId(jsonObject.getString("pnId"));
                        pnmVisitRealmModel.setMid(jsonObject.getString("mid"));
                        pnmVisitRealmModel.setPicmeId(jsonObject.getString("picmeId"));
                        pnmVisitRealmModel.setPnVisitNo(jsonObject.getString("pnVisitNo"));
                        pnmVisitRealmModel.setPnDueDate(jsonObject.getString("pnDueDate"));
                        pnmVisitRealmModel.setPnCareProvidedDate(jsonObject.getString("pnCareProvidedDate"));
                        pnmVisitRealmModel.setPnPlace(jsonObject.getString("pnPlace"));
                        pnmVisitRealmModel.setPnAnyComplaints(jsonObject.getString("pnAnyComplaints"));
                        pnmVisitRealmModel.setPnBPSystolic(jsonObject.getString("pnBPSystolic"));
                        pnmVisitRealmModel.setPnPulseRate(jsonObject.getString("pnPulseRate"));
                        pnmVisitRealmModel.setPnTemp(jsonObject.getString("pnTemp"));
                        pnmVisitRealmModel.setPnEpistomyTear(jsonObject.getString("pnEpistomyTear"));
                        pnmVisitRealmModel.setPnPVDischarge(jsonObject.getString("pnPVDischarge"));
                        pnmVisitRealmModel.setPnBreastFeedingReason(jsonObject.getString("pnBreastFeedingReason"));
                        pnmVisitRealmModel.setPnBreastExamination(jsonObject.getString("pnBreastExamination"));
                        pnmVisitRealmModel.setPnOutCome(jsonObject.getString("pnOutCome"));
                        pnmVisitRealmModel.setCWeight(jsonObject.getString("cWeight"));
                        pnmVisitRealmModel.setCTemp(jsonObject.getString("cTemp"));
                        pnmVisitRealmModel.setCUmbilicalStump(jsonObject.getString("cUmbilicalStump"));
                        pnmVisitRealmModel.setCCry(jsonObject.getString("cCry"));
                        pnmVisitRealmModel.setCEyes(jsonObject.getString("cEyes"));
                        pnmVisitRealmModel.setCSkin(jsonObject.getString("cSkin"));
                        pnmVisitRealmModel.setCBreastFeeding(jsonObject.getString("cBreastFeeding"));
                        pnmVisitRealmModel.setCBreastFeedingReason(jsonObject.getString("cBreastFeedingReason"));
                        pnmVisitRealmModel.setCOutCome(jsonObject.getString("cOutCome"));

//                        motherDeliveryPresenter.deliveryDetails(jsonObject.getString("picmeId"),jsonObject.getString("mid"));

                    }
                    realm.commitTransaction();
                    Log.e(mothers.class.getSimpleName(), "commitTransaction ANMother visit realm records");

                } else {
                    Log.e(mothers.class.getSimpleName(), "showPNVisitRecordsSuccess RECORD NOT FOUND");

                }
            } else {
                Log.e(mothers.class.getSimpleName(), "showPNVisitRecords success"+message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPNVisitRecordsFailiur(String response) {
        Log.e(mothers.class.getSimpleName(), "showPNVisitRecords failiur"+response);

    }

    @Override
    public void showLoginSuccess(String response) {

        Log.e(AllMotherListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel =null;
                motherListAdapterRealmModel= realm.where(PNMMotherListRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNMMotherListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
//                    txt_no_records_found.setVisibility(View.GONE);
//                    pDialog.show();
                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pnmMotherListRealmModel = realm.createObject(PNMMotherListRealmModel.class);
                        mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pnmMotherListRealmModel.setMid(jsonObject.getString("mid"));
                        pnmMotherListRealmModel.setmName(jsonObject.getString("mName"));
                        pnmMotherListRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
                        pnmMotherListRealmModel.setVhnId(jsonObject.getString("vhnId"));
                        pnmMotherListRealmModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                        pnmMotherListRealmModel.setMotherType(jsonObject.getString("motherType"));
                        pnmMotherListRealmModel.setmLatitude(jsonObject.getString("mLatitude"));
                        pnmMotherListRealmModel.setmLongitude(jsonObject.getString("mLongitude"));
                        pnmMotherListRealmModel.setmPhoto(jsonObject.getString("mPhoto"));
                        pnmMotherListRealmModel.setmRiskStatus(jsonObject.getString("mRiskStatus"));
                        pnmMotherListRealmModel.setmEDD(jsonObject.getString("mEDD"));
                        pnmMotherListRealmModel.setmHusbandName(jsonObject.getString("mHusbandName"));
                        pnmMotherListRealmModel.setmHusbandMobile(jsonObject.getString("mHusbandMobile"));
                        pnmMotherListRealmModel.setvLongitude(jsonObject.getString("vLongitude"));
                        pnmMotherListRealmModel.setvLatitude(jsonObject.getString("vLatitude"));
                        pnmMotherListRealmModel.setCurrentMonth(Integer.parseInt(jsonObject.getString("currentMonth")));
                        pnmMotherListRealmModel.setmLMP(jsonObject.getString("mLMP"));
                        pnmMotherListRealmModel.setmVillage(jsonObject.getString("mVillage"));
                        pnmMotherListRealmModel.setNextVisit(jsonObject.getString("nextVisit"));

                        if (jsonObject.getString("motherType").equalsIgnoreCase("AN")) {
                            Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("motherType"));
                            Log.e(mothers.class.getSimpleName(),i+"VisitANMotherRecords api call start");

                            pnmMotherListRealmModel.setGestAge(jsonObject.getString("gestAge"));
                            pnmMotherListRealmModel.setmWeight(jsonObject.getString("mWeight"));
                            getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                        } else if (jsonObject.getString("motherType").equalsIgnoreCase("PN")) {
                            Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("motherType"));
                            Log.e(mothers.class.getSimpleName(),i+"VisitPNMotherRecords api call start");

                            pnmMotherListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                            pnmMotherListRealmModel.setdBirthDetails(jsonObject.getString("dBirthDetails"));
                            pnmMotherListRealmModel.setdBirthWeight(jsonObject.getString("dBirthWeight"));
                            pnmMotherListRealmModel.setMeturityWeek(jsonObject.getString("meturityWeek"));
                            pnmMotherListRealmModel.setPnVisit(jsonObject.getString("pnVisit"));

                            getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                        }
                    }
                    realm.commitTransaction();
//                    pDialog.dismiss();
                } else {
                }
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValuetoUI();
    }

    private void setValuetoUI() {
//        preferenceData.getFilterStatus();
        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;
        Log.w(AllMotherListActivity.class.getSimpleName(), "setValuetoUI is Internet Conection-" + checkNetwork.isNetworkAvailable());
        realm.beginTransaction();

        if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
            Log.w(AllMotherListActivity.class.getSimpleName(), AppConstants.MOTHER_LIST_TITLE);
            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN").findAll();
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
            Log.w(AllMotherListActivity.class.getSimpleName(), AppConstants.MOTHER_LIST_TITLE);
            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN")
                    .equalTo("mRiskStatus", "HIGH").findAll();
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
            Log.w(AllMotherListActivity.class.getSimpleName(), AppConstants.MOTHER_LIST_TITLE);
            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class)
                    .equalTo("mRiskStatus", "HIGH").findAll();
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
            Log.w(AllMotherListActivity.class.getSimpleName(), AppConstants.MOTHER_LIST_TITLE);
            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class)
                    .equalTo("motherType", "PN").findAll();
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("All Mother List")) {
            if (preferenceData.getHighRiskStatus()) {
                Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T");

                if (preferenceData.getTermister().equalsIgnoreCase("1-3")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->1");

                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel,
                            "HIGH", 1, 3, preferenceData.getDescendingStatus());
                } else if (preferenceData.getTermister().equalsIgnoreCase("4-6")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->2");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel,
                            "HIGH", 4, 6, preferenceData.getDescendingStatus());

                } else if (preferenceData.getTermister().equalsIgnoreCase("7-10")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->3");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel,
                            "HIGH", 7, 10, preferenceData.getDescendingStatus());

                } else if (preferenceData.getTermister().equalsIgnoreCase("All")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(),
                            "strHighRisk --> T strDescending -->T    Termister -->Null");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel,
                            "HIGH", 0, 0, preferenceData.getDescendingStatus());
                }
            } else {

                Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> F ");

                if (preferenceData.getTermister().equalsIgnoreCase("1-3")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->1");

                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel, "", 1, 3, preferenceData.getDescendingStatus());
                } else if (preferenceData.getTermister().equalsIgnoreCase("4-6")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->2");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel, "", 4, 6, preferenceData.getDescendingStatus());

                } else if (preferenceData.getTermister().equalsIgnoreCase("7-10")) {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->3");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel, "", 7, 10, preferenceData.getDescendingStatus());

                } else {
                    Log.w(AllMotherListActivity.class.getSimpleName(), "strHighRisk --> T strDescending -->T    Termister -->Null");
                    motherListAdapterRealmModel = setTermister(motherListAdapterRealmModel, "", 1, 10, preferenceData.getDescendingStatus());

                }
            }
        }
        if (motherListAdapterRealmModel.size() == 0) {
            mother_recycler_view.setVisibility(View.GONE);
            txt_no_records_found.setVisibility(View.VISIBLE);
        } else {
            mother_recycler_view.setVisibility(View.VISIBLE);
            txt_no_records_found.setVisibility(View.GONE);
        }
        for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            Log.w(AllMotherListActivity.class.getSimpleName(), "PNMMotherListRealmModel:--------" + i);
            PNMMotherListRealmModel model = motherListAdapterRealmModel.get(i);
            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setMPicmeId(model.getmPicmeId());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setMLatitude(model.getmLatitude());
            mresponseResult.setMLongitude(model.getmLongitude());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();
        realm.commitTransaction();
    }

    private RealmResults<PNMMotherListRealmModel> setTermister(RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel, String riskStatus, int s, int s1, boolean strDescending) {
        if (riskStatus.equalsIgnoreCase("HIGH")) {
            if (preferenceData.getVillageName().equalsIgnoreCase("All")) {
                if (!preferenceData.getTermister().equalsIgnoreCase("All")) {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", riskStatus).between("currentMonth", s, s1).findAll();
                } else {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", riskStatus).findAll();
                }
            } else {
                if (!preferenceData.getTermister().equalsIgnoreCase("All")) {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", riskStatus).between("currentMonth", s, s1).equalTo("mVillage", preferenceData.getVillageName()).findAll();
                } else {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", riskStatus).findAll();
                }
            }
        } else {
            if (preferenceData.getVillageName().equalsIgnoreCase("All")) {
                if (!preferenceData.getTermister().equalsIgnoreCase("All")) {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).between("currentMonth", s, s1).findAll();
                } else {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();

                }
            } else {
                if (!preferenceData.getTermister().equalsIgnoreCase("All")) {

                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).between("currentMonth", s, s1).equalTo("mVillage", preferenceData.getVillageName()).findAll();
                } else {
                    motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mVillage", preferenceData.getVillageName()).findAll();

                }
            }
        }
        if (strDescending) {
            motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.DESCENDING);
        } else {
            motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.ASCENDING);
        }
        return motherListAdapterRealmModel;
    }

    @Override
    public void showLoginError(String string) {

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }

    @Override
    public void makeCall(String mMotherMobile) {

        isDataUpdate = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getApplicationContext(), "Displaying Call permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }



    @Override
    public void onContactSelected(PNMotherListResponse.VhnAN_Mothers_List contact) {

    }
}
