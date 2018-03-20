package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherTrackActivity;
import com.unicef.vhn.activity.PNMotherVisitListActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.PNMotherResponseModel;

import java.util.List;

/**
 * Created by sathish on 3/20/2018.
 */

public class PNMothenListAdapter  extends RecyclerView.Adapter<PNMothenListAdapter.ViewHolder> {
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    Activity applicationContext;

    public PNMothenListAdapter(List<PNMotherListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext) {
     this.applicationContext =applicationContext;
        this.mResult =mResult;
    }

    @Override
    public PNMothenListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mothers,parent,false);
        return new PNMothenListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PNMothenListAdapter.ViewHolder holder, int position) {
        final PNMotherListResponse.VhnAN_Mothers_List  pNMotherResponseModel =mResult.get(position);
        holder.txt_username.setText(pNMotherResponseModel.getMName());
        holder.txt_picme_id.setText(pNMotherResponseModel.getMPicmeId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID=pNMotherResponseModel.getMid();
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(),MotherTrackActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_username, txt_picme_id;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
        }
    }
}
