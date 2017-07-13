package com.example.charlynbuchanan.hellocast.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charlynbuchanan.hellocast.R;
import com.example.charlynbuchanan.hellocast.cast.CastOptionsProvider;
import com.example.charlynbuchanan.hellocast.model.MediaItem;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

/**
 * Created by charlynbuchanan on 7/6/17.
 */

public class MovieHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final ImageView posterView;
    private final ImageView addButton;
    public CastSession castSession;
    private MediaItem movie;
    private Context context;


    public MovieHolder(final Context context, final View itemView) {
        super(itemView);
        this.context = context;


        //setup UI widgets*
        this.titleView = (TextView) itemView.findViewById(R.id.titleView);
        this.posterView = (ImageView) itemView.findViewById(R.id.imageView);
        this.addButton = (ImageView) itemView.findViewById(R.id.addButton);

        //create a video queue via addButton.
        //*intentionally an ImageView (Button within RecyclerView caused app to crash)
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaItem current = movie;
                Toast.makeText(context, current.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void bindMovie(MediaItem mediaItem) {
        this.movie = mediaItem;
        this.titleView.setText(mediaItem.getTitle());
        String imageUrl = mediaItem.getImageUrl();
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(posterView);
    }

}
