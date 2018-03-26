package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.R;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;

public class PNMotherDetailsActivity extends AppCompatActivity implements View.OnClickListener, MotherListsViews {
    LinearLayout ll_pn_mother_details;
    TextView txt_username, txt_picme_id, txt_mage, txt_risk,
            txt_date_of_delivery, txt_weight, txt_type_of_delivery, txt_maturity, txt_next_visit, txt_husb_name, txt_aww_name, txt_relationship, txt_aww_relationship;
    ImageView img_call_1, img_call_2;
    Button btn_view_location, btn_view_report;

    String strMobileNo,strAltMobileNo;
    String strLatitude,strLongitude;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pn_mother_details);
        showActionBar();
        intUI();
        onClickListner();

    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN Mother Detail");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onClickListner() {
        img_call_1.setOnClickListener(this);
        img_call_2.setOnClickListener(this);
        btn_view_location.setOnClickListener(this);
        btn_view_report.setOnClickListener(this);

    }

    private void intUI() {
        ll_pn_mother_details = (LinearLayout) findViewById(R.id.ll_pn_mother_details);
        ll_pn_mother_details.setVisibility(View.GONE);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(PNMotherDetailsActivity.this,this);
        pnMotherListPresenter.getSelectedPNMother(AppConstants.SELECTED_MID);


        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_mage = (TextView) findViewById(R.id.txt_age);
        txt_risk = (TextView) findViewById(R.id.txt_risk);
        txt_date_of_delivery = (TextView) findViewById(R.id.txt_date_of_delivery);
        txt_weight = (TextView) findViewById(R.id.txt_weight);
        txt_type_of_delivery = (TextView) findViewById(R.id.txt_type_of_delivery);
        txt_maturity = (TextView) findViewById(R.id.txt_maturity);
        txt_next_visit = (TextView) findViewById(R.id.txt_next_visit);
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_aww_name = (TextView) findViewById(R.id.txt_aww_name);
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_relationship = (TextView) findViewById(R.id.txt_relationship);
        txt_aww_relationship = (TextView) findViewById(R.id.txt_aww_relationship);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:
                break;

            case R.id.btn_view_report: startActivity(new Intent(getApplicationContext(),PNViewReportsActivity.class));
                break;
            case R.id.img_call_1:
                break;
            case R.id.img_call_2:
                break;
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

    @Override
    public void showLoginSuccess(String response) {
        Log.d(PNMotherDetailsActivity.class.getSimpleName(),"Response success"+response);
//        AppConstants.SELECTED_MID="0";
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")){
                ll_pn_mother_details.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("delveryInfo");
                txt_username.setText(mJsnobject_tracking.getString("mName"));
                txt_picme_id.setText(mJsnobject_tracking.getString("dpicmeId"));
                strMobileNo = mJsnobject_tracking.getString("mMotherMobile");
                strAltMobileNo = mJsnobject_tracking.getString("mHusbandMobile");
                txt_mage.setText(mJsnobject_tracking.getString("mAge"));
                txt_risk.setText(mJsnobject_tracking.getString("mRiskStatus"));

                txt_date_of_delivery.setText(mJsnobject_tracking.getString("ddatetime"));
                txt_type_of_delivery.setText(mJsnobject_tracking.getString("dtime"));
                txt_weight.setText(mJsnobject_tracking.getString("mWeight"));
                txt_maturity.setText(mJsnobject_tracking.getString("meaturityDate"));
                txt_next_visit.setText(mJsnobject_tracking.getString(""));

                strLatitude = mJsnobject_tracking.getString("mLatitude");
                strLongitude =mJsnobject_tracking.getString("mLongitude");
            }
            else{
                ll_pn_mother_details.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
finish();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String response) {
        ll_pn_mother_details.setVisibility(View.GONE);

        Log.d(PNMotherDetailsActivity.class.getSimpleName(),"Response Error"+response);
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
