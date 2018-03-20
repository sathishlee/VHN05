package com.unicef.vhn.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unicef.vhn.R;
import com.unicef.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class ImmunizationMotherListActivity extends AppCompatActivity {
    private List<Movie> movieList;
    private RecyclerView recyclerView;

    Movie movie;
    ArrayList<String> mothers_type_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization_mother_list);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Immunization Mothers List");

        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        movieList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

//        mAdapter = new MoviesAdapter(this, movieList,"Immunization");
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
        prepareMovieData();

    }

    private void prepareMovieData() {
        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new
                Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "High");
        movieList.add(movie);

        movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
//        mAdapter.notifyDataSetChanged();
    }
}
