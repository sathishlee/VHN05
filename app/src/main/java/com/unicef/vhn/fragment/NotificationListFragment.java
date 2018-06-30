package com.unicef.vhn.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.NotificationPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.activity.VisitActivity;
import com.unicef.vhn.adapter.NotificationAdapter;
import com.unicef.vhn.application.RealmController;
import com.unicef.vhn.model.NotificationListResponseModel;

import com.unicef.vhn.realmDbModel.NotificationListRealm;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.NotificationViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class NotificationListFragment extends Fragment implements NotificationViews, MakeCallInterface {
    TextView txt_today_visit_count, txt_count_today_visit;
    LinearLayout ll_go_visit_list;
    NotificationAdapter mAdapter;
    ArrayList<NotificationListResponseModel.Vhn_migrated_mothers> moviesList;
    NotificationListResponseModel.Vhn_migrated_mothers mresponseResult;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    PreferenceData preferenceData;
    ProgressDialog pDialog;

    NotificationPresenter notificationPresenter;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    CheckNetwork checkNetwork;
    boolean isoffline = false;
    TextView txt_no_internet,txt_no_records_found;
    Realm realm;
    NotificationListRealm notificationListRealm;

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
        realm = RealmController.with(getActivity()).getRealm(); // opens "myrealm.realm"

        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        checkNetwork = new CheckNetwork(getActivity());
        notificationPresenter = new NotificationPresenter(getActivity(), this);
        if (checkNetwork.isNetworkAvailable()){
            notificationPresenter.getNotificationList(preferenceData.getVhnCode(), preferenceData.getVhnId());
        }else{
            isoffline = true;
        }
        notificationPresenter.getTodayVisitCount(preferenceData.getVhnCode(), preferenceData.getVhnId());

        txt_no_internet = view.findViewById(R.id.txt_no_internet);
        txt_no_records_found = view.findViewById(R.id.txt_no_records_found);
        txt_no_internet.setVisibility(View.GONE);
        txt_today_visit_count = view.findViewById(R.id.txt_today_visit_count);
        txt_count_today_visit = view.findViewById(R.id.txt_count_today_visit);
        ll_go_visit_list = view.findViewById(R.id.ll_go_visit_list);
//        if (preferenceData.getTodayVisitCount().equalsIgnoreCase("0")) {
//            txt_today_visit_count.setText(preferenceData.getTodayVisitCount());
//            txt_count_today_visit.setText(preferenceData.getTodayVisitCount()+" Mothers Visiting Today");
//        }
        ll_go_visit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VisitActivity.class));
            }
        });
        mRecyclerView = view.findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);


        mRecyclerView = view.findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        moviesList = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new NotificationAdapter(moviesList, getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
        if (isoffline) {
            txt_no_internet.setVisibility(View.VISIBLE);
            showOfflineData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Record Not Found");
            builder.create();
        }
//        prepareMovieData();
        return view;
    }

    private void showOfflineData() {
        realm.beginTransaction();
        RealmResults<NotificationListRealm> userInfoRealmResult = realm.where(NotificationListRealm.class).findAll();
        if (userInfoRealmResult.size()!=0) {
            for (int i = 0; i < userInfoRealmResult.size(); i++) {
                mresponseResult = new NotificationListResponseModel.Vhn_migrated_mothers();


                NotificationListRealm model = userInfoRealmResult.get(i);

                mresponseResult.setMPicmeId(model.getMPicmeId());
                mresponseResult.setMName(model.getMName());
                mresponseResult.setMid(model.getMid());
                mresponseResult.setVhnId(model.getVhnId());
                mresponseResult.setSubject(model.getSubject());
                mresponseResult.setMMotherMobile(model.getMMotherMobile());
                mresponseResult.setClickHeremId(model.getClickHeremId());
                mresponseResult.setMigratedmId(model.getMigratedmId());
                mresponseResult.setNoteId(model.getNoteId());
                mresponseResult.setNoteStartDateTime(model.getNoteStartDateTime());
                mresponseResult.setMtype(model.getMtype());
                mresponseResult.setMessage(model.getMessage());
                mresponseResult.setmPhoto(model.getmPhoto());
                mresponseResult.setNoteType(model.getNoteType());


                moviesList.add(mresponseResult);

            }
        }else{
            txt_no_records_found.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
        realm.commitTransaction();
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

        Log.d(NotificationListFragment.class.getSimpleName(), "Notification response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            if (status.equalsIgnoreCase("1")) {
                RealmResults<NotificationListRealm> motherListAdapterRealmModel = realm.where(NotificationListRealm.class).findAll();
                Log.e("Realm size ---->", motherListAdapterRealmModel.size() + "");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(NotificationListRealm.class);
                    }
                });
                JSONArray jsonArray = jsonObject.getJSONArray("vhn_Mother_notification");
                Log.d(NotificationListFragment.class.getSimpleName(), "Notification count" + jsonArray.length());
                realm.beginTransaction();
                for (int i = 0; i < jsonArray.length(); i++) {
//                    movie = new NotificationListResponseModel.Vhn_migrated_mothers();

                    notificationListRealm = realm.createObject(NotificationListRealm.class);

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                    notificationListRealm.setMPicmeId(jsonObject1.getString("mPicmeId"));
                    notificationListRealm.setMName(jsonObject1.getString("mName"));
                    notificationListRealm.setMid(jsonObject1.getString("mid"));
                    notificationListRealm.setVhnId(jsonObject1.getString("vhnId"));
                    notificationListRealm.setSubject(jsonObject1.getString("subject"));
                    notificationListRealm.setMMotherMobile(jsonObject1.getString("mMotherMobile"));
                    notificationListRealm.setClickHeremId(jsonObject1.getString("clickHeremId"));
                    notificationListRealm.setMigratedmId(jsonObject1.getString("migratedmId"));
                    notificationListRealm.setNoteId(jsonObject1.getString("noteId"));
                    notificationListRealm.setNoteStartDateTime(jsonObject1.getString("noteStartDateTime"));
                    notificationListRealm.setMtype(jsonObject1.getString("mtype"));
                    notificationListRealm.setMessage(jsonObject1.getString("message"));
                    notificationListRealm.setNoteType(jsonObject1.getString("noteType"));

                    /*movie.setMPicmeId(jsonObject1.getString("mPicmeId"));
                    movie.setMName(jsonObject1.getString("mName"));
                    movie.setMid(jsonObject1.getString("mid"));
                    movie.setVhnId(jsonObject1.getString("vhnId"));
                    movie.setSubject(jsonObject1.getString("subject"));
                    movie.setMMotherMobile(jsonObject1.getString("mMotherMobile"));
                    movie.setClickHeremId(jsonObject1.getString("clickHeremId"));
                    movie.setMigratedmId(jsonObject1.getString("migratedmId"));
                    movie.setNoteId(jsonObject1.getString("noteId"));
                    movie.setNoteStartDateTime(jsonObject1.getString("noteStartDateTime"));
<<<<<<< HEAD
                    movie.setMtype(jsonObject1.getString("mtype"));*/
//                    Log.d(NotificationListFragment.class.getSimpleName(), "Notification details" + i + movie);

//                    moviesList.add(movie);

//                    mAdapter.notifyDataSetChanged();
                   /* movie.setMtype(jsonObject1.getString("mtype"));
                    Log.d(NotificationListFragment.class.getSimpleName(), "Notification details" + i + movie);
                    moviesList.add(movie);*/
                    mAdapter.notifyDataSetChanged();
                }
                realm.commitTransaction(); //close table
            } else {
                Log.d(NotificationListFragment.class.getSimpleName(), "Notification messsage-->" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setValueToUI();


//        prepareMovieData(response);
    }

    private void setValueToUI() {

        Log.e("ON LINE ->",  "on line");

        realm.beginTransaction();
        RealmResults<NotificationListRealm> userInfoRealmResult = realm.where(NotificationListRealm.class).findAll();
        Log.e("Mother list size ->", userInfoRealmResult.size() + "");
        for (int i = 0; i < userInfoRealmResult.size(); i++) {
            mresponseResult = new NotificationListResponseModel.Vhn_migrated_mothers();
            NotificationListRealm model = userInfoRealmResult.get(i);
            mresponseResult.setMPicmeId(model.getMPicmeId());
            mresponseResult.setMName(model.getMName());
            mresponseResult.setMid(model.getMid());
            mresponseResult.setVhnId(model.getVhnId());
            mresponseResult.setSubject(model.getSubject());
            mresponseResult.setMMotherMobile(model.getMMotherMobile());
            mresponseResult.setClickHeremId(model.getClickHeremId());
            mresponseResult.setMigratedmId(model.getMigratedmId());
            mresponseResult.setNoteId(model.getNoteId());
            mresponseResult.setNoteStartDateTime(model.getNoteStartDateTime());
            mresponseResult.setMtype(model.getMtype());
            mresponseResult.setMessage(model.getMessage());
            mresponseResult.setmPhoto(model.getmPhoto());
            mresponseResult.setNoteType(model.getNoteType());
            moviesList.add(mresponseResult);
        }
        mAdapter.notifyDataSetChanged();
        realm.commitTransaction();
    }

    @Override
    public void NotificationResponseError(String response) {
        Log.d(NotificationListFragment.class.getSimpleName(), "Notification List Error response" + response);

    }

    @Override
    public void TodayVisitResponseSuccess(String response) {


        Log.d(MainActivity.class.getSimpleName(), "Notification count response success" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            String strTodayVisitCount = "0";
            if (status.equalsIgnoreCase("1")) {
//                preferenceData.setTodayVisitCount(strTodayVisitCount);
                strTodayVisitCount = jsonObject.getString("visitCount");

                txt_today_visit_count.setText(strTodayVisitCount);
                txt_count_today_visit.setText(strTodayVisitCount + " Mothers Visiting Today");

                Log.d(MainActivity.class.getSimpleName(), "Today Visit Count-->" + strTodayVisitCount);

            } else {
                txt_today_visit_count.setText(strTodayVisitCount);
                txt_count_today_visit.setText(strTodayVisitCount + " Mothers Visiting Today");
                Log.d(MainActivity.class.getSimpleName(), "Today Visit messsage-->" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void TodayVisitResponseError(String response) {

    }

    @Override
    public void NotificationCountSuccess(String respons) {

    }

    @Override
    public void NotificationCountError(String respons) {

    }

    @Override
    public void makeCall(String mMotherMobile) {
//        isDataUpdate = false;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCallPermission();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + mMotherMobile)));
        }

    }

    private void requestCallPermission() {

//        Log.i(ANTT1MothersList.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(), Manifest.permission.CALL_PHONE)) {
//            Toast.makeText(getActivity(), "Displaying Call permission rationale to provide additional context.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getActivity(), "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
