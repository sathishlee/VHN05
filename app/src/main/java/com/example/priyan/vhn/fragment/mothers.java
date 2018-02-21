package com.example.priyan.vhn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.example.priyan.vhn.MothersDetailsActivity;
import com.example.priyan.vhn.R;
import com.example.priyan.vhn.adapter.MoviesAdapter;
import com.example.priyan.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class mothers extends Fragment{
    private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;
    EditText edt_search;
    CardView card_mother;
    public static mothers newInstance()
    {
        mothers fragment = new mothers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mothers, container, false);
        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        edt_search = view.findViewById(R.id.edt_search);
        card_mother = view.findViewById(R.id.card_mother);
        mAdapter = new MoviesAdapter(getActivity(),movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                card_mother.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        prepareMovieData();
        card_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getActivity(), MothersDetailsActivity.class));

            }
        });
        return view;

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
        mAdapter.notifyDataSetChanged();
    }

}
