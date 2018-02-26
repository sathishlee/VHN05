package com.unicef.vhn.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.unicef.vhn.ImmunizationActivity;
import com.unicef.vhn.ImmunizationDoseListActivity;
import com.unicef.vhn.ImmunizationMotherListActivity;
import com.unicef.vhn.PNMotherVisitListActivity;
import com.unicef.vhn.PNMotherVisitListDetailsActivity;
import com.unicef.vhn.R;


public class ImmunizationFragment extends Fragment {
TableLayout imm_tabs;
    private OnFragmentInteractionListener mListener;

    public ImmunizationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ImmunizationFragment newInstance( ) {
        ImmunizationFragment fragment = new ImmunizationFragment();

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
        View view =  inflater.inflate(R.layout.fragment_immunization_list, container, false);
        imm_tabs = view.findViewById(R.id.imm_tabs);

        imm_tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ImmunizationMotherListActivity.class));
            }
        });
        return view;
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
