package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.ImmunizationActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.ImmunizationListResponseModel;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ImmunizationListAdapter extends RecyclerView.Adapter<ImmunizationListAdapter.ViewHolder> {

    private List<ImmunizationListResponseModel.Immunization_list> immunization_lists;
    Activity activity;
    String strMid;

    public ImmunizationListAdapter(List<ImmunizationListResponseModel.Immunization_list> immunization_lists, Activity activity) {
        this.activity = activity;
        this.immunization_lists = immunization_lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_immunization, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ImmunizationListResponseModel.Immunization_list immunization_list = immunization_lists.get(position);
        holder.txt_username.setText(immunization_list.getMName());
        holder.txt_picme_id.setText(immunization_list.getMPicmeId());
        holder.txt_dose.setText(immunization_list.getImmDoseNumber());
        strMid = immunization_list.getMid();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = immunization_list.getMid();
                Log.e(ImmunizationListAdapter.class.getSimpleName(),"  AppConstants.SELECTED_MID "+  AppConstants.SELECTED_MID );
                activity.startActivity(new Intent(activity.getApplicationContext(), ImmunizationActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return immunization_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_picme_id, txt_username, txt_dose;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_dose = itemView.findViewById(R.id.txt_dose);
        }
    }


}
