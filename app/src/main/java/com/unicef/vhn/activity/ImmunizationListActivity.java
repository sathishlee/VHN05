package com.unicef.vhn.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.ImmunizationPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherList.AllMotherListActivity;
import com.unicef.vhn.adapter.ImmunizationListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.ImmunizationListResponseModel;
import com.unicef.vhn.realmDbModel.ANTT1RealmModel;
import com.unicef.vhn.realmDbModel.ImmuniationListRealmModel;
import com.unicef.vhn.realmDbModel.ImmunizationDeatilsListRealmModel;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.ImmunizationViews;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ImmunizationListActivity extends AppCompatActivity implements ImmunizationViews {
    String TAG = ImmunizationListActivity.class.getSimpleName();
    ProgressDialog pDialog;
    ImmunizationPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<ImmunizationListResponseModel.Immunization_list> immunization_lists;
    ImmunizationListResponseModel.Immunization_list immunizationList;

    private RecyclerView recyclerView;
    private TextView textView, txt_no_internet, txt_immu_list_count;
    private ImmunizationListAdapter immunizationListAdapter;
    private LinearLayout llFilter;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    ImmuniationListRealmModel immuniationListRealmModel;

    ArrayList<String> vhnVillageList;
    ArrayList<String> doseList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_immunization_list);
        showActionBar();
        initUI();
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Immunization Mother's List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void initUI() {
        checkNetwork = new CheckNetwork(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new ImmunizationPresenter(ImmunizationListActivity.this, this);

        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getImmunizationList(preferenceData.getVhnCode(), preferenceData.getVhnId(), "1");
        } else {
            isoffline = true;
        }

        immunization_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.immunization_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_immu_list_count = (TextView) findViewById(R.id.txt_immu_list_count);
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        immunizationListAdapter = new ImmunizationListAdapter(immunization_lists, ImmunizationListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ImmunizationListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(immunizationListAdapter);


        if (isoffline) {
            setValueToUI();
            txt_no_internet.setVisibility(View.VISIBLE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getImmunizationList(preferenceData.getVhnCode(), preferenceData.getVhnId(), "1");
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    isoffline = true;
                }
            }
        });
        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ImmunizationListActivity.this);
                dialog.setContentView(R.layout.dialog_immunization);
                final Spinner sp_village_wise, sp_dose_wise;
                sp_village_wise = (Spinner) dialog.findViewById(R.id.sp_village_wise);
                sp_dose_wise = (Spinner) dialog.findViewById(R.id.sp_dose_wise);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

                vhnVillageList = new ArrayList<>();

                realm.beginTransaction();
                RealmResults<ImmuniationListRealmModel> getDoseList = null;
                getDoseList = realm.where(ImmuniationListRealmModel.class).findAll();
                vhnVillageList.add("All");
                for (int i = 0; i < getDoseList.size(); i++) {
//                    strVillageName = getVillageList.get(0).getmVillage();
                    vhnVillageList.add(getDoseList.get(i).getmVillage());
                    Log.e("vhnVillageList", getDoseList.get(i).getmVillage());
                }
                realm.commitTransaction();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        vhnVillageList
                );
                /* android.R.layout.simple_spinner_item,*/
                sp_village_wise.setAdapter(adapter);


                sp_dose_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        preferenceData.setVillageName(vhnVillageList.get(position));
//                        preferenceData.setVillageNamePosition(position);
                        AppConstants.DOSE_NO_IMMUNIZATION = sp_dose_wise.getSelectedItem().toString();
//                        Toast.makeText(getApplicationContext(),sp_dose_wise.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp_village_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        AppConstants.VILLAGENAME_IMMUNIZATION = sp_village_wise.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), ImmunizationListActivity.class));
                        finish();
                    }
                });
                dialog.show();

            }
        });
    }


    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void getImmunizationListSuccess(String response) {
        Log.e(TAG, "Response Success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("immunization_list");
                RealmResults<ImmuniationListRealmModel> motherListAdapterRealmModel = realm.where(ImmuniationListRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(ImmuniationListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    realm.beginTransaction();       //create or open
                    for (int i = 0; i < jsonArray.length(); i++) {
                        immuniationListRealmModel = realm.createObject(ImmuniationListRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        immunizationList = new ImmunizationListResponseModel.Immunization_list();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        immuniationListRealmModel.setMName(jsonObject.getString("mName"));
                        immuniationListRealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        immuniationListRealmModel.setImmDoseNumber(jsonObject.getString("immDoseNumber"));
                        immuniationListRealmModel.setMid(jsonObject.getString("mid"));
                        immuniationListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                        immuniationListRealmModel.setImmId(jsonObject.getString("immId"));
                        immuniationListRealmModel.setImmDoseId(jsonObject.getString("immDoseId"));
                        immuniationListRealmModel.setImmDoseNumber(jsonObject.getString("immDoseNumber"));
                        immuniationListRealmModel.setImmActualDate(jsonObject.getString("immActualDate"));
                        immuniationListRealmModel.setImmOpvStatus(jsonObject.getString("immOpvStatus"));
                        immuniationListRealmModel.setImmPentanvalentStatus(jsonObject.getString("immPentanvalentStatus"));
                        immuniationListRealmModel.setImmRotaStatus(jsonObject.getString("immRotaStatus"));
                        immuniationListRealmModel.setImmIpvStatus(jsonObject.getString("immIpvStatus"));
                        immuniationListRealmModel.setImmCarePovidedDate(jsonObject.getString("immCarePovidedDate"));
                        immuniationListRealmModel.setmVillage(jsonObject.getString("mVillage"));

                    }
                    realm.commitTransaction();       //create or open

                } else {
//                    recyclerView.setVisibility(View.GONE);
//                    textView.setVisibility(View.VISIBLE);
                }
            } else {
                RealmResults<ImmuniationListRealmModel> motherListAdapterRealmModel = realm.where(ImmuniationListRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(ImmuniationListRealmModel.class);
                    }
                });
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        setValueToUI();

    }

    @Override
    public void getImmunizationListError(String response) {


        Log.e(TAG, "Response Error" + response);

    }

    @Override
    public void callMotherDetailsApi() {
        swipeRefreshLayout.setRefreshing(false);

        RealmResults<ImmuniationListRealmModel> immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", immuniationListRealmModels.size() + "");
        for (int i = 0; i < immuniationListRealmModels.size(); i++) {
            ImmuniationListRealmModel model = immuniationListRealmModels.get(i);
            pnMotherListPresenter.getSelectedImmuMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), model.getMid());
        }
    }


    //    ImmunizationDeatilsListRealmModel
    @Override
    public void getImmunizationDetailsSuccess(String response) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("immunization_list");
                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    RealmResults<ImmunizationDeatilsListRealmModel> immunizationDeatilsListRealmModelRealmResults = null;
                    immunizationDeatilsListRealmModelRealmResults = realm.where(ImmunizationDeatilsListRealmModel.class).findAll();
                    if (immunizationDeatilsListRealmModelRealmResults.size() != 0) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(ImmunizationDeatilsListRealmModel.class);
                            }
                        });
                    }
                    realm.beginTransaction();
                    ImmunizationDeatilsListRealmModel immunizationDeatilsListRealmModel = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        immunizationDeatilsListRealmModel = realm.createObject(ImmunizationDeatilsListRealmModel.class);
                        immunizationList = new ImmunizationListResponseModel.Immunization_list();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        immunizationDeatilsListRealmModel.setMid(jsonObject.getString("mid"));
                        immunizationDeatilsListRealmModel.setMName(jsonObject.getString("mName"));
                        immunizationDeatilsListRealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        immunizationDeatilsListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                        immunizationDeatilsListRealmModel.setImmId(jsonObject.getString("immId"));
                        immunizationDeatilsListRealmModel.setImmDoseId(jsonObject.getString("immDoseId"));
                        immunizationDeatilsListRealmModel.setImmDoseNumber(jsonObject.getString("immDoseNumber"));
                        immunizationDeatilsListRealmModel.setImmActualDate(jsonObject.getString("immActualDate"));
                        immunizationDeatilsListRealmModel.setImmOpvStatus(jsonObject.getString("immOpvStatus"));

                        immunizationDeatilsListRealmModel.setImmPentanvalentStatus(jsonObject.getString("immPentanvalentStatus"));
                        immunizationDeatilsListRealmModel.setImmRotaStatus(jsonObject.getString("immRotaStatus"));
                        immunizationDeatilsListRealmModel.setImmIpvStatus(jsonObject.getString("immIpvStatus"));

                        immunizationDeatilsListRealmModel.setImmDueDate(jsonObject.getString("immDueDate"));
                        immunizationDeatilsListRealmModel.setImmCarePovidedDate(jsonObject.getString("immCarePovidedDate"));
                        immunizationDeatilsListRealmModel.setmVillage(jsonObject.getString("mVillage"));
                    }
                    realm.commitTransaction();
                }
            } else {
                Log.e(TAG, "No Record found");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void getImmunizationDetailsError(String string) {
        swipeRefreshLayout.setRefreshing(false);
    }


    private void setValueToUI() {
        realm.beginTransaction();
        RealmResults<ImmuniationListRealmModel> immuniationListRealmModels = null;
        if (AppConstants.DOSE_NO_IMMUNIZATION.equalsIgnoreCase("All") && AppConstants.VILLAGENAME_IMMUNIZATION.equalsIgnoreCase("All")) {
            immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).findAll();
        } else if (AppConstants.DOSE_NO_IMMUNIZATION.equalsIgnoreCase("All") && !AppConstants.VILLAGENAME_IMMUNIZATION.equalsIgnoreCase("All")) {
            immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).equalTo("mVillage", AppConstants.VILLAGENAME_IMMUNIZATION).findAll();
        } else if (!AppConstants.DOSE_NO_IMMUNIZATION.equalsIgnoreCase("All") && AppConstants.VILLAGENAME_IMMUNIZATION.equalsIgnoreCase("All")) {
            immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).equalTo("immDoseNumber", AppConstants.DOSE_NO_IMMUNIZATION).findAll();
        } else {
            immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).equalTo("mVillage", AppConstants.VILLAGENAME_IMMUNIZATION).equalTo("immDoseNumber", AppConstants.DOSE_NO_IMMUNIZATION).findAll();
        }

        Log.e("ANTT1 list size ->", immuniationListRealmModels.size() + "");
        if (immuniationListRealmModels.size() != 0) {
            for (int i = 0; i < immuniationListRealmModels.size(); i++) {
                immunizationList = new ImmunizationListResponseModel.Immunization_list();
                ImmuniationListRealmModel model = immuniationListRealmModels.get(i);
                immunizationList.setMName(model.getMName());
                immunizationList.setMPicmeId(model.getMPicmeId());
                immunizationList.setImmDoseNumber(model.getImmDoseNumber());
                immunizationList.setMid(model.getMid());
                immunization_lists.add(immunizationList);
                txt_immu_list_count.setText(getResources().getString(R.string.immunization_lists) + "(" + immunization_lists.size() + ")");
                immunizationListAdapter.notifyDataSetChanged();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        realm.commitTransaction();

    }
}
