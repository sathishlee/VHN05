package com.unicef.vhn.activity;

import android.Manifest;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.MotherListAdapterRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MotherListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private TextView txt_no_records_found;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    LinearLayout ll_filter;
    final Context context = this;
    TextView txt_filter;
    String str_mPhoto;
    ImageView cardview_image;

    CheckNetwork checkNetwork;
    boolean isoffline = false;

    Realm realm;



    MotherListAdapterRealmModel dashBoardRealmModel;
    RealmResults<MotherListAdapterRealmModel> userInfoRealmResult;
    MotherListAdapterRealmModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mothers_list_activity);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"

        showActionBar();
        initUI();


    }

    private void initUI() {
        checkNetwork = new CheckNetwork(this);

        txt_filter = (TextView) findViewById(R.id.txt_filter);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MotherListActivity.this, this);
        mResult = new ArrayList<>();
        mother_recycler_view.setVisibility(View.GONE);
        txt_no_records_found.setVisibility(View.GONE);

        if (checkNetwork.isNetworkAvailable()) {
            if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("mother_count")) {

                pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
            } else if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("risk_count")) {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_MOTHERS_RISK, preferenceData.getVhnCode(), preferenceData.getVhnId());

            } else if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("sos_count")) {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_SOS_MOTHER_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());

            } else if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("an_mother_total_count")) {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_AN_MOTHERS_DETAILS, preferenceData.getVhnCode(), preferenceData.getVhnId());

            } else if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_AN_RISK_MOTHERS_DETAILS, preferenceData.getVhnCode(), preferenceData.getVhnId());
            } else {
                Log.e(MotherListActivity.class.getSimpleName(), "no url");
            }

        }else{

            isoffline = true;
        }

        if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("an_mother_total_count")) {
            mAdapter = new MotherListAdapter(mResult, MotherListActivity.this, "AN", this);
        }
        if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
            mAdapter = new MotherListAdapter(mResult, MotherListActivity.this, "Risk", this);
        } else {
            mAdapter = new MotherListAdapter(mResult, MotherListActivity.this, "", this);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MotherListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_fragment);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        if (isoffline){
//            showOffLineData();
            Log.e("OFF LINE ->",  "off line");

            setValueToUI();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }

    private void showOffLineData() {
        Log.e("off ->",  "offline");

        realm.beginTransaction();
        userInfoRealmResult = realm.where(MotherListAdapterRealmModel.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            model = userInfoRealmResult.get(i);
            Log.e("off list size ->", model+ "");

            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
    }

    private void showActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(AppConstants.MOTHER_LIST_TITLE);
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
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {

        Log.e(MotherListActivity.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");

                RealmResults<MotherListAdapterRealmModel> motherListAdapterRealmModel = realm.where(MotherListAdapterRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MotherListAdapterRealmModel.class);
                    }
                });

                Log.e("After Realm size  ---->", motherListAdapterRealmModel.size() + "");

                //create new realm Table
                realm.beginTransaction();       //create or open


                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                      /*  mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mresponseResult.setMid(jsonObject.getString("mid"));
                        mresponseResult.setMName(jsonObject.getString("mName"));
                        mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));
                        mresponseResult.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                        mresponseResult.setMotherType(jsonObject.getString("motherType"));
                        mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
                        mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                        mresponseResult.setmPhoto(jsonObject.getString("mPhoto"));
                        mResult.add(mresponseResult);
                        mAdapter.notifyDataSetChanged();*/
                        dashBoardRealmModel = realm.createObject(MotherListAdapterRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                        JSONObject Jsnobject = jsonArray.getJSONObject(i);


                        dashBoardRealmModel.setMName(Jsnobject.getString("mName"));
                        dashBoardRealmModel.setMPicmeId(Jsnobject.getString("mPicmeId"));
                        dashBoardRealmModel.setMid(Jsnobject.getString("mid"));
                        dashBoardRealmModel.setmMotherMobile(Jsnobject.getString("mMotherMobile"));
                        dashBoardRealmModel.setVhnId(Jsnobject.getString("vhnId"));
                        dashBoardRealmModel.setMLatitude(Jsnobject.getString("mLatitude"));
                        dashBoardRealmModel.setMLongitude(Jsnobject.getString("mLongitude"));
                        dashBoardRealmModel.setMotherType(Jsnobject.getString("motherType"));
                        dashBoardRealmModel.setmPhoto(Jsnobject.getString("mPhoto"));
                    }
                } else {
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                }
                realm.commitTransaction(); //close table
            }else{
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();
    }


    private void setValueToUI() {
        Log.e("ON LINE ->",  "on line");

        realm.beginTransaction();
        RealmResults<MotherListAdapterRealmModel> userInfoRealmResult = realm.where(MotherListAdapterRealmModel.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherListAdapterRealmModel model = userInfoRealmResult.get(i);
           /* Log.e("Mother list size ->", model+ "");
            Log.e("getMid", model.getMid() + "");
            Log.e("getMName", model.getMName() + "");
            Log.e("getMPicmeId", model.getMPicmeId() + "");
            Log.e("getmMotherMobile", model.getmMotherMobile() + "");
            Log.e("getVhnId", model.getVhnId() + "");
            Log.e("getvLatitude", model.getvLatitude() + "");
            Log.e("getvLongitude", model.getvLongitude() + "");
            Log.e("getMotherType", model.getMotherType() + "");
            Log.e("getmPhoto", model.getmPhoto() + "");*/


            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());
            mResult.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
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
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + mMotherMobile)));
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
}


