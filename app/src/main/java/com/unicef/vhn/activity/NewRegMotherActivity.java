package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.NewRegMotherPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ANTT1Adapter;
import com.unicef.vhn.adapter.NewMothersListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.responseModel.NewRegMotherResponseModel;
import com.unicef.vhn.realmDbModel.ANTT1RealmModel;
import com.unicef.vhn.realmDbModel.NewRegMotherRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.NewRegMotherView;
import io.realm.Realm;
import io.realm.RealmResults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewRegMotherActivity extends AppCompatActivity implements NewRegMotherView, MakeCallInterface {
SwipeRefreshLayout swipe_refresh_layout;
TextView txt_no_internet,txt_no_records_found;
RecyclerView mother_recycler_view;

    ProgressDialog pDialog;
    PreferenceData preferenceData;
    NewRegMotherPresenter newRegMotherPresenter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private List<NewRegMotherResponseModel.Result> resultsLists;
    NewRegMotherResponseModel.Result result;

    NewMothersListAdapter newMothersListAdapter;
    final Context context = this;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;

    NewRegMotherRealmModel newRegMotherRealmModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reg_mother);
        Log.e(NewRegMotherActivity.class.getSimpleName(),"Activity created");

        showActionBar();
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        checkNetwork =new CheckNetwork(this);
        preferenceData = new PreferenceData(this);
        newRegMotherPresenter = new NewRegMotherPresenter(NewRegMotherActivity.this,NewRegMotherActivity.this);
        if (checkNetwork.isNetworkAvailable()) {
            newRegMotherPresenter.getAllRegisterMother(preferenceData.getVhnId());
        }else{
            isoffline=true;
        }

        swipe_refresh_layout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        txt_no_internet =(TextView) findViewById(R.id.txt_no_internet);
        txt_no_records_found =(TextView)findViewById(R.id.txt_no_records_found);
        txt_no_records_found.setText("Please wait...");
        mother_recycler_view = (RecyclerView)findViewById(R.id.mother_recycler_view);

        resultsLists = new ArrayList<>();
        newMothersListAdapter = new NewMothersListAdapter(resultsLists, NewRegMotherActivity.this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewRegMotherActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(newMothersListAdapter);

        if (isoffline){
            txt_no_internet.setVisibility(View.VISIBLE);
            setValueToUI();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    newRegMotherPresenter.getAllRegisterMother(preferenceData.getVhnId());
                }else{
                    swipe_refresh_layout.setRefreshing(false);
                    isoffline = true;
                    txt_no_internet.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Arival mothers");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
pDialog.hide();
    }

    @Override
    public void showRegMotherSuccess(String response) {
        swipe_refresh_layout.setRefreshing(false);
        Log.e(NewRegMotherActivity.class.getSimpleName(),"Success ->"+response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("result");
//                RealmResults<NewRegMotherRealmModel> motherListAdapterRealmModel = realm.where(NewRegMotherRealmModel.class).findAll();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(NewRegMotherRealmModel.class);
                    }
                });
                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
                    realm.beginTransaction();       //create or open
                    for (int i = 0; i < jsonArray.length(); i++) {
                        newRegMotherRealmModel = realm.createObject(NewRegMotherRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        result = new NewRegMotherResponseModel.Result();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        newRegMotherRealmModel.setmName(jsonObject.getString("mName"));
                        newRegMotherRealmModel.setmMobileNumber(jsonObject.getString("mMobileNumber"));
                        newRegMotherRealmModel.setmDOB(jsonObject.getString("mDOB"));
                        newRegMotherRealmModel.setmHusbandName(jsonObject.getString("mHusbandName"));
                        newRegMotherRealmModel.setmHusbandMobile(jsonObject.getString("mHusbandMobile"));
                        newRegMotherRealmModel.setStatus(jsonObject.getString("status"));
                        newRegMotherRealmModel.setPhcid(jsonObject.getString("phcid"));
                        newRegMotherRealmModel.setDeviceToken(jsonObject.getString("deviceToken"));
                        newRegMotherRealmModel.setBkid(jsonObject.getString("bkid"));
                        newRegMotherRealmModel.setdCode(jsonObject.getString("dCode"));
                        newRegMotherRealmModel.setVhnId(jsonObject.getString("vhnId"));
                        newRegMotherRealmModel.setMlong(jsonObject.getString("mlong"));
                        newRegMotherRealmModel.setMlat(jsonObject.getString("mlat"));
                        newRegMotherRealmModel.setRegid(jsonObject.getString("regid"));
                        newRegMotherRealmModel.setDatetime(jsonObject.getString("datetime"));
                    }
                    realm.commitTransaction(); //close table
                } else {
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                }
            } else {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
                txt_no_records_found.setText(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();
    }

    private void setValueToUI() {

        resultsLists.clear();
        realm.beginTransaction();
        RealmResults<NewRegMotherRealmModel> antt1listRealmResult = realm.where(NewRegMotherRealmModel.class).findAll();
        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            result = new NewRegMotherResponseModel.Result();

            NewRegMotherRealmModel model = antt1listRealmResult.get(i);
            result.setmName(model.getmName());
            result.setDatetime(model.getDatetime());
            result.setmMobileNumber(model.getmMobileNumber());
            resultsLists.add(result);
            newMothersListAdapter.notifyDataSetChanged();
//            txt_an_tt.setText(getResources().getString(R.string.an_tt_1_due_s_list) + "(" + tt1_lists.size() + ")");
        }

        realm.commitTransaction();

    }

    @Override
    public void showRegMotherError(String response) {
        swipe_refresh_layout.setRefreshing(false);
        Log.e(NewRegMotherActivity.class.getSimpleName(),"error ->"+response);

    }

    @Override
    public void makeCall(String mMotherMobile) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
//        Log.i(TAG, "CALL permission has NOT been granted. Requesting permission.");
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
}
