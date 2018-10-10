package com.unicef.vhn.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.constant.Apiconstants;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.PNHBNCDueListModel;
import com.unicef.vhn.utiltiy.RoundedTransformation;

import java.util.List;

/**
 * Created by sathish on 3/29/2018.
 */

public class PNHBNCDueAdapter extends RecyclerView.Adapter<PNHBNCDueAdapter.ViewHolder> {
    private List<PNHBNCDueListModel.VPNHBNC_List> tt1_lists;
    Activity activity;
    MakeCallInterface makeCallInterface;
    String str_mPhoto;


    public PNHBNCDueAdapter(List<PNHBNCDueListModel.VPNHBNC_List> tt1_lists, Activity activity, MakeCallInterface makeCallInterface) {
        this.tt1_lists = tt1_lists;
        this.activity = activity;
        this.makeCallInterface = makeCallInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pnhbnc_due_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PNHBNCDueListModel.VPNHBNC_List tt1_list = tt1_lists.get(position);
        holder.txt_username.setText(tt1_list.getMotherName());
        holder.txt_picme_id.setText(tt1_list.getPicmeId());
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

        String strunvisit1 = null;
        String strunvisit2 = null;
        String strunvisit3 = null;
        String strunvisit4 = null;
        String strunvisit5 = null;
        String strunvisit6 = null;
        String strunvisit7 = null;
        if (tt1_list.getVisit1() == null) {
            strunvisit1 = "1";
        }
        if (tt1_list.getVisit2() == null) {
            strunvisit2 = "2";
        }
        if (tt1_list.getVisit3() == null) {
            strunvisit3 = "3";
        }
        if (tt1_list.getVisit4() == null) {
            strunvisit4 = "4";
        }
        if (tt1_list.getVisit5() == null) {
            strunvisit5 = "5";
        }
        if (tt1_list.getVisit6() == null) {

            strunvisit6 = "6";
        }
        if (tt1_list.getVisit7() == null) {
            strunvisit7 = "";
        }
        holder.txt_visit_list.setText(strunvisit1 + "," + strunvisit2 + "," + strunvisit3 + "," + strunvisit4 + "," + strunvisit5 + "," + strunvisit6 + "," + strunvisit7);
        if (tt1_list.getMobile().equalsIgnoreCase("null") || tt1_list.getMobile().length() < 10) {
            holder.img_call_mother.setVisibility(View.GONE);
        } else {
            holder.img_call_mother.setVisibility(View.VISIBLE);
        }
        holder.img_call_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(tt1_list.getMobile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tt1_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username, txt_picme_id, txt_visit_list;
        ImageView img_call_mother;
        ImageView cardview_image;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            txt_visit_list = itemView.findViewById(R.id.txt_visit_list);
            img_call_mother = itemView.findViewById(R.id.img_call_mother);
            cardview_image = itemView.findViewById(R.id.cardview_image);

        }
    }
}
