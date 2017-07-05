package com.example.charlynbuchanan.hellocast;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class VideoFetcher  {

    private static final String TAG = "VideoFetcher";
    protected static List<MediaItem> mediaList;
    private static String mimeType;
    private static String title;
    private static int duration;
    private static String videoUrl;
    private static String imageUrl;
    private static String videoUrlPrefix;
    private static String imageUrlPrefix;
    public static String retrofitJson;
    public static String json;
    private static Retrofit retrofit;
    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";
    public static SharedPreferences jsonData;




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


    public static String getJsonString() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                // add other factories here, if needed.
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        retrofit2.Call<ResponseBody> result = apiInterface.getJSON();
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    retrofitJson = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
        return retrofitJson;
    }


    public static List<MediaItem> buildMedia(String json) throws JSONException {
        if (null != mediaList) {
            return mediaList;
        }
        mediaList = new ArrayList<>();
        retrofitJson = MainActivity.jsonData.getString("json", null);
        //Something is happening here. I've resorted to saving the string to Shared Preferences and
        //still, the string is null when it gets to the JSONObject line

//        Log.d("buildMedia", retrofitJson);
        JSONObject jsonObject = new JSONObject(MainActivity.jsonData.getString("json", null));
        JSONArray categories = jsonObject.getJSONArray("categories");
        if (null != categories) {
            //here, there is only one item in categories
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
                    MediaItem current = new MediaItem.MediaItemBuilder(title, videoUrl, imageUrl, duration, mimeType).build();
                    mediaList.add(current);
                }
            }

        }
        for (MediaItem mediaItem : mediaList) {
            System.out.println(mediaItem.getTitle());
        }
        return mediaList;

    }




//    public static MediaItem buildMediaInfo(String title, String videoUrl, String imageUrl, int duration, String mimeType){
//        MediaItem.MediaItemBuilder mediaItem = new MediaItem.MediaItemBuilder(title, videoUrl, imageUrl, duration, mimeType);
//        mediaItem.setTitle(title);
//        mediaItem.setVideoUrl(videoUrl);
//        mediaItem.setImageUrl(imageUrl);
//        mediaItem.setDuration(duration);
//        mediaItem.setMimeType(mimeType);
//
//        return mediaItem;
//    }

    public static List<MediaItem> getVideoList(){
        return mediaList;
    }


}

