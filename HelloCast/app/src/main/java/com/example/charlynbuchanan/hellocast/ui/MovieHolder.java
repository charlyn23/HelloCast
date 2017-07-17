package com.example.charlynbuchanan.hellocast.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v7.media.RemotePlaybackClient;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charlynbuchanan.hellocast.MainActivity;
import com.example.charlynbuchanan.hellocast.R;
import com.example.charlynbuchanan.hellocast.cast.CastOptionsProvider;
import com.example.charlynbuchanan.hellocast.model.MediaItem;
import com.example.charlynbuchanan.hellocast.playlist.QueueDataProvider;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.images.WebImage;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    MediaInfo mediaInfo;
    MediaMetadata movieMetadata;
    private ArrayList<MediaQueueItem> playlist;
    MediaQueueItem[] queueArray;
    private CastContext castContext;
    private SessionManager sessionManager;

    public MovieHolder(final Context context, final View itemView) {
        super(itemView);
        this.context = context;

        //setup UI widgets*
        this.titleView = (TextView) itemView.findViewById(R.id.titleView);
        this.posterView = (ImageView) itemView.findViewById(R.id.imageView);
        this.addButton = (ImageView) itemView.findViewById(R.id.addButton);
        this.addButton.bringToFront();

        //create a video queue via addButton.
        //*intentionally an ImageView (Button within RecyclerView caused app to crash)
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaItem current = movie;
                movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                movieMetadata.putString("title", movie.getTitle());
                movieMetadata.addImage(new WebImage((Uri.parse(movie.getImageUrl()))));
                movieMetadata.putString("videoUrl", movie.getVideoUrl());
                movieMetadata.putInt("duration", movie.getDuration());

                mediaInfo = new MediaInfo.Builder(movie.getVideoUrl())
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setMetadata(movieMetadata)
                        .setStreamDuration(movie.getDuration() * 1000)
                        .setContentType("videos/mp4")
                        .build();
                MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo)
                        .setAutoplay(true)
                        .build();
                if (playlist == null || playlist.size() == 0) {
                    playlist = new ArrayList<>();
                }
                playlist.add(queueItem);

                    castContext = CastContext.getSharedInstance(context);
                    sessionManager = castContext.getSessionManager();
                    castSession = sessionManager.getCurrentCastSession();
                if (castSession == null) {
                    Toast.makeText(context, "Activate Cast to build your playlist", Toast.LENGTH_SHORT).show();
                }
                else {
                    RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                    remoteMediaClient.queueLoad(convertToArray(playlist), 0, 0, null);
                    Toast.makeText(context, "Playlist has " + String.valueOf(playlist.size()) + " videos", Toast.LENGTH_SHORT).show();
                    QueueDataProvider.getInstance(context);
                }


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

    public MediaQueueItem[] convertToArray(ArrayList<MediaQueueItem> queueItems) {
        if (queueArray == null || queueItems.size() == 0) {
            queueArray = new MediaQueueItem[playlist.size()];
        }
        int index = 0;
        for (MediaQueueItem item : playlist) {
            queueArray[index] = item;
            index++;
        }
        return queueArray;
    }

}
