package com.unicef.vhn.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.model.ANTT1ResponseModel;
import com.unicef.vhn.model.responseModel.NewRegMotherResponseModel;

import java.util.List;

public class NewMothersListAdapter extends   RecyclerView.Adapter<NewMothersListAdapter.ViewHolder>  {
    private List<NewRegMotherResponseModel.Result> resultList;
    Activity activity;
    MakeCallInterface makeCallInterface;

    public NewMothersListAdapter(List<NewRegMotherResponseModel.Result> resultList, Activity activity, MakeCallInterface makeCallInterface) {
        this.resultList = resultList;
        this.activity = activity;
        this.makeCallInterface = makeCallInterface;
    }

    @NonNull
    @Override
    public NewMothersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_mothers, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMothersListAdapter.ViewHolder holder, int position) {
        final NewRegMotherResponseModel.Result tt1_list = resultList.get(position);

       holder.txt_mothername.setText(tt1_list.getmName());
        if (tt1_list.getDatetime().equalsIgnoreCase("null")){
            holder.txt_date.setText("-");
        }
        else {
            holder.txt_date.setText(tt1_list.getDatetime());
        }

        if (tt1_list.getmMobileNumber().equalsIgnoreCase("null") || tt1_list.getmMobileNumber().length() < 10) {
            holder.img_call_mother.setVisibility(View.GONE);
        } else {
            holder.img_call_mother.setVisibility(View.VISIBLE);
        }
        holder.img_call_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallInterface.makeCall(tt1_list.getmMobileNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date, txt_mothername;
        ImageView img_call_mother;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_mothername = itemView.findViewById(R.id.txt_mothername);
            txt_date = itemView.findViewById(R.id.txt_date);
            img_call_mother = itemView.findViewById(R.id.img_call_mother);
        }
    }
}
