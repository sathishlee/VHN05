package com.example.priyan.vhn.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.priyan.vhn.AppConstent;
import com.example.priyan.vhn.ImmunizationActivity;
import com.example.priyan.vhn.MothersDetailsActivity;
import com.example.priyan.vhn.R;
import com.example.priyan.vhn.VhnProfile;


public class home extends Fragment {

    CardView cardview, vhn_profile;
Button btnShowImmunizationList;
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
         View view = inflater.inflate(R.layout.fragment_home, container, false);

         cardview = view.findViewById(R.id.cardview);
         cardview.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AppConstent.isfromhome=1;
                 startActivity(new Intent(getActivity(), MothersDetailsActivity.class));
             }
         });


        vhn_profile =(CardView) view.findViewById(R.id.vhn_profile);

        vhn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VhnProfile.class);
                getActivity().finish();
                startActivity(intent);
            }

        });
        btnShowImmunizationList = view.findViewById(R.id.btn_show_immunization_list);

        btnShowImmunizationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ImmunizationActivity.class));
            }
        });
        return view;

    }




}
