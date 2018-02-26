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
import com.unicef.vhn.adapter.NotificationAdapter;
import com.unicef.vhn.model.Movie;
import com.unicef.vhn.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class alert extends Fragment{
    NotificationAdapter mAdapter;
    ArrayList<NotificationModel> moviesList;
    NotificationModel movie;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private NotificationListFragment.OnFragmentInteractionListener mListener;
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
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        mRecyclerView = view. findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        moviesList=new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new NotificationAdapter(moviesList,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        prepareMovieData();

        prepareMovieData();

        return view;

    }

    private void prepareMovieData() {
        movie = new NotificationModel("PicMe Id:1001", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("104", "Message Deatils", "02/05/2018",R.mipmap.ic_alert104_round);
        moviesList.add(movie);

        movie = new NotificationModel("PicMe Id:1003", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("104", "Message Deatils", "02/05/2018",R.mipmap.ic_alert104_round);

        movie = new NotificationModel("PicMe Id:1005", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("104", "Message Deatils", "02/05/2018",R.mipmap.ic_alert104_round);
        moviesList.add(movie);

        movie = new NotificationModel("PicMe Id:1007", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("104", "Message Deatils", "02/05/2018",R.mipmap.ic_alert104_round);
        moviesList.add(movie);

        mAdapter.notifyDataSetChanged();
    }

}
