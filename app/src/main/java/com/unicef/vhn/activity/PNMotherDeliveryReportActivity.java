package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherDeliveryPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.MotherDeliveryViews;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details_view);
        initUI();
        showActionBar();

    }

    private void initUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        motherDeliveryPresenter = new MotherDeliveryPresenter(PNMotherDeliveryReportActivity.this, this);
        motherDeliveryPresenter.deliveryDetails(AppConstants.MOTHER_PICME_ID, AppConstants.SELECTED_MID);
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
        finish();
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
        }
    }
}
