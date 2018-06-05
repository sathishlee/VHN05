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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.GetVisitANMotherPresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.ANMVisitRealmModel;
import com.unicef.vhn.realmDbModel.DelevaryDetailsPnMotherRealmModel;
import com.unicef.vhn.realmDbModel.MotherListRealm;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.realmDbModel.PNMVisitRealmModel;
import com.unicef.vhn.realmDbModel.PrimaryMotherDetailsRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherDeliveryViews;
import com.unicef.vhn.view.MotherListsViews;
import com.unicef.vhn.view.PrimaryRegisterViews;
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

public class mothers extends Fragment implements MotherListsViews, MakeCallInterface, VisitANMotherViews
//        , PrimaryRegisterViews, MotherDeliveryViews
{

    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    boolean isDataUpdate = true;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    Realm realm;
    MotherListRealm dashBoardRealmModel;


    GetVisitANMotherPresenter getVisitANMotherPresenter;

    ANMVisitRealmModel mhealthRecordResponseModel;
    PNMVisitRealmModel pnmVisitRealmModel;


//    MotherPrimaryRegisterPresenter motherPrimaryRegisterPresenter;
//    PrimaryMotherDetailsRealmModel primaryMotherDetailsRealmModel;

//    MotherDeliveryPresenter motherDeliveryPresenter;
//    DelevaryDetailsPnMotherRealmModel delevaryDetailsPnMotherRealmModel;

    boolean isPNMother = false;

    boolean isgetvisitreportdone = false;
//    ArrayList<String> motherPicmeIdList = null;
//    ArrayList<String> mothermidList = null;
//    ArrayList<String> mothertypeList = null;

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
        Log.d(mothers.class.getSimpleName(),"mother fragment oncreate view");
        getActivity().setTitle("Mothers List");
        initUI(view);

        return view;
    }

  /*  private void getDelevaryDetailsFromServer() {
        realm.beginTransaction();
        RealmResults<PNMVisitRealmModel> motherListAdapterRealmModel = realm.where(PNMVisitRealmModel.class).findAll();
        Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
        for (int i = 0; i <= motherListAdapterRealmModel.size(); i++) {
            motherDeliveryPresenter.deliveryDetails(motherListAdapterRealmModel.get(i).getPicmeId(), motherListAdapterRealmModel.get(i).getMid());

        }
        realm.commitTransaction();
    }*/

  /*  private void getPrimaryDataFromServer() {
        realm.beginTransaction();
        RealmResults<ANMVisitRealmModel> motherListAdapterRealmModel = realm.where(ANMVisitRealmModel.class).findAll();
        Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");

        for (int i = 0; i <= motherListAdapterRealmModel.size(); i++) {
            motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(motherListAdapterRealmModel.get(i).getPicmeId());
        }
        realm.commitTransaction();
    }*/

    private void initUI(View view) {
        checkNetwork = new CheckNetwork(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());

//        motherPicmeIdList = new ArrayList<>();
//        mothermidList = new ArrayList<>();
//        mothertypeList = new ArrayList<>();
        Log.d(mothers.class.getSimpleName(),"mother fragment initUI view");

        pnMotherListPresenter = new MotherListPresenter(getActivity(), this);
        //ANMOtherVisitReportActivity
        getVisitANMotherPresenter = new GetVisitANMotherPresenter(getActivity(), this);
        //Motherprimary record presenter
//        motherPrimaryRegisterPresenter = new MotherPrimaryRegisterPresenter(this, getActivity());
//        motherDeliveryPresenter = new MotherDeliveryPresenter(getActivity(), this);

        if (checkNetwork.isNetworkAvailable()) {
            Log.d(mothers.class.getSimpleName(),"mother fragment NetworkAvailable"+checkNetwork.isNetworkAvailable()+"YOU ARE IN ON LINE");
            Log.d(mothers.class.getSimpleName(),"mother fragment MOTHER_DETAILS_LIST API CALL START");

            pnMotherListPresenter.getPNMotherList(Apiconstants.MOTHER_DETAILS_LIST, preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            Log.d(mothers.class.getSimpleName(),"mother fragment NetworkAvailable"+checkNetwork.isNetworkAvailable()+"YOU ARE IN OFF LINE");

            isoffline = true;
        }

        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView) view.findViewById(R.id.mother_recycler_view);
        mAdapter = new MotherListAdapter(mResult, getActivity(), "", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);

//        getPrimaryDataFromServer();
//        getDelevaryDetailsFromServer();
        /*if (checkNetwork.isNetworkAvailable()) {
            for (int i = 0; i < motherPicmeIdList.size(); i++) {
                Log.e(" aftervisit", "Mother type-->" + mothertypeList.get(i) + "\npicme id--->" + motherPicmeIdList.get(i) + "\nmid" + mothermidList.get(i));
                if (mothertypeList.get(i).equalsIgnoreCase("AN")) {
                    motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(motherPicmeIdList.get(i));

                } else {
                    motherDeliveryPresenter.deliveryDetails(motherPicmeIdList.get(i), mothermidList.get(i));

                }
            }
        }*/
        if (isoffline) {
//            showOfflineData();
            setValueToUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }


    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    /*@Override
    public void deliveryDetailsSuccess(String response) {

        JSONObject jsonObject_res = null;
        try {
            jsonObject_res = new JSONObject(response);
            String status = jsonObject_res.getString("status");

            String message = jsonObject_res.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONObject jsonObject = jsonObject_res.getJSONObject("Delevery_Info");
//                preferenceData.storeDid(jsonObject.getString("did"));
//                Log.d("response---->", response);
                realm.beginTransaction();
                delevaryDetailsPnMotherRealmModel = realm.createObject(DelevaryDetailsPnMotherRealmModel.class);
                delevaryDetailsPnMotherRealmModel.setDid(jsonObject.getString("did"));
                delevaryDetailsPnMotherRealmModel.setDdatetime(jsonObject.getString("ddatetime"));
                delevaryDetailsPnMotherRealmModel.setDtime(jsonObject.getString("dtime"));
                delevaryDetailsPnMotherRealmModel.setDplace(jsonObject.getString("dplace"));
                delevaryDetailsPnMotherRealmModel.setDdeleveryDetails(jsonObject.getString("ddeleveryDetails"));
                delevaryDetailsPnMotherRealmModel.setDdeleveryOutcomeMother(jsonObject.getString("ddeleveryOutcomeMother"));
                delevaryDetailsPnMotherRealmModel.setDnewBorn(jsonObject.getString("dnewBorn"));
                delevaryDetailsPnMotherRealmModel.setdInfantId(jsonObject.getString("dInfantId"));
                delevaryDetailsPnMotherRealmModel.setdBirthDetails(jsonObject.getString("dBirthDetails"));
                delevaryDetailsPnMotherRealmModel.setdBirthWeight(jsonObject.getString("dBirthWeight"));
                delevaryDetailsPnMotherRealmModel.setdBirthHeight(jsonObject.getString("dBirthHeight"));
                delevaryDetailsPnMotherRealmModel.setdBreastFeedingGiven(jsonObject.getString("dBreastFeedingGiven"));
                delevaryDetailsPnMotherRealmModel.setdAdmittedSNCU(jsonObject.getString("dAdmittedSNCU"));
                delevaryDetailsPnMotherRealmModel.setdSNCUDate(jsonObject.getString("dSNCUDate"));
                delevaryDetailsPnMotherRealmModel.setdSNCUOutcome(jsonObject.getString("dSNCUOutcome"));
                delevaryDetailsPnMotherRealmModel.setdBCGDate(jsonObject.getString("dBCGDate"));
                delevaryDetailsPnMotherRealmModel.setdOPVDate(jsonObject.getString("dOPVDate"));
                delevaryDetailsPnMotherRealmModel.setdHEPBDate(jsonObject.getString("dHEPBDate"));

                realm.commitTransaction();


            } else {
                Log.d("message---->", message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

    /*@Override
    public void deliveryDetailsFailure(String response) {

    }*/

    /*@Override
    public void getAllMotherPrimaryRegisterSuccess(String response) {
        storePrimaryMotherValueOnRealm(response);
    }*/

    /*private void storePrimaryMotherValueOnRealm(String response) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(response);
            int status = jObj.getInt("status");
            String message = jObj.getString("message");
            if (status == 1) {
                realm.beginTransaction();
                primaryMotherDetailsRealmModel = realm.createObject(PrimaryMotherDetailsRealmModel.class);
                primaryMotherDetailsRealmModel.setId(jObj.getString("id"));
                primaryMotherDetailsRealmModel.setmName(jObj.getString("mName"));
                primaryMotherDetailsRealmModel.setmAge(jObj.getString("mAge"));
                primaryMotherDetailsRealmModel.setmLMP(jObj.getString("mLMP"));
                primaryMotherDetailsRealmModel.setmEDD(jObj.getString("mEDD"));
                primaryMotherDetailsRealmModel.setmMotherMobile(jObj.getString("mMotherMobile"));
                primaryMotherDetailsRealmModel.setmHusbandName(jObj.getString("mHusbandName"));
                primaryMotherDetailsRealmModel.setmHusbandMobile(jObj.getString("mHusbandMobile"));
                primaryMotherDetailsRealmModel.setMasterId(jObj.getString("masterId"));
                primaryMotherDetailsRealmModel.setPicmeId(jObj.getString("picmeId"));
                primaryMotherDetailsRealmModel.setVhnId(jObj.getString("vhnId"));
                primaryMotherDetailsRealmModel.setTrasVhnId(jObj.getString("trasVhnId"));
                primaryMotherDetailsRealmModel.setAwwId(jObj.getString("awwId"));
                primaryMotherDetailsRealmModel.setPhcId(jObj.getString("phcId"));
                primaryMotherDetailsRealmModel.setmRiskStatus(jObj.getString("mRiskStatus"));
                primaryMotherDetailsRealmModel.setmMotherOccupation(jObj.getString("mMotherOccupation"));
                primaryMotherDetailsRealmModel.setmHusbandOccupation(jObj.getString("mHusbandOccupation"));
                primaryMotherDetailsRealmModel.setmAgeatMarriage(jObj.getString("mAgeatMarriage"));
                primaryMotherDetailsRealmModel.setmConsanguineousMarraige(jObj.getString("mConsanguineousMarraige"));
                primaryMotherDetailsRealmModel.setmHistoryIllness(jObj.getString("mHistoryIllness"));
                primaryMotherDetailsRealmModel.setmHistoryIllnessFamily(jObj.getString("mHistoryIllnessFamily"));
                primaryMotherDetailsRealmModel.setmAnySurgeryBefore(jObj.getString("mAnySurgeryBefore"));
                primaryMotherDetailsRealmModel.setmUseTobacco(jObj.getString("mUseTobacco"));
                primaryMotherDetailsRealmModel.setmUseAlcohol(jObj.getString("mUseAlcohol"));
                primaryMotherDetailsRealmModel.setmAnyMeditation(jObj.getString("mAnyMeditation"));
                primaryMotherDetailsRealmModel.setmAllergicToanyDrug(jObj.getString("mAllergicToanyDrug"));
                primaryMotherDetailsRealmModel.setmHistroyPreviousPreganancy(jObj.getString("mHistroyPreviousPreganancy"));
                primaryMotherDetailsRealmModel.setmLscsDone(jObj.getString("mLscsDone"));
                primaryMotherDetailsRealmModel.setmAnyComplecationDuringPreganancy(jObj.getString("mAnyComplecationDuringPreganancy"));
                primaryMotherDetailsRealmModel.setmPresentPreganancyG(jObj.getString("mPresentPreganancyG"));
                primaryMotherDetailsRealmModel.setmPresentPreganancyP(jObj.getString("mPresentPreganancyP"));
                primaryMotherDetailsRealmModel.setmPresentPreganancyA(jObj.getString("mPresentPreganancyA"));
                primaryMotherDetailsRealmModel.setmPresentPreganancyL(jObj.getString("mPresentPreganancyL"));
                primaryMotherDetailsRealmModel.setmRegistrationWeek(jObj.getString("mRegistrationWeek"));
                primaryMotherDetailsRealmModel.setmANTT1(jObj.getString("mANTT1"));
                primaryMotherDetailsRealmModel.setmANTT2(jObj.getString("mANTT2"));
                primaryMotherDetailsRealmModel.setmIFAStateDate(jObj.getString("mIFAStateDate"));
                primaryMotherDetailsRealmModel.setmHeight(jObj.getString("mHeight"));
                primaryMotherDetailsRealmModel.setmBloodGroup(jObj.getString("mBloodGroup"));
                primaryMotherDetailsRealmModel.setmHIV(jObj.getString("mHIV"));
                primaryMotherDetailsRealmModel.setmVDRL(jObj.getString("mVDRL"));
                primaryMotherDetailsRealmModel.setmHepatitis(jObj.getString("mHepatitis"));
                primaryMotherDetailsRealmModel.sethBloodGroup(jObj.getString("hBloodGroup"));
                primaryMotherDetailsRealmModel.sethVDRL(jObj.getString("hVDRL"));
                primaryMotherDetailsRealmModel.sethHIV(jObj.getString("hHIV"));
                primaryMotherDetailsRealmModel.sethHepatitis(jObj.getString("hHepatitis"));


                realm.commitTransaction();

            } else {
                Log.d("message---->", message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

 /*   @Override
    public void getAllMotherPrimaryRegisterFailure(String response) {

    }*/

    @Override
    public void showANVisitRecordsSuccess(String response) {
        Log.d(mothers.class.getSimpleName(),  "ANVisitRecordsSuccess api call success");

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Log.d(mothers.class.getSimpleName(),  "ANVisitRecordsSuccess api call success status"+status);

                JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<ANMVisitRealmModel> motherListAdapterRealmModel = realm.where(ANMVisitRealmModel.class).findAll();
                Log.e(mothers.class.getSimpleName(), "already anm visit data available is size"+motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.e(mothers.class.getSimpleName(), "delete ANMother visit realm records");

                        realm.delete(ANMVisitRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel realm talbe create");

                        mhealthRecordResponseModel = realm.createObject(ANMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call"+i+"th visit details"+jsonObject.getString("picmeId"));
                        Log.e(mothers.class.getSimpleName(), " ANMVisitRealmModel get data from api call"+i+"th visit details"+jsonObject.getString("vid"));

                        mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                        mhealthRecordResponseModel.setVFacility(jsonObject.getString("vFacility"));
                        //              mhealthRecordResponseModel.setMLongitude(jsonObject.getString("mLongitude"));
                        //              mhealthRecordResponseModel.setMLatitude(jsonObject.getString("mLatitude"));
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
//                mhealthRecordResponseModel.setVAnyComplaintsOthers(jsonObject.getString("vAnyComplaintsOthers"));
                        mhealthRecordResponseModel.setVAnyComplaints(jsonObject.getString("vAnyComplaints"));
//                mhealthRecordResponseModel.setVFacilityOthers(jsonObject.getString("vFacilityOthers"));
                        mhealthRecordResponseModel.setVtypeOfVisit(jsonObject.getString("vtypeOfVisit"));
                        mhealthRecordResponseModel.setPicmeId(jsonObject.getString("picmeId"));
                        mhealthRecordResponseModel.setMid(jsonObject.getString("mid"));
                        mhealthRecordResponseModel.setVisitId(jsonObject.getString("visitId"));
                        mhealthRecordResponseModel.setVDate(jsonObject.getString("vDate"));
                        mhealthRecordResponseModel.setVid(jsonObject.getString("vid"));

//                        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(motherListAdapterRealmModel.get(i).getPicmeId());

                    }
                    realm.commitTransaction();
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                } else {
                    Log.e(mothers.class.getSimpleName(), "ANMother visit records NOt Found");

                }
            } else {
                Log.e(mothers.class.getSimpleName(), "ANMother visit records"+message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showANVisitRecordsFailiur(String response) {
//Toast.makeText(getActivity(),"showANVisitRecordsSuccess"+response.toString(),Toast.LENGTH_LONG).show();

        Log.e(mothers.class.getSimpleName(), "ANMother visit records api failiur");


    }

    @Override
    public void showPNVisitRecordsSuccess(String response) {
        Log.d(mothers.class.getSimpleName(),  "PNVisitRecordsSuccess api call success");

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Log.d(mothers.class.getSimpleName(),  "PNVisitRecordsSuccess api call success status"+status);

                JSONArray jsonArray = mJsnobject.getJSONArray("pnMothersVisit");
                RealmResults<PNMVisitRealmModel> motherListAdapterRealmModel = realm.where(PNMVisitRealmModel.class).findAll();
                Log.d(mothers.class.getSimpleName(),  "PNVisitRecords motherListAdapterRealmModel size "+motherListAdapterRealmModel.size());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(mothers.class.getSimpleName(),  "PNVisitRecords delete");
                        realm.delete(PNMVisitRealmModel.class);
                    }
                });

                if (jsonArray.length() != 0) {
                    Log.e(mothers.class.getSimpleName(), "beginTransaction ANMother visit realm records");

                    realm.beginTransaction();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pnmVisitRealmModel = realm.createObject(PNMVisitRealmModel.class);  //this will create a UserInfoRealmModel object which will be inserted in database
                        Log.e(mothers.class.getSimpleName(), "PNMVisitRealmModel visit realm table created");

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

//                        motherDeliveryPresenter.deliveryDetails(jsonObject.getString("picmeId"),jsonObject.getString("mid"));

                    }
                    realm.commitTransaction();
                    Log.e(mothers.class.getSimpleName(), "commitTransaction ANMother visit realm records");

                } else {
                    Log.e(mothers.class.getSimpleName(), "showPNVisitRecordsSuccess RECORD NOT FOUND");

                }
            } else {
                Log.e(mothers.class.getSimpleName(), "showPNVisitRecords success"+message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showPNVisitRecordsFailiur(String response) {
//        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
 Log.e(mothers.class.getSimpleName(), "showPNVisitRecords failiur"+response);
    }


    @Override
    public void showLoginSuccess(String response) {

        Log.e(mothers.class.getSimpleName(), "mother list api call success" + response);
        try {
            JSONObject res_mJsnobject = new JSONObject(response);
            String status = res_mJsnobject.getString("status");
            String message = res_mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {
                Log.e(mothers.class.getSimpleName(), "mother list api call success status" + status);

                JSONArray jsonArray = res_mJsnobject.getJSONArray("vhnAN_Mothers_List");
                RealmResults<PNMMotherListRealmModel> motherListAdapterRealmModel = realm.where(PNMMotherListRealmModel.class).findAll();
                Log.e(mothers.class.getSimpleName(), "alrady data available in realm is size"+motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.e(mothers.class.getSimpleName(), "delete  MotherListRealmModel");
                        realm.delete(PNMMotherListRealmModel.class);
                    }
                });

                Log.e(mothers.class.getSimpleName(),"After delete MotherListRealmModel Realm size  ---->"+ motherListAdapterRealmModel.size() + "");

                //create new realm Table
                realm.beginTransaction();       //create or open
                Log.e(mothers.class.getSimpleName(),"Mother list realm table beginTransaction");


                for (int i = 0; i < jsonArray.length(); i++) {
                     /*dashBoardRealmModel = realm.createObject(MotherListRealm.class);  //this will create a UserInfoRealmModel object which will be inserted in database

                    JSONObject mJsnobject = jsonArray.getJSONObject(i);
                    motherPicmeIdList.add(mJsnobject.getString("mPicmeId"));
                    mothermidList.add(mJsnobject.getString("mid"));
                    mothertypeList.add(mJsnobject.getString("motherType"));

                    dashBoardRealmModel.setMName(mJsnobject.getString("mName"));
                    dashBoardRealmModel.setMPicmeId(mJsnobject.getString("mPicmeId"));

                    dashBoardRealmModel.setMid(mJsnobject.getString("mid"));
                    dashBoardRealmModel.setmMotherMobile(mJsnobject.getString("mMotherMobile"));
                    dashBoardRealmModel.setVhnId(mJsnobject.getString("vhnId"));
                    dashBoardRealmModel.setMLatitude(mJsnobject.getString("mLatitude"));
                    dashBoardRealmModel.setMLongitude(mJsnobject.getString("mLongitude"));
                    dashBoardRealmModel.setMotherType(mJsnobject.getString("motherType"));
                    dashBoardRealmModel.setmPhoto(mJsnobject.getString("mPhoto"));

                      if (jsonObject.getString("motherType").equalsIgnoreCase("AN")) {
                        getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), mJsnobject.getString("mid"));
                    }else if (jsonObject.getString("motherType").equalsIgnoreCase("PN")){
                        getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(),preferenceData.getVhnId(),mJsnobject.getString("mid"));
                    }
                 */

                    Log.e(mothers.class.getSimpleName(),"Mother list realm table will create");

                    PNMMotherListRealmModel pnmMotherListRealmModel = realm.createObject(PNMMotherListRealmModel.class);
                    Log.e(mothers.class.getSimpleName(),"Mother list realm table will created");

                    mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e(mothers.class.getSimpleName(),i+"th Mother details get fom api and stored realm table");
                    Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("mid"));
                    Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("mName"));
                    Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("mPicmeId"));

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

                    if (jsonObject.getString("motherType").equalsIgnoreCase("AN")) {
                        Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("motherType"));
                        Log.e(mothers.class.getSimpleName(),i+"VisitANMotherRecords api call start");

                        getVisitANMotherPresenter.getVisitANMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                    } else if (jsonObject.getString("motherType").equalsIgnoreCase("PN")) {
                        Log.e(mothers.class.getSimpleName(),i+jsonObject.getString("motherType"));
                        Log.e(mothers.class.getSimpleName(),i+"VisitPNMotherRecords api call start");
                        getVisitANMotherPresenter.getVisitPNMotherRecords(preferenceData.getVhnCode(), preferenceData.getVhnId(), jsonObject.getString("mid"));
                    }
                }

                realm.commitTransaction(); //close table
                Log.e(mothers.class.getSimpleName(),"Mother list realm table commitTransaction");


            } else {
                Log.e(mothers.class.getSimpleName(), message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(mothers.class.getSimpleName(),"Call setValuetoUI Method");

        setValueToUI();
    }

    private void setValueToUI() {
        Log.e(mothers.class.getSimpleName(),"Called setValuetoUI Method");


        realm.beginTransaction();
        Log.e(mothers.class.getSimpleName()," setValuetoUI beginTransaction");

//        RealmResults<MotherListRealm> userInfoRealmResult = realm.where(MotherListRealm.class).findAll();
        RealmResults<PNMMotherListRealmModel> userInfoRealmResult = realm.where(PNMMotherListRealmModel.class).findAll();
        Log.e(mothers.class.getSimpleName()," setValuetoUI RealmResult list size"+userInfoRealmResult.size());


        for (int i = 0; i < userInfoRealmResult.size(); i++) {

            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();
            Log.e(mothers.class.getSimpleName()," setValuetoUI RealmResult of - "+i+" - Mother details");

//            MotherListRealm model = userInfoRealmResult.get(i);
            PNMMotherListRealmModel model = userInfoRealmResult.get(i);
            Log.e(mothers.class.getSimpleName()," setValuetoUI getMother details of "+i+"th Mother mid"+model.getMid()+"from realm");
            Log.e(mothers.class.getSimpleName()," setValuetoUI getMother details of "+i+"th Mother mid"+model.getmName()+"from realm");
            Log.e(mothers.class.getSimpleName()," setValuetoUI getMother details of "+i+"th Mother mid"+model.getmPicmeId()+"from realm");

            mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getmName());
            mresponseResult.setMPicmeId(model.getmPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());/* mresponseResult.setMid(model.getMid());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setmMotherMobile(model.getmMotherMobile());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setMLatitude(model.getvLongitude());
            mresponseResult.setMLongitude(model.getvLongitude());
            mresponseResult.setMotherType(model.getMotherType());
            mresponseResult.setmPhoto(model.getmPhoto());*/
            Log.e(mothers.class.getSimpleName()," setValuetoUI add Mother details into list "+i+model);

            mResult.add(mresponseResult);
        }
        Log.e(mothers.class.getSimpleName()," mAdapter call");

        mAdapter.notifyDataSetChanged();

        realm.commitTransaction();
        Log.e(mothers.class.getSimpleName()," setValuetoUI beginTransaction");

    }



   /* private void showOfflineData() {
        Log.e("off ->",  "offline");

        realm.beginTransaction();
          RealmResults<MotherListRealm> realmResults = realm.where(MotherListRealm.class).findAll();
        Log.e("Mother list size ->", realmResults.size() + "");
        for (int i = 0; i < realmResults.size(); i++) {
            mresponseResult = new PNMotherListResponse.VhnAN_Mothers_List();

            MotherListRealm model = realmResults.get(i);

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
    }*/


    @Override
    public void showLoginError(String response) {
//        Log.e(mothers.class.getSimpleName(), "Response success" + response);
        Log.e(mothers.class.getSimpleName(), "mother list api call Erorr" + response);


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
        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
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
}
