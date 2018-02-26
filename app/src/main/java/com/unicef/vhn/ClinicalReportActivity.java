package com.unicef.vhn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class ClinicalReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_report);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Back");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Intent intent = new Intent(ClinicalReportActivity.this, MainActivity.class);
        finish();
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
