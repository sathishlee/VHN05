package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherPrimaryRegisterPresenter;
import com.unicef.vhn.R;
<<<<<<< HEAD
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.DelevaryDetailsPnMotherRealmModel;
import com.unicef.vhn.realmDbModel.PrimaryMotherDetailsRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
=======
import com.unicef.vhn.constant.AppConstants;
>>>>>>> origin/new
import com.unicef.vhn.view.PrimaryRegisterViews;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MothersPrimaryRecordsActivity extends AppCompatActivity implements PrimaryRegisterViews {

    TextView txt_name, txt_mother_age, txt_lmp_date, txt_edd_date, txt_pry_mobile_no,
            txt_alter_mobile_no,txt_mother_occupation, txt_hus_occupation, txt_age_at_marriage,txt_consanguineous_marraige,
            txt_history_of_illness, txt_history_of_illness_family, txt_any_surgery_done, txt_tobacco, txt_alcohol,
            txt_on_any_medication, txt_allergic_to_any_drug, txt_history_of_previous_pregnancy, txt_lscs_done,
            txt_any_complication, txt_g, txt_p, txt_a, txt_l, txt_registration_week, txt_an_tt_1st, txt_an_tt_2nd,
            txt_ifa_start_date, txt_height, txt_blood_group, txt_hiv, txt_vdrl, txt_Hepatitis, txt_hus_blood_group,
            txt_hus_hiv, txt_hus_vdrl, txt_hus_Hepatitis;

    CheckNetwork checkNetwork;
    ProgressDialog pDialog;
    MotherPrimaryRegisterPresenter motherPrimaryRegisterPresenter;
    PreferenceData preferenceData;

    boolean isoffline = false;
    Realm realm;
    PrimaryMotherDetailsRealmModel primaryMotherDetailsRealmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.primary_details);
        initUI();
        showActionBar();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mother Primary Records");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void initUI() {
        checkNetwork =new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        motherPrimaryRegisterPresenter = new MotherPrimaryRegisterPresenter(MothersPrimaryRecordsActivity.this, this);
<<<<<<< HEAD
//        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(preferenceData.getPicmeId());
        if (checkNetwork.isNetworkAvailable()) {
            motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(AppConstants.MOTHER_PICME_ID);
        }else{
            isoffline=true;
        }
=======
        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(AppConstants.MOTHER_PICME_ID);
>>>>>>> origin/new
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_mother_age = (TextView) findViewById(R.id.txt_mother_age);
        txt_lmp_date = (TextView) findViewById(R.id.txt_lmp_date);
        txt_edd_date = (TextView) findViewById(R.id.txt_edd_date);
        txt_pry_mobile_no = (TextView) findViewById(R.id.txt_pry_mobile_no);
        txt_alter_mobile_no = (TextView) findViewById(R.id.txt_alter_mobile_no);
        txt_mother_occupation = (TextView) findViewById(R.id.txt_mother_occupation);
        txt_hus_occupation = (TextView) findViewById(R.id.txt_hus_occupation);
        txt_age_at_marriage = (TextView) findViewById(R.id.txt_age_at_marriage);
        txt_consanguineous_marraige = (TextView) findViewById(R.id.txt_consanguineous_marraige);
        txt_history_of_illness = (TextView) findViewById(R.id.txt_history_of_illness);
        txt_history_of_illness_family = (TextView) findViewById(R.id.txt_history_of_illness_family);
        txt_any_surgery_done = (TextView) findViewById(R.id.txt_any_surgery_done);
        txt_tobacco = (TextView) findViewById(R.id.txt_tobacco);
        txt_alcohol = (TextView) findViewById(R.id.txt_alcohol);
        txt_on_any_medication = (TextView) findViewById(R.id.txt_on_any_medication);
        txt_allergic_to_any_drug = (TextView) findViewById(R.id.txt_allergic_to_any_drug);
        txt_history_of_previous_pregnancy = (TextView) findViewById(R.id.txt_history_of_previous_pregnancy);
        txt_lscs_done = (TextView) findViewById(R.id.txt_lscs_done);
        txt_any_complication = (TextView) findViewById(R.id.txt_any_complication);
        txt_g = (TextView) findViewById(R.id.txt_g);
        txt_p = (TextView) findViewById(R.id.txt_p);
        txt_a = (TextView) findViewById(R.id.txt_a);
        txt_l = (TextView) findViewById(R.id.txt_l);
        txt_registration_week = (TextView) findViewById(R.id.txt_registration_week);
        txt_an_tt_1st = (TextView) findViewById(R.id.txt_an_tt_1st);
        txt_an_tt_2nd = (TextView) findViewById(R.id.txt_an_tt_2nd);
        txt_ifa_start_date = (TextView) findViewById(R.id.txt_ifa_start_date);
        txt_height = (TextView) findViewById(R.id.txt_height);
        txt_blood_group = (TextView) findViewById(R.id.txt_blood_group);
        txt_hiv = (TextView) findViewById(R.id.txt_hiv);
        txt_vdrl = (TextView) findViewById(R.id.txt_vdrl);
        txt_Hepatitis = (TextView) findViewById(R.id.txt_Hepatitis);
        txt_hus_blood_group = (TextView) findViewById(R.id.txt_hus_blood_group);
        txt_hus_hiv = (TextView) findViewById(R.id.txt_hus_hiv);
        txt_hus_vdrl = (TextView) findViewById(R.id.txt_hus_vdrl);
        txt_hus_Hepatitis = (TextView) findViewById(R.id.txt_hus_Hepatitis);
        if (isoffline) {
            getValuefromRealm();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Record Not Found");
            builder.create();
        }
    }

    private void getValuefromRealm() {
//        pDialog.show();
        RealmResults<PrimaryMotherDetailsRealmModel> primaryMotherDetailsRealmModelRealmResults;
realm.beginTransaction();
         primaryMotherDetailsRealmModelRealmResults = realm.where(PrimaryMotherDetailsRealmModel.class).equalTo("picmeId", AppConstants.MOTHER_PICME_ID).findAll();
//       primaryMotherDetailsRealmModelRealmResults = realm.where(PrimaryMotherDetailsRealmModel.class).findAll();
        Log.e(String.valueOf(MothersPrimaryRecordsActivity.class),primaryMotherDetailsRealmModelRealmResults.size()+"");
        Log.e(MothersPrimaryRecordsActivity.class.getSimpleName(),"primaryMotherDetailsRealmModelRealmResults  -->"+primaryMotherDetailsRealmModelRealmResults);
        for(int i=0;i<primaryMotherDetailsRealmModelRealmResults.size();i++) {
            PrimaryMotherDetailsRealmModel model =primaryMotherDetailsRealmModelRealmResults.get(i);
            txt_name.setText(model.getmName());
            txt_mother_age.setText(model.getmAge());
            txt_lmp_date.setText(model.getmLMP());
            txt_edd_date.setText(model.getmEDD());
            txt_pry_mobile_no.setText(model.getmMotherMobile());
            txt_alter_mobile_no.setText(model.getmHusbandMobile());
            txt_mother_occupation.setText(model.getmMotherOccupation());
            txt_hus_occupation.setText(model.getmHusbandOccupation());
            txt_age_at_marriage.setText(model.getmAgeatMarriage());
            txt_consanguineous_marraige.setText(model.getmConsanguineousMarraige());
            txt_history_of_illness.setText(model.getmHistoryIllness());
            txt_history_of_illness_family.setText(model.getmHistoryIllnessFamily());
            txt_any_surgery_done.setText(model.getmAnySurgeryBefore());
            txt_tobacco.setText(model.getmUseTobacco());
            txt_alcohol.setText(model.getmUseAlcohol());
            txt_on_any_medication.setText(model.getmAnyMeditation());
            txt_allergic_to_any_drug.setText(model.getmAllergicToanyDrug());
            txt_history_of_previous_pregnancy.setText(model.getmHistroyPreviousPreganancy());
            txt_lscs_done.setText(model.getmLscsDone());
            txt_any_complication.setText(model.getmAnyComplecationDuringPreganancy());
            txt_g.setText(model.getmPresentPreganancyG());
            txt_p.setText(model.getmPresentPreganancyP());
            txt_a.setText(model.getmPresentPreganancyA());
            txt_l.setText(model.getmPresentPreganancyL());
            txt_registration_week.setText(model.getmRegistrationWeek());
            txt_an_tt_1st.setText(model.getmANTT1());
            txt_an_tt_2nd.setText(model.getmANTT2());
            txt_ifa_start_date.setText(model.getmIFAStateDate());
            txt_height.setText(model.getmHeight());
            txt_blood_group.setText(model.getmBloodGroup());
            txt_hiv.setText(model.getmHIV());
            txt_vdrl.setText(model.getmVDRL());
            txt_Hepatitis.setText(model.getmHepatitis());
            txt_hus_blood_group.setText(model.gethBloodGroup());
            txt_hus_hiv.setText(model.gethHIV());
            txt_hus_vdrl.setText(model.gethVDRL());
            txt_hus_Hepatitis.setText(model.gethHepatitis());
        }
realm.commitTransaction();
        pDialog.dismiss();
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
    public void getAllMotherPrimaryRegisterSuccess(String response) {
        Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), "Success response" + response);
        setValuetoUI(response);
    }

    private void setValuetoUI(String response) {


        JSONObject jObj = null;
        try {
            jObj = new JSONObject(response);
            int status = jObj.getInt("status");
            String message = jObj.getString("message");
            if (status == 1) {
                Log.d("message---->", message);
                if (jObj.getString("mName") != "")
                    txt_name.setText(jObj.getString("mName"));
                if (jObj.getString("mAge") != "")
                    txt_mother_age.setText(jObj.getString("mAge"));
                if (jObj.getString("mLMP") != "")
                    txt_lmp_date.setText(jObj.getString("mLMP"));
                if (jObj.getString("mEDD") != "")
                    txt_edd_date.setText(jObj.getString("mEDD"));
                if (jObj.getString("mMotherMobile") != "")
                    txt_pry_mobile_no.setText(jObj.getString("mMotherMobile"));
                if (jObj.getString("mHusbandMobile") != "")
                    txt_alter_mobile_no.setText(jObj.getString("mHusbandMobile"));
                if (jObj.getString("mMotherOccupation") != "")
                    txt_mother_occupation.setText(jObj.getString("mMotherOccupation"));
                if (jObj.getString("mHusbandOccupation") != "")
                    txt_hus_occupation.setText(jObj.getString("mHusbandOccupation"));
                if (jObj.getString("mAgeatMarriage") != "")
                    txt_age_at_marriage.setText(jObj.getString("mAgeatMarriage"));
                if (jObj.getString("mConsanguineousMarraige") != "")
                    txt_consanguineous_marraige.setText(jObj.getString("mConsanguineousMarraige"));
                if (jObj.getString("mHistoryIllness") != "")
                    txt_history_of_illness.setText(jObj.getString("mHistoryIllness"));
                if (jObj.getString("mHistoryIllnessFamily") != "")
                    txt_history_of_illness_family.setText(jObj.getString("mHistoryIllnessFamily"));
                if (jObj.getString("mAnySurgeryBefore") != "")
                    txt_any_surgery_done.setText(jObj.getString("mAnySurgeryBefore"));
                if (jObj.getString("mUseTobacco") != "")
                    txt_tobacco.setText(jObj.getString("mUseTobacco"));
                if (jObj.getString("mUseAlcohol") != "")
                    txt_alcohol.setText(jObj.getString("mUseAlcohol"));
                if (jObj.getString("mAnyMeditation") != "")
                    txt_on_any_medication.setText(jObj.getString("mAnyMeditation"));
                if (jObj.getString("mAllergicToanyDrug") != "")
                    txt_allergic_to_any_drug.setText(jObj.getString("mAllergicToanyDrug"));
                if (jObj.getString("mHistroyPreviousPreganancy") != "")
                    txt_history_of_previous_pregnancy.setText(jObj.getString("mHistroyPreviousPreganancy"));
                if (jObj.getString("mLscsDone") != "")
                    txt_lscs_done.setText(jObj.getString("mLscsDone"));
                if (jObj.getString("mAnyComplecationDuringPreganancy") != "")
                    txt_any_complication.setText(jObj.getString("mAnyComplecationDuringPreganancy"));
                if (jObj.getString("mPresentPreganancyG") != "")
                    txt_g.setText(jObj.getString("mPresentPreganancyG"));
                if (jObj.getString("mPresentPreganancyP") != "")
                    txt_p.setText(jObj.getString("mPresentPreganancyP"));
                if (jObj.getString("mPresentPreganancyA") != "")
                    txt_a.setText(jObj.getString("mPresentPreganancyA"));
                if (jObj.getString("mPresentPreganancyL") != "")
                    txt_l.setText(jObj.getString("mPresentPreganancyL"));
                if (jObj.getString("mRegistrationWeek") != "")
                    txt_registration_week.setText(jObj.getString("mRegistrationWeek"));
                if (jObj.getString("mANTT1") != "")
                    txt_an_tt_1st.setText(jObj.getString("mANTT1"));
                if (jObj.getString("mANTT2") != "")
                    txt_an_tt_2nd.setText(jObj.getString("mANTT2"));
                if (jObj.getString("mIFAStateDate") != "")
                    txt_ifa_start_date.setText(jObj.getString("mIFAStateDate"));
                if (jObj.getString("mHeight") != "")
                    txt_height.setText(jObj.getString("mHeight"));
                if (jObj.getString("mBloodGroup") != "")
                    txt_blood_group.setText(jObj.getString("mBloodGroup"));
                if (jObj.getString("mHIV") != "")
                    txt_hiv.setText(jObj.getString("mHIV"));
                if (jObj.getString("mVDRL") != "")
                    txt_vdrl.setText(jObj.getString("mVDRL"));
                if (jObj.getString("mHepatitis") != "")
                    txt_Hepatitis.setText(jObj.getString("mHepatitis"));
                if (jObj.getString("hBloodGroup") != "")
                    txt_hus_blood_group.setText(jObj.getString("hBloodGroup"));
                if (jObj.getString("hVDRL") != "")
                    txt_hus_hiv.setText(jObj.getString("hVDRL"));
                if (jObj.getString("hHIV") != "")
                    txt_hus_vdrl.setText(jObj.getString("hHIV"));
                if (jObj.getString("hHepatitis") != "")
                    txt_hus_Hepatitis.setText(jObj.getString("hHepatitis"));




                RealmResults<PrimaryMotherDetailsRealmModel> delevaryDetailsPnMotherRealmModels = realm.where(PrimaryMotherDetailsRealmModel.class).findAll();
                if (delevaryDetailsPnMotherRealmModels.size()!=0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(PrimaryMotherDetailsRealmModel.class);
                        }
                    });
                }
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
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getAppversion());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getPicmeId());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getmName());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getAwwId());


            } else {
                Log.d("message---->", message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


/*
        try {
            jObj = new JSONObject(response);
            int status = jObj.getInt("status");
            String message = jObj.getString("message");
            if (status == 1) {
                RealmResults<PrimaryMotherDetailsRealmModel> delevaryDetailsPnMotherRealmModels = realm.where(PrimaryMotherDetailsRealmModel.class).findAll();
                if (delevaryDetailsPnMotherRealmModels.size()!=0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(PrimaryMotherDetailsRealmModel.class);
                        }
                    });
                }
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
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getAppversion());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getPicmeId());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getmName());
//                Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), primaryMotherDetailsRealmModel.getAwwId());

            } else {
                Log.d("message---->", message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
//        getValuefromRealm();
    }

    @Override
    public void getAllMotherPrimaryRegisterFailure(String response) {
        Log.d(MothersPrimaryRecordsActivity.class.getSimpleName(), "failure" + response);

    }
}
