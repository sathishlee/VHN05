package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


public class MothersDetailsActivity extends AppCompatActivity implements View.OnClickListener, MotherListsViews {
    TextView txt_mother_name,txt_picme_id,txt_mage,txt_risk_status,txt_gest_week,txt_weight,txt_lmp_date,txt_edd_date,txt_next_visit;
    String strMobileNo,strAltMobileNo;
    String strLatitude,strLongitude;
    ImageView img_call_1,img_call_2;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;

    Button btn_view_location, btn_view_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mothers_details);
        initUI();
        showActionBar();
        onClickListner();
    }

    private void onClickListner() {
        btn_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MotherLocationActivity.class));
            }
        });
        btn_view_report.setOnClickListener(this);
        img_call_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(strMobileNo);
            }
        });img_call_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               makeCall(strAltMobileNo);
            }
        });
    }

    private void showActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AN Mother Detail");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void initUI(){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MothersDetailsActivity.this,this);
        pnMotherListPresenter.getSelectedMother(preferenceData.getVhnCode(),preferenceData.getVhnId(),  AppConstants.SELECTED_MID);

        txt_mother_name = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_mage = (TextView) findViewById(R.id.txt_age);
        txt_risk_status = (TextView) findViewById(R.id.txt_risk);
        txt_gest_week = (TextView) findViewById(R.id.txt_gest_week);
        txt_weight = (TextView) findViewById(R.id.txt_weight);
        txt_lmp_date = (TextView) findViewById(R.id.txt_lmp_date);
        txt_edd_date = (TextView) findViewById(R.id.txt_edd_date);
        txt_next_visit = (TextView) findViewById(R.id.txt_next_visit);
        img_call_1 =(ImageView)findViewById(R.id.img_call_1);
        img_call_2 =(ImageView)findViewById(R.id.img_call_2);


        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
    }


    private void makeCall(String str_mobile_number) {
        Toast.makeText(getApplicationContext(),str_mobile_number,Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCallPermission();

        } else {

            // Camera permissions is already available, show the camera preview.
            Log.i(MothersDetailsActivity.class.getSimpleName(),"CALL permission has already been granted. Displaying camera preview.");
//            showCameraPreview();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+"+str_mobile_number)));

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
            Log.i(MothersDetailsActivity.class.getSimpleName(),            "Displaying camera permission rationale to provide additional context.");
            Toast.makeText(this,"Displaying camera permission rationale to provide additional context.",Toast.LENGTH_SHORT).show();

        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
// END_INCLUDE(camera_permission_request)

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_view_location:startActivity(new Intent(getApplicationContext(),MotherLocationActivity.class));
                break;
            case  R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(),ANViewReportsActivity.class));
                break;
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MothersDetailsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);

        return super.onOptionsItemSelected(item);
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
//        AppConstants.SELECTED_MID="0";
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
                strMobileNo = mJsnobject_tracking.getString("mMotherMobile");
                strAltMobileNo = mJsnobject_tracking.getString("mHusbandMobile");
                txt_mage.setText(mJsnobject_tracking.getString("mAge"));
                txt_risk_status.setText(mJsnobject_tracking.getString("mRiskStatus"));
                txt_gest_week.setText(mJsnobject_tracking.getString("GSTAge"));
                txt_weight.setText(mJsnobject_tracking.getString("mWeight"));
                txt_next_visit.setText(mJsnobject_tracking.getString("nextVisit"));
                txt_lmp_date.setText(mJsnobject_tracking.getString("mLMP"));
                txt_edd_date.setText(mJsnobject_tracking.getString("mEDD"));
                strLatitude = mJsnobject_tracking.getString("mLatitude");
               strLongitude =mJsnobject_tracking.getString("mLongitude");
            }else{
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String message) {
//        AppConstants.SELECTED_MID="0";

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
