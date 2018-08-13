package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.ANTT2Adapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.ANTT2ResponseModel;
import com.unicef.vhn.realmDbModel.ANTT2RealmModel;
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

public class ANTT2MothersList extends AppCompatActivity implements MotherListsViews, MakeCallInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    private List<ANTT2ResponseModel.TT2_List> tt2_lists;
    ANTT2ResponseModel.TT2_List tt2list;

    boolean isDataUpdate = true;
    private RecyclerView recyclerView;
    private TextView textView,txt_no_internet,txt_antt2;
    private ANTT2Adapter antt2Adapter;

    PreferenceData preferenceData;

    final Context context = this;
    TextView txt_filter;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    ANTT2RealmModel antt2RealmModel;
SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.antt2_mother_list_activity);
        showActionBar();
        initUI();
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AN TT 2 Due List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ANTT2MothersList.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    public void initUI() {
        checkNetwork = new CheckNetwork(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(ANTT2MothersList.this, this, realm);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getANTT2MotherList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        }else{
            isoffline=true;
        }
        tt2_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.antt2_mother_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_antt2 = (TextView) findViewById(R.id.txt_antt2);
        txt_no_internet.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        antt2Adapter = new ANTT2Adapter(tt2_lists, ANTT2MothersList.this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ANTT2MothersList.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(antt2Adapter);
        if (isoffline) {
            txt_no_internet.setVisibility(View.VISIBLE);

            setValueToUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getANTT2MotherList(preferenceData.getVhnCode(), preferenceData.getVhnId());
                }else{
                    isoffline=true;
                    swipeRefreshLayout.setRefreshing(false);
                    txt_no_internet.setVisibility(View.VISIBLE);

                }
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
    public void showLoginSuccess(String response) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("TT2_List");

                RealmResults<ANTT2RealmModel> motherListAdapterRealmModel = realm.where(ANTT2RealmModel.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(ANTT2RealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    realm.beginTransaction();       //create or open

                    for (int i = 0; i < jsonArray.length(); i++) {
                        antt2RealmModel = realm.createObject(ANTT2RealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                        tt2list = new ANTT2ResponseModel.TT2_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        antt2RealmModel.setMName(jsonObject.getString("mName"));
                        antt2RealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));;
                        antt2RealmModel.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                    }
                    realm.commitTransaction();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }else{
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(message);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();

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
        Log.i(ANTT2MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
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



    private void setValueToUI() {
        tt2_lists.clear();
         realm.beginTransaction();
        RealmResults<ANTT2RealmModel> antt1listRealmResult = realm.where(ANTT2RealmModel.class).findAll();

        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            tt2list = new ANTT2ResponseModel.TT2_List();
            ANTT2RealmModel model = antt1listRealmResult.get(i);
            tt2list.setMName(model.getMName());
            tt2list.setMPicmeId(model.getMPicmeId());
            tt2list.setMMotherMobile(model.getMMotherMobile());
            tt2_lists.add(tt2list);
            antt2Adapter.notifyDataSetChanged();
                    }
        txt_antt2.setText(getResources().getString(R.string.an_tt_1_due_s_list)+"("+tt2_lists.size()+")");

        realm.commitTransaction();
    }


}
