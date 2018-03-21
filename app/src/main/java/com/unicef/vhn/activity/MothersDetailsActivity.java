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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.unicef.vhn.R;
import com.unicef.vhn.model.ViewRecordActivity;


public class MothersDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    Button btn_view_location, btn_view_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mothers_details);
        initUI();
        showActionBar();

    }
    private void showActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mother Detail");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void initUI(){

        btn_view_location = (Button) findViewById(R.id.btn_view_location);

        btn_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MothersDetailsActivity.this, MotherTrackActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }


    private void makeCall() {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + "9876543210";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(MothersDetailsActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_view_location:
                break;
            case  R.id.btn_view_report: startActivity(new Intent(getApplicationContext(),ViewRecordActivity.class));break;
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
}
