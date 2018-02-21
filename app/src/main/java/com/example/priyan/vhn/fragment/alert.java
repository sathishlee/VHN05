package com.example.priyan.vhn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyan.vhn.R;
import com.example.priyan.vhn.adapter.MoviesAdapter;
import com.example.priyan.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class alert extends Fragment{
    private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;
    public static alert newInstance()
    {
        alert fragment = new alert();
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

        mAdapter = new MoviesAdapter(getActivity(),movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();

        return view;

    }

    private void prepareMovieData() {
         movie = new Movie("Tamil selvi", "1001", "Normal");
        movieList.add(movie);
        movie = new Movie("Amutha", "1002", "High");
        movieList.add(movie);
        movie = new Movie("Suganya", "1003", "Normal");
        movieList.add(movie);

        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }

}
