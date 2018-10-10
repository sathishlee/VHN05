package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherPrimaryRegisterPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.DelevaryDetailsPnMotherRealmModel;
import com.unicef.vhn.realmDbModel.PrimaryMotherDetailsRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.constant.AppConstants;
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
            txt_alter_mobile_no, txt_mother_occupation, txt_hus_occupation, txt_age_at_marriage, txt_consanguineous_marraige,
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
    TextView txt_no_internet, txt_no_records_found;
    LinearLayout ll_primary_view;

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
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        ll_primary_view = (LinearLayout) findViewById(R.id.ll_primary_view);
        motherPrimaryRegisterPresenter = new MotherPrimaryRegisterPresenter(MothersPrimaryRecordsActivity.this, this);
//        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(preferenceData.getPicmeId());
        if (checkNetwork.isNetworkAvailable()) {
            motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(AppConstants.MOTHER_PICME_ID);
        } else {
            isoffline = true;

        }
        motherPrimaryRegisterPresenter.getAllMotherPrimaryRegistration(AppConstants.MOTHER_PICME_ID);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        txt_no_internet.setVisibility(View.GONE);
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
            txt_no_internet.setVisibility(View.VISIBLE);
            getValuefromRealm();
        } else {
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
        Log.e(String.valueOf(MothersPrimaryRecordsActivity.class), primaryMotherDetailsRealmModelRealmResults.size() + "");
        Log.e(MothersPrimaryRecordsActivity.class.getSimpleName(), "primaryMotherDetailsRealmModelRealmResults  -->" + primaryMotherDetailsRealmModelRealmResults);
        for (int i = 0; i < primaryMotherDetailsRealmModelRealmResults.size(); i++) {
            PrimaryMotherDetailsRealmModel model = primaryMotherDetailsRealmModelRealmResults.get(i);

            if (model.getmName().equalsIgnoreCase("null")) {
                txt_name.setText("-");
            } else {
                txt_name.setText(model.getmName());
            }
            if (model.getmAge().equalsIgnoreCase("null")) {
                txt_mother_age.setText("-");
            } else {
                txt_mother_age.setText(model.getmAge());
            }
            if (model.getmLMP().equalsIgnoreCase("-")) {
                txt_lmp_date.setText("-");
            } else {
                txt_lmp_date.setText(model.getmLMP());
            }
            if (model.getmEDD().equalsIgnoreCase("null")) {
                txt_edd_date.setText("-");
            } else {
                txt_edd_date.setText(model.getmEDD());
            }
            if (model.getmMotherMobile().equalsIgnoreCase("null")) {
                txt_pry_mobile_no.setText("-");
            } else {
                txt_pry_mobile_no.setText(model.getmMotherMobile());
            }
            if (model.getmHusbandMobile().equalsIgnoreCase("null")) {
                txt_alter_mobile_no.setText("-");
            } else {
                txt_alter_mobile_no.setText(model.getmHusbandMobile());
            }
            if (model.getmMotherOccupation().equalsIgnoreCase("null")) {
                txt_mother_occupation.setText("-");
            } else {
                txt_mother_occupation.setText(model.getmMotherOccupation());
            }
            if (model.getmHusbandOccupation().equalsIgnoreCase("null")) {
                txt_hus_occupation.setText("-");
            } else {
                txt_hus_occupation.setText(model.getmHusbandOccupation());
            }
            if (model.getmAgeatMarriage().equalsIgnoreCase("null")) {
                txt_age_at_marriage.setText("-");
            } else {
                txt_age_at_marriage.setText(model.getmAgeatMarriage());
            }
            if (model.getmConsanguineousMarraige().equalsIgnoreCase("null")) {
                txt_consanguineous_marraige.setText("-");
            } else {
                txt_consanguineous_marraige.setText(model.getmConsanguineousMarraige());
            }
            if (model.getmHistoryIllness().equalsIgnoreCase("null")) {
                txt_history_of_illness.setText("-");
            } else {
                txt_history_of_illness.setText(model.getmHistoryIllness());
            }
            if (model.getmHistoryIllnessFamily().equalsIgnoreCase("null")) {
                txt_history_of_illness_family.setText("-");
            } else {
                txt_history_of_illness_family.setText(model.getmHistoryIllnessFamily());
            }
            if (model.getmAnySurgeryBefore().equalsIgnoreCase("null")) {
                txt_any_surgery_done.setText("-");
            } else {
                txt_any_surgery_done.setText(model.getmAnySurgeryBefore());
            }
            if (model.getmUseTobacco().equalsIgnoreCase("null")) {
                txt_tobacco.setText("-");
            } else {
                txt_tobacco.setText(model.getmUseTobacco());
            }
            if (model.getmUseAlcohol().equalsIgnoreCase("-")) {
                txt_alcohol.setText("-");
            } else {
                txt_alcohol.setText(model.getmUseAlcohol());
            }
            if (model.getmAnyMeditation().equalsIgnoreCase("null")) {
                txt_on_any_medication.setText("-");
            } else {
                txt_on_any_medication.setText(model.getmAnyMeditation());
            }
            if (model.getmAllergicToanyDrug().equalsIgnoreCase("null")) {
                txt_allergic_to_any_drug.setText("-");
            } else {
                txt_allergic_to_any_drug.setText(model.getmAllergicToanyDrug());
            }
            if (model.getmHistroyPreviousPreganancy().equalsIgnoreCase("null")) {
                txt_history_of_previous_pregnancy.setText("-");
            } else {
                txt_history_of_previous_pregnancy.setText(model.getmHistroyPreviousPreganancy());
            }
            if (model.getmLscsDone().equalsIgnoreCase("null")) {
                txt_lscs_done.setText("-");
            } else {
                txt_lscs_done.setText(model.getmLscsDone());
            }
            if (model.getmAnyComplecationDuringPreganancy().equalsIgnoreCase("null")) {
                txt_any_complication.setText("-");
            } else {
                txt_any_complication.setText(model.getmAnyComplecationDuringPreganancy());
            }
            if (model.getmPresentPreganancyG().equalsIgnoreCase("null")) {
                txt_g.setText("-");
            } else {
                txt_g.setText(model.getmPresentPreganancyG());
            }
            if (model.getmPresentPreganancyP().equalsIgnoreCase("null")) {
                txt_p.setText("-");
            } else {
                txt_p.setText(model.getmPresentPreganancyP());
            }
            if (model.getmPresentPreganancyA().equalsIgnoreCase("null")) {
                txt_a.setText("-");
            } else {
                txt_a.setText(model.getmPresentPreganancyA());
            }
            if (model.getmPresentPreganancyL().equalsIgnoreCase("null")) {
                txt_l.setText("-");
            } else {
                txt_l.setText(model.getmPresentPreganancyL());
            }
            if (model.getmRegistrationWeek().equalsIgnoreCase("null")) {
                txt_registration_week.setText("-");
            } else {
                txt_registration_week.setText(model.getmRegistrationWeek());
            }
            if (model.getmANTT1().equalsIgnoreCase("null")) {
                txt_an_tt_1st.setText("-");
            } else {
                txt_an_tt_1st.setText(model.getmANTT1());
            }
            if (model.getmANTT2().equalsIgnoreCase("null")) {
                txt_an_tt_2nd.setText("-");
            } else {
                txt_an_tt_2nd.setText(model.getmANTT2());
            }
            if (model.getmIFAStateDate().equalsIgnoreCase("null")) {
                txt_ifa_start_date.setText("-");
            } else {
                txt_ifa_start_date.setText(model.getmIFAStateDate());
            }
            if (model.getmHeight().equalsIgnoreCase("null")) {
                txt_height.setText("-");
            } else {
                txt_height.setText(model.getmHeight());
            }
            if (model.getmBloodGroup().equalsIgnoreCase("null")) {
                txt_blood_group.setText("-");
            } else {
                txt_blood_group.setText(model.getmBloodGroup());
            }
            if (model.getmHIV().equalsIgnoreCase("null")) {
                txt_hiv.setText("-");
            } else {
                txt_hiv.setText(model.getmHIV());
            }
            if (model.getmVDRL().equalsIgnoreCase("null")) {
                txt_vdrl.setText("-");
            } else {
                txt_vdrl.setText(model.getmVDRL());
            }
            if (model.getmHepatitis().equalsIgnoreCase("null")) {
                txt_Hepatitis.setText(model.getmHepatitis());
            }
            if (model.gethBloodGroup().equalsIgnoreCase("null")) {
                txt_hus_blood_group.setText("-");
            } else {
                txt_hus_blood_group.setText(model.gethBloodGroup());
            }
            if (model.gethHIV().equalsIgnoreCase("null")) {
                txt_hus_hiv.setText("-");
            } else {
                txt_hus_hiv.setText(model.gethHIV());
            }
            if (model.gethVDRL().equalsIgnoreCase("null")) {
                txt_hus_vdrl.setText("-");
            } else {
                txt_hus_vdrl.setText(model.gethVDRL());
            }
            if (model.gethHepatitis().equalsIgnoreCase("null")) {
                txt_hus_Hepatitis.setText("-");
            } else {
                txt_hus_Hepatitis.setText(model.gethHepatitis());
            }

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
                if (jObj.getString("mName").equalsIgnoreCase("null")) {
                    txt_name.setText("-");
                } else {
                    txt_name.setText(jObj.getString("mName"));
                }
                if (jObj.getString("mAge").equalsIgnoreCase("null")) {
                    txt_mother_age.setText("-");
                } else {
                    txt_mother_age.setText(jObj.getString("mAge"));
                }
                if (jObj.getString("mLMP").equalsIgnoreCase("null")) {
                    txt_lmp_date.setText("-");
                } else {
                    txt_lmp_date.setText(jObj.getString("mLMP"));
                }
                if (jObj.getString("mEDD").equalsIgnoreCase("null")) {
                    txt_edd_date.setText("-");
                } else {
                    txt_edd_date.setText(jObj.getString("mEDD"));
                }
                if (jObj.getString("mMotherMobile").equalsIgnoreCase("null")) {
                    txt_pry_mobile_no.setText("-");
                } else {
                    txt_pry_mobile_no.setText(jObj.getString("mMotherMobile"));
                }
                if (jObj.getString("mHusbandMobile").equalsIgnoreCase("null")) {
                    txt_alter_mobile_no.setText("-");
                } else {
                    txt_alter_mobile_no.setText(jObj.getString("mHusbandMobile"));
                }
                if (jObj.getString("mMotherOccupation").equalsIgnoreCase("null")) {
                    txt_mother_occupation.setText("-");
                } else {
                    txt_mother_occupation.setText(jObj.getString("mMotherOccupation"));
                }
                if (jObj.getString("mHusbandOccupation").equalsIgnoreCase("null")) {
                    txt_hus_occupation.setText("-");
                } else {
                    txt_hus_occupation.setText(jObj.getString("mHusbandOccupation"));
                }

                if (jObj.getString("mAgeatMarriage").equalsIgnoreCase("null")) {
                    txt_age_at_marriage.setText("-");
                } else {
                    txt_age_at_marriage.setText(jObj.getString("mAgeatMarriage"));
                }
                if (jObj.getString("mConsanguineousMarraige").equalsIgnoreCase("null")) {
                    txt_consanguineous_marraige.setText("-");
                } else {
                    txt_consanguineous_marraige.setText(jObj.getString("mConsanguineousMarraige"));
                }
                if (jObj.getString("mHistoryIllness").equalsIgnoreCase("null")) {
                    txt_history_of_illness.setText("-");
                } else {
                    txt_history_of_illness.setText(jObj.getString("mHistoryIllness"));
                }
                if (jObj.getString("mHistoryIllnessFamily").equalsIgnoreCase("null")) {
                    txt_history_of_illness_family.setText("-");
                } else {
                    txt_history_of_illness_family.setText(jObj.getString("mHistoryIllnessFamily"));
                }
                if (jObj.getString("mAnySurgeryBefore").equalsIgnoreCase("null")) {
                    txt_any_surgery_done.setText("-");
                } else {
                    txt_any_surgery_done.setText(jObj.getString("mAnySurgeryBefore"));
                }
                if (jObj.getString("mUseTobacco").equalsIgnoreCase("null")) {
                    txt_tobacco.setText("-");
                } else {
                    txt_tobacco.setText(jObj.getString("mUseTobacco"));
                }
                if (jObj.getString("mUseAlcohol").equalsIgnoreCase("null")) {
                    txt_alcohol.setText("-");
                } else {
                    txt_alcohol.setText(jObj.getString("mUseAlcohol"));
                }
                if (jObj.getString("mAnyMeditation").equalsIgnoreCase("null")) {
                    txt_on_any_medication.setText("-");
                } else {
                    txt_on_any_medication.setText(jObj.getString("mAnyMeditation"));
                }
                if (jObj.getString("mAllergicToanyDrug").equalsIgnoreCase("null")) {
                    txt_allergic_to_any_drug.setText("-");
                } else {
                    txt_allergic_to_any_drug.setText(jObj.getString("mAllergicToanyDrug"));
                }
                if (jObj.getString("mHistroyPreviousPreganancy").equalsIgnoreCase("null")) {
                    txt_history_of_previous_pregnancy.setText("-");
                } else {
                    txt_history_of_previous_pregnancy.setText(jObj.getString("mHistroyPreviousPreganancy"));
                }
                if (jObj.getString("mLscsDone").equalsIgnoreCase("null")) {
                    txt_lscs_done.setText("-");
                } else {
                    txt_lscs_done.setText(jObj.getString("mLscsDone"));
                }
                if (jObj.getString("mAnyComplecationDuringPreganancy").equalsIgnoreCase("null")) {
                    txt_any_complication.setText("-");
                } else {
                    txt_any_complication.setText(jObj.getString("mAnyComplecationDuringPreganancy"));
                }
                if (jObj.getString("mPresentPreganancyG").equalsIgnoreCase("null")) {
                    txt_g.setText("-");
                } else {
                    txt_g.setText(jObj.getString("mPresentPreganancyG"));
                }
                if (jObj.getString("mPresentPreganancyP").equalsIgnoreCase("null")) {
                    txt_p.setText("-");
                } else {
                    txt_p.setText(jObj.getString("mPresentPreganancyP"));
                }
                if (jObj.getString("mPresentPreganancyA").equalsIgnoreCase("null")) {
                    txt_a.setText("-");
                } else {
                    txt_a.setText(jObj.getString("mPresentPreganancyA"));
                }
                if (jObj.getString("mPresentPreganancyL").equalsIgnoreCase("null")) {
                    txt_l.setText("-");
                } else {
                    txt_l.setText(jObj.getString("mPresentPreganancyL"));
                }
                if (jObj.getString("mRegistrationWeek").equalsIgnoreCase("null")) {
                    txt_registration_week.setText("-");
                } else {
                    txt_registration_week.setText(jObj.getString("mRegistrationWeek"));
                }
                if (jObj.getString("mANTT1").equalsIgnoreCase("null")) {
                    txt_an_tt_1st.setText("-");
                } else {
                    txt_an_tt_1st.setText(jObj.getString("mANTT1"));
                }
                if (jObj.getString("mANTT2").equalsIgnoreCase("null")) {
                    txt_an_tt_2nd.setText("-");
                } else {
                    txt_an_tt_2nd.setText(jObj.getString("mANTT2"));
                }
                if (jObj.getString("mIFAStateDate").equalsIgnoreCase("null")) {
                    txt_ifa_start_date.setText("-");
                } else {
                    txt_ifa_start_date.setText(jObj.getString("mIFAStateDate"));
                }
                if (jObj.getString("mHeight").equalsIgnoreCase("null")) {
                    txt_height.setText("-");
                } else {
                    txt_height.setText(jObj.getString("mHeight")+"cms");
                }
                if (jObj.getString("mBloodGroup").equalsIgnoreCase("null")) {
                    txt_blood_group.setText("-");
                } else {
                    txt_blood_group.setText(jObj.getString("mBloodGroup"));
                }
                if (jObj.getString("mHIV").equalsIgnoreCase("null")) {
                    txt_hiv.setText("-");
                } else {
                    txt_hiv.setText(jObj.getString("mHIV"));
                }
                if (jObj.getString("mVDRL").equalsIgnoreCase("null")) {
                    txt_vdrl.setText("-");
                } else {
                    txt_vdrl.setText(jObj.getString("mVDRL"));
                }
                if (jObj.getString("mHepatitis").equalsIgnoreCase("null")) {
                    txt_Hepatitis.setText("-");
                } else {
                    txt_Hepatitis.setText(jObj.getString("mHepatitis"));
                }
                if (jObj.getString("hBloodGroup").equalsIgnoreCase("null")) {
                    txt_hus_blood_group.setText("-");
                } else {
                    txt_hus_blood_group.setText(jObj.getString("hBloodGroup"));
                }
                if (jObj.getString("hHIV").equalsIgnoreCase("null")) {
                    txt_hus_hiv.setText("-");
                } else {
                    txt_hus_hiv.setText(jObj.getString("hHIV"));
                }
                if (jObj.getString("hVDRL").equalsIgnoreCase("null")) {
                    txt_hus_vdrl.setText("-");
                } else {
                    txt_hus_vdrl.setText(jObj.getString("hVDRL"));
                }
                if (jObj.getString("hHepatitis").equalsIgnoreCase("null")) {
                    txt_hus_Hepatitis.setText("-");
                } else {
                    txt_hus_Hepatitis.setText(jObj.getString("hHepatitis"));
                }

                RealmResults<PrimaryMotherDetailsRealmModel> delevaryDetailsPnMotherRealmModels = realm.where(PrimaryMotherDetailsRealmModel.class).findAll();
                if (delevaryDetailsPnMotherRealmModels.size() != 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(PrimaryMotherDetailsRealmModel.class);
                        }
                    });
                }
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
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
