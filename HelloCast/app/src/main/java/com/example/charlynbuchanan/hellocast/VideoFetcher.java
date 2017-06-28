package com.example.charlynbuchanan.hellocast;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoFetcher {

    private static final String TAG = "VideoFetcher";
    protected static List<MediaItem> mediaList;
    private static String mimeType;
    private static String title;
    private static int duration;
    private static String videoUrl;
    private static String imageUrl;
    private static String videoUrlPrefix;
    private static String imageUrlPrefix;

    protected JSONObject parseUrl(String urlString) {
        InputStream is = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            URLConnection urlConnection = url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "iso-8859-1"), 1024);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Log.d("json", json);
            return new JSONObject(json);
        } catch (Exception e) {
            Log.d(TAG, "Failed to parse the json for media list", e);
            return null;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static List<MediaItem> buildMedia(String url) throws JSONException {
        if (null != mediaList) {
            return mediaList;
        }
        mediaList = new ArrayList<>();
        JSONObject jsonObject = new VideoFetcher().parseUrl(url);
        JSONArray categories = jsonObject.getJSONArray("categories");
        if (null != categories) {
            //here, there is only one item in categores
            JSONObject moviesBlob = categories.getJSONObject(0);
            videoUrlPrefix = moviesBlob.getString("hls");
            imageUrlPrefix = moviesBlob.getString("images");
            JSONArray videos = moviesBlob.getJSONArray("videos");
            //each item in videos is a video
            if (null != videos) {
                for (int j = 0;  j < videos.length(); j++) {
                    JSONObject videoInfo = videos.getJSONObject(j);
                    JSONArray sources = videoInfo.getJSONArray("sources");
                    if (null != sources) {
                        //isolate 0th item and fetch video link suffix and mime type
                        JSONObject spec = sources.getJSONObject(0);
                        videoUrl = videoUrlPrefix + spec.getString("url");
                        mimeType = spec.getString("mime");
                    }
                    imageUrl = imageUrlPrefix + videoInfo.getString("image-480x270");
                    title = videoInfo.getString("title");
                    duration = videoInfo.getInt("duration");
                    mediaList.add(buildMediaInfo(title, videoUrl, imageUrl, duration, mimeType));
                }
            }

        }
        for (MediaItem mediaItem : mediaList) {
            System.out.println(mediaItem.getTitle());
        }
        return mediaList;

    }

    public static MediaItem buildMediaInfo(String title, String videoUrl, String imageUrl, int duration, String mimeType){
        MediaItem mediaItem = new MediaItem();
        mediaItem.setTitle(title);
        mediaItem.setVideoUrl(videoUrl);
        mediaItem.setImageUrl(imageUrl);
        mediaItem.setDuration(duration);
        mediaItem.setMimeType(mimeType);

        return mediaItem;
    }

    public static List<MediaItem> getVideoList(){
        return mediaList;
    }
}

