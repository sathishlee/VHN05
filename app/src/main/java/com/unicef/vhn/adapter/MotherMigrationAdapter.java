package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.activity.PNMotherDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.MotherMigrationResponseModel;
import com.unicef.vhn.model.PNMotherListResponse;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MotherMigrationAdapter extends RecyclerView.Adapter<MotherMigrationAdapter.ViewHolder> {

    private List<MotherMigrationResponseModel.Vhn_migrated_mothers>  vhn_migrated_mothers;
    Activity activity;
    String type;
    MakeCallInterface makeCallInterface;
    String strMid;

    public MotherMigrationAdapter(List<MotherMigrationResponseModel.Vhn_migrated_mothers>  vhn_migrated_mothers, Activity activity, String type, MakeCallInterface makeCallInterface) {
        this.activity =activity;
        this.vhn_migrated_mothers =vhn_migrated_mothers;
        this.type = type;
        this.makeCallInterface = makeCallInterface;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_migration,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MotherMigrationAdapter.ViewHolder holder, int position) {

        final MotherMigrationResponseModel.Vhn_migrated_mothers vhn_migrated_mother = vhn_migrated_mothers.get(position);
        holder.txt_username.setText(vhn_migrated_mother.getMName());
        holder.txt_picme_id.setText(vhn_migrated_mother.getMPicmeId());
        holder.txt_list_type.setText(vhn_migrated_mother.getMtype());
        holder.txt_migrated_from.setText(vhn_migrated_mother.getSubject());

        strMid=vhn_migrated_mother.getMid();

//        if (type.equalsIgnoreCase("PN")) {
//            holder.txt_list_type.setText(type);
//        }else if (type.equalsIgnoreCase("AN")) {
//            holder.txt_list_type.setText(type);
//        }else if (type.equalsIgnoreCase("Risk")) {
//            holder.txt_list_type.setText(type);
//        }

        holder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(vhn_migrated_mother.getMMotherMobile());
            }
        });

        holder.ll_track_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = vhn_migrated_mother.getMid() ;
                activity.startActivity(new Intent(activity.getApplicationContext(), MotherLocationActivity.class));
            }
        });


        holder.ll_ll_mother_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID=vhn_migrated_mother.getMid();
                if (type.equalsIgnoreCase("PN")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), PNMotherDetailsActivity.class));

                }else if (type.equalsIgnoreCase("AN")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

                }else if (type.equalsIgnoreCase("Risk")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

                }else{
                    activity.startActivity(new Intent(activity.getApplicationContext(),MothersDetailsActivity.class));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vhn_migrated_mothers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_username, txt_picme_id,txt_list_type, txt_migrated_from;
        LinearLayout ll_ll_mother_type, ll_track_location, ll_call;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_list_type = itemView.findViewById(R.id.txt_list_type);
            ll_ll_mother_type = itemView.findViewById(R.id.ll_ll_mother_type);
            ll_track_location = itemView.findViewById(R.id.ll_track_location);
            ll_call = itemView.findViewById(R.id.ll_call);
            txt_migrated_from = itemView.findViewById(R.id.txt_migrated_from);
        }
    }
}
