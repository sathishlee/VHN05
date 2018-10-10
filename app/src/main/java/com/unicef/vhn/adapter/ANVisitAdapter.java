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
            txtPluseRate, txtWeight, txtFunbalHeight, txtFhs, txtPep, txtHp, txtFbs, txtPpbs, txtGtt, txtSugar, txtUrinAlubin, txtFetus, txtGestationSac, txtLiquor, txtPlacenta, txtTsh;


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
        if (mhealthRecordResponseModel.getVDate().equalsIgnoreCase("null")) {
            txtVisitedDate.setText("-");
        } else {
            txtVisitedDate.setText(mhealthRecordResponseModel.getVDate());
        }
        if (mhealthRecordResponseModel.getVtypeOfVisit().equalsIgnoreCase("null")) {
            txtVisitedType.setText("-");
        } else {
            txtVisitedType.setText(mhealthRecordResponseModel.getVtypeOfVisit());
        }
        if (mhealthRecordResponseModel.getVFacility().equalsIgnoreCase("null")) {
            txtFacility.setText("-");
        } else {
            txtFacility.setText(mhealthRecordResponseModel.getVFacility());
        }
        if (mhealthRecordResponseModel.getVAnyComplaints().equalsIgnoreCase("null")) {
            txtAnyComplient.setText("-");
        } else {
            txtAnyComplient.setText(mhealthRecordResponseModel.getVAnyComplaints());
        }
        if (mhealthRecordResponseModel.getVClinicalBPDiastolic().equalsIgnoreCase("null") && mhealthRecordResponseModel.getVClinicalBPSystolic().equalsIgnoreCase("null")) {
            txtBPValue.setText("-");
        } else {
            txtBPValue.setText(mhealthRecordResponseModel.getVClinicalBPSystolic() + " mm Hg" + "/" + mhealthRecordResponseModel.getVClinicalBPDiastolic() + " mm Hg");
        }
        if (mhealthRecordResponseModel.getVEnterPulseRate().equalsIgnoreCase("null")) {
            txtPluseRate.setText("-");
        } else {
            txtPluseRate.setText(mhealthRecordResponseModel.getVEnterPulseRate() + " Per Min");
        }
        if (mhealthRecordResponseModel.getVEnterWeight().equalsIgnoreCase("null")) {
            txtWeight.setText("-");
        } else {
            txtWeight.setText(mhealthRecordResponseModel.getVEnterWeight() + " Kg");
        }
        if (mhealthRecordResponseModel.getVFundalHeight().equalsIgnoreCase("null")) {
            txtFunbalHeight.setText("-");
        } else {
            txtFunbalHeight.setText(mhealthRecordResponseModel.getVFundalHeight() + " Wks");
        }
        if (mhealthRecordResponseModel.getVFHS().equalsIgnoreCase("null")) {
            txtFhs.setText("-");
        } else {
            txtFhs.setText(mhealthRecordResponseModel.getVFHS() + " Per Min");
        }

        if (mhealthRecordResponseModel.getVPedalEdemaPresent().equalsIgnoreCase("null")) {
            txtPep.setText("-");
        } else {
            txtPep.setText(mhealthRecordResponseModel.getVPedalEdemaPresent());
        }
        if (mhealthRecordResponseModel.getVHemoglobin().equalsIgnoreCase("null")) {
            txtHp.setText("-");
        } else {
            txtHp.setText(mhealthRecordResponseModel.getVHemoglobin() + " g %");
        }

        if (mhealthRecordResponseModel.getVFBS().equalsIgnoreCase("null")) {
            txtFbs.setText("-");
        } else {
            txtFbs.setText(mhealthRecordResponseModel.getVFBS() + "mg/dl");
        }
        if (mhealthRecordResponseModel.getVPPBS().equalsIgnoreCase("null")) {
            txtPpbs.setText("-");
        } else {
            txtPpbs.setText(mhealthRecordResponseModel.getVPPBS() + " mg/dl");
        }

        if (mhealthRecordResponseModel.getVGTT().equalsIgnoreCase("null")) {
            txtGtt.setText("-");
        } else {
            txtGtt.setText(mhealthRecordResponseModel.getVGTT() + " mg/dl");
        }
        if (mhealthRecordResponseModel.getUsgFetus().equalsIgnoreCase("null")) {
            txtFetus.setText("-");
        } else {
            txtFetus.setText(mhealthRecordResponseModel.getUsgFetus());
        }
        if (mhealthRecordResponseModel.getVUrinSugar().equalsIgnoreCase("null")) {
            txtSugar.setText("-");
        } else {
            txtSugar.setText(mhealthRecordResponseModel.getVUrinSugar());
        }
        if (mhealthRecordResponseModel.getVAlbumin().equalsIgnoreCase("null")) {
            txtUrinAlubin.setText("-");
        } else {
            txtUrinAlubin.setText(mhealthRecordResponseModel.getVAlbumin());
        }
        if (mhealthRecordResponseModel.getUsgGestationSac().equalsIgnoreCase("null")) {
            txtGestationSac.setText("-");
        } else {
            txtGestationSac.setText(mhealthRecordResponseModel.getUsgGestationSac());
        }
        if (mhealthRecordResponseModel.getUsgLiquor().equalsIgnoreCase("null")) {
            txtLiquor.setText("-");
        } else {
            txtLiquor.setText(mhealthRecordResponseModel.getUsgLiquor());
        }
        if (mhealthRecordResponseModel.getUsgPlacenta().equalsIgnoreCase("null")) {
            txtPlacenta.setText("-");
        } else {
            txtPlacenta.setText(mhealthRecordResponseModel.getUsgPlacenta());
        }
        if (mhealthRecordResponseModel.getVTSH().equalsIgnoreCase("null")) {
            txtTsh.setText("-");
        } else {
            txtTsh.setText(mhealthRecordResponseModel.getVTSH() + " MU/L");
        }


//        txtVisitedDate.setText(mhealthRecordResponseModel.getVDate());
//        txtVisitedType.setText(mhealthRecordResponseModel.getVtypeOfVisit());
//        txtFacility.setText(mhealthRecordResponseModel.getVFacility());
//        txtAnyComplient.setText(mhealthRecordResponseModel.getVAnyComplaints());
//        txtBPValue.setText(mhealthRecordResponseModel.getVClinicalBPDiastolic()+ "mm" + "/" + mhealthRecordResponseModel.getVClinicalBPDiastolic()+ "Hg");
//        txtPluseRate.setText(mhealthRecordResponseModel.getVEnterPulseRate()+ "Per min");
//        txtWeight.setText(mhealthRecordResponseModel.getVEnterWeight()+"kg");
//        txtFunbalHeight.setText(mhealthRecordResponseModel.getVFundalHeight()+ "cm");
//        txtFhs.setText(mhealthRecordResponseModel.getVFHS()+ "Per min");
//        txtPep.setText(mhealthRecordResponseModel.getVPedalEdemaPresent());
//        txtHp.setText(mhealthRecordResponseModel.getVHemoglobin()+ "%");
//        txtFbs.setText(mhealthRecordResponseModel.getVFBS()+ "mg");
//        txtPpbs.setText(mhealthRecordResponseModel.getVPPBS()+ "mg");
//        txtGtt.setText(mhealthRecordResponseModel.getVGTT());
//        txtFetus.setText(mhealthRecordResponseModel.getUsgFetus());
//        txtSugar.setText(mhealthRecordResponseModel.getVUrinSugar());
//        txtGestationSac.setText(mhealthRecordResponseModel.getUsgGestationSac());
//        txtLiquor.setText(mhealthRecordResponseModel.getUsgLiquor());
//        txtPlacenta.setText(mhealthRecordResponseModel.getUsgPlacenta());

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
        txtUrinAlubin = healthRecordLayout.findViewById(R.id.txt_urin_alubin);
        txtFetus = healthRecordLayout.findViewById(R.id.txt_fetus);
        txtGestationSac = healthRecordLayout.findViewById(R.id.txt_gestation_sac);
        txtLiquor = healthRecordLayout.findViewById(R.id.txt_liquor);
        txtPlacenta = healthRecordLayout.findViewById(R.id.txt_placenta);
        txtTsh = healthRecordLayout.findViewById(R.id.txt_tsh);

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
