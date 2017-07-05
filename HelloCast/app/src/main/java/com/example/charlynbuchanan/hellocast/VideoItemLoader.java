package com.example.charlynbuchanan.hellocast;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoItemLoader extends AsyncTaskLoader<List<MediaItem>> {

    private static String json;

    VideoItemLoader(Context context, String json) {
        super(context);

        this.json = json;
    }

    //This is called in a background thread
    @Override
    public List<MediaItem> loadInBackground() {
        try {

            return VideoFetcher.buildMedia(json);
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

