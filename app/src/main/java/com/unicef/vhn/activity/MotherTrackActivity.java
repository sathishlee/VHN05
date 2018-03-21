package com.unicef.vhn.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.PNMotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.PNMotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;


public class MotherTrackActivity extends AppCompatActivity implements PNMotherListsViews {
TextView txt_mother_name,txt_picme_id,txt_mobile_number,txt_mage,txt_mlmpdate,txt_medddate;
    ProgressDialog pDialog;
    PNMotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mothers_details);

//        setContentView(R.layout.activity_maps);
        showActionBar();
        initUI();
        onClickListner();
    }

    private void onClickListner() {

    }

    public void showActionBar(){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Mother's Detail ");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new PNMotherListPresenter(MotherTrackActivity.this,this);
//        pnMotherListPresenter.getSelectedPNMother("V10001","1","1");
        pnMotherListPresenter.getSelectedPNMother(preferenceData.getVhnCode(),preferenceData.getVhnId(),  AppConstants.SELECTED_MID);

        txt_mother_name = (TextView) findViewById(R.id.txt_mother_name);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_mage = (TextView) findViewById(R.id.txt_mage);
        txt_mlmpdate = (TextView) findViewById(R.id.txt_mlmpdate);
        txt_medddate = (TextView) findViewById(R.id.txt_medddate);
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
        AppConstants.SELECTED_MID="0";
        Log.e(MotherTrackActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("tracking");
                txt_mother_name.setText(mJsnobject_tracking.getString("mName"));
                txt_picme_id.setText(mJsnobject_tracking.getString("mPicmeId"));
                txt_mobile_number.setText(mJsnobject_tracking.getString("mMotherMobile"));
                txt_mage.setText(mJsnobject_tracking.getString("mAge"));
                txt_mlmpdate.setText(mJsnobject_tracking.getString("mLMP"));
                txt_medddate.setText(mJsnobject_tracking.getString("mEDD"));
            }else{
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String message) {
        AppConstants.SELECTED_MID="0";

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }
}
