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
/*
    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        MediaItem currentMovie = getItem(position);
        Toast.makeText(getContext(), currentMovie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        TextView titleView;
        ImageView posterView;
        ImageView addButton;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MediaItem currentMovie = getItem(position);
        ViewHolder viewHolder;
        View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movie_row, parent, false);
            viewHolder.titleView = (TextView)convertView.findViewById(R.id.titleView);
            viewHolder.posterView = (ImageView)convertView.findViewById(R.id.imageView);
            viewHolder.addButton = (ImageView)convertView.findViewById(R.id.addButton);
//            viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getContext(), currentMovie.getTitle() + " was added to your playlist", Toast.LENGTH_SHORT).show();
//                    //TODO: Add to playlist
//
//                }
//            });
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.titleView.setText(currentMovie.getTitle());
        String imgURL = currentMovie.getImageUrl();
        Glide.with(getContext())
                .load(imgURL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.posterView);
        return result;
    }

    public void setData(List<MediaItem> data) {
        if (movies != null) {
            movies.clear();
        }
        else {
            movies = new ArrayList<>();
        }
        if (data != null) {
            movies.addAll(data);
        }
        notifyDataSetChanged();
    }


}
*/
