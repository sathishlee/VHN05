package com.unicef.vhn.model;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unicef.vhn.R;
//import com.unicef.vhn.adapter.ViewPagerAdapter;

public class ViewRecordActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_health_records);
        viewPager = (ViewPager) findViewById(R.id.hre_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.hre_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
       /* ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Present Status\nVISIT 5");
        adapter.addFragment(new TwoFragment(), "VISIT 4");
        adapter.addFragment(new ThreeFragment(), "VISIT 3");
        adapter.addFragment(new OneFragment(), "VISIT 2");
        adapter.addFragment(new TwoFragment(), "VISIT 1");*/
//        adapter.addFragment(new ThreeFragment(), "VISIT 6");
//        adapter.addFragment(new OneFragment(), "VISIT 7");
//        adapter.addFragment(new TwoFragment(), "VISIT 8");
//        viewPager.setAdapter(adapter);
    }
}
