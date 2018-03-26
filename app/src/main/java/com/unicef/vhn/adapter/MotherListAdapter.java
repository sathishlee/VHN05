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
import com.unicef.vhn.activity.PNMotherDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;

import java.util.List;

/**
 * Created by sathish on 3/20/2018.
 */

public class MotherListAdapter extends RecyclerView.Adapter<MotherListAdapter.ViewHolder> {
    private List<PNMotherListResponse.VhnAN_Mothers_List> mResult ;
    Activity applicationContext;
    String type;

    public MotherListAdapter(List<PNMotherListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext, String type) {
     this.applicationContext =applicationContext;
        this.mResult =mResult;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mothers_an,parent,false);
        return new ViewHolder(view);
    }
/*new*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PNMotherListResponse.VhnAN_Mothers_List  pNMotherResponseModel =mResult.get(position);
        holder.txt_username.setText(pNMotherResponseModel.getMName());
        holder.txt_picme_id.setText(pNMotherResponseModel.getMPicmeId());
//        if (pNMotherResponseModel.getMotherType().equalsIgnoreCase("")){
            if (type.equalsIgnoreCase("PN")) {
                holder.txt_list_type.setText(type);
            }else if (type.equalsIgnoreCase("AN")) {
                holder.txt_list_type.setText(type);
            }else if (type.equalsIgnoreCase("Risk")) {
                holder.txt_list_type.setText(type);
            }
//        }else {
//            holder.txt_list_type.setText(pNMotherResponseModel.getMotherType());
//        }
        /*
        if (type.equalsIgnoreCase("PN")) {
            holder.txt_list_type.setText(type);
        }else if (type.equalsIgnoreCase("AN")) {
            holder.txt_list_type.setText(type);
        }else if (type.equalsIgnoreCase("Risk")) {
            holder.txt_list_type.setText(type);
        }else{
            holder.ll_ll_mother_type.setVisibility(View.GONE);
        }*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID=pNMotherResponseModel.getMid();
                if (type.equalsIgnoreCase("PN")){
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherDetailsActivity.class));

                }else if (type.equalsIgnoreCase("AN")){
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));

                }else if (type.equalsIgnoreCase("Risk")){
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));

                }else{
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(),MothersDetailsActivity.class));

                }

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
        TextView txt_username, txt_picme_id,txt_list_type;
        LinearLayout ll_ll_mother_type;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_list_type = itemView.findViewById(R.id.txt_list_type);
            ll_ll_mother_type = itemView.findViewById(R.id.ll_ll_mother_type);
        }
    }
}
