package com.unicef.vhn.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherList.PushNotificationListActivity;
import com.unicef.vhn.model.PNMotherListResponse;
import com.unicef.vhn.realmDbModel.PushNotificationListRealmModel;

import java.util.List;

import io.realm.internal.Context;

public class PushNotificationListAdapter extends  RecyclerView.Adapter<PushNotificationListAdapter.ViewHolder> implements Filterable {

    List<PushNotificationListRealmModel> notifyList;
    Activity activity;
    public PushNotificationListAdapter(List<PushNotificationListRealmModel> notifyList, Activity activity ) {
        this.activity=activity;
        this.notifyList=notifyList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,message,intime;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.txt_title);
            message=itemView.findViewById(R.id.txt_body);
            intime=itemView.findViewById(R.id.txt_intime);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_push_notify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PushNotificationListRealmModel pNMotherResponseModel = notifyList.get(position);
        Log.e("Notify Adapter","Tilte--->"+position+"-->"+pNMotherResponseModel.getTitle());
        Log.e("Notify Adapter","Body--->"+position+"-->"+pNMotherResponseModel.getBody());
        Log.e("Notify Adapter","Intime--->"+position+"-->"+pNMotherResponseModel.getIntime());

        holder.title.setText(pNMotherResponseModel.getTitle());
        holder.message.setText(pNMotherResponseModel.getBody());
        holder.intime.setText(pNMotherResponseModel.getIntime());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
