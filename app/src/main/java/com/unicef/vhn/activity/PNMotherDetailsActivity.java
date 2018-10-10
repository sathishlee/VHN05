package com.unicef.vhn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.R;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.MotherListsViews;

import io.realm.Realm;
import org.json.JSONException;
import org.json.JSONObject;

/*PN Mother Details  have api call working well is not use this project,
use for PNMotherDetailviewActivity */
public class PNMotherDetailsActivity extends AppCompatActivity implements View.OnClickListener, MotherListsViews {
    LinearLayout ll_pn_mother_details;
    TextView txt_username, txt_picme_id, txt_mage, txt_risk,
            txt_date_of_delivery, txt_weight, txt_type_of_delivery, txt_maturity, txt_next_visit,
            txt_husb_name, txt_aww_name, txt_relationship, txt_aww_relationship, txt_mother_name_call,
            txt_gest_week, txt_lmp_date, txt_edd_date, txt_delivery_date, txt_birth_weight, txt_type_delivery, txt_pn_next_visit;
    ImageView img_call_1, img_call_2, cardview_image;
    Button btn_view_location, btn_view_report;
    Context context;
    String strMobileNo, strAltMobileNo;
    String strLatitude, strLongitude, str_mPhoto;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(PNMotherDetailsActivity.this, this, realm);
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
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_relationship = (TextView) findViewById(R.id.txt_relationship);
        txt_aww_relationship = (TextView) findViewById(R.id.txt_aww_relationship);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        txt_mother_name_call = (TextView) findViewById(R.id.txt_mother_name_call);

        txt_gest_week = (TextView) findViewById(R.id.txt_gest_week);
        txt_lmp_date = (TextView) findViewById(R.id.txt_lmp_date);
        txt_edd_date = (TextView) findViewById(R.id.txt_edd_date);
        txt_delivery_date = (TextView) findViewById(R.id.txt_delivery_date);
        txt_birth_weight = (TextView) findViewById(R.id.txt_birth_weight);
        txt_type_delivery = (TextView) findViewById(R.id.txt_type_delivery);
        txt_pn_next_visit = (TextView) findViewById(R.id.txt_pn_next_visit);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
                break;
            case R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(), PNViewReportsActivity.class));
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
        Log.d(PNMotherDetailsActivity.class.getSimpleName(), "Response success" + response);
//        AppConstants.SELECTED_MID="0";
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                ll_pn_mother_details.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("delveryInfo");
                txt_username.setText(mJsnobject_tracking.getString("mName"));
                txt_picme_id.setText(mJsnobject_tracking.getString("dpicmeId"));
                strMobileNo = mJsnobject_tracking.getString("mMotherMobile");
                strAltMobileNo = mJsnobject_tracking.getString("mHusbandMobile");
                txt_mage.setText(mJsnobject_tracking.getString("mAge"));
                txt_lmp_date.setText(mJsnobject_tracking.getString("mLMP"));
                txt_edd_date.setText(mJsnobject_tracking.getString("mEDD"));
                txt_delivery_date.setText(mJsnobject_tracking.getString("ddatetime"));
                txt_birth_weight.setText(mJsnobject_tracking.getString("dBirthWeight"));
                txt_type_delivery.setText(mJsnobject_tracking.getString("dBirthDetails"));
                if (mJsnobject_tracking.getString("NextVisit").equalsIgnoreCase("Visits Closed")) {
                    txt_pn_next_visit.setText(mJsnobject_tracking.getString("PN" + "NextVisit"));
                } else {
                    txt_pn_next_visit.setText(mJsnobject_tracking.getString("NextVisit"));
                }

                txt_risk.setText(mJsnobject_tracking.getString("mRiskStatus"));
                txt_date_of_delivery.setText(mJsnobject_tracking.getString("ddatetime"));
                txt_type_of_delivery.setText(mJsnobject_tracking.getString("dtime"));
                txt_weight.setText(mJsnobject_tracking.getString("mWeight"));
                txt_maturity.setText(mJsnobject_tracking.getString("meaturityDate") + "Weeks");
                if (mJsnobject_tracking.getString("NextVisit").equalsIgnoreCase("Visits Closed")) {
                    txt_next_visit.setText(mJsnobject_tracking.getString("AN" + "NextVisit"));
                }
                strLatitude = mJsnobject_tracking.getString("mLatitude");
                strLongitude = mJsnobject_tracking.getString("mLongitude");
                txt_mother_name_call.setText(mJsnobject_tracking.getString("mName"));

                str_mPhoto = mJsnobject_tracking.getString("mPhoto");
                Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

                Picasso.with(context)
                        .load(Apiconstants.MOTHER_PHOTO_URL + str_mPhoto)
                        .placeholder(R.drawable.girl)
                        .fit()
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .transform(new RoundedTransformation(90, 4))
                        .error(R.drawable.girl)
                        .into(cardview_image);
            } else {
                ll_pn_mother_details.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String response) {
        ll_pn_mother_details.setVisibility(View.GONE);

        Log.d(PNMotherDetailsActivity.class.getSimpleName(), "Response Error" + response);
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
