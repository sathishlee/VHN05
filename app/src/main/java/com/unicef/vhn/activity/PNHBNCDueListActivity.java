package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
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
import com.unicef.vhn.adapter.PNHBNCDueAdapter;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.PNHBNCDueListModel;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    boolean isDataUpdate=true;
    private RecyclerView recyclerView;
    private TextView textView;
    private PNHBNCDueAdapter antt1Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnhbnc_mothers_list_activity);
        showActionBar();
        initUI();


    }

    public void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN/HBNC Mothers List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUI(){

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);

        pnMotherListPresenter = new MotherListPresenter(PNHBNCDueListActivity.this, this);
//        pnMotherListPresenter.getPNMotherList("V10001","1");
        pnMotherListPresenter.getPNHBNCDUEMotherList(preferenceData.getVhnCode(), preferenceData.getVhnId(),"1");
        tt1_lists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
        textView = (TextView) findViewById(R.id.txt_no_records_found);
        antt1Adapter = new PNHBNCDueAdapter(tt1_lists, PNHBNCDueListActivity.this,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PNHBNCDueListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(antt1Adapter);
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


        Log.e(ANTT1MothersList.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status =mJsnobject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vPNHBNC_List");
                if (jsonArray.length()!=0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        tt1List = new PNHBNCDueListModel.VPNHBNC_List();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        tt1List.setMotherName(jsonObject.getString("motherName"));
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
                        antt1Adapter.notifyDataSetChanged();
                    }
                }else{
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
