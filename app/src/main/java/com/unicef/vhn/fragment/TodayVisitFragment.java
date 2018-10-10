package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.unicef.vhn.adapter.VisitListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.VisitListResponseModel;
import com.unicef.vhn.realmDbModel.VisitListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class TodayVisitFragment extends Fragment implements MotherListsViews, MakeCallInterface {
    String TAG = TodayVisitFragment.class.getSimpleName();
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<VisitListResponseModel.Vhn_current_visits> mResult;
    VisitListResponseModel.Vhn_current_visits mresponseResult;

    private RecyclerView mother_recycler_view;
    private TextView txt_no_records_found, txt_today_visit_size;
    private VisitListAdapter mAdapter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    VisitListRealmModel visitListRealmMode;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_today_visit, container, false);

        initUI(view);
        realm = RealmController.with(getActivity()).getRealm();

        return view;
    }

    public void initUI(View view) {
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        pnMotherListPresenter = new MotherListPresenter(getActivity(), this, realm);
        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.CURRENT_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) view.findViewById(R.id.mother_recycler_view);
        txt_no_records_found = (TextView) view.findViewById(R.id.txt_no_records_found);
        txt_today_visit_size = (TextView) view.findViewById(R.id.txt_today_visit_size);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mAdapter = new VisitListAdapter(getActivity(), mResult, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

        if (isoffline) {
            setValuetoUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getPNMotherList(Apiconstants.CURRENT_VISIT_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    isoffline = true;
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
        pDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {
        swipeRefreshLayout.setRefreshing(false);

        Log.e(TAG, Apiconstants.CURRENT_VISIT_LIST + "api response" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String msg = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("todaymothers");

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(VisitListRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {


                    mResult.clear();
                    realm.beginTransaction();       //create or open
                    VisitListRealmModel visitListRealmModels = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        visitListRealmModels = realm.createObject(VisitListRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                        mresponseResult = new VisitListResponseModel.Vhn_current_visits();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        visitListRealmModels.setNoteId(jsonObject.getString("noteId"));
                        mresponseResult.setNoteId(jsonObject.getString("noteId"));

                        visitListRealmModels.setMasterId(jsonObject.getString("masterId"));
                        mresponseResult.setMasterId(jsonObject.getString("masterId"));

                        visitListRealmModels.setMid(jsonObject.getString("mid"));
                        mresponseResult.setMid(jsonObject.getString("mid"));

                        visitListRealmModels.setMName(jsonObject.getString("mName"));
                        mresponseResult.setMName(jsonObject.getString("mName"));

                        visitListRealmModels.setPicmeId(jsonObject.getString("picmeId"));
                        mresponseResult.setPicmeId(jsonObject.getString("picmeId"));

                        visitListRealmModels.setVhnId(jsonObject.getString("vhnId"));
                        mresponseResult.setVhnId(jsonObject.getString("vhnId"));

                        visitListRealmModels.setMMotherMobile(jsonObject.getString("mMotherMobile"));
                        mresponseResult.setMMotherMobile(jsonObject.getString("mMotherMobile"));

                        visitListRealmModels.setMtype(jsonObject.getString("mtype"));
                        mresponseResult.setMtype(jsonObject.getString("mtype"));

                        visitListRealmModels.setNextVisit(jsonObject.getString("nextVisit"));
                        mresponseResult.setNextVisit(jsonObject.getString("nextVisit"));

                        visitListRealmModels.setMonth(jsonObject.getString("month"));
                        mresponseResult.setMonth(jsonObject.getString("month"));

//                        mResult.add(mresponseResult);
//                        mAdapter.notifyDataSetChanged();

                    }
                    realm.commitTransaction();
                } else {
                    mother_recycler_view.setVisibility(View.GONE);
                    txt_no_records_found.setVisibility(View.VISIBLE);
                }
            } else {
                mother_recycler_view.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValuetoUI();
    }

    private void setValuetoUI() {
        mResult.clear();
        if (realm.isInTransaction()) {
            realm.cancelTransaction();
        }
        realm.beginTransaction();
        RealmResults<VisitListRealmModel> motherListAdapterRealmModel = realm.where(VisitListRealmModel.class).findAll();
        Log.e(TAG, "Today Visit realm db size" + motherListAdapterRealmModel.size());

        if (motherListAdapterRealmModel.size() != 0) {

            for (int i = 0; i < motherListAdapterRealmModel.size(); i++) {
                VisitListRealmModel model = motherListAdapterRealmModel.get(i);

                /*Log.e(TAG, i+ " Note Id" + model.getNoteId());
                Log.e(TAG, i+ " MId" + model.getMid());
                Log.e(TAG, i+ " MASTER Id" + model.getMasterId());
                Log.e(TAG, i+ " NAME" + model.getMName());
                Log.e(TAG, i+ " PICME ID" + model.getPicmeId());
                Log.e(TAG, i+ " VHN ID" + model.getVhnId());
                Log.e(TAG, i+ " MOTHER MOBILE" + model.getMMotherMobile());
                Log.e(TAG, i+ " MOTHER NEXT VISIT" + model.getNextVisit());
                Log.e(TAG, i+ " MOTHER TYPE" + model.getMtype());*/


                mresponseResult = new VisitListResponseModel.Vhn_current_visits();

                mresponseResult.setNoteId(model.getNoteId());
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMasterId(model.getMasterId());
                mresponseResult.setMName(model.getMName());
                mresponseResult.setPicmeId(model.getPicmeId());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setMMotherMobile(model.getMMotherMobile());
                mresponseResult.setNextVisit(model.getNextVisit());
                mresponseResult.setMtype(model.getMtype());
                mresponseResult.setMonth(model.getMonth());


                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();
                txt_today_visit_size.setText(getResources().getString(R.string.today_visit_list) + "(" + mResult.size() + ")");

//                mresponseResult = new VisitListResponseModel.Vhn_current_visits();

            /*
                mresponseResult.setNoteId(model.getNoteId());
                mresponseResult.setMasterId(model.getMasterId());
                mresponseResult.setMid(model.getMid());
                mresponseResult.setMName(model.getMName());
                mresponseResult.setPicmeId(model.getPicmeId());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setMMotherMobile(model.getMMotherMobile());
                mresponseResult.setMtype(model.getMtype());
                mresponseResult.setNextVisit(model.getNextVisit());

                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();*/
            }

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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + mMotherMobile)));
        }
    }

    private void requestCallPermission() {

        Log.i(TodayVisitFragment.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
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
