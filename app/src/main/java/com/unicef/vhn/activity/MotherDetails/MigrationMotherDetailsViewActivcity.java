package com.unicef.vhn.activity.MotherDetails;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANViewReportsActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.adapter.MotherMigrationAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.realmDbModel.MigrationMotherDetailsRealmModel;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class MigrationMotherDetailsViewActivcity extends AppCompatActivity implements View.OnClickListener, MotherListsViews {

    TextView txt_mother_name, txt_picme_id, txt_mage, txt_risk_status, txt_gest_week, txt_weight, txt_lmp_date, txt_edd_date, txt_next_visit, txt_husb_name, txt_mother_name_call;
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
    CheckNetwork checkNetwork;
    boolean isoffline;

    LinearLayout view_norecords,view_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm(); // opens "myrealm.realm"
        setContentView(R.layout.activity_all_mother_details_view_activcity);
        Log.w(MigrationMotherDetailsViewActivcity.class.getSimpleName(), "Activity created");
        Log.w(MotherMigrationAdapter.class.getSimpleName(),"MID  ----"+AppConstants.SELECTED_MID);
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
        checkNetwork = new CheckNetwork(this);
        pnMotherListPresenter = new MotherListPresenter(MigrationMotherDetailsViewActivcity.this, this);
        if (checkNetwork.isNetworkAvailable()) {
            Log.w(MotherMigrationAdapter.class.getSimpleName(),"MID  ----"+AppConstants.SELECTED_MID);
            Log.w(MotherMigrationAdapter.class.getSimpleName(),"vhn code  ----"+preferenceData.getVhnCode());
            Log.w(MotherMigrationAdapter.class.getSimpleName(),"vhn id  ----"+preferenceData.getVhnId());
                      pnMotherListPresenter.getSelectedMother(preferenceData.getVhnCode(), preferenceData.getVhnId(), AppConstants.SELECTED_MID);
        } else {
            isoffline = true;
        }
        view_norecords = (LinearLayout) findViewById(R.id.view_no_records);
        view_block = (LinearLayout) findViewById(R.id.view_block);
        view_norecords.setVisibility(View.GONE);
        view_block.setVisibility(View.GONE);
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
        txt_husb_name = (TextView) findViewById(R.id.txt_husb_name);
        txt_mother_name_call = (TextView) findViewById(R.id.txt_mother_name_call);
        img_call_1 = (ImageView) findViewById(R.id.img_call_1);
        img_call_2 = (ImageView) findViewById(R.id.img_call_2);
        btn_view_location = (Button) findViewById(R.id.btn_view_location);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);

        if (isoffline) {
            getValuesFromRealm();
        }
    }

    private void getValuesFromRealm() {

        realm.beginTransaction();
        RealmResults<MigrationMotherDetailsRealmModel> realmResults = realm.where(MigrationMotherDetailsRealmModel.class).equalTo("mid", AppConstants.SELECTED_MID).findAll();
        Log.w(MigrationMotherDetailsRealmModel.class.getSimpleName(), realmResults.size() + "");
        if (realmResults.size() == 0) {
//            finish();
//            Toast.makeText(getApplicationContext(), "Mother Details Not Available", Toast.LENGTH_LONG).show();
            view_norecords.setVisibility(View.VISIBLE);
            view_block.setVisibility(View.GONE);

        } else {
            view_norecords.setVisibility(View.GONE);
            view_block.setVisibility(View.VISIBLE);

            for (int i = 0; i < realmResults.size(); i++) {
                MigrationMotherDetailsRealmModel model = realmResults.get(i);

                Log.w(MigrationMotherDetailsViewActivcity.class.getSimpleName(), i + "MID" + model.getMid());
                Log.w(MigrationMotherDetailsViewActivcity.class.getSimpleName(), i + "PICMEID" + model.getMPicmeId());
                Log.w(MigrationMotherDetailsViewActivcity.class.getSimpleName(), i + "Name" + model.getMName());

                txt_mother_name.setText(model.getMName());
                txt_picme_id.setText(model.getMPicmeId());
                strMobileNo = model.getMMotherMobile();
                txt_husb_name.setText(model.getMHusbandName());
                txt_mother_name_call.setText(model.getMMotherMobile());
                if (strMobileNo.equalsIgnoreCase("null")||strMobileNo.length()<10){
                    img_call_1.setVisibility(View.GONE);
                }else{
                    img_call_1.setVisibility(View.VISIBLE);

                }
                strAltMobileNo = model.getMHusbandMobile();
                if (strAltMobileNo.equalsIgnoreCase("null")||strAltMobileNo.length()<10){
                    img_call_2.setVisibility(View.GONE);
                }else{
                    img_call_2.setVisibility(View.VISIBLE);

                }
                txt_mage.setText(model.getMAge());
                txt_risk_status.setText(model.getMRiskStatus());
                txt_gest_week.setText(model.getGSTAge());
                txt_weight.setText(model.getMWeight());
                txt_next_visit.setText(model.getNextVisit());
                txt_lmp_date.setText(model.getMLMP());
                txt_edd_date.setText(model.getMEDD());
                strLatitude = model.getMLatitude();
                strLongitude = model.getMLongitude();

                str_mPhoto = model.getmPhoto();

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

            }
        }
        realm.commitTransaction();

    }


    private void makeCall(String str_mobile_number) {
        Toast.makeText(getApplicationContext(), str_mobile_number, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            Log.i(MigrationMotherDetailsViewActivcity.class.getSimpleName(), "CALL permission has already been granted. Displaying camera preview.");
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + str_mobile_number)));
        }
    }

    private void requestCallPermission() {
        Log.i(MigrationMotherDetailsViewActivcity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Log.i(MigrationMotherDetailsViewActivcity.class.getSimpleName(), "Displaying camera permission rationale to provide additional context.");
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

                Toast.makeText(getApplicationContext(), "you are in offline, check Internet connection", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MotherLocationActivity.class));
                break;
            case R.id.btn_view_report:
                startActivity(new Intent(getApplicationContext(), ANViewReportsActivity.class));
//                startActivity(new Intent(getApplicationContext(), ANMotherVisitReportActivity.class));
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
        pDialog.hide();
    }

    @Override
    public void showLoginSuccess(String response) {

//        AppConstants.SELECTED_MID="0";
        Log.e(MigrationMotherDetailsViewActivcity.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                view_norecords.setVisibility(View.GONE);
                view_block.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                RealmResults<MigrationMotherDetailsRealmModel> realmResults = realm.where(MigrationMotherDetailsRealmModel.class).findAll();
                if (realmResults.size() != 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(MigrationMotherDetailsRealmModel.class);
                        }
                    });
                }

                JSONObject mJsnobject_tracking = mJsnobject.getJSONObject("tracking");
                realm.beginTransaction();
                MigrationMotherDetailsRealmModel model = realm.createObject(MigrationMotherDetailsRealmModel.class);

                model.setMAge(mJsnobject_tracking.getString("mAge"));
                model.setMid(mJsnobject_tracking.getString("mid"));
                model.setMName(mJsnobject_tracking.getString("mName"));
                model.setMPicmeId(mJsnobject_tracking.getString("mPicmeId"));
                model.setMMotherMobile(mJsnobject_tracking.getString("mMotherMobile"));
                model.setMHusbandMobile(mJsnobject_tracking.getString("mHusbandMobile"));
                model.setMHusbandName(mJsnobject_tracking.getString("mHusbandName"));
                model.setDeleveryDate(mJsnobject_tracking.getString("deleveryDate"));
                model.setMRiskStatus(mJsnobject_tracking.getString("mRiskStatus"));
                model.setGSTAge(Integer.parseInt(mJsnobject_tracking.getString("GSTAge")));
                model.setMWeight(mJsnobject_tracking.getString("mWeight"));
                model.setNextVisit(mJsnobject_tracking.getString("nextVisit"));
                model.setMLMP(mJsnobject_tracking.getString("mLMP"));
                model.setMEDD(mJsnobject_tracking.getString("mEDD"));
                model.setMLatitude(mJsnobject_tracking.getString("mLatitude"));
                model.setMLongitude(mJsnobject_tracking.getString("mLongitude"));
                model.setVLongitude(mJsnobject_tracking.getString("VLongitude"));
                model.setVLatitude(mJsnobject_tracking.getString("vLatitude"));
                model.setmPhoto(mJsnobject_tracking.getString("mPhoto"));

                realm.commitTransaction();
                /*txt_mother_name.setText(mJsnobject_tracking.getString("mName"));
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
                strLongitude = mJsnobject_tracking.getString("mLongitude");

                str_mPhoto = mJsnobject_tracking.getString("mPhoto");*/


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
                view_norecords.setVisibility(View.VISIBLE);
                view_block.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoginError(String string) {
        view_norecords.setVisibility(View.VISIBLE);
        view_block.setVisibility(View.GONE);
    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
