package com.unicef.vhn.fragment.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class VisitFragment extends Fragment{
    private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;


    TextView txt_today,txt_village,txt_reminder;
    Spinner sp_village;
    public static VisitFragment newInstance()
    {
        VisitFragment fragment = new VisitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_visit, container, false);

        getActivity().setTitle("Visit");
        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        txt_today = view.findViewById(R.id.txt_today_visit);
        txt_village = view.findViewById(R.id.txt_village_wise_visit);
        txt_reminder = view.findViewById(R.id.txt_reminder_visit);
        sp_village = view.findViewById(R.id.sp_village);
        sp_village.setVisibility(View.GONE);
        txt_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_village.setVisibility(View.GONE);
                movieList.clear();
                prepareMovieData();
            }
        }); txt_village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_village.setVisibility(View.VISIBLE);
                movieList.clear();
                prepareMovieData1();
            }
        }); txt_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieList.clear();

                sp_village.setVisibility(View.GONE);
                prepareMovieData2();
            }
        });
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
        mAdapter.notifyDataSetChanged();
    }private void prepareMovieData2() {
        movie = new Movie("Amutha", "1002", "Mictutrion");
        movieList.add(movie);
         movie = new Movie("Tamil selvi", "1001", "Breathlessness");
        movieList.add(movie);

        movie = new Movie("Suganya", "1003", "Bleeding PV");
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }private void prepareMovieData1() {
        movie = new Movie("Suganya", "1003", "Bleeding PV");
        movieList.add(movie);
         movie = new Movie("Tamil selvi", "1001", "Breathlessness");
        movieList.add(movie);
        movie = new Movie("Amutha", "1002", "Mictutrion");
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }

}
