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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unicef.vhn.Preference.PreferenceData;
import com.unicef.vhn.Presenter.HomePresenter;
import com.unicef.vhn.Presenter.MotherListPresenter;
import com.unicef.vhn.R;
//import com.unicef.vhn.activity.HighRiskListActivity;
import com.unicef.vhn.activity.ANTT1MothersList;
import com.unicef.vhn.activity.ANTT2MothersList;
import com.unicef.vhn.activity.InfantListActivity;
import com.unicef.vhn.activity.MainActivity;
import com.unicef.vhn.activity.MotherListActivity;
import com.unicef.vhn.activity.MotherTrackActivity;
import com.unicef.vhn.activity.NoInternetConnectionActivity;
import com.unicef.vhn.activity.PNHBNCListActivity;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.activity.TreamPreTreamListActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.view.MotherListsViews;

import org.json.JSONException;
import org.json.JSONObject;


public class home extends Fragment implements MotherListsViews {
    ImageView img_mother_count, img_high_risk_count, img_infant_count;
    TextView txt_mother_count, txt_high_risk_count, txt_infants_count, txt_sos_count;
    Button but_an_mother_total_count, but_an_mother_high_risk_count, but_an_mother_pn_hbnc_totlal_count, but_an_mother_pn_hbnc_term_preterm_count;
    TextView txt_antt_1_due, txt_antt_2_due, txt_pnhbnc_due;

    TextView txt_vhn_name, txt_hsc, txt_phc, txt_block, txt_address;
    ProgressDialog pDialog;
    HomePresenter homePresenter;
    PreferenceData preferenceData;
    LinearLayout ll_sos_view;
    CheckNetwork checkNetwork;

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
        checkInterNetConnection();
        initUI(view);


        img_mother_count = (ImageView) view.findViewById(R.id.img_mother_count);
        img_mother_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "mother_count";
                AppConstants.MOTHER_LIST_TITLE = "All Mother List";

                startActivity(new Intent(getActivity(), MotherListActivity.class));

            }
        });

        ll_sos_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "sos_count";
                AppConstants.MOTHER_LIST_TITLE = "SOS List";
                startActivity(new Intent(getActivity(), SosAlertListActivity.class));

            }
        });

        img_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "risk_count";
                AppConstants.MOTHER_LIST_TITLE = "High Risk Mother List";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        img_infant_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "infant_count";
                AppConstants.MOTHER_LIST_TITLE = "Infant List";

                startActivity(new Intent(getActivity(), TreamPreTreamListActivity.class));
            }
        });

        but_an_mother_total_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "an_mother_total_count";
                AppConstants.MOTHER_LIST_TITLE = "AN Mother List";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });

        but_an_mother_high_risk_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "high_risk_count";
                AppConstants.MOTHER_LIST_TITLE = "AN High Risk Mother List";

                startActivity(new Intent(getActivity(), MotherListActivity.class));
            }
        });
        but_an_mother_pn_hbnc_totlal_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "pn_hbnc_totlal_coun";
                AppConstants.MOTHER_LIST_TITLE = "PN/HBNC Mother List";

                startActivity(new Intent(getActivity(), PNHBNCListActivity.class));
            }
        });
        but_an_mother_pn_hbnc_term_preterm_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.GET_MOTHER_LIST_TYPE = "pn_hbnc_term_preterm_count";
                AppConstants.MOTHER_LIST_TITLE = "TERM/PRE TERM List";

                startActivity(new Intent(getActivity(), TreamPreTreamListActivity.class));
            }
        });
        txt_antt_1_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ANTT_1_LIST="TT1_List";
                AppConstants.ANTT_1_TITLE="AN TT 1 Due List";

                startActivity(new Intent(getActivity(), ANTT1MothersList.class));
            }
        });

        txt_antt_2_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ANTT_2_LIST="TT2_List";
                AppConstants.ANTT_2_TITLE="AN TT 2 Due List";

                startActivity(new Intent(getActivity(), ANTT2MothersList.class));
            }
        });


        return view;

    }

    private void checkInterNetConnection() {
        checkNetwork = new CheckNetwork(getActivity());
        if (checkNetwork.isNetworkAvailable()) {
            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
        } else {
            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
            startActivity(new Intent(getActivity(), NoInternetConnectionActivity.class));
        }
    }

    private void initUI(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(getActivity());
        homePresenter = new HomePresenter(getActivity(), this);
        if (checkNetwork.isNetworkAvailable()) {
            homePresenter.getDashBoard(preferenceData.getVhnCode(), preferenceData.getVhnId());
        } else {
            Log.w(home.class.getSimpleName(), "Is" + checkNetwork.isNetworkAvailable());
            startActivity(new Intent(getActivity(), NoInternetConnectionActivity.class));
        }
        ll_sos_view = view.findViewById(R.id.ll_sos_view);
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

        txt_antt_1_due = view.findViewById(R.id.txt_antt_1_due);
        txt_antt_2_due = view.findViewById(R.id.txt_antt_2_due);
        txt_pnhbnc_due = view.findViewById(R.id.txt_pnhbnc_due);

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
                txt_antt_1_due.setText(mJsnobject.getString("ANTT1"));
                txt_antt_2_due.setText(mJsnobject.getString("ANTT2"));
                txt_pnhbnc_due.setText(mJsnobject.getString("pnhbncCount"));


                but_an_mother_total_count.setText("Total: " + mJsnobject.getString("ANMothersCount"));
                but_an_mother_high_risk_count.setText("High Risk: " + mJsnobject.getString("ANMotherRiskCount"));
                but_an_mother_pn_hbnc_totlal_count.setText("Total: " + mJsnobject.getString("PNMotherCount"));
                but_an_mother_pn_hbnc_term_preterm_count.setText("Term/Preterm: " + mJsnobject.getString("termsCount"));



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

    @Override
    public void showAlertClosedSuccess(String response) {

    }

    @Override
    public void showAlertClosedError(String string) {

    }
}
