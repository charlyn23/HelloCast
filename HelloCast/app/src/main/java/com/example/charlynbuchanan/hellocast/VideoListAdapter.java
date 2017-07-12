package com.example.charlynbuchanan.hellocast;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.charlynbuchanan.hellocast.model.MediaItem;
import com.example.charlynbuchanan.hellocast.model.Movie;
import com.example.charlynbuchanan.hellocast.ui.CustomItemClickListener;
import com.example.charlynbuchanan.hellocast.ui.MovieHolder;

import java.util.ArrayList;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<MovieHolder> {

    private final ArrayList<MediaItem> movies;
    private  Context context;
    private int resource;
    private CustomItemClickListener onClickListener;


    public VideoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MediaItem> movies, CustomItemClickListener onClickListener) {
        //intialize adapter
        this.movies = movies;
        this.context = context;
        this.resource = resource;
        this.onClickListener = onClickListener;
    }


    @Override
    public MovieHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.resource, parent, false);
        final MovieHolder movieHolder = new MovieHolder(this.context, view, onClickListener);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClick(view, movieHolder.getAdapterPosition());
            }
        });
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        MediaItem movie = this.movies.get(position);
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }
}

