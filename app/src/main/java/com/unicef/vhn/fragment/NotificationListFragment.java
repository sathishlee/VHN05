package com.unicef.vhn.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.NotificationPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.adapter.NotificationAdapter;
import com.unicef.vhn.model.NotificationListResponseModel;
import com.unicef.vhn.view.NotificationViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationListFragment extends Fragment implements NotificationViews {
    TextView txt_today_visit_count,txt_count_today_visit;
    LinearLayout ll_go_visit_list;
    NotificationAdapter mAdapter;
    ArrayList<NotificationListResponseModel.Vhn_migrated_mothers> moviesList;
    NotificationListResponseModel.Vhn_migrated_mothers movie;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    PreferenceData preferenceData;
    ProgressDialog pDialog;

    NotificationPresenter notificationPresenter;

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
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        txt_today_visit_count=view.findViewById(R.id.txt_today_visit_count);
        txt_count_today_visit=view.findViewById(R.id.txt_count_today_visit);
        ll_go_visit_list = view.findViewById(R.id.ll_go_visit_list);
//        if (preferenceData.getTodayVisitCount().equalsIgnoreCase("0")) {
//
//            txt_today_visit_count.setText(preferenceData.getTodayVisitCount());
//            txt_count_today_visit.setText(preferenceData.getTodayVisitCount()+" Mothers Visiting Today");
//        }
      /*  ll_go_visit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VisitActivity.class));
            }
        });*/
        mRecyclerView = view.findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        preferenceData = new PreferenceData(getActivity());
        notificationPresenter = new NotificationPresenter(getActivity(), this);
        notificationPresenter.getNotificationList(preferenceData.getVhnCode(),preferenceData.getVhnId());

        mRecyclerView = view. findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        moviesList=new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new NotificationAdapter(moviesList,getActivity());
        mRecyclerView.setAdapter(mAdapter);
//        prepareMovieData();
        return view;
    }

    private void prepareMovieData() {/*
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

        mAdapter.notifyDataSetChanged();*/
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

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void NotificationResponseSuccess(String response) {

        Log.d(NotificationListFragment.class.getSimpleName(), "Notification List Success response" + response);

        Log.d(NotificationListFragment.class.getSimpleName(), "Notification count response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            movie = new NotificationListResponseModel.Vhn_migrated_mothers();
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("vhn_migrated_mothers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    movie.setMPicmeId(jsonObject1.getString("mPicmeId"));
                    movie.setMName(jsonObject1.getString("mName"));
                    movie.setMid(jsonObject1.getString("mid"));
                    movie.setVhnId(jsonObject1.getString("vhnId"));
                    movie.setSubject(jsonObject1.getString("subject"));
                    movie.setMMotherMobile(jsonObject1.getString("mMotherMobile"));
                    movie.setMigratedmId(jsonObject1.getString("migratedmId"));
                    movie.setNoteId(jsonObject1.getString("noteId"));
                    movie.setNoteStartDateTime(jsonObject1.getString("noteStartDateTime"));
                    movie.setMtype(jsonObject1.getString("mtype"));

                    moviesList.add(movie);

                }
//                mAdapter.notifyDataSetChanged();
            } else {
                Log.d(NotificationListFragment.class.getSimpleName(), "Notification messsage-->" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        prepareMovieData(response);
    }

    @Override
    public void NotificationResponseError(String response) {
        Log.d(NotificationListFragment.class.getSimpleName(), "Notification List Error response" + response);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
