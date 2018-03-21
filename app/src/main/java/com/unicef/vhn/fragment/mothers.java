package com.unicef.vhn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.MothersDetailsActivity;
//import com.unicef.vhn.adapter.MoviesAdapter;


/**
 * Created by priyan on 2/3/2018.
 */

public class mothers extends Fragment{

    CardView mother_card;
    public static mothers newInstance() {
        mothers fragment = new mothers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mothers, container, false);


        getActivity().setTitle("Mothers List");

        mother_card = (CardView) view.findViewById(R.id.mother_card);
        mother_card.setOnClickListener(new View.OnClickListener() {
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
