package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherMigration;
import com.unicef.vhn.adapter.MotherMigrationAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.MotherMigrationResponseModel;
import com.unicef.vhn.realmDbModel.MotherMigrationRealmModel;
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

public class NativeMotherFragment extends Fragment implements MotherListsViews, MakeCallInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;

    PreferenceData preferenceData;
    private List<MotherMigrationResponseModel.Vhn_migrated_mothers> vhn_migrated_mothers;

    MotherMigrationResponseModel.Vhn_migrated_mothers getVhn_migrated_mothers;

    boolean isDataUpdate = true;

    private RecyclerView recyclerView;
    private TextView textView,txt_native_mother_list;
    private MotherMigrationAdapter motherMigrationAdapter;


    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherMigrationRealmModel motherMigrationRealmModel;
private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.activity_native_mother_migration, container, false);
        realm = RealmController.with(getActivity()).getRealm();
        initUI(view);
        onClickListner();
        return view;
    }

    private void onClickListner() {


    }

    private void initUI(View view) {
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());

        pnMotherListPresenter = new MotherListPresenter(getActivity(),this, realm);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getMigratedMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        }else{
            isoffline=true;
        }

        vhn_migrated_mothers = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.mother_recycler_view);
        textView = (TextView) view.findViewById(R.id.txt_no_records_found);
        txt_native_mother_list = (TextView) view.findViewById(R.id.txt_native_mother_list);
        swipeRefreshLayout =view.findViewById(R.id.swipe_refresh_layout);

        motherMigrationAdapter = new MotherMigrationAdapter(vhn_migrated_mothers, getActivity(), "", this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(motherMigrationAdapter);
        if (isoffline){
            showOfflineData();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getMigratedMothersList(preferenceData.getVhnCode(), preferenceData.getVhnId());
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    isoffline=true;
                }
            }
        });
    }

    private void showOfflineData() {

        Log.d(MotherMigration.class.getSimpleName(),  "off line");

        realm.beginTransaction();

        RealmResults<MotherMigrationRealmModel> motherMigrationrealmResults = realm.where(MotherMigrationRealmModel.class).findAll();
        for (int i=0;i<motherMigrationrealmResults.size();i++){
            getVhn_migrated_mothers =new  MotherMigrationResponseModel.Vhn_migrated_mothers();

            MotherMigrationRealmModel model = motherMigrationrealmResults.get(i);

            getVhn_migrated_mothers.setMid(model.getMid());
            getVhn_migrated_mothers.setMName(model.getMName());
            getVhn_migrated_mothers.setMPicmeId(model.getMPicmeId());
            getVhn_migrated_mothers.setMtype(model.getMtype());
            getVhn_migrated_mothers.setSubject(model.getSubject());
            getVhn_migrated_mothers.setMMotherMobile(model.getMMotherMobile());

            vhn_migrated_mothers.add(getVhn_migrated_mothers);
            motherMigrationAdapter.notifyDataSetChanged();
            txt_native_mother_list.setText(getResources().getString(R.string.native_mother)+"("+vhn_migrated_mothers.size()+")");

        }
        realm.commitTransaction();

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

        Log.e(MotherMigration.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status =mJsnobject.getString("status");
            String message =mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {

                JSONArray jsonArray = mJsnobject.getJSONArray("vhn_migrated_mothers");
                RealmResults<MotherMigrationRealmModel> motherListAdapterRealmModel = null;

                motherListAdapterRealmModel = realm.where(MotherMigrationRealmModel.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MotherMigrationRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    realm.beginTransaction();       //create or open

                    for (int i = 0; i < jsonArray.length(); i++) {

                        motherMigrationRealmModel = realm.createObject(MotherMigrationRealmModel.class);

                        getVhn_migrated_mothers = new MotherMigrationResponseModel.Vhn_migrated_mothers();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        motherMigrationRealmModel.setMid(jsonObject.getString("mid"));
                        motherMigrationRealmModel.setMName(jsonObject.getString("mName"));
                        motherMigrationRealmModel.setMPicmeId(jsonObject.getString("mPicmeId"));
                        motherMigrationRealmModel.setMtype(jsonObject.getString("mtype"));
                        motherMigrationRealmModel.setSubject(jsonObject.getString("subject"));
                        motherMigrationRealmModel.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                    }

                    realm.commitTransaction();       //create or open

                } else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }/*else{
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();

    }

    private void setValueToUI() {
        vhn_migrated_mothers.clear();
        Log.d(MotherMigration.class.getSimpleName(),  "online");
        RealmResults<MotherMigrationRealmModel> motherMigrationrealmResults = null;
        realm.beginTransaction();
        motherMigrationrealmResults = realm.where(MotherMigrationRealmModel.class).findAll();
        for (int i=0;i<motherMigrationrealmResults.size();i++){
            getVhn_migrated_mothers =new  MotherMigrationResponseModel.Vhn_migrated_mothers();
            MotherMigrationRealmModel model = motherMigrationrealmResults.get(i);
            getVhn_migrated_mothers.setMid(model.getMid());
            getVhn_migrated_mothers.setMName(model.getMName());
            getVhn_migrated_mothers.setMPicmeId(model.getMPicmeId());
            getVhn_migrated_mothers.setMtype(model.getMtype());
            getVhn_migrated_mothers.setSubject(model.getSubject());
            getVhn_migrated_mothers.setMMotherMobile(model.getMMotherMobile());
            vhn_migrated_mothers.add(getVhn_migrated_mothers);
            motherMigrationAdapter.notifyDataSetChanged();
        }
        txt_native_mother_list.setText(getResources().getString(R.string.native_mother)+"("+vhn_migrated_mothers.size()+")");

        realm.commitTransaction();
    }

    @Override
    public void showLoginError(String string) {
        swipeRefreshLayout.setRefreshing(false);
        Log.e(MotherMigration.class.getSimpleName(), "Response Error" + string);
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {
        Log.i(MotherMigration.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(getActivity(), "Displaying Call permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getActivity(), "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
