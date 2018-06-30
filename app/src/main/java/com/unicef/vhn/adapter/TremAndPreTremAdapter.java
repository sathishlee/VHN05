package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.InfantTermDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.TremAndPreTremResponseModel;

import java.util.List;

/**
 * Created by sathish on 3/24/2018.
 */

public class TremAndPreTremAdapter extends RecyclerView.Adapter<TremAndPreTremAdapter.ViewHolder> {
    private List<TremAndPreTremResponseModel.DelveryInfo> mResult;
    Activity applicationContext;
    String strMid;

    public TremAndPreTremAdapter(List<TremAndPreTremResponseModel.DelveryInfo> mResult, Activity applicationContext) {
        this.mResult = mResult;
        this.applicationContext = applicationContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_pre_term_infant_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TremAndPreTremResponseModel.DelveryInfo tremAndPreTremModel = mResult.get(position);
        holder.txt_infant_id.setText(tremAndPreTremModel.getDInfantId());
        holder.txt_birth_type.setText(tremAndPreTremModel.getDBirthDetails());
        holder.txt_delivery_date.setText(tremAndPreTremModel.getDdatetime());
        holder.txt_delivery_time.setText(tremAndPreTremModel.getDtime());
        holder.txt_username.setText("Mother Name:" + tremAndPreTremModel.getmName());
        strMid = tremAndPreTremModel.getMid();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = strMid;

//                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), TremAndPreTreamDetailsActivity.class));
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), InfantTermDetailsActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_infant_id, txt_birth_type, txt_delivery_date, txt_delivery_time, txt_username;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_infant_id = itemView.findViewById(R.id.txt_infant_id);
            txt_birth_type = itemView.findViewById(R.id.txt_birth_type);
            txt_delivery_date = itemView.findViewById(R.id.txt_delivery_date);
            txt_delivery_time = itemView.findViewById(R.id.txt_delivery_time);
            txt_username = itemView.findViewById(R.id.txt_username);

            /*aa*/
        }
    }
}
