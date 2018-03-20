package com.unicef.vhn;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

public class ImmunizationActivity extends AppCompatActivity {
   /* private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Immunization Dose Details");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        /*movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(this,movieList,"Immunization");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();*/
    }

    /*private void prepareMovieData() {

        movie = new Movie("Tamil selvi", "1001", "Breathlessness");
        movieList.add(movie);
        movie = new Movie("Amutha", "1002", "Mictutrion");
        movieList.add(movie);
        movie = new Movie("Suganya", "1003", "Bleeding PV");

        movie = new Movie("Tamil selvi", "1001", "Breathlessness");
        movieList.add(movie);
        movie = new Movie("Amutha", "1002", "Mictutrion");
        movieList.add(movie);
        movie = new Movie("Suganya", "1003", "Bleeding PV");
        movieList.add(movie);
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ImmunizationActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
