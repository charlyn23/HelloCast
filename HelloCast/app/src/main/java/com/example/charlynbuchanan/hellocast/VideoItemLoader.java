package com.example.charlynbuchanan.hellocast;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoItemLoader extends AsyncTaskLoader<List<MediaItem>> {

    private final String mUrl;

    public VideoItemLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    //This is called in a background thread
    @Override
    public List<MediaItem> loadInBackground() {
        try {
            return VideoFetcher.buildMedia(mUrl);
        } catch (Exception e) {
            Log.e("VideoItemLoader", "Failed to fetch media data", e);
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("onStartLoading", "LOADING");
        forceLoad();
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}

