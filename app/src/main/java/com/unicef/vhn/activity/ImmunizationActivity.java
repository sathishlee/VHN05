package com.unicef.vhn.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.unicef.vhn.R;

public class ImmunizationActivity extends AppCompatActivity {
   /* private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization_list);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Immunization Dose Details");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ImmunizationActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
