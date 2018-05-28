package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.PNHBNCDueAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.PNHBNCDueListModel;
import com.unicef.vhn.realmDbModel.PNHBNCDueListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNHBNCDueListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNHBNCDueListModel.VPNHBNC_List> tt1_lists;
    PNHBNCDueListModel.VPNHBNC_List tt1List;
    private List<String> visit;
    boolean isDataUpdate = true;
    private RecyclerView recyclerView;
    private TextView textView;
    private PNHBNCDueAdapter antt1Adapter;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    PNHBNCDueListRealmModel pnhbncDueListRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.pnhbnc_mothers_list_activity);
        showActionBar();
        initUI();


    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN/HBNC Mothers List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
checkNetwork =new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(PNHBNCDueListActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNHBNCDUEMotherList(preferenceData.getVhnCode(), preferenceData.getVhnId(), "1");
        }else{
            isoffline = true ;
                    }
        tt1_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        antt1Adapter = new PNHBNCDueAdapter(tt1_lists, PNHBNCDueListActivity.this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PNHBNCDueListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(antt1Adapter);
        if (isoffline){
            showOfflineData();
        }else{
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
        pDialog.dismiss();

    }

    @Override
    public void showLoginSuccess(String response) {


        Log.e(ANTT1MothersList.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vPNHBNC_List");

                RealmQuery<PNHBNCDueListRealmModel> pnhbncDueListRealmModels = realm.where(PNHBNCDueListRealmModel.class);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNHBNCDueListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                         pnhbncDueListRealmModel= realm.createObject(PNHBNCDueListRealmModel.class);

                        tt1List = new PNHBNCDueListModel.VPNHBNC_List();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        pnhbncDueListRealmModel.setMotherName(jsonObject.getString("motherName"));
                        pnhbncDueListRealmModel.setPicmeId(jsonObject.getString("picmeId"));
                        pnhbncDueListRealmModel.setMobile(jsonObject.getString("mobile"));
                        pnhbncDueListRealmModel.setVisit1(jsonObject.getString("visit1"));
                        pnhbncDueListRealmModel.setVisit2(jsonObject.getString("visit2"));
                        pnhbncDueListRealmModel.setVisit3(jsonObject.getString("visit3"));
                        pnhbncDueListRealmModel.setVisit4(jsonObject.getString("visit4"));
                        pnhbncDueListRealmModel.setVisit5(jsonObject.getString("visit5"));
                        pnhbncDueListRealmModel.setVisit6(jsonObject.getString("visit6"));
                        pnhbncDueListRealmModel.setVisit7(jsonObject.getString("visit7"));


                 /*       tt1List.setMotherName(jsonObject.getString("motherName"));
                        tt1List.setPicmeId(jsonObject.getString("picmeId"));
                        tt1List.setMobile(jsonObject.getString("mobile"));
                        tt1List.setVisit1(jsonObject.getString("visit1"));
                        tt1List.setVisit2(jsonObject.getString("visit2"));
                        tt1List.setVisit3(jsonObject.getString("visit3"));
                        tt1List.setVisit4(jsonObject.getString("visit4"));
                        tt1List.setVisit5(jsonObject.getString("visit5"));
                        tt1List.setVisit6(jsonObject.getString("visit6"));
                        tt1List.setVisit7(jsonObject.getString("visit7"));
                        tt1_lists.add(tt1List);
                        antt1Adapter.notifyDataSetChanged();*/
                    }
                    realm.commitTransaction();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();

    }

    private void setValueToUI() {
        Log.d(PNHBNCDueListActivity.class.getSimpleName(),"On Line");

        realm.beginTransaction();
        RealmResults<PNHBNCDueListRealmModel> realmModelRealmResults =realm.where(PNHBNCDueListRealmModel.class).findAll();
        for (int i=0;i<realmModelRealmResults.size();i++){
            tt1List  =new PNHBNCDueListModel.VPNHBNC_List();

            PNHBNCDueListRealmModel model = realmModelRealmResults.get(i);
            tt1List.setMotherName(model.getMotherName());
            tt1List.setPicmeId(model.getPicmeId());
            tt1List.setMobile(model.getMobile());
            tt1List.setVisit1(model.getVisit1());
            tt1List.setVisit2(model.getVisit2());
            tt1List.setVisit3(model.getVisit3());
            tt1List.setVisit4(model.getVisit4());
            tt1List.setVisit5(model.getVisit5());
            tt1List.setVisit6(model.getVisit6());
            tt1List.setVisit7(model.getVisit7());
            tt1_lists.add(tt1List);
            antt1Adapter.notifyDataSetChanged();
        }
        realm.commitTransaction();
    }

    private void showOfflineData() {

        Log.d(PNHBNCDueListActivity.class.getSimpleName(),"Off Line");

        realm.beginTransaction();
        RealmResults<PNHBNCDueListRealmModel> realmModelRealmResults =realm.where(PNHBNCDueListRealmModel.class).findAll();
        for (int i=0;i<realmModelRealmResults.size();i++){
            tt1List  =new PNHBNCDueListModel.VPNHBNC_List();

            PNHBNCDueListRealmModel model = realmModelRealmResults.get(i);
            tt1List.setMotherName(model.getMotherName());
            tt1List.setPicmeId(model.getPicmeId());
            tt1List.setMobile(model.getMobile());
            tt1List.setVisit1(model.getVisit1());
            tt1List.setVisit2(model.getVisit2());
            tt1List.setVisit3(model.getVisit3());
            tt1List.setVisit4(model.getVisit4());
            tt1List.setVisit5(model.getVisit5());
            tt1List.setVisit6(model.getVisit6());
            tt1List.setVisit7(model.getVisit7());
            tt1_lists.add(tt1List);
            antt1Adapter.notifyDataSetChanged();
        }
        realm.commitTransaction();

    }

    @Override
    public void showLoginError(String string) {
        Log.e(ANTT1MothersList.class.getSimpleName(), "Response Error" + string);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }

    @Override
    public void makeCall(String response) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + response)));
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
}
