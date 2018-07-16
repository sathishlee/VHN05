package com.unicef.vhn.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.unicef.vhn.R;
import com.unicef.vhn.adapter.SlidingImage_Adapter;

import java.util.ArrayList;

public class ImageFullViewActivity  extends AppCompatActivity {

    private static ViewPager mPager;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_view);
        showActionBar();
        init();
    }

    private void showActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("THAIMAI");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void init() {
//        ImagesArray= (ArrayList<VisitRecordsSingleResponseModel>) getIntent().getSerializableExtra("mylist");
        String [] imagelist= getIntent().getExtras().getStringArray("mylist");
        for(int i = 0; i< imagelist.length; i++)
        {
            ImagesArray.add(imagelist[i]);
        }
        Log.e(ImageFullViewActivity.class.getSimpleName(),imagelist.length+"");

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(ImageFullViewActivity.this,ImagesArray));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

