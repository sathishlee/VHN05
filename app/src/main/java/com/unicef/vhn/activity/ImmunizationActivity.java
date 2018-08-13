package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.ImmunizationPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ImmunizationAdapter;
import com.unicef.vhn.adapter.ImmunizationListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ImmunizationListResponseModel;
import com.unicef.vhn.realmDbModel.ImmuniationListRealmModel;
import com.unicef.vhn.realmDbModel.ImmunizationDeatilsListRealmModel;
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

public class ImmunizationActivity extends AppCompatActivity implements ImmunizationViews {
//implements MotherListsViews

    ProgressDialog pDialog;
//    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<ImmunizationListResponseModel.Immunization_list> immunization_lists;
    ImmunizationListResponseModel.Immunization_list immunizationList;

    private RecyclerView recyclerView;
    private TextView textView,txt_no_internet;
    private ImmunizationAdapter immunizationAdapter;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    ImmunizationPresenter pnMotherListPresenter;


Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_immunization_details);
        showActionBar();
        initUI();
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mother Dose List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        checkNetwork =new CheckNetwork(this);
//        pnMotherListPresenter = new MotherListPresenter(ImmunizationActivity.this, this);
//        pnMotherListPresenter.getSelectedImmuMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);


        pnMotherListPresenter = new ImmunizationPresenter(ImmunizationActivity.this, this);
        if (checkNetwork.isNetworkAvailable()){
            pnMotherListPresenter.getSelectedImmuMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);
        }else{
            isoffline=true;
        }

        immunization_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        immunizationAdapter = new ImmunizationAdapter(immunization_lists, ImmunizationActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ImmunizationActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(immunizationAdapter);

if (isoffline) {
//        getRealmData();
    txt_no_internet.setVisibility(View.VISIBLE);
}else{
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Record Not Found");
    builder.create();
}
    }

    private void getRealmData() {
        Log.e(ImmunizationActivity.class.getSimpleName(),"  AppConstants.SELECTED_MID "+  AppConstants.SELECTED_MID );

        realm.beginTransaction();
        RealmResults<ImmunizationDeatilsListRealmModel> immuniationListRealmModels = realm.where(ImmunizationDeatilsListRealmModel.class).equalTo("mid",AppConstants.SELECTED_MID).findAll();
        Log.e(ImmunizationActivity.class.getSimpleName(),"Immunization realm size ->"+ immuniationListRealmModels.size() + "");
        for (int i = 0; i < immuniationListRealmModels.size(); i++) {
            immunizationList = new ImmunizationListResponseModel.Immunization_list();

            ImmunizationDeatilsListRealmModel model = immuniationListRealmModels.get(i);

            immunizationList.setMid(model.getMid());

            immunizationList.setMid(model.getMid());
            immunizationList.setMName(model.getMName());
            immunizationList.setMPicmeId(model.getMPicmeId());
            immunizationList.setDeleveryDate(model.getDeleveryDate());
            immunizationList.setImmDoseId(model.getImmDoseId());
            immunizationList.setImmDoseNumber(model.getImmDoseNumber());
            immunizationList.setImmActualDate(model.getImmActualDate());
            immunizationList.setImmOpvStatus(model.getImmOpvStatus());
            immunizationList.setImmPentanvalentStatus(model.getImmPentanvalentStatus());
            immunizationList.setImmRotaStatus(model.getImmRotaStatus());
            immunizationList.setImmIpvStatus(model.getImmIpvStatus());
            immunizationList.setImmDueDate(model.getImmDueDate());
            immunizationList.setImmCarePovidedDate(model.getImmCarePovidedDate());

//            immunization_lists.add(immunizationList);
//            immunizationAdapter.notifyDataSetChanged();
        }
        Log.e(ImmunizationActivity.class.getSimpleName(),"immunization_lists  size ->"+ immunization_lists.size() + "");

        realm.commitTransaction();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
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
    public void getImmunizationListSuccess(String response) {

    }

    @Override
    public void getImmunizationListError(String string) {

    }

    @Override
    public void callMotherDetailsApi() {

    }

    @Override
    public void getImmunizationDetailsSuccess(String response) {


        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("immunization_list");
                Log.e(ImmunizationActivity.class.getSimpleName(),"immunization_list size success "+jsonArray.length());
                if (jsonArray.length() != 0) {
                    /*
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    RealmResults<ImmunizationDeatilsListRealmModel> immunizationDeatilsListRealmModelRealmResults=null;
                    immunizationDeatilsListRealmModelRealmResults = realm.where(ImmunizationDeatilsListRealmModel.class).findAll();
                    if (immunizationDeatilsListRealmModelRealmResults.size()!=0) {
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
               */
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        immunizationList = new ImmunizationListResponseModel.Immunization_list();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        immunizationList.setMid(jsonObject.getString("mid"));
                        immunizationList.setMName(jsonObject.getString("mName"));
                        immunizationList.setMPicmeId(jsonObject.getString("mPicmeId"));
                        immunizationList.setDeleveryDate(jsonObject.getString("deleveryDate"));
                        immunizationList.setImmId(jsonObject.getString("immId"));
                        immunizationList.setImmDoseId(jsonObject.getString("immDoseId"));
                        immunizationList.setImmDoseNumber(jsonObject.getString("immDoseNumber"));
                        immunizationList.setImmActualDate(jsonObject.getString("immActualDate"));
                        immunizationList.setImmOpvStatus(jsonObject.getString("immOpvStatus"));

                        immunizationList.setImmPentanvalentStatus(jsonObject.getString("immPentanvalentStatus"));
                        immunizationList.setImmRotaStatus(jsonObject.getString("immRotaStatus"));
                        immunizationList.setImmIpvStatus(jsonObject.getString("immIpvStatus"));

                        immunizationList.setImmDueDate(jsonObject.getString("immDueDate"));
                        immunizationList.setImmCarePovidedDate(jsonObject.getString("immCarePovidedDate"));
                        immunizationList.setmVillage(jsonObject.getString("mVillage"));

                        immunization_lists.add(immunizationList);
                        immunizationAdapter.notifyDataSetChanged();
                    }
                    Log.e(ImmunizationActivity.class.getSimpleName(),"immunization_lists size"+immunization_lists.size());
                }
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
//        getRealmData();
    }

    @Override
    public void getImmunizationDetailsError(String string) {

    }

  /*  @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoginSuccess(String response) {

        AppConstants.SELECTED_MID = "0";
        Log.e(ImmunizationActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("immunization_list");
                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        immunizationList = new ImmunizationListResponseModel.Immunization_list();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        immunizationList.setImmDueDate(jsonObject.getString("immDueDate"));
                        immunizationList.setImmCarePovidedDate(jsonObject.getString("immCarePovidedDate"));
                        immunizationList.setImmOpvStatus(jsonObject.getString("immOpvStatus"));
                        immunizationList.setImmPentanvalentStatus(jsonObject.getString("immPentanvalentStatus"));
                        immunizationList.setImmRotaStatus(jsonObject.getString("immRotaStatus"));
                        immunizationList.setImmIpvStatus(jsonObject.getString("immIpvStatus"));

                        immunization_lists.add(immunizationList);
                        immunizationAdapter.notifyDataSetChanged();
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }else{
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoginError(String message) {
        AppConstants.SELECTED_MID = "0";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }*/
}
