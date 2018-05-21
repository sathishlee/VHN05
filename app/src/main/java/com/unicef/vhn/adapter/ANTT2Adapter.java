package com.unicef.vhn.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.ANTT2ResponseModel;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ANTT2Adapter extends RecyclerView.Adapter<ANTT2Adapter.ViewHolder> {

    private List<ANTT2ResponseModel.TT2_List> tt2_lists;
    Activity activity;
    MakeCallInterface makeCallInterface;

    public ANTT2Adapter(List<ANTT2ResponseModel.TT2_List> tt2_lists, Activity activity, MakeCallInterface makeCallInterface) {
        this.tt2_lists = tt2_lists;
        this.activity = activity;
        this.makeCallInterface = makeCallInterface;
    }

    @Override
    public ANTT2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tt_mothers, parent, false);
        return new ANTT2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ANTT2Adapter.ViewHolder holder, int position) {
        final ANTT2ResponseModel.TT2_List tt2_list = tt2_lists.get(position);
        holder.txt_username.setText(tt2_list.getMName());
        holder.txt_picme_id.setText(tt2_list.getMPicmeId());
        holder.img_call_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(tt2_list.getMMotherMobile());
            }
        });

    }


    @Override
    public int getItemCount() {
        return tt2_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_picme_id, txt_username;
        ImageView img_call_mother;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_picme_id = itemView.findViewById(R.id.txt_picme_id);
            img_call_mother = itemView.findViewById(R.id.img_call_mother);
        }
    }
}
