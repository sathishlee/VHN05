package com.example.priyan.vhn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyan.vhn.MothersDetailsActivity;
import com.example.priyan.vhn.R;
import com.example.priyan.vhn.model.Movie;

import java.util.List;

/**
 * Created by priyan on 2/6/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_mother_name);
            genre = (TextView) view.findViewById(R.id.txt_mother_id);
            year = (TextView) view.findViewById(R.id.txt_risk_status);
        }
    }


    public MoviesAdapter(FragmentActivity activity, List<Movie> moviesList) {
        this.moviesList = moviesList;
        this.context = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_mothers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), MothersDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}