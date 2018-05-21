package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;

public class SosMotherDetailsActivity extends AppCompatActivity implements MotherListsViews, View.OnClickListener {
    TextView txt_username, txt_picme_id, txt_age, txt_risk, txt_message;
    String strMotherName, strPicmeId, strAge, strRisk, strMessage, strMobileNo, strAltMobileNo, strLatitude, strLongitude, strMotherType;
    ImageView img_call_1, img_call_2;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;

    Button btn_view_location, btn_view_report, btn_alert_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_mother_details);
        initUI();
        srtValue();
        showActionBar();
        onClickListner();
    }

    private void srtValue() {
        txt_username.setText(strMotherName);
        txt_picme_id.setText(strPicmeId);
        txt_age.setText(strAge);
        txt_risk.setText(strRisk);
        txt_message.setText(strMessage);
    }

    private void onClickListner() {
        btn_view_location.setOnClickListener(this);
        btn_view_report.setOnClickListener(this);
        btn_alert_close.setOnClickListener(this);
        img_call_1.setOnClickListener(this);
        img_call_2.setOnClickListener(this);
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sos Mother Detail");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(SosMotherDetailsActivity.this, this);

        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_age = (TextView) findViewById(R.id.txt_age);
        txt_risk = (TextView) findViewById(R.id.txt_risk);
        txt_message = (TextView) findViewById(R.id.txt_message);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
        btn_alert_close = (Button) findViewById(R.id.btn_alert_close);
        pnMotherListPresenter.getSelectedSosMother(preferenceData.getVhnId(), preferenceData.getVhnCode(), AppConstants.SOS_ID);
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
        Log.e(MotherTrackActivity.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONObject mJsnobject_vhnSOSDetails = mJsnobject.getJSONObject("vhnSOSDetails");
                txt_username.setText(mJsnobject_vhnSOSDetails.getString("mName"));
                strMotherName = mJsnobject_vhnSOSDetails.getString("mName");
                txt_picme_id.setText(mJsnobject_vhnSOSDetails.getString("mPicmeId"));
                strPicmeId = mJsnobject_vhnSOSDetails.getString("mPicmeId");
                strMobileNo = mJsnobject_vhnSOSDetails.getString("mMotherMobile");
                strAltMobileNo = mJsnobject_vhnSOSDetails.getString("mHusbandMobile");
                Log.d(SosMotherDetailsActivity.class.getSimpleName(), "strMobileNo" + strMobileNo);
                Log.d(SosMotherDetailsActivity.class.getSimpleName(), "strAltMobileNo" + strAltMobileNo);
                txt_age.setText(mJsnobject_vhnSOSDetails.getString("mAge"));
                strAge = mJsnobject_vhnSOSDetails.getString("mAge");
                txt_risk.setText(mJsnobject_vhnSOSDetails.getString("mRiskStatus"));
                strRisk = mJsnobject_vhnSOSDetails.getString("mRiskStatus");
                txt_message.setText(mJsnobject_vhnSOSDetails.getString("message"));
                strMessage = mJsnobject_vhnSOSDetails.getString("message");
                strLatitude = mJsnobject_vhnSOSDetails.getString("mLatitude");
                strLongitude = mJsnobject_vhnSOSDetails.getString("mLongitude");
                strMotherType = mJsnobject_vhnSOSDetails.getString("motherType");
                Log.d(SosMotherDetailsActivity.class.getSimpleName(), "strLatitude" + strLatitude);
                Log.d(SosMotherDetailsActivity.class.getSimpleName(), "strLongitude" + strLongitude);
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String response) {
        Log.e(MotherTrackActivity.class.getSimpleName(), "Response Error" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {
        Log.e(MotherTrackActivity.class.getSimpleName(), "Response Alert Closed success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAlertClosedError(String response) {
        Log.e(MotherTrackActivity.class.getSimpleName(), "Response Alert Closed success" + response);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
                break;
            case R.id.btn_view_report:
                if (strMotherType.equalsIgnoreCase("AN")) {
                    startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));
                } else if (strMotherType.equalsIgnoreCase("PN")) {
                    startActivity(new Intent(getApplicationContext(), PNViewReportsActivity.class));
                } else {

                }
                break;
            case R.id.btn_alert_close:
                pnMotherListPresenter.closeSosAlertSelectedMother(preferenceData.getVhnId(), preferenceData.getVhnCode(), AppConstants.SOS_ID);
                break;

            case R.id.img_call_1:
                makeCall(strMobileNo);
                break;
            case R.id.img_call_2:
                makeCall(strAltMobileNo);
                break;
        }
    }

    private void makeCall(String str_mobile_number) {

        Toast.makeText(getApplicationContext(), str_mobile_number, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCallPermission();

        } else {

            // Camera permissions is already available, show the camera preview.
            Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has already been granted. Displaying camera preview.");
//            showCameraPreview();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + str_mobile_number)));

        }

    }

    private void requestCallPermission() {


        Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(MothersDetailsActivity.class.getSimpleName(), "Displaying camera permission rationale to provide additional context.");
            Toast.makeText(this, "Displaying camera permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();

        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
// END_INCLUDE(camera_permission_request)

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    dial.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
