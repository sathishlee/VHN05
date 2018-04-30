package com.unicef.vhn.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unicef.vhn.R;
//import com.unicef.vhn.adapter.ViewPagerAdapter;

public class MothersViewRecordActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    Button btn_primary_report,btn_view_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);
        initUI();
        onClickListner();



    }

    public void initUI(){
        viewPager = (ViewPager) findViewById(R.id.hre_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.hre_tabs);
        tabLayout.setupWithViewPager(viewPager);
        btn_primary_report = (Button) findViewById(R.id.btn_primary_report);
        btn_view_report = (Button) findViewById(R.id.btn_view_report);
    }

    private void onClickListner(){
        btn_primary_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MothersPrimaryRecordsActivity.class));
            }
        });
        btn_view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MothersViewRecordActivity.class));
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {

    }

}
