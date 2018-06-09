package com.unicef.vhn.activity.MotherList;

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
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.ANMotherListRealmModel;
import com.unicef.vhn.realmDbModel.ANMotherListRiskRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ANMotherListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface, MotherListAdapter.ContactsAdapterListener {
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
    boolean isOffline;
    Realm realm;
    ANMotherListRealmModel anmMotherListRealmModel;
    ANMotherListRiskRealmModel anmMotherListRiskRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_anmother_list);
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
        checkNetwork = new CheckNetwork(this);
        txt_filter = (TextView) findViewById(R.id.txt_filter);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        mother_recycler_view = (RecyclerView) findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(ANMotherListActivity.this, this);
        if (checkNetwork.isNetworkAvailable()) {
            Log.w(AllMotherListActivity.class.getSimpleName(), "Is Internet connection :-" + checkNetwork.isNetworkAvailable());
            if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_AN_RISK_MOTHERS_DETAILS, preferenceData.getVhnCode(), preferenceData.getVhnId());
            } else {
                pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_AN_MOTHERS_DETAILS, preferenceData.getVhnCode(), preferenceData.getVhnId());
            }
        } else {
            Log.w(AllMotherListActivity.class.getSimpleName(), "Is Internet connection :-" + checkNetwork.isNetworkAvailable());

            isOffline = true;
        }
        mResult = new ArrayList<>();
//        mother_recycler_view.setVisibility(View.GONE);
//        txt_no_records_found.setVisibility(View.GONE);
        Log.w(AllMotherListActivity.class.getSimpleName(), "Adapter list size :-" + mResult.size());

        mAdapter = new MotherListAdapter(mResult, ANMotherListActivity.this, "AN", this,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ANMotherListActivity.this);
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
        if (isOffline) {
            Log.w(AllMotherListActivity.class.getSimpleName(), "is Offline -" + checkNetwork.isNetworkAvailable());
            setValuetoUI();
        } else {
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
    public void showLoginSuccess(String response) {

        Log.e(AllMotherListActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
                    RealmResults<ANMotherListRiskRealmModel> anMotherListRiskRealmModels = realm.where(ANMotherListRiskRealmModel.class).findAll();
                    Log.e("Realm size ---->", anMotherListRiskRealmModels.size() + "");
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(ANMotherListRiskRealmModel.class);
                        }
                    });
                } else {
                    RealmResults<ANMotherListRealmModel> motherListAdapterRealmModel = realm.where(ANMotherListRealmModel.class).findAll();
                    Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(ANMotherListRealmModel.class);
                        }
                    });
                }
                if (jsonArray.length() != 0) {
                    mother_recycler_view.setVisibility(View.VISIBLE);
                    txt_no_records_found.setVisibility(View.GONE);
                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {
                            anmMotherListRiskRealmModel = realm.createObject(ANMotherListRiskRealmModel.class);
                            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            anmMotherListRiskRealmModel.setMid(jsonObject.getString("mid"));
                            anmMotherListRiskRealmModel.setmName(jsonObject.getString("mName"));
                            anmMotherListRiskRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
                            anmMotherListRiskRealmModel.setVhnId(jsonObject.getString("vhnId"));
                            anmMotherListRiskRealmModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                            anmMotherListRiskRealmModel.setMotherType(jsonObject.getString("motherType"));
                            anmMotherListRiskRealmModel.setmLatitude(jsonObject.getString("mLatitude"));
                            anmMotherListRiskRealmModel.setmLongitude(jsonObject.getString("mLongitude"));
                            anmMotherListRiskRealmModel.setmPhoto(jsonObject.getString("mPhoto"));
                        } else {
                            anmMotherListRealmModel = realm.createObject(ANMotherListRealmModel.class);
                            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            anmMotherListRealmModel.setMid(jsonObject.getString("mid"));
                            anmMotherListRealmModel.setmName(jsonObject.getString("mName"));
                            anmMotherListRealmModel.setmPicmeId(jsonObject.getString("mPicmeId"));
                            anmMotherListRealmModel.setVhnId(jsonObject.getString("vhnId"));
                            anmMotherListRealmModel.setmMotherMobile(jsonObject.getString("mMotherMobile"));
                            anmMotherListRealmModel.setMotherType(jsonObject.getString("motherType"));
                            anmMotherListRealmModel.setmLatitude(jsonObject.getString("mLatitude"));
                            anmMotherListRealmModel.setmLongitude(jsonObject.getString("mLongitude"));
                            anmMotherListRealmModel.setmPhoto(jsonObject.getString("mPhoto"));
                        }

                        /*mresponseResult.setMid(jsonObject.getString("mid"));
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
                    }
                    realm.commitTransaction();
                } else {
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
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
        Log.w(AllMotherListActivity.class.getSimpleName(), "setValuetoUI is Internet Conection-" + checkNetwork.isNetworkAvailable());
        realm.beginTransaction();
        if (AppConstants.GET_MOTHER_LIST_TYPE.equalsIgnoreCase("high_risk_count")) {

            RealmResults<ANMotherListRiskRealmModel> anMotherListRiskRealmModels = realm.where(ANMotherListRiskRealmModel.class).findAll();

            for (int i = 0; i < anMotherListRiskRealmModels.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                Log.w(AllMotherListActivity.class.getSimpleName(), "ANMotherListRealmModel:--------" + i);

                ANMotherListRiskRealmModel model = anMotherListRiskRealmModels.get(i);
                Log.w(AllMotherListActivity.class.getSimpleName(), "ANMotherListRealmModel:--------" + anMotherListRiskRealmModels.get(i));

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
        } else {
            RealmResults<ANMotherListRealmModel> motherListAdapterRealmModel = realm.where(ANMotherListRealmModel.class).findAll();

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                Log.w(AllMotherListActivity.class.getSimpleName(), "ANMotherListRealmModel:--------" + i);
                ANMotherListRealmModel model = motherListAdapterRealmModel.get(i);
                Log.w(AllMotherListActivity.class.getSimpleName(), "ANMotherListRealmModel:--------" + motherListAdapterRealmModel.get(i));
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onContactSelected(PNMotherListResponse.VhnAN_Mothers_List contact) {

    }
}
