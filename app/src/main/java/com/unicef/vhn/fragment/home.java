package com.unicef.vhn.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicef.vhn.R;


public class home extends Fragment {

   /* CardView cardview, vhn_profile;
Button btnShowImmunizationList;*/
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

         /*cardview = view.findViewById(R.id.cardview);
         cardview.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AppConstent.isfromhome=1;
                 startActivity(new Intent(getActivity(), MothersDetailsActivity.class));
             }
         });*/


//        vhn_profile =(CardView) view.findViewById(R.id.vhn_profile);

//        vhn_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), VhnProfile.class);
//                getActivity().finish();
//                startActivity(intent);
//            }
//
//        });
//        btnShowImmunizationList = view.findViewById(R.id.btn_show_immunization_list);


        return view;

    }




}
