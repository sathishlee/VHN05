package com.unicef.vhn.fragment.fragment;

import android.content.Context;
import android.net.Uri;
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


public class PnMotherListFragment extends Fragment {
    private List<Movie> movieList ;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    Movie movie;

    private OnFragmentInteractionListener mListener;

    public PnMotherListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PnMotherListFragment newInstance() {
        PnMotherListFragment fragment = new PnMotherListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=  inflater.inflate(R.layout.fragment_pn_mother_list, container, false);
        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(getActivity(),movieList,"PNMothers");
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
