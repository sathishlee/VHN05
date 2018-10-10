package com.unicef.vhn.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ANTT1Adapter extends RecyclerView.Adapter<ANTT1Adapter.ViewHolder> {

    private List<ANTT1ResponseModel.TT1_List> tt1_lists;
    Activity activity;
    MakeCallInterface makeCallInterface;
    String str_mPhoto;

    public ANTT1Adapter(List<ANTT1ResponseModel.TT1_List> tt1_lists, Activity activity, MakeCallInterface makeCallInterface) {
        this.tt1_lists = tt1_lists;
        this.activity = activity;
        this.makeCallInterface = makeCallInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tt_mothers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ANTT1ResponseModel.TT1_List tt1_list = tt1_lists.get(position);
        holder.txt_username.setText(tt1_list.getMName());
        holder.txt_picme_id.setText(tt1_list.getMPicmeId());

        str_mPhoto = tt1_list.getmPhoto();
        Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

        if (!TextUtils.isEmpty(tt1_list.getmPhoto())) {
            Picasso.with(activity)
                    .load(!TextUtils.isEmpty(tt1_list.getmPhoto()) ? Apiconstants.MOTHER_PHOTO_URL + tt1_list.getmPhoto() : "")
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

        if (tt1_list.getMMotherMobile().equalsIgnoreCase("null") || tt1_list.getMMotherMobile().length() < 10) {
            holder.img_call_mother.setVisibility(View.GONE);
        } else {
            holder.img_call_mother.setVisibility(View.VISIBLE);
        }
        holder.img_call_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCallInterface.makeCall(tt1_list.getMMotherMobile());
            }
        });

    }


    @Override
    public int getItemCount() {
        return tt1_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_picme_id, txt_username;
        ImageView img_call_mother;
        ImageView cardview_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            img_call_mother = itemView.findViewById(R.id.img_call_mother);
            cardview_image = itemView.findViewById(R.id.cardview_image);
        }
    }
}
