package com.unicef.vhn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.ANViewReportsActivity;
import com.unicef.vhn.model.ANMotherVisitResponseModel;

import java.util.ArrayList;

/**
 * Created by sathish on 3/23/2018.
 */

public class ANVisitAdapter extends PagerAdapter {
    Context mContext;
    private ArrayList<ANMotherVisitResponseModel.VhnAN_Mothers_List> mhealthRecordList;
    ANMotherVisitResponseModel.VhnAN_Mothers_List mhealthRecordResponseModel;
    private LayoutInflater inflater;

    TextView txtVisitedDate, txtVisitedType, txtFacility, txtAnyComplient, txtBPValue,
            txtPluseRate, txtWeight, txtFunbalHeight, txtFhs, txtPep, txtHp, txtFbs, txtPpbs, txtGtt, txtSugar, txtFetus, txtGestationSac, txtLiquor, txtPlacenta;


    public ANVisitAdapter(Context mContext, ArrayList<ANMotherVisitResponseModel.VhnAN_Mothers_List> mhealthRecordList) {
        Log.e("ANVIEWREPORT List", mhealthRecordList.size() + "");

        this.mContext = mContext;
        this.mhealthRecordList = mhealthRecordList;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View healthRecordLayout = inflater.inflate(R.layout.item_visit_screen, view, false);
        mhealthRecordResponseModel = mhealthRecordList.get(position);
//                Log.e(HealthRecordsAdapter.class.getSimpleName(),mhealthRecordResponseModel.getVisitId());
        initUI(healthRecordLayout);
        setValuetoUI(mhealthRecordResponseModel);
        view.addView(healthRecordLayout);

        return healthRecordLayout;
    }

    private void setValuetoUI(ANMotherVisitResponseModel.VhnAN_Mothers_List mhealthRecordResponseModel) {

        txtVisitedDate.setText(mhealthRecordResponseModel.getVDate());
        txtVisitedType.setText(mhealthRecordResponseModel.getVtypeOfVisit());
        txtFacility.setText(mhealthRecordResponseModel.getVFacility());
        txtAnyComplient.setText(mhealthRecordResponseModel.getVAnyComplaints());
        txtBPValue.setText(mhealthRecordResponseModel.getVClinicalBPDiastolic() + "/" + mhealthRecordResponseModel.getVClinicalBPDiastolic());
        txtPluseRate.setText(mhealthRecordResponseModel.getVEnterPulseRate());
        txtWeight.setText(mhealthRecordResponseModel.getVEnterWeight());
        txtFunbalHeight.setText(mhealthRecordResponseModel.getVFundalHeight());
        txtFhs.setText(mhealthRecordResponseModel.getVFHS());
        txtPep.setText(mhealthRecordResponseModel.getVPedalEdemaPresent());
        txtHp.setText(mhealthRecordResponseModel.getVHemoglobin());
        txtFbs.setText(mhealthRecordResponseModel.getVFBS());
        txtPpbs.setText(mhealthRecordResponseModel.getVPPBS());
        txtGtt.setText(mhealthRecordResponseModel.getVGTT());
        txtFetus.setText(mhealthRecordResponseModel.getUsgFetus());
        txtSugar.setText(mhealthRecordResponseModel.getVUrinSugar());
        txtGestationSac.setText(mhealthRecordResponseModel.getUsgGestationSac());
        txtLiquor.setText(mhealthRecordResponseModel.getUsgLiquor());
        txtPlacenta.setText(mhealthRecordResponseModel.getUsgPlacenta());

    }

    private void initUI(View healthRecordLayout) {

//        llClickPickMeVisit = healthRecordLayout.findViewById(R.id.ll_click_pickme_visit);
//        llClickOtherVisit = healthRecordLayout.findViewById(R.id.ll_click_other_visit);
        txtVisitedDate = healthRecordLayout.findViewById(R.id.txt_visited_date);
        txtVisitedType = healthRecordLayout.findViewById(R.id.txt_visited_type);
        txtFacility = healthRecordLayout.findViewById(R.id.txt_facility);
        txtAnyComplient = healthRecordLayout.findViewById(R.id.txt_any_complient);
        txtBPValue = healthRecordLayout.findViewById(R.id.txt_bp_value);
        txtPluseRate = healthRecordLayout.findViewById(R.id.txt_pluse_rate);
        txtWeight = healthRecordLayout.findViewById(R.id.txt_weight);
        txtFunbalHeight = healthRecordLayout.findViewById(R.id.txt_funbal_height);
        txtFhs = healthRecordLayout.findViewById(R.id.txt_fhs);
        txtPep = healthRecordLayout.findViewById(R.id.txt_pep);
        txtHp = healthRecordLayout.findViewById(R.id.txt_hp);
        txtFbs = healthRecordLayout.findViewById(R.id.txt_fbs);
        txtPpbs = healthRecordLayout.findViewById(R.id.txt_ppbs);
        txtGtt = healthRecordLayout.findViewById(R.id.txt_gtt);
        txtSugar = healthRecordLayout.findViewById(R.id.txt_sugar);
        txtFetus = healthRecordLayout.findViewById(R.id.txt_fetus);
        txtGestationSac = healthRecordLayout.findViewById(R.id.txt_gestation_sac);
        txtLiquor = healthRecordLayout.findViewById(R.id.txt_liquor);
        txtPlacenta = healthRecordLayout.findViewById(R.id.txt_placenta);

    }
    /*FragmentActivity activity;
    TypeOfHealthRecords mTypeOfHealthRecords;
    LinearLayout llClickPickMeVisit,llClickOtherVisit;
    TextView txtVisitedDate, txtVisitedType, txtFacility, txtAnyComplient, txtBPValue,
            txtPluseRate, txtWeight, txtFunbalHeight, txtFhs, txtPep, txtHp, txtFbs, txtPpbs, txtGtt, txtSugar, txtFetus, txtGestationSac, txtLiquor, txtPlacenta;
*/

    @Override
    public int getCount() {
        Log.e("ANVIEWREPORT List", mhealthRecordList.size() + "");
        return mhealthRecordList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Visit " + mhealthRecordList.get(position).getVisitId();
    }
}
