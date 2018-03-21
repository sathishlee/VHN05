package com.unicef.vhn.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.unicef.vhn.R;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class PNHBNCListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnhbnc_mothers_list_activity);
        showActionBar();
        initUI();


    }

    public void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PN/HBNC Mothers");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUI(){

    }

}
