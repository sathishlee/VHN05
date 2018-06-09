package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.activity.SosMotherDetailsActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.SOSListResponse;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.List;

/**
 * Created by sathish on 3/23/2018.
 */

public class SOSListAdapter extends RecyclerView.Adapter<SOSListAdapter.ViewHolder> {
    private List<SOSListResponse.VhnAN_Mothers_List> mResult;
    Activity applicationContext;
    String strSosId, strVHNID, str_mPhoto, SosStatus;

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

        str_mPhoto = SosMotherResponseModel.getmPhoto();
        Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

        if (!TextUtils.isEmpty(SosMotherResponseModel.getmPhoto())) {
            Picasso.with(applicationContext)
                    .load(!TextUtils.isEmpty(SosMotherResponseModel.getmPhoto()) ? Apiconstants.MOTHER_PHOTO_URL + SosMotherResponseModel.getmPhoto() : "")
                    .placeholder(R.drawable.girl)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new RoundedTransformation(90, 4))
                    .error(R.drawable.girl)
                    .into(holder.cardview_image);
        } else {
            holder.cardview_image.setImageResource(R.drawable.girl);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
//                AppConstants.SOS_ID = strSosId;
=======
>>>>>>> origin/new
                AppConstants.SOS_ID = SosMotherResponseModel.getSosId();
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
        ImageView cardview_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_mother_type = itemView.findViewById(R.id.txt_mother_type);
            cardview_image = itemView.findViewById(R.id.cardview_image);
        }
    }
}
