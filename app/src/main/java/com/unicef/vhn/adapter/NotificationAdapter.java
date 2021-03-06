package com.unicef.vhn.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
import com.unicef.vhn.Interface.MakeCallInterface;
import com.unicef.vhn.R;
import com.unicef.vhn.activity.MotherDetails.ANMotherDetailsViewActivcity;
import com.unicef.vhn.activity.MotherDetails.PNMotherDetailsViewActivity;
import com.unicef.vhn.activity.MotherLocationActivity;
import com.unicef.vhn.constant.AppConstants;
import com.unicef.vhn.model.NotificationListResponseModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;



/**
 * Created by Suthishan on 20/1/2018.
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationListResponseModel.Vhn_migrated_mothers> moviesList;
    FragmentActivity activity;
    String str_mPhoto;
    MakeCallInterface makeCallInterface;

    public NotificationAdapter(List<NotificationListResponseModel.Vhn_migrated_mothers> moviesList,
                               FragmentActivity activity, MakeCallInterface makeCallInterface) {
        this.moviesList = moviesList;
        this.activity = activity;
        this.makeCallInterface = makeCallInterface;

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
//        if (movie.getClickHeremId().equalsIgnoreCase("1")) {
        if (movie.getNoteType().equalsIgnoreCase("7")) {
            holder.ll_mig_view.setVisibility(View.GONE);
            holder.ll_flash_notify_view.setVisibility(View.VISIBLE);
            holder.ll_otp_view.setVisibility(View.GONE);

        } else if (movie.getNoteType().equalsIgnoreCase("3")) {
            holder.ll_mig_view.setVisibility(View.VISIBLE);
            holder.ll_flash_notify_view.setVisibility(View.GONE);
            holder.ll_otp_view.setVisibility(View.GONE);

        } else if (movie.getNoteType().equalsIgnoreCase("6")) {
            holder.ll_mig_view.setVisibility(View.GONE);
            holder.ll_flash_notify_view.setVisibility(View.GONE);
            holder.ll_otp_view.setVisibility(View.VISIBLE);
        }
        holder.txt_flash_name.setText(movie.getMName());
        holder.txt_mig_name.setText(movie.getMName());
        holder.txt_otp_name.setText(movie.getMName());

        holder.txt_flash_message.setText(movie.getSubject());
        holder.txt_mig_message.setText(movie.getSubject());
        holder.txt_otp_message.setText(movie.getSubject());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txt_flash_notify_time.setText(timeago(movie.getNoteStartDateTime()));
            holder.txt_mig_notify_time.setText(timeago(movie.getNoteStartDateTime()));
            holder.txt_otp_notify_time.setText(timeago(movie.getNoteStartDateTime()));
        }else{
            holder.txt_flash_notify_time.setText( movie.getNoteStartDateTime());
            holder.txt_mig_notify_time.setText(movie.getNoteStartDateTime());
            holder.txt_otp_notify_time.setText(movie.getNoteStartDateTime());
        }


        /*str_mPhoto = movie.getmPhoto();
        Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

        if (!TextUtils.isEmpty(movie.getmPhoto())) {
            Log.d("mphoto-->", Apiconstants.MOTHER_PHOTO_URL + str_mPhoto);

            Picasso.with(activity)
                    .load(!TextUtils.isEmpty(movie.getmPhoto()) ? Apiconstants.MOTHER_PHOTO_URL + movie.getmPhoto() : "")
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
        }*/

        Date date = null;
        //      String dtStart = "2010-10-15T09:27:37Z";
        String dtStart = movie.getNoteStartDateTime();   ////31-03-2018 14:35:54
        //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                date = format.parse(dtStart);
                System.out.println("ReferenceTime-->" + date);
                holder.txt_flash_notify_timestamp.setReferenceTime(date.getTime());
                holder.txt_mig_notify_timestamp.setReferenceTime(date.getTime());
                holder.txt_otp_notify_timestamp.setReferenceTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            Log.e(NotificationAdapter.class.getSimpleName(),"android.os.Build.VERSION.SDK_INT -->"+android.os.Build.VERSION.SDK_INT);
        }


        //        holder.txt_flash_notify_timestamp.setReferenceTime(date.getTime());
//        holder.txt_mig_notify_timestamp.setReferenceTime(date.getTime());
        if (movie.getMMotherMobile().equalsIgnoreCase("") || movie.getMMotherMobile().length() < 10) {
            holder.txt_flash_call.setVisibility(View.GONE);
        } else {
            holder.txt_flash_call.setVisibility(View.VISIBLE);

        }
        holder.txt_flash_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(), "make call" + movie.getMMotherMobile(), Toast.LENGTH_SHORT).show();
                makeCallInterface.makeCall(movie.getMMotherMobile());

            }
        });

        holder.ll_mig_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SELECTED_MID = movie.getMid();
                activity.startActivity(new Intent(activity.getApplicationContext(), MotherLocationActivity.class));

            }
        });

        holder.ll_otp_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.SELECTED_MID = movie.getMid();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(movie.getMName());
                builder.setMessage(movie.getMessage());
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });


// Create the AlertDialog
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
        holder.ll_flash_notify_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AppConstants.SELECTED_MID = movie.getMid();
                if (movie.getMtype().equalsIgnoreCase("PN")) {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), PNMotherVisitDetailsActivity.class));

                    AppConstants.SELECTED_MID = movie.getMid();
                    AppConstants.MOTHER_PICME_ID = movie.getMPicmeId();
                    activity.startActivity(new Intent(activity.getApplicationContext(), PNMotherDetailsViewActivity.class));

                } else if (movie.getMtype().equalsIgnoreCase("AN")) {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));
                    AppConstants.SELECTED_MID = movie.getMid();
                    AppConstants.MOTHER_PICME_ID = movie.getMPicmeId();

                    activity.startActivity(new Intent(activity.getApplicationContext(), ANMotherDetailsViewActivcity.class));

                } else {
//                    activity.startActivity(new Intent(activity.getApplicationContext(), MothersDetailsActivity.class));

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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

        public TextView txt_flash_notify_time, txt_flash_message, txt_flash_name,
                txt_mig_notify_time, txt_mig_message, txt_mig_name, txt_flash_call,
                txt_otp_notify_time, txt_otp_message, txt_otp_name;
        public ImageView txt_mig_unread, txt_flash_unread, txt_otp_unread;
        //        public ImageView imageView;
        public LinearLayout ll_mig_view, ll_flash_notify_view, ll_otp_view;
        RelativeTimeTextView txt_flash_notify_timestamp, txt_mig_notify_timestamp, txt_otp_notify_timestamp;
//        ImageView cardview_image;


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
//            cardview_image = view.findViewById(R.id.cardview_image);

            txt_otp_notify_time = view.findViewById(R.id.txt_otp_notify_time);
            txt_otp_message = view.findViewById(R.id.txt_otp_message);
            txt_otp_name = view.findViewById(R.id.txt_otp_name);
            txt_otp_unread = view.findViewById(R.id.txt_otp_unread);
            txt_otp_notify_timestamp = view.findViewById(R.id.txt_otp_notify_timestamp);
            ll_otp_view = view.findViewById(R.id.ll_otp_view);

        }
    }
}

