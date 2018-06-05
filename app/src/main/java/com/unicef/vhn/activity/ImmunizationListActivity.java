package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ImmunizationListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.ImmunizationListResponseModel;
import com.unicef.vhn.realmDbModel.ANTT1RealmModel;
import com.unicef.vhn.realmDbModel.ImmuniationListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
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

public class ImmunizationListActivity extends AppCompatActivity implements MotherListsViews {

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<ImmunizationListResponseModel.Immunization_list> immunization_lists;
    ImmunizationListResponseModel.Immunization_list immunizationList;

    private RecyclerView recyclerView;
    private TextView textView;
    private ImmunizationListAdapter immunizationListAdapter;



    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    ImmuniationListRealmModel immuniationListRealmModel;

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
        Intent intent = new Intent(ImmunizationListActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void initUI() {
        checkNetwork =new CheckNetwork(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(ImmunizationListActivity.this, this);

        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getImmunizationList(preferenceData.getVhnCode(), preferenceData.getVhnId(), "1");
        }else{
            isoffline =true;
        }

        immunization_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.immunization_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        immunizationListAdapter = new ImmunizationListAdapter(immunization_lists, ImmunizationListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ImmunizationListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(immunizationListAdapter);


        if (isoffline) {
//            showOfflineData();
            setValueToUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
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
    public void showLoginSuccess(String response) {
        Log.e(ImmunizationListActivity.class.getSimpleName(), "Response Success" + response);

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
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    realm.beginTransaction();       //create or open

                    for (int i = 0; i < jsonArray.length(); i++) {
                        immuniationListRealmModel = realm.createObject(ImmuniationListRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                        immunizationList = new ImmunizationListResponseModel.Immunization_list();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                       /* immunizationList.setMName(jsonObject.getString("mName"));
                        immunizationList.setMPicmeId(jsonObject.getString("mPicmeId"));
                        immunizationList.setImmDoseNumber(jsonObject.getString("immDoseNumber"));
                        immunizationList.setMid(jsonObject.getString("mid"));

                        immunization_lists.add(immunizationList);
                        immunizationListAdapter.notifyDataSetChanged();*/



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

                    }
                    realm.commitTransaction();       //create or open

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

        setValueToUI();

    }

    private void setValueToUI() {



        realm.beginTransaction();
        RealmResults<ImmuniationListRealmModel> immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", immuniationListRealmModels.size() + "");
        for (int i = 0; i < immuniationListRealmModels.size(); i++) {
            immunizationList = new ImmunizationListResponseModel.Immunization_list();

            ImmuniationListRealmModel model = immuniationListRealmModels.get(i);

            immunizationList.setMName(model.getMName());
            immunizationList.setMPicmeId(model.getMPicmeId());
            immunizationList.setImmDoseNumber(model.getImmDoseNumber());
            immunizationList.setMid(model.getMid());

            immunization_lists.add(immunizationList);
            immunizationListAdapter.notifyDataSetChanged();
        }

        realm.commitTransaction();

    }


    private void showOfflineData() {



        Log.e(ANTT1MothersList.class.getSimpleName(),"ON LINE ");

        realm.beginTransaction();
        RealmResults<ImmuniationListRealmModel> immuniationListRealmModels = realm.where(ImmuniationListRealmModel.class).findAll();
        Log.e("ANTT1 list size ->", immuniationListRealmModels.size() + "");
        for (int i = 0; i < immuniationListRealmModels.size(); i++) {
            immunizationList = new ImmunizationListResponseModel.Immunization_list();

            ImmuniationListRealmModel model = immuniationListRealmModels.get(i);
            immunizationList.setMName(model.getMName());
            immunizationList.setMPicmeId(model.getMPicmeId());
            immunizationList.setImmDoseNumber(model.getImmDoseNumber());
            immunizationList.setMid(model.getMid());

            immunization_lists.add(immunizationList);
            immunizationListAdapter.notifyDataSetChanged();
        }

        realm.commitTransaction();


    }

    @Override
    public void showLoginError(String string) {
        Log.e(ImmunizationListActivity.class.getSimpleName(), "Response Error" + string);
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
