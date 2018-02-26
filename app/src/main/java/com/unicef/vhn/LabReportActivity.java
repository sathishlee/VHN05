package com.unicef.vhn;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class LabReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_report);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Back");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Intent intent = new Intent(LabReportActivity.this, MainActivity.class);
        finish();
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
