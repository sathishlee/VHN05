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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MotherHighRiskListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
//    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private TextView txt_no_records_found;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate=true;

    LinearLayout ll_filter;
    final Context context = this;
    TextView txt_filter;
    String str_mPhoto;
    ImageView cardview_image;
    CheckNetwork checkNetwork;
    boolean isOffline;
    Realm realm;
    PNMMotherListRealmModel pnmMotherListRealmModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.mothers_list_activity);
        showActionBar();
        initUI();
    }

    private void initUI() {
        checkNetwork= new CheckNetwork(this);
        txt_filter = (TextView) findViewById(R.id.txt_filter);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MotherHighRiskListActivity.this,this);

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
    }
        /*else if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("today_visit")) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.CURRENT_VISIT_LIST,preferenceData.getVhnCode(),preferenceData.getVhnId());

        }*/
    else {
        Log.e(MotherHighRiskListActivity.class.getSimpleName(), "no url");
    }
}
else{
    isOffline =true;
}

        mResult = new ArrayList<>();
//        mother_recycler_view.setVisibility(View.GONE);
//        txt_no_records_found.setVisibility(View.GONE);

        if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("an_mother_total_count")) {
            mAdapter = new MotherListAdapter(mResult, MotherHighRiskListActivity.this, "AN",this);
        }if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
            mAdapter = new MotherListAdapter(mResult, MotherHighRiskListActivity.this, "Risk",this);
        }else{
            mAdapter = new MotherListAdapter(mResult, MotherHighRiskListActivity.this, "",this);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MotherHighRiskListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
        if (isOffline){
//            showOffLineData();
            setValuetoUI();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }

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
        Log.e(MotherHighRiskListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if(status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNMMotherListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
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

                    }
                    realm.commitTransaction();
                }else{
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                    }
            }else{
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        setValuetoUI();


    }

    private void setValuetoUI() {
        Log.e(MotherHighRiskListActivity.class.getSimpleName(),"on line");
        realm.beginTransaction();
        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();

        for (int i=0;i<motherListAdapterRealmModel.size();i++){
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

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
        isDataUpdate=false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+"+mMotherMobile)));
        }
    }
    private void requestCallPermission() {
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getApplicationContext(),"Displaying Call permission rationale to provide additional context.",Toast.LENGTH_SHORT).show();
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


