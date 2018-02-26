package com.unicef.vhn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class risk extends Fragment{
    private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;
    public static risk newInstance()
    {
        risk fragment = new risk();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_risk, container, false);
        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(getActivity(),movieList,"ANMother");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();

        return view;

    }

    private void prepareMovieData() {
         movie = new Movie("Tamil selvi", "1001", "Breathlessness");
        movieList.add(movie);
        movie = new Movie("Amutha", "1002", "Mictutrion");
        movieList.add(movie);
        movie = new Movie("Suganya", "1003", "Bleeding PV");
        movieList.add(movie);
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }

}
