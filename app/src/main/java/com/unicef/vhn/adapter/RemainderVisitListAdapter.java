package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.ANMotherDetailsViewActivcity;
import com.unicef.vhn.activity.MotherDetails.PNMotherDetailsViewActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.fragment.RemainderVisitFragment;
import com.unicef.vhn.model.RemainderVisitResponseModel;
import com.unicef.vhn.model.VisitListResponseModel;

import java.util.List;

/**
 * Created by sathish on 3/31/2018.
 */

public class RemainderVisitListAdapter extends RecyclerView.Adapter<RemainderVisitListAdapter.ViewHolder> {
    String TAG= RemainderVisitListAdapter.class.getSimpleName();
    private List< RemainderVisitResponseModel.Remaindermothers> mResult;
    Activity applicationContext;
    String type;
    MakeCallInterface makeCallInterface;

    String strMid;

    public RemainderVisitListAdapter(List<RemainderVisitResponseModel.Remaindermothers> mResult, Activity alertActivity, MakeCallInterface makeCallInterface) {
        this.applicationContext = alertActivity;
        this.mResult = mResult;
        Log.e(RemainderVisitListAdapter.class.getSimpleName(),"mResult size"+mResult);
        this.makeCallInterface = makeCallInterface;
    }


   /* public RemainderVisitListAdapter(FragmentActivity activity, List<VisitListResponseModel.Vhn_current_visits> mResult, RemainderVisitFragment makeCallInterface) {
        this.applicationContext = applicationContext;
//        this.mResult = mResult;
        Log.e(RemainderVisitListAdapter.class.getSimpleName(),"mResult size"+mResult);
        this.makeCallInterface = makeCallInterface;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_visit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RemainderVisitResponseModel.Remaindermothers current_visits = mResult.get(position);
        if (current_visits.getMName().equalsIgnoreCase("null")) {
            holder.txt_username.setText("-");
        } else {
            holder.txt_username.setText(current_visits.getMName());
        }
        if (current_visits.getPicmeId().equalsIgnoreCase("null")) {
            holder.txt_picme_id.setText("-");
        } else {
            holder.txt_picme_id.setText(current_visits.getPicmeId());
        }
        if (current_visits.getMtype().equalsIgnoreCase("null")){
            holder.txt_list_type.setText("-");
        }
        else {
        holder.txt_list_type.setText(current_visits.getMtype());
    }
        if (current_visits.getNextVisit().equalsIgnoreCase("null")){
            holder.txt_current_visit.setText("-");
        }
        else {
            holder.txt_current_visit.setText(current_visits.getNextVisit());
        }

        if (current_visits.getMonth().equalsIgnoreCase("null")){
            holder.txt_visit_month.setText("-");
        }
        else {
            holder.txt_visit_month.setText("V.No : "+current_visits.getMonth());
        }
        if (current_visits.getMMotherMobile().equalsIgnoreCase("null")||current_visits.getMMotherMobile().length()<10){
            holder.txt_call.setVisibility(View.GONE);
        }else{
            holder.txt_call.setVisibility(View.VISIBLE);
        }
        holder.txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(current_visits.getMMotherMobile());

            }
        });
        holder.txt_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = current_visits.getMid();
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MotherLocationActivity.class));

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = current_visits.getMid();
                AppConstants.SELECTED_VISIT_NOTE_ID = current_visits.getNoteId();
                AppConstants.IS_TODAY_VIST_LIST=true;
                if (current_visits.getMtype().equalsIgnoreCase("PN")) {
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherDetailsViewActivity.class));

                } else if (current_visits.getMtype().equalsIgnoreCase("AN")) {
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), ANMotherDetailsViewActivcity.class));

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_list_type, txt_track, txt_call, txt_current_visit,txt_visit_month;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_list_type = itemView.findViewById(R.id.txt_list_type);
            txt_track = itemView.findViewById(R.id.txt_track);
            txt_call = itemView.findViewById(R.id.txt_call);
            txt_current_visit = itemView.findViewById(R.id.txt_current_visit);
            txt_visit_month = itemView.findViewById(R.id.txt_visit_month);
        }
    }
}
