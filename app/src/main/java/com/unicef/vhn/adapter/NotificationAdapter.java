package com.unicef.vhn.adapter;/*
package com.unicef.vhn.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.unicef.vhn.R;
import com.unicef.vhn.model.NotificationModel;

import java.util.List;


*/
/**
 * Created by Suthishan on 20/1/2018.
 *//*


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private List<NotificationModel> moviesList;
    FragmentActivity activity;
    public NotificationAdapter(List<NotificationModel> moviesList, FragmentActivity activity) {
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
        NotificationModel movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
//        Picasso.with(activity).load(movie.getImg_id()).into(holder.imageView);
        holder.imageView.setImageDrawable(activity.getResources().getDrawable(movie.getImg_id()));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, year, genre;
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            imageView = (ImageView) view.findViewById(R.id.cardview_image);
        }
    }
}
*/
