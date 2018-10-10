package com.unicef.vhn.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.ANMotherDetailsViewActivcity;
import com.unicef.vhn.activity.MotherDetails.PNMotherDetailsViewActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.SosAlertListActivity;
import com.unicef.vhn.activity.SosMotherDetailsActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.model.SOSListResponse;
import com.unicef.vhn.utiltiy.CheckNetwork;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.List;

/**
 * Created by sathish on 3/23/2018.
 */

public class SOSListAdapter extends RecyclerView.Adapter<SOSListAdapter.ViewHolder> {
    private List<SOSListResponse.VhnAN_Mothers_List> mResult;
    Activity applicationContext;
    String strSosId, strVHNID, str_mPhoto, SosStatus;
    CheckNetwork checkNetwork;
    MakeCallInterface makeCallInterface;

    public SOSListAdapter(List<SOSListResponse.VhnAN_Mothers_List> mResult, Activity applicationContext, MakeCallInterface makeCallInterface) {
        this.mResult = mResult;
        this.applicationContext = applicationContext;
        checkNetwork = new CheckNetwork(applicationContext);
        this.makeCallInterface = makeCallInterface;

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
                final String type = SosMotherResponseModel.getMotherType();
                if (checkNetwork.isNetworkAvailable()) {
//                AppConstants.SOS_ID = strSosId;
                    AppConstants.SOS_ID = SosMotherResponseModel.getSosId();
                    AppConstants.SELECTED_MID = SosMotherResponseModel.getMid();
                    AppConstants.MOTHER_PICME_ID = SosMotherResponseModel.getMPicmeId();
                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), SosMotherDetailsActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                    builder.setTitle("You can't close this alert!");
                    builder.setMessage("You are in offline, please connect internet.");
                    // Add the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            if (type.equalsIgnoreCase("PN")) {
                                AppConstants.SELECTED_MID = SosMotherResponseModel.getMid();
                                AppConstants.MOTHER_PICME_ID = SosMotherResponseModel.getMPicmeId();
//                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherVisitDetailsActivity.class));
                                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), PNMotherDetailsViewActivity.class));

                            } else if (type.equalsIgnoreCase("AN")) {
                                AppConstants.SELECTED_MID = SosMotherResponseModel.getMid();
                                AppConstants.MOTHER_PICME_ID = SosMotherResponseModel.getMPicmeId();

//                    applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MothersDetailsActivity.class));
                                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), ANMotherDetailsViewActivcity.class));

                            }
                            dialog.dismiss();
                        }
                    });


// Create the AlertDialog
                    AlertDialog dialog = builder.create();

                    dialog.show();

                }
            }
        });


        holder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(SosMotherResponseModel.getmMotherMobile());
            }
        });


        holder.ll_track_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = SosMotherResponseModel.getMid();
                applicationContext.startActivity(new Intent(applicationContext.getApplicationContext(), MotherLocationActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_mother_type;
        LinearLayout ll_ll_mother_type, ll_call, ll_track_location;
        ImageView cardview_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_mother_type = itemView.findViewById(R.id.txt_mother_type);
            cardview_image = itemView.findViewById(R.id.cardview_image);
            ll_ll_mother_type = itemView.findViewById(R.id.ll_ll_mother_type);
            ll_call = itemView.findViewById(R.id.ll_call);
            ll_track_location = itemView.findViewById(R.id.ll_track_location);
        }
    }
}
