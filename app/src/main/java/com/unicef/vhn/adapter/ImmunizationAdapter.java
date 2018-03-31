package com.unicef.vhn.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicef.vhn.R;
import com.unicef.vhn.model.ImmunizationListResponseModel;

import java.util.List;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class ImmunizationAdapter extends RecyclerView.Adapter<ImmunizationAdapter.ViewHolder> {

    private List<ImmunizationListResponseModel.Immunization_list> immunization_lists;
    Activity activity;

    public ImmunizationAdapter(List<ImmunizationListResponseModel.Immunization_list>immunization_lists, Activity activity){
        this.activity = activity;
        this.immunization_lists = immunization_lists;
    }

    @Override
    public ImmunizationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_immunization,parent,false);
        return new ImmunizationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImmunizationAdapter.ViewHolder holder, int position) {
        final ImmunizationListResponseModel.Immunization_list immunization_list = immunization_lists.get(position);

        holder.txt_due_date.setText(immunization_list.getImmDueDate());
        holder.txt_provided_date.setText(immunization_list.getImmCarePovidedDate());
        holder.txt_opv_given.setText(immunization_list.getImmOpvStatus());
        holder.txt_prentavalent_given.setText(immunization_list.getImmPentanvalentStatus());
        holder.txt_rota_given.setText(immunization_list.getImmRotaStatus());
        holder.txt_ipv_given.setText(immunization_list.getImmIpvStatus());


    }

    @Override
    public int getItemCount() {
        return immunization_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_due_date,txt_provided_date, txt_opv_given, txt_prentavalent_given, txt_rota_given, txt_ipv_given;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_due_date = itemView.findViewById(R.id.txt_due_date);
            txt_provided_date = itemView.findViewById(R.id.txt_provided_date);
            txt_opv_given = itemView.findViewById(R.id.txt_opv_given);
            txt_prentavalent_given = itemView.findViewById(R.id.txt_prentavalent_given);
            txt_rota_given = itemView.findViewById(R.id.txt_rota_given);
            txt_ipv_given = itemView.findViewById(R.id.txt_ipv_given);
        }
    }
}
