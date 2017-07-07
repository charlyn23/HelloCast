package com.example.charlynbuchanan.hellocast;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<MovieHolder> {

    private final ArrayList<MediaItem> movies;
    private  Context context;
    private int resource;


    public VideoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MediaItem> movies) {
        //intialize adapter
        this.movies = movies;
        this.context = context;
        this.resource = resource;
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.resource, parent, false);
        return new MovieHolder(this.context, view);    }

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

