package com.unicef.vhn.activity.ChildDevelopment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;


public class FullViewImageActivity extends AppCompatActivity {
    ImageView img_full_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view_image);
        showActionBar();
//        Bundle bundle =getIntent().getExtras();
//        String strImageUri = bundle.getString("strImageUri");
        img_full_view = (ImageView)findViewById(R.id.img_full_view);
        Log.e(FullViewImageActivity.class.getSimpleName(),"Image uri --->"+AppConstants.selectedFullViewImageUri);
        Picasso.with(this)
                .load(Apiconstants.CHILD_TRACKING_IMAGES + AppConstants.selectedFullViewImageUri)
                .placeholder(R.drawable.no_image)
                .fit()
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.no_image)
                .into(img_full_view);
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Image");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
