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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;

import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.MotherListsViews;

import io.realm.Realm;
import org.json.JSONException;
import org.json.JSONObject;

/*AN Mother Details  have api call working well is not use this project,
use for ANMotherDetailsViewActivity */
public class MothersDetailsActivity extends AppCompatActivity implements View.OnClickListener, MotherListsViews {
    TextView txt_mother_name, txt_picme_id, txt_mage, txt_risk_status, txt_gest_week, txt_weight,
            txt_lmp_date, txt_edd_date, txt_next_visit, txt_husb_name, txt_mother_name_call;
    String strMobileNo, strAltMobileNo;
    Context context;
    String strLatitude, strLongitude, str_mPhoto;
    ImageView img_call_1, img_call_2, cardview_image;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;

    Button btn_view_location, btn_view_report;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        setContentView(R.layout.activity_mothers_details);
        initUI();
        showActionBar();
        onClickListner();
    }

    private void onClickListner() {
        btn_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
            }
        });
        btn_view_report.setOnClickListener(this);
        img_call_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(strMobileNo);
            }
        });
        img_call_2.setOnClickListener(new View.OnClickListener() {
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

    public void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MothersDetailsActivity.this, this, realm);

        pnMotherListPresenter.getSelectedMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);
        preferenceData = new PreferenceData(this);
        pnMotherListPresenter = new MotherListPresenter(MothersDetailsActivity.this, this, realm);
        pnMotherListPresenter.getSelectedMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);

        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        txt_mother_name = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_mage = (TextView) findViewById(R.id.txt_age);
        txt_risk_status = (TextView) findViewById(R.id.txt_risk);
        txt_gest_week = (TextView) findViewById(R.id.txt_gest_week);
        txt_weight = (TextView) findViewById(R.id.txt_weight);
        txt_lmp_date = (TextView) findViewById(R.id.txt_lmp_date);
        txt_edd_date = (TextView) findViewById(R.id.txt_edd_date);
        txt_next_visit = (TextView) findViewById(R.id.txt_next_visit);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_mother_name_call = (TextView) findViewById(R.id.txt_mother_name_call);

        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
    }


    private void makeCall(String str_mobile_number) {
        Toast.makeText(getApplicationContext(), str_mobile_number, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has already been granted. Displaying camera preview.");
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + str_mobile_number)));

        }
    }

    private void requestCallPermission() {
        Log.i(MothersDetailsActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Log.i(MothersDetailsActivity.class.getSimpleName(), "Displaying camera permission rationale to provide additional context.");
            Toast.makeText(this, "Displaying camera permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
                break;
            case R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));
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
        finish();
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
        Log.e(MothersDetailsActivity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                txt_husb_name.setText(mJsnobject_tracking.getString("mHusbandName"));
                txt_mother_name_call.setText(mJsnobject_tracking.getString("mName"));
                strLatitude = mJsnobject_tracking.getString("mLatitude");
                strLongitude = mJsnobject_tracking.getString("mLongitude");

                AppConstants.MOTHER_PICME_ID = mJsnobject_tracking.getString("mPicmeId");

                str_mPhoto = mJsnobject_tracking.getString("mPhoto");
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
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginError(String message) {
//        AppConstants.SELECTED_MID="0";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
