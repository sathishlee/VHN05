package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.activity.SosMotherDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.SOSListResponse;

import java.util.List;

/**
 * Created by sathish on 3/23/2018.
 */

public class SOSListAdapter extends RecyclerView.Adapter<SOSListAdapter.ViewHolder> {
    private List<SOSListResponse.VhnAN_Mothers_List> mResult;
    Activity applicationContext;
    String strSosId, strVHNID, str, SosStatus;

    public SOSListAdapter(List<SOSListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext) {
        this.mResult = mResult;
        this.applicationContext = applicationContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_sos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SOSListResponse.VhnAN_Mothers_List SosMotherResponseModel = mResult.get(position);
        holder.txt_username.setText(SosMotherResponseModel.getMName());
        holder.txt_picme_id.setText(SosMotherResponseModel.getMPicmeId());
        holder.txt_mother_type.setText(SosMotherResponseModel.getMotherType());
        strSosId = SosMotherResponseModel.getSosId();
        strVHNID = SosMotherResponseModel.getVhnId();
        SosStatus = SosMotherResponseModel.getSosStatus();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SOS_ID = strSosId;
                AppConstants.SELECTED_MID = SosMotherResponseModel.getMid();
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), SosMotherDetailsActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_mother_type;
        LinearLayout ll_ll_mother_type;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_mother_type = itemView.findViewById(R.id.txt_mother_type);
        }
    }
}
