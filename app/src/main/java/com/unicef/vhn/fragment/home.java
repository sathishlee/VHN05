package com.unicef.vhn.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.InfantListActivity;
import com.unicef.vhn.activity.MotherListActivity;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;


public class home extends Fragment implements MotherListsViews {
ImageView img_mother_count,high_risk_count,infant_count;
TextView txt_vhn_name,txt_high_risk_count,txt_infants_count;
Button but_an_mother_total_count,but_an_mother_high_risk_count,but_an_mother_pn_hbnc_totlal_count,but_an_mother_pn_hbnc_term_preterm_count;
Button but_an_tt1,but_an_tt2,but_an_pn_hbnc;

    TextView txt_mother_count,txt_hsc,txt_phc,txt_block,txt_address;
    ProgressDialog pDialog;
    MotherListPresenter MotherListPresenter;
    PreferenceData preferenceData;

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

        initUI(view);

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

    private void initUI(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData =new PreferenceData(getActivity());
        MotherListPresenter =new MotherListPresenter(getActivity(),this);


        txt_vhn_name = view.findViewById(R.id.txt_vhn_name);
        txt_hsc = view.findViewById(R.id.txt_hsc);
        txt_phc = view.findViewById(R.id.txt_phc);
        txt_block = view.findViewById(R.id.txt_block);
        txt_address = view.findViewById(R.id.txt_address);
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
        Log.e(home.class.getSimpleName(), "Response success" + response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");

            if (status.equalsIgnoreCase("1")) {

                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
                txt_vhn_name.setText(mJsnobject_phcDetails.getString("vhnName"));
                txt_phc.setText(mJsnobject_phcDetails.getString("phcName"));
                txt_hsc.setText(mJsnobject_phcDetails.getString("facilityName"));
                txt_block.setText(mJsnobject_phcDetails.getString("block"));
                txt_address.setText(mJsnobject_phcDetails.getString("District"));
            }
        }
    catch (JSONException e) {
        e.printStackTrace();
    }


    }

    @Override
    public void showLoginError(String string) {

    }
}
