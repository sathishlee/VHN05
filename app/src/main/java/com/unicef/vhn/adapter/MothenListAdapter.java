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
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;

import java.util.List;

/**
 * Created by sathish on 3/20/2018.
 */

public class MothenListAdapter extends RecyclerView.Adapter<MothenListAdapter.ViewHolder> {
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    Activity applicationContext;

    public MothenListAdapter(List<PNMotherListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext) {
     this.applicationContext =applicationContext;
        this.mResult =mResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mothers_an,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PNMotherListResponse.VhnAN_Mothers_List  pNMotherResponseModel =mResult.get(position);
        holder.txt_username.setText(pNMotherResponseModel.getMName());
        holder.txt_picme_id.setText(pNMotherResponseModel.getMPicmeId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID=pNMotherResponseModel.getMid();
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(),MothersDetailsActivity.class));

               /* Uri gmmIntentUri = Uri.parse("google.navigation:q="+pNMotherResponseModel.getMLatitude()+","+pNMotherResponseModel.getMLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                    applicationContext. startActivity(mapIntent);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_username, txt_picme_id;
        LinearLayout ll_img_view_location;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
//            ll_img_view_location = itemView.findViewById(R.id.ll_img_view_location);
        }
    }
}
