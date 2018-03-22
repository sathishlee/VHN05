package com.unicef.vhn.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.HighRiskListActivity;
import com.unicef.vhn.activity.InfantListActivity;
import com.unicef.vhn.activity.MotherListActivity;


public class home extends Fragment {

    ImageView img_mother_count, high_risk_count, infant_count;

    public static home newInstance()
    {
        home fragment = new home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
         View view = inflater.inflate(R.layout.fragment_home_new, container, false);

        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);
        img_mother_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MotherListActivity.class));

            }
        });

        high_risk_count = (ImageView) view.findViewById(R.id.high_risk_count);
        img_mother_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),HighRiskListActivity.class));
            }
        });

        infant_count = (ImageView) view.findViewById(R.id.infant_count);
        infant_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InfantListActivity.class));
            }
        });


        return view;

    }


}
