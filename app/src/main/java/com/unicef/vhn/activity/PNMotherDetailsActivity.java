package com.unicef.vhn.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.AppConstent;
import com.unicef.vhn.R;

public class PNMotherDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Button viewLocation,viewReport;
    LinearLayout otpView;
    CardView vhn_profile;
    ImageView img_call,img_call_a;
    TextView call_vhn;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnmother_details);

        viewLocation =(Button) findViewById(R.id.btn_view_location);
        otpView = (LinearLayout)findViewById(R.id.otpview);
        vhn_profile = (CardView) findViewById(R.id.vhn_profile);
        viewReport =(Button) findViewById(R.id.btn_view_report);
        img_call= (ImageView)findViewById(R.id.img_call);
//        img_call_a= (ImageView)findViewById(R.id.img_call_a);
        call_vhn= (TextView)findViewById(R.id.call_vhn);

        if (AppConstent.isfromhome==1){
            vhn_profile.setVisibility(View.GONE);
            otpView.setVisibility(View.VISIBLE);
            //AppConstent.isfromhome=0;
        } else if(AppConstent.isfromhome==2){
            otpView.setVisibility(View.GONE);
            vhn_profile.setVisibility(View.VISIBLE);
            //AppConstent.isfromhome=0;
        }else{
            otpView.setVisibility(View.GONE);
            vhn_profile.setVisibility(View.GONE);
        }
        viewLocation.setOnClickListener(this);
        viewReport.setOnClickListener(this);
        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });img_call_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
        call_vhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Mother Details");
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    private void makeCall() {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + "9876543210";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(PNMotherDetailsActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_location:
                break;
            case R.id.btn_view_report:{
                if (AppConstent.PNMotherDetails.equalsIgnoreCase("Immunization")) {
//                    startActivity(new Intent(getApplicationContext(), ImmunizationDoseListActivity.class));

                } else {
                    startActivity(new Intent(getApplicationContext(), PNMotherVisitListActivity.class));

                }
        }break;
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
        Intent intent = new Intent(PNMotherDetailsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


}
