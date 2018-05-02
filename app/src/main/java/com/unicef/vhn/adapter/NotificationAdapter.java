package com.unicef.vhn.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.unicef.vhn.R;
import com.unicef.vhn.model.NotificationListResponseModel;

import java.util.List;



/**
 * Created by Suthishan on 20/1/2018.
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private List<NotificationListResponseModel.Vhn_migrated_mothers> moviesList;
    FragmentActivity activity;
    public NotificationAdapter(List<NotificationListResponseModel.Vhn_migrated_mothers> moviesList, FragmentActivity activity) {
        this.moviesList = moviesList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_migration, parent, false);

        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationListResponseModel.Vhn_migrated_mothers movie = moviesList.get(position);

        holder.txt_flash_name.setText(movie.getMName());
        holder.txt_mig_name.setText(movie.getMName());

        holder.txt_flash_message.setText(movie.getSubject());
        holder.txt_mig_message.setText(movie.getSubject());

        holder.txt_flash_notify_time.setText(movie.getNoteStartDateTime());
        holder.txt_mig_notify_time.setText(movie.getNoteStartDateTime());

        holder.txt_flash_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(),"make call"+ movie.getMMotherMobile(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_flash_notify_time, txt_flash_message,txt_flash_name,txt_mig_notify_time,
                txt_mig_message,txt_mig_name,txt_flash_call;
//        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            txt_flash_name = view.findViewById(R.id.txt_flash_name);
            txt_flash_message=view.findViewById(R.id.txt_flash_message);
            txt_flash_notify_time = view.findViewById(R.id.txt_flash_notify_time);
            txt_flash_call=view.findViewById(R.id.txt_flash_call);

            txt_mig_name=view.findViewById(R.id.txt_mig_name);
            txt_mig_message=view.findViewById(R.id.txt_mig_message);
            txt_mig_notify_time=view.findViewById(R.id.txt_mig_name);
        }
    }
}

