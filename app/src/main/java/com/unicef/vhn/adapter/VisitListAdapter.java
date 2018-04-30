package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.activity.PNMotherDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.VisitListResponseModel;

import java.util.List;

/**
 * Created by sathish on 3/31/2018.
 */

public class VisitListAdapter extends RecyclerView.Adapter<VisitListAdapter.ViewHolder> {
    private List<VisitListResponseModel.Vhn_current_visits> mResult;
    Activity applicationContext;
    String type;
    MakeCallInterface makeCallInterface;

    String strMid;
    public VisitListAdapter( Activity applicationContext, List<VisitListResponseModel.Vhn_current_visits> mResult,  MakeCallInterface makeCallInterface) {
        this.applicationContext = applicationContext;
        this.mResult = mResult;
        this.makeCallInterface =makeCallInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_visit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VisitListAdapter.ViewHolder holder, int position) {
        final  VisitListResponseModel.Vhn_current_visits current_visits=  mResult.get(position);
        holder.txt_username.setText(current_visits.getMName());
        holder.txt_picme_id.setText(current_visits.getPicmeId());
        holder.txt_list_type.setText(current_visits.getMtype());
        holder.txt_current_visit.setText(current_visits.getNextVisit());

        holder.txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(current_visits.getMMotherMobile());

            }
        });  holder.txt_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = current_visits.getMid() ;
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MotherLocationActivity.class));

            }
        });
holder.txt_list_type.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppConstants.SELECTED_MID=current_visits.getMid();
        if (current_visits.getMtype().equalsIgnoreCase("PN")){
            applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherDetailsActivity.class));

        }else if (current_visits.getMtype().equalsIgnoreCase("AN")){
            applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));

        }

    }
});
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username,txt_picme_id,txt_list_type,txt_track,txt_call, txt_current_visit;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_username =itemView.findViewById(R.id.txt_username);
            txt_picme_id =itemView.findViewById(R.id.txt_picme_id);
            txt_list_type =itemView.findViewById(R.id.txt_list_type);
            txt_track =itemView.findViewById(R.id.txt_track);
            txt_call =itemView.findViewById(R.id.txt_call);
            txt_current_visit =itemView.findViewById(R.id.txt_current_visit);
        }
    }
}