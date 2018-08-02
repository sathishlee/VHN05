package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.BackPressedFragment;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.ANMVisitRealmModel;
import com.unicef.vhn.realmDbModel.MotherListRealm;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.realmDbModel.PNMVisitRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.view.VisitANMotherViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by priyan on 2/3/2018.
 */

public class mothers extends Fragment implements MotherListsViews, MakeCallInterface,
        VisitANMotherViews, MotherListAdapter.ContactsAdapterListener  {
//        , PrimaryRegisterViews, MotherDeliveryViews

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;
    TextView txt_no_internet,txt_no_records_found;
    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherListRealm dashBoardRealmModel;


    GetVisitANMotherPresenter getVisitANMotherPresenter;

    ANMVisitRealmModel mhealthRecordResponseModel;
    PNMVisitRealmModel pnmVisitRealmModel;
    private SearchView searchView;
    SwipeRefreshLayout pullToRefresh;


    public static mothers newInstance() {
        mothers fragment = new mothers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mothers, container, false);
        realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"
        Log.d(mothers.class.getSimpleName(), "mother fragment oncreate view");
        getActivity().setTitle("Mothers List");
        initUI(view);

        return view;
    }

    private void initUI(View view) {
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());

        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);

        searchView = (SearchView) view.findViewById(R.id.search_mother);
        txt_no_internet = view.findViewById(R.id.txt_no_internet);
        txt_no_records_found = view.findViewById(R.id.txt_no_records_found);
        txt_no_internet.setVisibility(View.GONE);
        txt_no_records_found.setVisibility(View.GONE);
        Log.d(mothers.class.getSimpleName(), "mother fragment initUI view");

        pnMotherListPresenter = new MotherListPresenter(getActivity(), this, realm);
        getVisitANMotherPresenter = new GetVisitANMotherPresenter(getActivity(), this,realm);

        if (checkNetwork.isNetworkAvailable()) {
            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST,
                    preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            isoffline = true;
        }

        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) view.findViewById(R.id.mother_recycler_view);
        mAdapter = new MotherListAdapter(mResult, getActivity(), "", this, mothers.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

        if (isoffline) {
            txt_no_internet.setVisibility(View.VISIBLE);
            setValueToUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                AppConstants.ISQUERYFILTER = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                AppConstants.ISQUERYFILTER = true;
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetwork.isNetworkAvailable()) {
                    pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST,
                            preferenceData.getVhnCode(), preferenceData.getVhnId());
                } else {
                    isoffline = true;
                }
            }
        });

        /*if (isoffline) {
                    txt_no_internet.setVisibility(View.VISIBLE);
                    setValueToUI();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Record Not Found");
                    builder.create();
                }*/
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
    public void showANVisitRecordsSuccess(String response) {/*
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<ANMVisitRealmModel> motherListAdapterRealmModel = realm.where(ANMVisitRealmModel.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(ANMVisitRealmModel.class);
                    }
                });

if (jsonArray.length() != 0) {
realm.beginTransaction();
for (int i = 0; i < jsonArray.length(); i++) {
    //this will create a UserInfoRealmModel object which will be inserted in database
    mhealthRecordResponseModel = realm.createObject(ANMVisitRealmModel.class);
    JSONObject jsonObject = jsonArray.getJSONObject(i);
    mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
    mhealthRecordResponseModel.setVFacility(jsonObject.getString("vFacility"));
    mhealthRecordResponseModel.setMotherStatus(jsonObject.getString("motherStatus"));
    mhealthRecordResponseModel.setMotherCloseDate(jsonObject.getString("motherCloseDate"));
    mhealthRecordResponseModel.setMRiskStatus(jsonObject.getString("mRiskStatus"));
    mhealthRecordResponseModel.setMEDD(jsonObject.getString("mEDD"));
    mhealthRecordResponseModel.setMLMP(jsonObject.getString("mLMP"));
    mhealthRecordResponseModel.setPhcId(jsonObject.getString("phcId"));
    mhealthRecordResponseModel.setAwwId(jsonObject.getString("awwId"));
    mhealthRecordResponseModel.setVhnId(jsonObject.getString("vhnId"));
    mhealthRecordResponseModel.setMasterId(jsonObject.getString("masterId"));
    mhealthRecordResponseModel.setVTSH(jsonObject.getString("vTSH"));
    mhealthRecordResponseModel.setUsgPlacenta(jsonObject.getString("usgPlacenta"));
    mhealthRecordResponseModel.setUsgLiquor(jsonObject.getString("usgLiquor"));
    mhealthRecordResponseModel.setUsgGestationSac(jsonObject.getString("usgGestationSac"));
    mhealthRecordResponseModel.setUsgFetus(jsonObject.getString("usgFetus"));
    mhealthRecordResponseModel.setVAlbumin(jsonObject.getString("vAlbumin"));
    mhealthRecordResponseModel.setVUrinSugar(jsonObject.getString("vUrinSugar"));
    mhealthRecordResponseModel.setVGTT(jsonObject.getString("vGTT"));
    mhealthRecordResponseModel.setVPPBS(jsonObject.getString("vPPBS"));
    mhealthRecordResponseModel.setVFBS(jsonObject.getString("vFBS"));
    mhealthRecordResponseModel.setVRBS(jsonObject.getString("vRBS"));
    mhealthRecordResponseModel.setVFHS(jsonObject.getString("vFHS"));
    mhealthRecordResponseModel.setVHemoglobin(jsonObject.getString("vHemoglobin"));
    mhealthRecordResponseModel.setVBodyTemp(jsonObject.getString("vBodyTemp"));
    mhealthRecordResponseModel.setVPedalEdemaPresent(jsonObject.getString("vPedalEdemaPresent"));
    mhealthRecordResponseModel.setVFundalHeight(jsonObject.getString("vFundalHeight"));
    mhealthRecordResponseModel.setVEnterWeight(jsonObject.getString("vEnterWeight"));
    mhealthRecordResponseModel.setVEnterPulseRate(jsonObject.getString("vEnterPulseRate"));
    mhealthRecordResponseModel.setVClinicalBPDiastolic(jsonObject.getString("vClinicalBPDiastolic"));
    mhealthRecordResponseModel.setVClinicalBPSystolic(jsonObject.getString("vClinicalBPSystolic"));
    mhealthRecordResponseModel.setVAnyComplaints(jsonObject.getString("vAnyComplaints"));
    mhealthRecordResponseModel.setVtypeOfVisit(jsonObject.getString("vtypeOfVisit"));
    mhealthRecordResponseModel.setPicmeId(jsonObject.getString("picmeId"));
    mhealthRecordResponseModel.setMid(jsonObject.getString("mid"));
    mhealthRecordResponseModel.setVisitId(jsonObject.getString("visitId"));
    mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
    mhealthRecordResponseModel.setVid(jsonObject.getString("vid"));
}
realm.commitTransaction();
} else {
                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public void showANVisitRecordsFailiur(String response) {
    }

    @Override
    public void showPNVisitRecordsSuccess(String response) {/*
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersVisit");
                RealmResults<PNMVisitRealmModel> motherListAdapterRealmModel = realm.where(PNMVisitRealmModel.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNMVisitRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pnmVisitRealmModel = realm.createObject(PNMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pnmVisitRealmModel.setPnId(jsonObject.getString("pnId"));
                        pnmVisitRealmModel.setMid(jsonObject.getString("mid"));
                        pnmVisitRealmModel.setPicmeId(jsonObject.getString("picmeId"));
                        pnmVisitRealmModel.setPnVisitNo(jsonObject.getString("pnVisitNo"));
                        pnmVisitRealmModel.setPnDueDate(jsonObject.getString("pnDueDate"));
                        pnmVisitRealmModel.setPnCareProvidedDate(jsonObject.getString("pnCareProvidedDate"));
                        pnmVisitRealmModel.setPnPlace(jsonObject.getString("pnPlace"));
                        pnmVisitRealmModel.setPnAnyComplaints(jsonObject.getString("pnAnyComplaints"));
                        pnmVisitRealmModel.setPnBPSystolic(jsonObject.getString("pnBPSystolic"));
                        pnmVisitRealmModel.setPnPulseRate(jsonObject.getString("pnPulseRate"));
                        pnmVisitRealmModel.setPnTemp(jsonObject.getString("pnTemp"));
                        pnmVisitRealmModel.setPnEpistomyTear(jsonObject.getString("pnEpistomyTear"));
                        pnmVisitRealmModel.setPnPVDischarge(jsonObject.getString("pnPVDischarge"));
                        pnmVisitRealmModel.setPnBreastFeedingReason(jsonObject.getString("pnBreastFeedingReason"));
                        pnmVisitRealmModel.setPnBreastExamination(jsonObject.getString("pnBreastExamination"));
                        pnmVisitRealmModel.setPnOutCome(jsonObject.getString("pnOutCome"));
                        pnmVisitRealmModel.setCWeight(jsonObject.getString("cWeight"));
                        pnmVisitRealmModel.setCTemp(jsonObject.getString("cTemp"));
                        pnmVisitRealmModel.setCUmbilicalStump(jsonObject.getString("cUmbilicalStump"));
                        pnmVisitRealmModel.setCCry(jsonObject.getString("cCry"));
                        pnmVisitRealmModel.setCEyes(jsonObject.getString("cEyes"));
                        pnmVisitRealmModel.setCSkin(jsonObject.getString("cSkin"));
                        pnmVisitRealmModel.setCBreastFeeding(jsonObject.getString("cBreastFeeding"));
                        pnmVisitRealmModel.setCBreastFeedingReason(jsonObject.getString("cBreastFeedingReason"));
                        pnmVisitRealmModel.setCOutCome(jsonObject.getString("cOutCome"));
                    }
                    realm.commitTransaction();

                } else {

                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public void showPNVisitRecordsFailiur(String response) {
    }


    @Override
    public void showLoginSuccess(String response) {
        mResult.clear();
        pullToRefresh.setRefreshing(false);

        try {
            JSONObject res_mJsnobject = new JSONObject(response);
            String status = res_mJsnobject.getString("status");
            String message = res_mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {

           JSONArray jsonArray = res_mJsnobject.getJSONArray("vhnAN_Mothers_List");
                   /*  RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PNMMotherListRealmModel.class);
                    }
                });*/


                //create new realm Table
//                realm.beginTransaction();       //create or open
                for (int i = 0; i < jsonArray.length(); i++) {
//                    PNMMotherListRealmModel pnmMotherListRealmModel = realm.createObject(PNMMotherListRealmModel.class);

                    mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    /*
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
                    pnmMotherListRealmModel.setmHusbandMobile(jsonObject.getString("mHusbandMobile"));
                    pnmMotherListRealmModel.setvLongitude(jsonObject.getString("vLongitude"));
                    pnmMotherListRealmModel.setvLatitude(jsonObject.getString("vLatitude"));
                    pnmMotherListRealmModel.setCurrentMonth(Integer.parseInt(jsonObject.getString("currentMonth")));
                    pnmMotherListRealmModel.setmLMP(jsonObject.getString("mLMP"));
                    pnmMotherListRealmModel.setmVillage(jsonObject.getString("mVillage"));
                    pnmMotherListRealmModel.setNextVisit(jsonObject.getString("nextVisit"));

                    pnmMotherListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                    pnmMotherListRealmModel.setdBirthDetails(jsonObject.getString("dBirthDetails"));
                    pnmMotherListRealmModel.setdBirthWeight(jsonObject.getString("dBirthWeight"));
                    pnmMotherListRealmModel.setMeturityWeek(jsonObject.getString("meturityWeek"));
                    pnmMotherListRealmModel.setPnVisit(jsonObject.getString("pnVisit"));

                    pnmMotherListRealmModel.setGestAge(jsonObject.getString("gestAge"));
                    pnmMotherListRealmModel.setmWeight(jsonObject.getString("mWeight"));
                    */

                    if (jsonObject.getString("motherType").equalsIgnoreCase("AN")) {
//                        pnmMotherListRealmModel.setGestAge(jsonObject.getString("gestAge"));
//                        pnmMotherListRealmModel.setmWeight(jsonObject.getString("mWeight"));
                       getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                    } else if (jsonObject.getString("motherType").equalsIgnoreCase("PN")) {
                       /* pnmMotherListRealmModel.setDeleveryDate(jsonObject.getString("deleveryDate"));
                        pnmMotherListRealmModel.setdBirthDetails(jsonObject.getString("dBirthDetails"));
                        pnmMotherListRealmModel.setdBirthWeight(jsonObject.getString("dBirthWeight"));
                        pnmMotherListRealmModel.setMeturityWeek(jsonObject.getString("meturityWeek"));
                        pnmMotherListRealmModel.setPnVisit(jsonObject.getString("pnVisit"));*/
                        getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                    }
                }

//                realm.commitTransaction(); //close table
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValueToUI();
    }

    private void setValueToUI() {
        realm.beginTransaction();
        RealmResults<PNMMotherListRealmModel> userInfoRealmResult = realm.where(PNMMotherListRealmModel.class).findAll();
        if (userInfoRealmResult.size()!=0){

        for (int i = 0; i < userInfoRealmResult.size(); i++) {

            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            PNMMotherListRealmModel model = userInfoRealmResult.get(i);
            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setmAge(model.getmAge());
            mresponseResult.setMPicmeId(model.getmPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());

            mResult.add(mresponseResult);
        }
        }else{
            txt_no_records_found.setVisibility(View.VISIBLE);
            mother_recycler_view.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
    }



    @Override
    public void showLoginError(String response) {
        pullToRefresh.setRefreshing(false);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
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


    @Override
    public void onContactSelected(PNMotherListResponse.VhnAN_Mothers_List contact) {

    }


}
