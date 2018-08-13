package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherDeliveryPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.DelevaryDetailsPnMotherRealmModel;
import com.unicef.vhn.realmDbModel.PrimaryMotherDetailsRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.MotherDeliveryViews;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNMotherDeliveryReportActivity extends AppCompatActivity implements MotherDeliveryViews {

    TextView txt_delivery_date, txt_delivery_time, txt_delivery_place, txt_delivery_type, txt_delivery_mother_outcome,
            txt_delivery_new_born, txt_infant_height, txt_infant_weight, txt_infant_id, txt_infant_birth_type,
            txt_breast_feeding_given, txt_admitted_in_sncu, new_born_sncu_date, txt_new_born_outcome, txt_bcg_given_date,
            txt_opv_given_date, txt_hepb_given_date;

    ProgressDialog progressDialog;
    MotherDeliveryPresenter motherDeliveryPresenter;
    PreferenceData preferenceData;
Realm realm;
CheckNetwork checkNetwork;
TextView txt_no_internet;
boolean isOffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm= RealmController.with(this).getRealm();
        setContentView(R.layout.activity_delivery_details_view);
        initUI();
        showActionBar();

    }

    private void initUI() {
        checkNetwork=new CheckNetwork(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        motherDeliveryPresenter = new MotherDeliveryPresenter(PNMotherDeliveryReportActivity.this, this);
//        motherDeliveryPresenter.deliveryDetails(preferenceData.getPicmeId(), preferenceData.getMId());
        if (checkNetwork.isNetworkAvailable()){
            motherDeliveryPresenter.deliveryDetails(AppConstants.MOTHER_PICME_ID,AppConstants.SELECTED_MID);
        }else{
            isOffline=true;
        }
        motherDeliveryPresenter.deliveryDetails(AppConstants.MOTHER_PICME_ID, AppConstants.SELECTED_MID);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_internet.setVisibility(View.GONE);
        txt_delivery_date = (TextView) findViewById(R.id.txt_delivery_date);
        txt_delivery_time = (TextView) findViewById(R.id.txt_delivery_time);
        txt_delivery_place = (TextView) findViewById(R.id.txt_delivery_place);
        txt_delivery_type = (TextView) findViewById(R.id.txt_delivery_type);
        txt_delivery_mother_outcome = (TextView) findViewById(R.id.txt_delivery_mother_outcome);
        txt_delivery_new_born = (TextView) findViewById(R.id.txt_delivery_new_born);
        txt_infant_height = (TextView) findViewById(R.id.txt_infant_height);
        txt_infant_weight = (TextView) findViewById(R.id.txt_infant_weight);
        txt_infant_id = (TextView) findViewById(R.id.txt_infant_id);
        txt_infant_birth_type = (TextView) findViewById(R.id.txt_infant_birth_type);
        txt_breast_feeding_given = (TextView) findViewById(R.id.txt_breast_feeding_given);
        txt_admitted_in_sncu = (TextView) findViewById(R.id.txt_admitted_in_sncu);
        new_born_sncu_date = (TextView) findViewById(R.id.new_born_sncu_date);
        txt_new_born_outcome = (TextView) findViewById(R.id.txt_new_born_outcome);
        txt_bcg_given_date = (TextView) findViewById(R.id.txt_bcg_given_date);
        txt_opv_given_date = (TextView) findViewById(R.id.txt_opv_given_date);
        txt_hepb_given_date = (TextView) findViewById(R.id.txt_hepb_given_date);
if (isOffline) {
    txt_no_internet.setVisibility(View.VISIBLE);
    getValuefromRealm();
}else{
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Record Not Found");
    builder.create();
}

    }

    private void getValuefromRealm() {
        realm.beginTransaction();
        RealmResults<DelevaryDetailsPnMotherRealmModel> primaryMotherDetailsRealmModelRealmResults = realm.where(DelevaryDetailsPnMotherRealmModel.class).equalTo("dpicmeId", AppConstants.MOTHER_PICME_ID).equalTo("mid", AppConstants.SELECTED_MID).findAll();
//        RealmResults<DelevaryDetailsPnMotherRealmModel> primaryMotherDetailsRealmModelRealmResults = realm.where(DelevaryDetailsPnMotherRealmModel.class).findAll();
        Log.e(MothersPrimaryRecordsActivity.class.getSimpleName(),"primaryMotherDetailsRealmModelRealmResults  -->"+primaryMotherDetailsRealmModelRealmResults);

        for(int i=0;i<primaryMotherDetailsRealmModelRealmResults.size();i++) {
//            preferenceData.storeDid(model.getDid());
//            Log.d("response---->", response);
            DelevaryDetailsPnMotherRealmModel model = primaryMotherDetailsRealmModelRealmResults.get(i);
            if(model.getDdatetime().equalsIgnoreCase("null")){
                txt_delivery_date.setText("-");
            }else {
                txt_delivery_date.setText(model.getDdatetime());
            }if(model.getDtime().equalsIgnoreCase("null")){
                txt_delivery_time.setText("-");
            }else {
                txt_delivery_time.setText(model.getDtime());

            }if(model.getDplace().equalsIgnoreCase("null")){
                txt_delivery_place.setText("-");
            }else {
                txt_delivery_place.setText(model.getDplace());


            }if(model.getDdeleveryDetails().equalsIgnoreCase("null")){
                txt_delivery_type.setText("-");
            }else {
                txt_delivery_type.setText(model.getDdeleveryDetails());
            }if(model.getDdeleveryOutcomeMother().equalsIgnoreCase("null")){
                txt_delivery_mother_outcome.setText("-");
            }else {
                txt_delivery_mother_outcome.setText(model.getDdeleveryOutcomeMother());

            }
            if(model.getDnewBorn().equalsIgnoreCase("null")){
                txt_delivery_new_born.setText("-");
            }else {
                txt_delivery_new_born.setText(model.getDnewBorn());
            }
           if(model.getdInfantId().equalsIgnoreCase("null")){
               txt_infant_id.setText("-");
            }else {
               txt_infant_id.setText(model.getdInfantId());
            }
           if(model.getdBirthDetails().equalsIgnoreCase("null")){
               txt_infant_birth_type.setText("-");
            }else {
               txt_infant_birth_type.setText(model.getdBirthDetails());

           }
           if(model.getdBirthWeight().equalsIgnoreCase("null")){
               txt_infant_weight.setText("-");
            }else {
               txt_infant_weight.setText(model.getdBirthWeight()+ " gm");
           }
           if(model.getdBirthHeight().equalsIgnoreCase("null")){
               txt_infant_height.setText("-");
            }else {
               txt_infant_height.setText(model.getdBirthHeight()+" cm");

           }
            if(model.getdBreastFeedingGiven().equalsIgnoreCase("null")){
                txt_breast_feeding_given.setText("-");
            }else {
                txt_breast_feeding_given.setText(model.getdBreastFeedingGiven());


            }
             if(model.getdAdmittedSNCU().equalsIgnoreCase("null")){
                 txt_admitted_in_sncu.setText("-");
            }else {
                 txt_admitted_in_sncu.setText(model.getdAdmittedSNCU());
            }
             if(model.getdSNCUDate().equalsIgnoreCase("null")){
                 new_born_sncu_date.setText("-");
            }else {
                 new_born_sncu_date.setText(model.getdSNCUDate());
            }

            if(model.getdSNCUOutcome().equalsIgnoreCase("null")){
                txt_new_born_outcome.setText("-");
            }else {
                txt_new_born_outcome.setText(model.getdSNCUOutcome());
            }
            if(model.getdBCGDate().equalsIgnoreCase("null")){
                txt_bcg_given_date.setText("-");
            }else {
                txt_bcg_given_date.setText(model.getdBCGDate());
            }
           if(model.getdOPVDate().equalsIgnoreCase("null")){
               txt_opv_given_date.setText("-");
            }else {
               txt_opv_given_date.setText(model.getdOPVDate());
            } if(model.getdHEPBDate().equalsIgnoreCase("null")){
                txt_hepb_given_date.setText("-");
            }else {
                txt_hepb_given_date.setText(model.getdHEPBDate());
            }

        }
        realm.commitTransaction();
        }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mother Delivery Details");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(AddReferral.this, ReferralList.class);
        finish();
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deliveryDetailsSuccess(String response) {
        Log.d(PNMotherDeliveryReportActivity.class.getSimpleName(), "Success Response" + response);
        deliveryValues(response);
    }

    @Override
    public void deliveryDetailsFailure(String response) {
        Log.d(PNMotherDeliveryReportActivity.class.getSimpleName(), "failure" + response);

    }

    public void deliveryValues(String response) {
        /*
        JSONObject jsonObject_res = null;
        try {
            jsonObject_res = new JSONObject(response);
            String status = jsonObject_res.getString("status");

            String message = jsonObject_res.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONObject jsonObject = jsonObject_res.getJSONObject("Delevery_Info");

                preferenceData.storeDid(jsonObject.getString("did"));
                Log.d("response---->", response);
                txt_delivery_date.setText(jsonObject.getString("ddatetime"));
                txt_delivery_time.setText(jsonObject.getString("dtime"));
                txt_delivery_place.setText(jsonObject.getString("dplace"));
                txt_delivery_type.setText(jsonObject.getString("ddeleveryDetails"));
                txt_delivery_mother_outcome.setText(jsonObject.getString("ddeleveryOutcomeMother"));
                txt_delivery_new_born.setText(jsonObject.getString("dnewBorn"));
                txt_infant_id.setText(jsonObject.getString("dInfantId"));
                txt_infant_birth_type.setText(jsonObject.getString("dBirthDetails"));
                txt_infant_weight.setText(jsonObject.getString("dBirthWeight"));
                txt_infant_height.setText(jsonObject.getString("dBirthHeight"));
                txt_breast_feeding_given.setText(jsonObject.getString("dBreastFeedingGiven"));
                txt_admitted_in_sncu.setText(jsonObject.getString("dAdmittedSNCU"));
                new_born_sncu_date.setText(jsonObject.getString("dSNCUDate"));
                txt_new_born_outcome.setText(jsonObject.getString("dSNCUOutcome"));
                txt_bcg_given_date.setText(jsonObject.getString("dBCGDate"));
                txt_opv_given_date.setText(jsonObject.getString("dOPVDate"));
                txt_hepb_given_date.setText(jsonObject.getString("dHEPBDate"));
            } else {
                Log.d("message---->", message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        JSONObject jsonObject_res = null;
        try {
            jsonObject_res = new JSONObject(response);
            String status = jsonObject_res.getString("status");

            String message = jsonObject_res.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONObject jsonObject = jsonObject_res.getJSONObject("Delevery_Info");
//                preferenceData.storeDid(jsonObject.getString("did"));
//                Log.d("response---->", response);
                if (realm.isInTransaction()){
                    realm.cancelTransaction();
                }
                RealmResults<DelevaryDetailsPnMotherRealmModel> delevaryDetailsPnMotherRealmModels = realm.where(DelevaryDetailsPnMotherRealmModel.class).findAll();
               if (delevaryDetailsPnMotherRealmModels.size()!=0) {
                   realm.executeTransaction(new Realm.Transaction() {
                       @Override
                       public void execute(Realm realm) {
                           realm.delete(DelevaryDetailsPnMotherRealmModel.class);
                       }
                   });
               }
                realm.beginTransaction();
                DelevaryDetailsPnMotherRealmModel delevaryDetailsPnMotherRealmModel = realm.createObject(DelevaryDetailsPnMotherRealmModel.class);
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
                delevaryDetailsPnMotherRealmModel.setDpicmeId(jsonObject.getString("dpicmeId"));
                delevaryDetailsPnMotherRealmModel.setMid(jsonObject.getString("mid"));
                delevaryDetailsPnMotherRealmModel.setChildGender(jsonObject.getString("childGender"));


                realm.commitTransaction();


            } else {
                Log.d("message---->", message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getValuefromRealm();

    }
}
