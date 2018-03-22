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
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.HomePresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.HighRiskListActivity;
import com.unicef.vhn.activity.InfantListActivity;
import com.unicef.vhn.activity.MotherListActivity;
import com.unicef.vhn.activity.MotherTrackActivity;
import com.unicef.vhn.activity.PNHBNCListActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;


public class home extends Fragment implements MotherListsViews {
    ImageView img_mother_count, img_high_risk_count, img_infant_count;
    TextView txt_mother_count, txt_high_risk_count, txt_infants_count, txt_sos_count;
    Button but_an_mother_total_count, but_an_mother_high_risk_count, but_an_mother_pn_hbnc_totlal_count, but_an_mother_pn_hbnc_term_preterm_count;
    TextView but_an_tt1, but_an_tt2, but_an_pn_hbnc;

    TextView txt_vhn_name, txt_hsc, txt_phc, txt_block, txt_address;
    ProgressDialog pDialog;
    HomePresenter homePresenter;
    PreferenceData preferenceData;

    public static home newInstance() {
        home fragment = new home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);

        initUI(view);



        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);
        img_mother_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="mother_count";
                startActivity(new Intent(getActivity(), MotherListActivity.class));

            }
        });

        img_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="risk_count";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        img_infant_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="infant_count";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        but_an_mother_total_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="an_mother_total_count";
                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        but_an_mother_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="high_risk_count";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });
        but_an_mother_pn_hbnc_totlal_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="pn_hbnc_totlal_coun";

                startActivity(new Intent(getActivity(), PNHBNCListActivity.class));
            }
        });
        but_an_mother_pn_hbnc_term_preterm_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE="pn_hbnc_term_preterm_count";
                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        return view;

    }

    private void initUI(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        homePresenter = new HomePresenter(getActivity(), this);
        homePresenter.getDashBoard(preferenceData.getVhnCode(), preferenceData.getVhnId());

        txt_vhn_name = view.findViewById(R.id.txt_vhn_name);
        txt_hsc = view.findViewById(R.id.txt_hsc);
        txt_phc = view.findViewById(R.id.txt_phc);
        txt_block = view.findViewById(R.id.txt_block);
        txt_address = view.findViewById(R.id.txt_address);
        txt_mother_count = view.findViewById(R.id.txt_mother_count);
        txt_high_risk_count = view.findViewById(R.id.txt_high_risk_count);
        txt_infants_count = view.findViewById(R.id.txt_infants_count);
        txt_sos_count = view.findViewById(R.id.txt_sos_count);

        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);
        img_high_risk_count = (ImageView) view.findViewById(R.id.img_high_risk_count);
        img_infant_count = (ImageView) view.findViewById(R.id.img_infant_count);

        but_an_mother_total_count = view.findViewById(R.id.but_an_mother_total_count);
        but_an_mother_high_risk_count = view.findViewById(R.id.but_an_mother_high_risk_count);
        but_an_mother_pn_hbnc_totlal_count = view.findViewById(R.id.but_an_mother_pn_hbnc_totlal_count);
//        but_an_mother_pn_hbnc_totlal_count = view.findViewById(R.id.but_an_mother_pn_hbnc_totlal_count);
        but_an_mother_pn_hbnc_term_preterm_count = view.findViewById(R.id.but_an_mother_pn_hbnc_term_preterm_count);

        but_an_tt1 = view.findViewById(R.id.but_an_tt1);
        but_an_tt2 = view.findViewById(R.id.but_an_tt2);
        but_an_pn_hbnc = view.findViewById(R.id.but_an_pn_hbnc);

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
                txt_mother_count.setText(mJsnobject.getString("mothersCount"));
                txt_high_risk_count.setText(mJsnobject.getString("riskMothersCount"));
                txt_infants_count.setText(mJsnobject.getString("infantCount"));
                txt_sos_count.setText(mJsnobject.getString("sosCount"));

                but_an_mother_total_count.setText("Total: "+mJsnobject.getString("ANMothersCount"));
                but_an_mother_high_risk_count.setText("High Risk: "+mJsnobject.getString("ANMotherRiskCount"));
                but_an_mother_pn_hbnc_totlal_count.setText("Total: "+mJsnobject.getString("PNMotherCount"));
                but_an_mother_pn_hbnc_term_preterm_count.setText("Term/Preterm: "+mJsnobject.getString("PNMotherRiskCount"));

                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
//                JSONObject mJsnobject_phcDetails = mJsnobject.getJSONObject("phcDetails");
                txt_vhn_name.setText(mJsnobject_phcDetails.getString("vhnName"));
                txt_phc.setText(mJsnobject_phcDetails.getString("phcName"));
                txt_hsc.setText(mJsnobject_phcDetails.getString("facilityName"));
                txt_block.setText(mJsnobject_phcDetails.getString("block"));
                txt_address.setText(mJsnobject_phcDetails.getString("District"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showLoginError(String response) {
        Log.e(home.class.getSimpleName(), "Response Error" + response);

    }
}
