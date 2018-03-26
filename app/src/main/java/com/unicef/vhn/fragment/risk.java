package com.unicef.vhn.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
//import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.adapter.MotherListAdapter;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class risk extends Fragment implements MotherListsViews {
    ProgressDialog pDialog;
    MotherListPresenter pnMotherListPresenter;
    PreferenceData preferenceData;
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    PNMotherListResponse.VhnAN_Mothers_List mresponseResult;
    //    private RecyclerView recyclerView;
    private RecyclerView mother_recycler_view;
    private MotherListAdapter mAdapter;

//    CardView mother_risk_card;

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

        initUI(view);
        /*mother_risk_card = (CardView) view.findViewById(R.id.mother_risk_card);

        mother_risk_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MothersDetailsActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });*/

        return view;
    }

    private void initUI(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(getActivity());
        pnMotherListPresenter = new MotherListPresenter(getActivity(),this);
        pnMotherListPresenter.getPNMotherList(Apiconstants.DASH_BOARD_MOTHERS_RISK,preferenceData.getVhnCode(),preferenceData.getVhnId());

//        pnMotherListPresenter.getPNMotherList("V10001","1");
        mResult = new ArrayList<>();
        mother_recycler_view = (RecyclerView)view. findViewById(R.id.recycler_view);
        mAdapter = new MotherListAdapter(mResult, getActivity(), "Risk");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mother_recycler_view.setLayoutManager(mLayoutManager);
        mother_recycler_view.setItemAnimator(new DefaultItemAnimator());
        mother_recycler_view.setAdapter(mAdapter);
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
    public void showLoginSuccess(String response) {

        Log.e(risk.class.getSimpleName(), "Response success" + response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            JSONArray jsonArray = mJsnobject.getJSONArray("vhnAN_Mothers_List");
            for (int i = 0; i < jsonArray.length(); i++) {
                mresponseResult =new PNMotherListResponse.VhnAN_Mothers_List();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mresponseResult.setMid(jsonObject.getString("mid"));
                mresponseResult.setMName(jsonObject.getString("mName"));
                mresponseResult.setMPicmeId(jsonObject.getString("mPicmeId"));
                mresponseResult.setVhnId(jsonObject.getString("vhnId"));
//                mresponseResult.setMLatitude(jsonObject.getString("mLatitude"));
//                mresponseResult.setMLongitude(jsonObject.getString("mLongitude"));
                mResult.add(mresponseResult);
                mAdapter.notifyDataSetChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showLoginError(String response) {
        Log.e(risk.class.getSimpleName(), "Response success" + response);

    }

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
