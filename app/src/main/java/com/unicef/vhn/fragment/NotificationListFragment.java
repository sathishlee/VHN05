package com.unicef.vhn.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicef.vhn.R;
import com.unicef.vhn.adapter.NotificationAdapter;
import com.unicef.vhn.model.NotificationModel;

import java.util.ArrayList;


public class NotificationListFragment extends Fragment {


    NotificationAdapter mAdapter;
    ArrayList<NotificationModel> moviesList;
    NotificationModel movie;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    public NotificationListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotificationListFragment newInstance() {
        NotificationListFragment fragment = new NotificationListFragment();

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
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
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
        return view;
    }

    private void prepareMovieData() {
        movie = new NotificationModel("PicMe Id:1001", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("PNH", "Message Deatils", "02/05/2018",R.drawable.ic_hospital);
        moviesList.add(movie);

        movie = new NotificationModel("PicMe Id:1003", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("HSC", "Message Deatils", "02/05/2018",R.drawable.ic_hospital);

        movie = new NotificationModel("PicMe Id:1005", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("HSC", "Message Deatils", "02/05/2018",R.drawable.ic_hospital);
        moviesList.add(movie);

        movie = new NotificationModel("PicMe Id:1007", "Message Deatils", "02/05/2018",R.drawable.ic_mother);
        moviesList.add(movie);

        movie = new NotificationModel("PNH", "Message Deatils", "02/05/2018",R.drawable.ic_hospital);
        moviesList.add(movie);

        mAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
