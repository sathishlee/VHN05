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
import com.unicef.vhn.adapter.ANTT1Adapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.realmDbModel.ANTT1RealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class ANTT1MothersList extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
    String TAG = ANTT1MothersList.class.getSimpleName();
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<ANTT1ResponseModel.TT1_List> tt1_lists;
    ANTT1ResponseModel.TT1_List tt1List;
    boolean isDataUpdate = true;
    private RecyclerView recyclerView;
    private TextView textView,txt_no_internet,txt_an_tt;
    private ANTT1Adapter antt1Adapter;

    final Context context = this;
    TextView txt_filter;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    ANTT1RealmModel antt1RealmModel;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"

        setContentView(R.layout.antt1_mother_list_activity);
        showActionBar();
        initUI();
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AN TT 1 Due List");
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

        pnMotherListPresenter = new MotherListPresenter(ANTT1MothersList.this, this, realm);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getANTTMotherList(preferenceData.getVhnCode(), preferenceData.getVhnId());

        } else {
            isoffline = true;
        }
        tt1_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.antt1_mother_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_an_tt = (TextView) findViewById(R.id.txt_an_tt);
        txt_no_internet.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        antt1Adapter = new ANTT1Adapter(tt1_lists, ANTT1MothersList.this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ANTT1MothersList.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(antt1Adapter);

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
                    pnMotherListPresenter.getANTTMotherList(preferenceData.getVhnCode(), preferenceData.getVhnId());

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    isoffline = true;
                    txt_no_internet.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(pDialog.isShowing()){
            pDialog.dismiss();
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
        swipeRefreshLayout.setRefreshing(false);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("TT1_List");
//                RealmResults<ANTT1RealmModel> motherListAdapterRealmModel = realm.where(ANTT1RealmModel.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(ANTT1RealmModel.class);
                    }
                });
                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    realm.beginTransaction();       //create or open
                    for (int i = 0; i < jsonArray.length(); i++) {
                        antt1RealmModel = realm.createObject(ANTT1RealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        tt1List = new ANTT1ResponseModel.TT1_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        antt1RealmModel.setMName(jsonObject.getString("mName"));
                        antt1RealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        antt1RealmModel.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                    }
                    realm.commitTransaction(); //close table
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();
    }

    private void setValueToUI() {
        tt1_lists.clear();
        realm.beginTransaction();
        RealmResults<ANTT1RealmModel> antt1listRealmResult = realm.where(ANTT1RealmModel.class).findAll();
        for (int i = 0; i < antt1listRealmResult.size(); i++) {
            tt1List = new ANTT1ResponseModel.TT1_List();
            ANTT1RealmModel model = antt1listRealmResult.get(i);
            tt1List.setMName(model.getMName());
            tt1List.setMPicmeId(model.getMPicmeId());
            tt1List.setMMotherMobile(model.getMMotherMobile());
            tt1_lists.add(tt1List);
            antt1Adapter.notifyDataSetChanged();
            txt_an_tt.setText(getResources().getString(R.string.an_tt_1_due_s_list)+"("+tt1_lists.size()+")");
        }

        realm.commitTransaction();
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
        Log.i(TAG, "CALL permission has NOT been granted. Requesting permission.");
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
