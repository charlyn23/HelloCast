package com.example.charlynbuchanan.hellocast;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import static java.security.AccessController.getContext;

/**
 * Created by charlynbuchanan on 7/6/17.
 */

public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView titleView;
    private final ImageView posterView;
    private final ImageView addButton;
    public CastSession castSession;


    private MediaItem movie;
    private Context context;


    public MovieHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;


        //setup UI widgets
        this.titleView = (TextView) itemView.findViewById(R.id.titleView);
        this.posterView = (ImageView) itemView.findViewById(R.id.imageView);
        this.addButton = (ImageView) itemView.findViewById(R.id.addButton);

        //set onclick
        itemView.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if (this.movie != null) {
            Toast.makeText(context, "Clicked on " + movie.getTitle(), Toast.LENGTH_SHORT).show();

            MediaItem current = movie;
            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString("title", movie.getTitle());
            movieMetadata.addImage(new WebImage((Uri.parse(movie.getImageUrl()))));
            movieMetadata.putString("videoUrl", movie.getVideoUrl());
            movieMetadata.putInt("duration", movie.getDuration());

            final CastContext castContext = CastContext.getSharedInstance(context);
            SessionManager sessionManager = castContext.getSessionManager();
            CastOptionsProvider castOptionsProvider = new CastOptionsProvider();
            castOptionsProvider.getCastOptions(context);

            castSession = sessionManager.getCurrentCastSession();
            if (castSession != null) {
                MediaInfo mediaInfo = new MediaInfo.Builder(current.getVideoUrl())
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setMetadata(movieMetadata)
                        .setStreamDuration(current.getDuration() * 1000)
                        .setContentType("videos/mp4")
                        .build();
                RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                remoteMediaClient.load(mediaInfo);
            }
            else {
                Toast.makeText(context, "This is where the media player would be.\nPress cast button for now", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
