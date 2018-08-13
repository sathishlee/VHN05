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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
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
        MakeCallInterface, MotherListAdapter.ContactsAdapterListener, VisitANMotherViews, RadioGroup.OnCheckedChangeListener {

    ProgressDialog pDialog;
    ProgressBar cus_progress;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;

    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private TextView txt_no_records_found, txt_no_internet,txt_total_of_list;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;
    final Context context = this;
    TextView txt_filter;
    ImageView cardview_image;
    CheckNetwork checkNetwork;
    boolean isOffline;
    Realm realm;
    boolean strHighRisk = false;
    String strVillageName = "";
    ArrayList<String> vhnVillageList;
    ArrayList<String> termisterlist;
    LinearLayout ll_filter_block;
    GetVisitANMotherPresenter getVisitANMotherPresenter;
    SwipeRefreshLayout swipeRefreshLayout;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2,radioButton3;

    String selectedUserType="All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_all_mother_list);
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");

        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(getApplicationContext(), this, realm);

        getVisitANMotherPresenter = new GetVisitANMotherPresenter(AllMotherListActivity.this, this, realm);

        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST,
                    preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isOffline = true;
        }

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        ll_filter_block = (LinearLayout) findViewById(R.id.ll_filter_block);
        if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
            ll_filter_block.setVisibility(View.VISIBLE);
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
//            ll_filter_block.setVisibility(View.GONE);
            ll_filter_block.setVisibility(View.VISIBLE);
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
            ll_filter_block.setVisibility(View.GONE);
        } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
//            ll_filter_block.setVisibility(View.GONE);
            ll_filter_block.setVisibility(View.VISIBLE);
        } else {
            ll_filter_block.setVisibility(View.VISIBLE);
        }
        radioGroup =(RadioGroup)findViewById(R.id.rg_user_type);
        radioButton1 = (RadioButton) findViewById(R.id.rb_user_all);
        radioButton2 = (RadioButton) findViewById(R.id.rb_user_android);
        radioButton3 = (RadioButton) findViewById(R.id.rb_user_non_android);
        radioButton1.setChecked(true);

        txt_filter = (TextView) findViewById(R.id.txt_filter);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_total_of_list = (TextView) findViewById(R.id.txt_total_of_list);
        txt_no_internet.setVisibility(View.GONE);
        cus_progress = (ProgressBar) findViewById(R.id.cus_progress);
        mResult = new ArrayList<>();

        mAdapter = new MotherListAdapter(mResult, AllMotherListActivity.this, "AN", this, this);

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
                CheckBox ch_highRisk, ch_desc, ch_anmother, ch_pnmother,ch_android,ch_non_android;
                Spinner sp_village_wise;
                LinearLayout llTrimester;
                RadioGroup rg_trim;
                RadioButton rb_trim_all, rb_trim_first, rb_trim_second, rb_trim_thrid;
                rg_trim = (RadioGroup) dialog.findViewById(R.id.rg_trim);
                ch_highRisk = (CheckBox) dialog.findViewById(R.id.ch_high_risk);
                ch_desc = (CheckBox) dialog.findViewById(R.id.ch_desc);

                ch_android = (CheckBox) dialog.findViewById(R.id.ch_android);
                ch_non_android = (CheckBox) dialog.findViewById(R.id.ch_non_android);

                ch_anmother = (CheckBox) dialog.findViewById(R.id.ch_anmother);
                ch_pnmother = (CheckBox) dialog.findViewById(R.id.ch_pnmother);
                sp_village_wise = (Spinner) dialog.findViewById(R.id.sp_village_wise);
                rb_trim_all = (RadioButton) dialog.findViewById(R.id.rb_trim_all);
                rb_trim_first = (RadioButton) dialog.findViewById(R.id.rb_trim_first);
                rb_trim_second = (RadioButton) dialog.findViewById(R.id.rb_trim_second);
                rb_trim_thrid = (RadioButton) dialog.findViewById(R.id.rb_trim_thrid);
                llTrimester = (LinearLayout) dialog.findViewById(R.id.ll_trimester);
                if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
                    llTrimester.setVisibility(View.GONE);
                } else {
                    llTrimester.setVisibility(View.VISIBLE);

                }
                if (preferenceData.getTermisterPosition() == 0) {
                    rb_trim_all.setChecked(true);
                } else if (preferenceData.getTermisterPosition() == 1) {
                    rb_trim_first.setChecked(true);
                } else if (preferenceData.getTermisterPosition() == 2) {
                    rb_trim_second.setChecked(true);
                } else if (preferenceData.getTermisterPosition() == 3) {
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
                if (preferenceData.getAndroidUser()) {
                    ch_desc.setChecked(true);
                }if (preferenceData.getNonAndroidUser()) {
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


                ch_android.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        strHighRisk = isChecked;
                        preferenceData.setAndroidUser(isChecked);
                    }
                });
                ch_non_android.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        preferenceData.setNonAndroidUser(isChecked);

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
                            } else if (rb.getText().toString().equalsIgnoreCase("1st")) {
                                preferenceData.setTermister("1-3");
                                preferenceData.setTermisterPosition(1);
                            }
                            if (rb.getText().toString().equalsIgnoreCase("2nd")) {
                                preferenceData.setTermister("4-6");
                                preferenceData.setTermisterPosition(2);
                            }
                            if (rb.getText().toString().equalsIgnoreCase("3rd")) {
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
                RealmResults<PNMMotherListRealmModel> getVillageList = null;
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                radioButton1.setChecked(true);
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST,
                            preferenceData.getVhnCode(), preferenceData.getVhnId());
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    isOffline = true;
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(this);

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
        swipeRefreshLayout.setRefreshing(false);

        Log.d(AllMotherListActivity.class.getSimpleName(), "ANVisitRecordsSuccess api call success");

    }

    @Override
    public void showANVisitRecordsFailiur(String response) {
        swipeRefreshLayout.setRefreshing(false);

        Log.e(mothers.class.getSimpleName(), "ANMother visit records api failiur");
    }

    @Override
    public void showPNVisitRecordsSuccess(String response) {
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void showPNVisitRecordsFailiur(String response) {
        swipeRefreshLayout.setRefreshing(false);

        Log.e(mothers.class.getSimpleName(), "showPNVisitRecords failiur" + response);

    }

    @Override
    public void showLoginSuccess(String response) {
        swipeRefreshLayout.setRefreshing(false);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.getString("motherType").equalsIgnoreCase("AN")) {
                            /* don't delete below code, is called mother an visit report
                            getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));*/

                        } else if (jsonObject.getString("motherType").equalsIgnoreCase("PN")) {


                            /*
                            don't delete below code, is called mother an visit report
                           getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));*/
                        }
                    }
                } else {
                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValuetoUI();
    }

    private void setValuetoUI() {
        mResult.clear();
        if (realm.isInTransaction()) {
            pDialog.show();
            realm.cancelTransaction();
        }
        realm.beginTransaction();
        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;

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
            cus_progress.setVisibility(View.VISIBLE);

        } else {
            mother_recycler_view.setVisibility(View.VISIBLE);
            txt_no_records_found.setVisibility(View.GONE);
            cus_progress.setVisibility(View.GONE);
        }
        for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            PNMMotherListRealmModel model = motherListAdapterRealmModel.get(i);
            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setMPicmeId(model.getmPicmeId());
            mresponseResult.setmAge(model.getmAge());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setMLatitude(model.getmLatitude());
            mresponseResult.setMLongitude(model.getmLongitude());
            mresponseResult.setmPhoto(model.getmPhoto());
            mresponseResult.setUserType(model.getUserType());

            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();
        txt_total_of_list.setText(getResources().getString(R.string.mother_s_list)+"("+mResult.size()+")");

        realm.commitTransaction();
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
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
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        RadioButton radioButton = (RadioButton)findViewById(checkedId);
//        Toast.makeText(getApplicationContext(),radioButton.getText().toString(),Toast.LENGTH_SHORT).show();



        if (checkedId == R.id.rb_user_all) {
            AppConstants.selectedUserType ="All";
            mResult.clear();
            if (realm.isInTransaction()) {
                pDialog.show();
                realm.cancelTransaction();
            }
            realm.beginTransaction();
            RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;
            if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "PN").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("All Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN")
                        .equalTo("mRiskStatus", "HIGH").findAll();
            }
            else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class)
                        .equalTo("mRiskStatus", "HIGH").findAll();

            }



            if (motherListAdapterRealmModel.size() == 0) {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
                cus_progress.setVisibility(View.VISIBLE);

            } else {
                mother_recycler_view.setVisibility(View.VISIBLE);
                txt_no_records_found.setVisibility(View.GONE);
                cus_progress.setVisibility(View.GONE);
            }

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                PNMMotherListRealmModel model = motherListAdapterRealmModel.get(i);
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMName(model.getmName());
                mresponseResult.setMPicmeId(model.getmPicmeId());
                mresponseResult.setmAge(model.getmAge());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setmMotherMobile(model.getmMotherMobile());
                mresponseResult.setMotherType(model.getMotherType());
                mresponseResult.setMLatitude(model.getmLatitude());
                mresponseResult.setMLongitude(model.getmLongitude());
                mresponseResult.setmPhoto(model.getmPhoto());
                mresponseResult.setUserType(model.getUserType());

                mResult.add(mresponseResult);
            }
            mAdapter.notifyDataSetChanged();
            txt_total_of_list.setText(getResources().getString(R.string.mother_s_list)+"("+mResult.size()+")");

            realm.commitTransaction();
            //some code
            if (pDialog.isShowing()) {
                pDialog.hide();
            }
        } else if (checkedId == R.id.rb_user_android) {
            AppConstants.selectedUserType ="Android";
            mResult.clear();
            if (realm.isInTransaction()) {
                pDialog.show();
                realm.cancelTransaction();
            }
            realm.beginTransaction();
            RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;



            if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN").equalTo("userType","1").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "PN").equalTo("userType","1").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("All Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("userType","1").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN")
                        .equalTo("mRiskStatus", "HIGH").equalTo("userType","1").findAll();
            }
            else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class)
                        .equalTo("mRiskStatus", "HIGH").equalTo("userType","1").findAll();

            }

            if (motherListAdapterRealmModel.size() == 0) {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
                cus_progress.setVisibility(View.VISIBLE);

            } else {
                mother_recycler_view.setVisibility(View.VISIBLE);
                txt_no_records_found.setVisibility(View.GONE);
                cus_progress.setVisibility(View.GONE);
            }





//            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                PNMMotherListRealmModel model = motherListAdapterRealmModel.get(i);
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMName(model.getmName());
                mresponseResult.setMPicmeId(model.getmPicmeId());
                mresponseResult.setmAge(model.getmAge());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setmMotherMobile(model.getmMotherMobile());
                mresponseResult.setMotherType(model.getMotherType());
                mresponseResult.setMLatitude(model.getmLatitude());
                mresponseResult.setMLongitude(model.getmLongitude());
                mresponseResult.setmPhoto(model.getmPhoto());
                mresponseResult.setUserType(model.getUserType());

                mResult.add(mresponseResult);
            }
            mAdapter.notifyDataSetChanged();
            txt_total_of_list.setText(getResources().getString(R.string.mother_s_list)+"("+mResult.size()+")");

            realm.commitTransaction();
            if (pDialog.isShowing()) {
                pDialog.hide();
            }
            //some code
        }else if (checkedId == R.id.rb_user_non_android) {
            AppConstants. selectedUserType="Non Android";
            //some code

            mResult.clear();
            if (realm.isInTransaction()) {
                pDialog.show();
                realm.cancelTransaction();
            }
            realm.beginTransaction();
            RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;

            if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN").equalTo("userType","0").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("PN/HBNC Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "PN").equalTo("userType","0").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("All Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("userType","0").findAll();
            }else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("AN High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("motherType", "AN")
                        .equalTo("mRiskStatus", "HIGH").equalTo("userType","0").findAll();
            } else if (AppConstants.MOTHER_LIST_TITLE.equalsIgnoreCase("High Risk Mother List")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class)
                        .equalTo("mRiskStatus", "HIGH").equalTo("userType","0").findAll();

            }


            if (motherListAdapterRealmModel.size() == 0) {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
                cus_progress.setVisibility(View.VISIBLE);

            } else {
                mother_recycler_view.setVisibility(View.VISIBLE);
                txt_no_records_found.setVisibility(View.GONE);
                cus_progress.setVisibility(View.GONE);
            }

//            motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("userType","0").findAll();
            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                PNMMotherListRealmModel model = motherListAdapterRealmModel.get(i);
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMName(model.getmName());
                mresponseResult.setMPicmeId(model.getmPicmeId());
                mresponseResult.setmAge(model.getmAge());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setmMotherMobile(model.getmMotherMobile());
                mresponseResult.setMotherType(model.getMotherType());
                mresponseResult.setMLatitude(model.getmLatitude());
                mresponseResult.setMLongitude(model.getmLongitude());
                mresponseResult.setmPhoto(model.getmPhoto());
                mresponseResult.setUserType(model.getUserType());

                mResult.add(mresponseResult);
            }
            mAdapter.notifyDataSetChanged();
            txt_total_of_list.setText(getResources().getString(R.string.mother_s_list)+"("+mResult.size()+")");

            realm.commitTransaction();

            if (pDialog.isShowing()) {
                pDialog.hide();
            }

        }
    }


}
