package com.unicef.vhn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicef.vhn.R;
//import com.unicef.vhn.adapter.MoviesAdapter;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyan on 2/3/2018.
 */

public class risk extends Fragment{

    CardView mother_risk_card;

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


        mother_risk_card = (CardView) view.findViewById(R.id.mother_risk_card);

        mother_risk_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MothersDetailsActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return view;

    }



}
