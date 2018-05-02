package com.unicef.vhn.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MapsActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.activity.MothersDetailsActivity;
import com.unicef.vhn.activity.PNMotherDetailsActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.interactor.NotificationListResponseModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * Created by Suthishan on 20/1/2018.
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
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
//ll
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationListResponseModel.Vhn_migrated_mothers movie = moviesList.get(position);
        if (movie.getClickHeremId().equalsIgnoreCase("1")) {
            holder.ll_mig_view.setVisibility(View.GONE);
            holder.ll_flash_notify_view.setVisibility(View.VISIBLE);
        } else  {
            holder.ll_mig_view.setVisibility(View.VISIBLE);
            holder.ll_flash_notify_view.setVisibility(View.GONE);
        }
        holder.txt_flash_name.setText(movie.getMName());
        holder.txt_mig_name.setText(movie.getMName());

        holder.txt_flash_message.setText(movie.getSubject());
        holder.txt_mig_message.setText(movie.getSubject());

        holder.txt_flash_notify_time.setText(timeago(movie.getNoteStartDateTime()));
        holder.txt_mig_notify_time.setText(timeago(movie.getNoteStartDateTime()));
        Date date = null;
        //      String dtStart = "2010-10-15T09:27:37Z";
        String dtStart = movie.getNoteStartDateTime();   ////31-03-2018 14:35:54
        //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
             date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txt_flash_notify_timestamp.setReferenceTime(date.getTime());
        holder.txt_mig_notify_timestamp.setReferenceTime(date.getTime());
        holder.txt_flash_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(), "make call" + movie.getMMotherMobile(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.ll_mig_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
                AppConstants.SELECTED_MID = movie.getMid() ;
                activity.startActivity(new Intent(activity.getApplicationContext(), MotherLocationActivity.class));

            }
        });

        holder.ll_flash_notify_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstants.SELECTED_MID=movie.getMid();
                if (movie.getMtype().equalsIgnoreCase("PN")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), PNMotherDetailsActivity.class));

                }else if (movie.getMtype().equalsIgnoreCase("AN")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

                }/*else if (movie.getMtype().equalsIgnoreCase("Risk")){
                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

                }*/else{
                    activity.startActivity(new Intent(activity.getApplicationContext(),MothersDetailsActivity.class));

                }

               /* Uri gmmIntentUri = Uri.parse("google.navigation:q="+pNMotherResponseModel.getMLatitude()+","+pNMotherResponseModel.getMLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                    applicationContext. startActivity(mapIntent);
                }*/
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private CharSequence timeago(String noteStartDateTime) {
        SimpleDateFormat sdf = null;
        CharSequence ago = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");      //31-03-2018 14:35:54
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                long time = sdf.parse(noteStartDateTime).getTime();
                long now = System.currentTimeMillis();
                ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                Log.e("time --->", String.valueOf(time));
                Log.e("currentt ime --->", String.valueOf(now));
                Log.e("ago time --->", String.valueOf(ago));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
           return ago;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_flash_notify_time, txt_flash_message, txt_flash_name, txt_mig_notify_time, txt_mig_message, txt_mig_name, txt_flash_call;
        public ImageView txt_mig_unread,txt_flash_unread;
        //        public ImageView imageView;
        public LinearLayout ll_mig_view, ll_flash_notify_view;
        RelativeTimeTextView txt_flash_notify_timestamp,txt_mig_notify_timestamp;
        @SuppressLint("WrongViewCast")
        public ViewHolder(View view) {
            super(view);
            txt_flash_name = view.findViewById(R.id.txt_flash_name);
            txt_flash_message = view.findViewById(R.id.txt_flash_message);
            txt_flash_notify_time = view.findViewById(R.id.txt_flash_notify_time);
            txt_flash_call = view.findViewById(R.id.txt_flash_call);

            txt_mig_name = view.findViewById(R.id.txt_mig_name);
            txt_mig_message = view.findViewById(R.id.txt_mig_message);
            txt_mig_notify_time = view.findViewById(R.id.txt_mig_notify_time);

            txt_flash_notify_timestamp = view.findViewById(R.id.txt_flash_notify_timestamp);
            txt_mig_notify_timestamp = view.findViewById(R.id.txt_mig_notify_timestamp);

            ll_mig_view = view.findViewById(R.id.ll_mig_view);
            ll_flash_notify_view = view.findViewById(R.id.ll_flash_notify_view);

            txt_mig_unread = view.findViewById(R.id.txt_mig_unread);
            txt_flash_unread = view.findViewById(R.id.txt_flash_unread);

        }
    }
}

