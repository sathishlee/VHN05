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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.activity.MotherListActivity;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
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
import io.realm.Sort;

import static android.R.layout.simple_spinner_item;

public class AllMotherListActivity extends AppCompatActivity implements MotherListsViews, MakeCallInterface {
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
    PNMMotherListRealmModel pnmMotherListRealmModel;
    boolean strHighRisk=false;
    boolean strDescending=false;
    String strVillageName="";
    String strTrimester="";
    boolean strMotherType;
    ArrayList<String> vhnVillageList;
    ArrayList<String> termisterlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(AllMotherListActivity.class.getSimpleName(), "Activity created");
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_all_mother_list);
        Log.w(AllMotherListActivity.class.getSimpleName(), "realm init");

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
        pnMotherListPresenter = new MotherListPresenter(AllMotherListActivity.this, this);
        if (checkNetwork.isNetworkAvailable()) {
            Log.w(AllMotherListActivity.class.getSimpleName(), "Is Internet connection :-" + checkNetwork.isNetworkAvailable());

            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            Log.w(AllMotherListActivity.class.getSimpleName(), "Is Internet connection :-" + checkNetwork.isNetworkAvailable());

            isOffline = true;
        }
        mResult = new ArrayList<>();
//        mother_recycler_view.setVisibility(View.GONE);
//        txt_no_records_found.setVisibility(View.GONE);
        Log.w(AllMotherListActivity.class.getSimpleName(), "Adapter list size :-" + mResult.size());

        mAdapter = new MotherListAdapter(mResult, AllMotherListActivity.this, "AN", this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllMotherListActivity.this);
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


                CheckBox ch_highRisk,ch_desc,ch_anmother,ch_pnmother;
                Spinner sp_village_wise,sp_trimester;
                ch_highRisk = (CheckBox)dialog.findViewById(R.id.ch_high_risk);
                ch_desc = (CheckBox)dialog.findViewById(R.id.ch_desc);
                ch_anmother = (CheckBox)dialog.findViewById(R.id.ch_anmother);
                ch_pnmother = (CheckBox)dialog.findViewById(R.id.ch_pnmother);
                sp_village_wise = (Spinner)dialog.findViewById(R.id.sp_village_wise);
                sp_trimester = (Spinner)dialog.findViewById(R.id.sp_trimester);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

                ch_highRisk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        strHighRisk=isChecked;
                    }
                });
                ch_desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         strDescending=isChecked;
                    }
                });
                 vhnVillageList=new ArrayList<>();
                termisterlist=new ArrayList<>();
                termisterlist.add("1-3");
                termisterlist.add("4-6");
                termisterlist.add("6-10");

                realm.beginTransaction();
                RealmResults<PNMMotherListRealmModel> getVillageList =realm.where(PNMMotherListRealmModel.class).findAll();
                Log.e("vhnVillageList",getVillageList.size()+"");

                for (int i=0; i< getVillageList.size();i++){
                    strVillageName =getVillageList.get(0).getmVillage();
                    vhnVillageList.add(getVillageList.get(i).getmVillage());
                    Log.e("vhnVillageList",getVillageList.get(i).getmVillage());
                }
                realm.commitTransaction();


//                for (int i=0; i< vhnVillageList.size();i++){
//                   Log.e("vhnVillageList",vhnVillageList.get(i));
//                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        vhnVillageList
                );

                sp_village_wise.setAdapter(adapter);
                ArrayAdapter<String> adapterT = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        termisterlist
                );

                sp_trimester.setAdapter(adapterT);
                sp_trimester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        strTrimester = termisterlist.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                sp_village_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        strVillageName = vhnVillageList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        setFilterDatatToUI();
                        dialog.dismiss();
                        setValuetoUI();
                    }
                });
                dialog.show();
            }
        });


    }

    private void setFilterDatatToUI() {
        mResult.clear();
        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = null;

        if (strHighRisk) {
            realm.beginTransaction();
            if (strTrimester.equalsIgnoreCase("1-3")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 1, 3).findAll();
            } else if (strTrimester.equalsIgnoreCase("4-6")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 4, 6).findAll();

            } else if (strTrimester.equalsIgnoreCase("7-10")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 7, 10).findAll();
            }
            if (strDescending){
                motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.DESCENDING);
        }else {
                motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.ASCENDING);

            }

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                Log.w(AllMotherListActivity.class.getSimpleName(), "PNMMotherListRealmModel:--------" + i);
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
        }else{

            realm.beginTransaction();
            if (strTrimester.equalsIgnoreCase("1-3")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 1, 3).findAll();
            } else if (strTrimester.equalsIgnoreCase("4-6")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 4, 6).findAll();

            } else if (strTrimester.equalsIgnoreCase("7-10")) {
                motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus", "HIGH").equalTo("mVillage", strVillageName).between("currentMonth", 7, 10).findAll();
            }
            if (strDescending){
                motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.DESCENDING);
            }else {
                motherListAdapterRealmModel = motherListAdapterRealmModel.sort("mPicmeId", Sort.ASCENDING);

            }

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                Log.w(AllMotherListActivity.class.getSimpleName(), "PNMMotherListRealmModel:--------" + i);
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
                        pnmMotherListRealmModel.setmRiskStatus(jsonObject.getString("mRiskStatus"));
                        pnmMotherListRealmModel.setmEDD(jsonObject.getString("mEDD"));
                        pnmMotherListRealmModel.setmHusbandName(jsonObject.getString("mHusbandName"));
                        pnmMotherListRealmModel.setvLongitude(jsonObject.getString("vLongitude"));
                        pnmMotherListRealmModel.setvLatitude(jsonObject.getString("vLatitude"));
                        pnmMotherListRealmModel.setCurrentMonth(Integer.parseInt(jsonObject.getString("currentMonth")));
                        pnmMotherListRealmModel.setmLMP(jsonObject.getString("mLMP"));
                        pnmMotherListRealmModel.setmVillage(jsonObject.getString("mVillage"));
//                        pnmMotherListRealmModel.setne(jsonObject.getString("nextVisit"));






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

        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
//        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).equalTo("mRiskStatus","LOW").equalTo("mVillage","PERAMANUR").findAll();
//        RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).between("currentMonth",1,10).equalTo("mVillage","PERAMANUR").findAll();
        motherListAdapterRealmModel =motherListAdapterRealmModel.sort("mPicmeId",Sort.ASCENDING);


        for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            Log.w(AllMotherListActivity.class.getSimpleName(), "PNMMotherListRealmModel:--------" + i);
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
