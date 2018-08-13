package com.unicef.vhn.activity.MotherDetails;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.Presenter.VerifyVisitOtpPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANViewReportsActivity;
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MotherVisitReport.ANMotherVisitReportActivity;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.MotherListRealm;
import com.unicef.vhn.realmDbModel.PNMMotherListRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.TodayVisitCloseViews;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class ANMotherDetailsViewActivcity extends AppCompatActivity implements View.OnClickListener, TodayVisitCloseViews {
    TextView txt_mother_name, txt_picme_id, txt_mage, txt_risk_status, txt_gest_week, txt_weight,
            txt_lmp_date, txt_edd_date, txt_next_visit, txt_husb_name, txt_mother_name_call;
    String strMobileNo, strAltMobileNo, strVerifyOtp;
    Context context;
    String strLatitude, strLongitude, str_mPhoto;
    ImageView img_call_1, img_call_2, cardview_image;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;

    Button btn_view_location, btn_view_report, but_verify_otp;
    EditText edt_otp;

    Realm realm;
    PNMMotherListRealmModel pnmMotherListRealmModel;
    MotherListRealm dashBoardRealmModel;
    TextView txt_no_internet;
    CheckNetwork checkNetwork;
    LinearLayout view_block, viewLowrisk, viewHighRisk, otpview;

    VerifyVisitOtpPresenter verifyVisitOtpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_all_mother_details_view_activcity);
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

        but_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
    }

    private void verifyOtp() {
        strVerifyOtp = edt_otp.getText().toString();
        if (strVerifyOtp.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "please enter otp", Toast.LENGTH_LONG).show();

        } else {
            verifyVisitOtpPresenter.getverifyOTP(AppConstants.SELECTED_VISIT_NOTE_ID, strVerifyOtp, AppConstants.SELECTED_MID);
        }

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

//        pnMotherListPresenter = new MotherListPresenter(ANMotherDetailsViewActivcity.this, this);
//        pnMotherListPresenter.getSelectedMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);

        checkNetwork = new CheckNetwork(this);
        verifyVisitOtpPresenter = new VerifyVisitOtpPresenter(getApplicationContext(), this);
        txt_no_internet = (TextView) findViewById(R.id.txt_no_internet);
        txt_no_internet.setVisibility(View.GONE);
        if (checkNetwork.isNetworkAvailable()) {
            txt_no_internet.setVisibility(View.GONE);
        } else {
            txt_no_internet.setVisibility(View.VISIBLE);
        }
        viewHighRisk = (LinearLayout) findViewById(R.id.ll_high_risk);
        viewLowrisk = (LinearLayout) findViewById(R.id.ll_low_risk);
        otpview = (LinearLayout) findViewById(R.id.otpview);
        if (AppConstants.IS_TODAY_VIST_LIST) {
            otpview.setVisibility(View.VISIBLE);
            AppConstants.IS_TODAY_VIST_LIST = false;
        } else {
            otpview.setVisibility(View.GONE);
        }
        view_block = (LinearLayout) findViewById(R.id.view_block);
        view_block.setVisibility(View.GONE);
        cardview_image = (ImageView) findViewById(R.id.cardview_image);
        txt_mother_name = (TextView) findViewById(R.id.txt_username);
        txt_picme_id = (TextView) findViewById(R.id.txt_picme_id);
        txt_mage = (TextView) findViewById(R.id.txt_age);
//        txt_risk_status = (TextView) findViewById(R.id.txt_risk);
        txt_gest_week = (TextView) findViewById(R.id.txt_gest_week);
        txt_weight = (TextView) findViewById(R.id.txt_weight);
        txt_lmp_date = (TextView) findViewById(R.id.txt_lmp_date);
        txt_edd_date = (TextView) findViewById(R.id.txt_edd_date);
        txt_next_visit = (TextView) findViewById(R.id.txt_next_visit);
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_mother_name_call = (TextView) findViewById(R.id.txt_mother_name_call);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);

        but_verify_otp = (Button) findViewById(R.id.but_verify_otp);
        edt_otp = (EditText) findViewById(R.id.edt_otp);


        getValuesFromRealm();
    }

    private void getValuesFromRealm() {
if (realm.isInTransaction()){
    realm.cancelTransaction();
    }
        realm.beginTransaction();
        RealmResults<PNMMotherListRealmModel> realmResults = realm.where(PNMMotherListRealmModel.class)
                .equalTo("mid", AppConstants.SELECTED_MID).findAll();
        if (realmResults.size() != 0) {
            view_block.setVisibility(View.VISIBLE);

            for (int i = 0; i < realmResults.size(); i++) {
                PNMMotherListRealmModel model = realmResults.get(i);

                if (model.getmName().equalsIgnoreCase("null")) {
                    txt_mother_name.setText("-");
                } else {
                    txt_mother_name.setText(model.getmName());

                }
                if (model.getmPicmeId().equalsIgnoreCase("null")) {
                    txt_picme_id.setText("-");
                } else {
                    txt_picme_id.setText(model.getmPicmeId());

                }
                if (model.getmAge().equalsIgnoreCase("null")) {
                    txt_mage.setText("-");

                } else {
                    txt_mage.setText(model.getmAge());
                }
                if (model.getGestAge().equalsIgnoreCase("null")) {
                    txt_gest_week.setText("-");
                } else {
                    txt_gest_week.setText(model.getGestAge());

                }
                if (model.getmWeight().equalsIgnoreCase("null")){
                    txt_weight.setText("-");
                }else {
                    txt_weight.setText(model.getmWeight() + " Kg");
                }
                strMobileNo = model.getmMotherMobile();
                if (strMobileNo.equalsIgnoreCase("null") || strMobileNo.length() < 10) {
                    img_call_1.setVisibility(View.GONE);
                } else {
                    img_call_1.setVisibility(View.VISIBLE);
                }
                if (model.getmHusbandName().equalsIgnoreCase("null")){
                    txt_husb_name.setText("-");
                }else {
                    txt_husb_name.setText(model.getmHusbandName());
                }
                if (model.getmName().equalsIgnoreCase("null")){
                    txt_mother_name_call.setText("-");
                }
                else{
                    txt_mother_name_call.setText(model.getmName());
                }
                strAltMobileNo = model.getmHusbandMobile();
                if (strAltMobileNo.equalsIgnoreCase("null") || strAltMobileNo.length() < 10) {
                    img_call_2.setVisibility(View.GONE);
                } else {
                    img_call_2.setVisibility(View.VISIBLE);

                }
//            txt_mage.setText(mJsnobject_tracking.);
//        txt_risk_status.setText(model.getmRiskStatus());
                if (model.getmRiskStatus().equalsIgnoreCase("HIGH")) {
                    viewLowrisk.setVisibility(View.GONE);
                    viewHighRisk.setVisibility(View.VISIBLE);
                } else {
                    viewLowrisk.setVisibility(View.VISIBLE);
                    viewHighRisk.setVisibility(View.GONE);
                }
//            txt_gest_week.setText(mJsnobject_tracking.getCurrentMonth());
//            txt_weight.setText(mJsnobject_tracking.);
                txt_next_visit.setText(model.getNextVisit());
                txt_lmp_date.setText(model.getmLMP());
                txt_edd_date.setText(model.getmEDD());
                strLatitude = model.getmLatitude();
                strLongitude = model.getmLongitude();

                str_mPhoto = model.getmPhoto();

      /*  Picasso.with(context)
                .load(Apiconstants.MOTHER_PHOTO_URL + str_mPhoto)
                .placeholder(R.drawable.girl)
                .fit()
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .transform(new RoundedTransformation(90, 4))
                .error(R.drawable.girl)
                .into(cardview_image);*/

            }
        } else {
            view_block.setVisibility(View.GONE);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Records Not Available!");
            builder.setMessage("You are in offline, please connect internet.");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    dialog.dismiss();
                    finish();
                }
            });


// Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        realm.commitTransaction();

    }


    private void makeCall(String str_mobile_number) {
        Toast.makeText(getApplicationContext(), str_mobile_number, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + str_mobile_number)));
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "Displaying camera permission rationale to provide additional context.",
                    Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:

                Toast.makeText(getApplicationContext(),
                        "you are in offline, check Internet connection", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
                break;
            case R.id.btn_view_report:
//                startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));
//                Toast.makeText(getApplicationContext(),"AN Mother MID"+AppConstants.SELECTED_MID ,Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(getApplicationContext(), ANMotherVisitReportActivity.class));
                startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    dial.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button",
                            Toast.LENGTH_SHORT).show();
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
    public void todayVisitCloseSuccess(String response) {
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void todayVisitCloseFailiur(String response) {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
    }
}
