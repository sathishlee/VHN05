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
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.MigrationMotherDetailsViewActivcity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.activity.PNMotherDetailsActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.MotherMigrationResponseModel;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class MotherMigrationAdapter extends RecyclerView.Adapter<MotherMigrationAdapter.ViewHolder> {

    private List<MotherMigrationResponseModel.Vhn_migrated_mothers> vhn_migrated_mothers;
    Activity activity;
    String type;
    MakeCallInterface makeCallInterface;
    String strMid, str_mPhoto;

    public MotherMigrationAdapter(List<MotherMigrationResponseModel.Vhn_migrated_mothers> vhn_migrated_mothers, Activity activity, String type, MakeCallInterface makeCallInterface) {
        this.activity = activity;
        this.vhn_migrated_mothers = vhn_migrated_mothers;
        this.type = type;
        this.makeCallInterface = makeCallInterface;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mother_migration, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MotherMigrationResponseModel.Vhn_migrated_mothers vhn_migrated_mother = vhn_migrated_mothers.get(position);
        holder.txt_username.setText(vhn_migrated_mother.getMName());
        holder.txt_picme_id.setText(vhn_migrated_mother.getMPicmeId());
        holder.txt_list_type.setText(vhn_migrated_mother.getMtype());
        holder.txt_migrated_from.setText(vhn_migrated_mother.getSubject());

        str_mPhoto = vhn_migrated_mother.getmPhoto();
        Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

        if (!TextUtils.isEmpty(vhn_migrated_mother.getmPhoto())) {
            Picasso.with(activity)
                    .load(!TextUtils.isEmpty(vhn_migrated_mother.getmPhoto()) ? Apiconstants.MOTHER_PHOTO_URL + vhn_migrated_mother.getmPhoto() : "")
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


        strMid = vhn_migrated_mother.getMid();

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
                AppConstants.SELECTED_MID = vhn_migrated_mother.getMid();
                activity.startActivity(new Intent(activity.getApplicationContext(), MotherLocationActivity.class));
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = vhn_migrated_mother.getMid();
                if (type.equalsIgnoreCase("PN")) {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), PNMotherDetailsActivity.class));
                    activity.startActivity(new Intent(activity.getApplicationContext(), MigrationMotherDetailsViewActivcity.class));


                } else if (type.equalsIgnoreCase("AN")) {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));
                    activity.startActivity(new Intent(activity.getApplicationContext(), MigrationMotherDetailsViewActivcity.class));

                } else if (type.equalsIgnoreCase("Risk")) {
                    activity.startActivity(new Intent(activity.getApplicationContext(), MigrationMotherDetailsViewActivcity.class));

//                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

                } else {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));
                    activity.startActivity(new Intent(activity.getApplicationContext(), MigrationMotherDetailsViewActivcity.class));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vhn_migrated_mothers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_list_type, txt_migrated_from;
        LinearLayout ll_ll_mother_type, ll_track_location, ll_call;
        ImageView cardview_image;



        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_list_type = itemView.findViewById(R.id.txt_list_type);
            ll_ll_mother_type = itemView.findViewById(R.id.ll_ll_mother_type);
            ll_track_location = itemView.findViewById(R.id.ll_track_location);
            ll_call = itemView.findViewById(R.id.ll_call);
            txt_migrated_from = itemView.findViewById(R.id.txt_migrated_from);
            cardview_image = itemView.findViewById(R.id.cardview_image);
        }
    }
}
