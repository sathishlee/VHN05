package com.unicef.vhn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.unicef.vhn.AppConstent;
import com.unicef.vhn.MothersDetailsActivity;
import com.unicef.vhn.PNMotherDetailsActivity;
import com.unicef.vhn.R;
import com.unicef.vhn.model.Movie;

import java.util.List;

/**
 * Created by priyan on 2/6/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Movie> moviesList;
    private int lastPosition = -1;
    private  String fromActivity;

    public MoviesAdapter(Context applicationContext, List<Movie> movieList, String anMother) {
        this.context= (FragmentActivity) applicationContext;
        this.moviesList = movieList;
        this.fromActivity =anMother;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_mother_name);
            genre = (TextView) view.findViewById(R.id.txt_mother_id);
            year = (TextView) view.findViewById(R.id.txt_risk_status);
        }
    }


    public MoviesAdapter(FragmentActivity activity, List<Movie> moviesList,String fromActivity) {
        this.moviesList = moviesList;
        this.context = activity;
        this.fromActivity =fromActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mothers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromActivity.equalsIgnoreCase("PNMothers")){
                    AppConstent.PNMotherDetails="PNMothers";
                    context.startActivity(new Intent(context.getApplicationContext(), PNMotherDetailsActivity.class));

                }
                else if(fromActivity.equalsIgnoreCase("Immunization")){
                    AppConstent.PNMotherDetails="Immunization";

                    context.startActivity(new Intent(context.getApplicationContext(), PNMotherDetailsActivity.class));

                }else {
                    context.startActivity(new Intent(context.getApplicationContext(), MothersDetailsActivity.class));
                }
            }
        });

//                        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View itemView, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.accelerate_decelerate_interpolator);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}