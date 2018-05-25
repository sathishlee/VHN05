package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;

public class TremAndPreTreamDetailsActivity extends AppCompatActivity implements MotherListsViews {
    ProgressDialog progressDialog;

    PreferenceData preferenceData;
    MotherListPresenter motherListPresenter;

    TextView txt_delivery_date, txt_delivery_time, txt_delivery_place, txt_delivery_type, txt_delivery_mother_outcome,
            txt_delivery_new_born, txt_infant_height, txt_infant_weight, txt_infant_id, txt_infant_birth_type,
            txt_breast_feeding_given, txt_admitted_in_sncu, new_born_sncu_date, txt_new_born_outcome, txt_bcg_given_date,
            txt_opv_given_date, txt_hepb_given_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trem_and_pre_tream_details);
        showActionBar();

        initUI();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Trem & Pre Trem Details");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(this, MainActivity.class);
        finish();
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        motherListPresenter = new MotherListPresenter(this, this);
        motherListPresenter.getTremAndPreTremMothers(preferenceData.getVhnId(), preferenceData.getVhnCode(), AppConstants.SELECTED_MID);
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
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showLoginSuccess(String response) {
        Log.d(TremAndPreTreamDetailsActivity.class.getSimpleName(), "Success Response" + response);
        deliveryValues(response);
    }

    private void deliveryValues(String response) {

        JSONObject jsonObject_res = null;
        try {
            jsonObject_res = new JSONObject(response);
            String status = jsonObject_res.getString("status");

            String message = jsonObject_res.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONObject jsonObject = jsonObject_res.getJSONObject("SingleDelveryInfo");

//                preferenceData.storeDid(jsonObject.getString("did" ));
                Log.d("message---->", message);
//                if(jsonObject.getString("ddatetime")!="")
                txt_delivery_date.setText(jsonObject.getString("ddatetime"));
//                if (jsonObject.getString("dtime")!="")
                txt_delivery_time.setText(jsonObject.getString("dtime"));
//                if (jsonObject.getString("dplace")!="")
                txt_delivery_place.setText(jsonObject.getString("dplace"));
//                if (jsonObject.getString("ddeleveryDetails")!="")
                txt_delivery_type.setText(jsonObject.getString("ddeleveryDetails"));
//                if (jsonObject.getString("ddeleveryOutcome")!="")
                txt_delivery_mother_outcome.setText(jsonObject.getString("ddeleveryOutcomeMother"));
//                if (jsonObject.getString("dnewBorn")!="")
                txt_delivery_new_born.setText(jsonObject.getString("dnewBorn"));
//                if (jsonObject.getString("dInfantId")!="")
                txt_infant_id.setText(jsonObject.getString("dInfantId"));
//                if (jsonObject.getString("dBirthDetails")!="")
                txt_infant_birth_type.setText(jsonObject.getString("dBirthDetails"));
//                if (jsonObject.getString("dBirthWeight")!="")
                txt_infant_weight.setText(jsonObject.getString("dBirthWeight"));

//                if (jsonObject.getString("dBirthHeight")!="")
                txt_infant_height.setText(jsonObject.getString("dBirthHeight"));
//                if (jsonObject.getString("dBreastFeedingGiven")!="")
                txt_breast_feeding_given.setText(jsonObject.getString("dBreastFeedingGiven"));
//                if (jsonObject.getString("dAdmittedSNCU")!="")
                txt_admitted_in_sncu.setText(jsonObject.getString("dAdmittedSNCU"));
//                if (jsonObject.getString("dSNCUDate")!="")
                new_born_sncu_date.setText(jsonObject.getString("dSNCUDate"));
//                if (jsonObject.getString("dSNCUOutcome")!="")
                txt_new_born_outcome.setText(jsonObject.getString("dSNCUOutcome"));
//                if (jsonObject.getString("dBCGDate")!="")
                txt_bcg_given_date.setText(jsonObject.getString("dBCGDate"));
//                if (jsonObject.getString("dOPVDate")!="")
                txt_opv_given_date.setText(jsonObject.getString("dOPVDate"));
//                if (jsonObject.getString("dHEPBDate")!="")
                txt_hepb_given_date.setText(jsonObject.getString("dHEPBDate"));
            } else {
                Log.d("message---->", message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showLoginError(String response) {
        Log.d(TremAndPreTreamDetailsActivity.class.getSimpleName(), "Error Response" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
