package com.example.charlynbuchanan.hellocast;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
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

public  class VideoListAdapter extends ArrayAdapter<MediaItem> implements View.OnClickListener {

    private static ArrayList<MediaItem> movies;
    private static Context context;


    public VideoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MediaItem> movies) {
        super(context, R.layout.movie_row, movies);
        this.movies = movies;
        this.context = context;
    }

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

