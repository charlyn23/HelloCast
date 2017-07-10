//package com.example.charlynbuchanan.hellocast;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.example.charlynbuchanan.hellocast.api.ApiService;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.ResponseBody;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//
///**
// * Created by charlynbuchanan on 6/28/17.
// */
//
//public class VideoFetcher  {
//
//    private static final String TAG = "VideoFetcher";
//    protected static List<MediaItem> mediaList;
//    private static String mimeType;
//    private static String title;
//    private static int duration;
//    private static String videoUrl;
//    private static String imageUrl;
//    private static String videoUrlPrefix;
//    private static String imageUrlPrefix;
//    public static String retrofitJson;
//    public static String json;
//    private static Retrofit retrofit;
//    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";
//    public static SharedPreferences jsonData = MainActivity.jsonData;
//
//
//
//    public static String fetchData() throws IOException {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                // add other factories here, if needed.
//                .build();
//
//        ApiService apiInterface = retrofit.create(ApiService.class);
//        retrofit2.Call<ResponseBody> call = apiInterface.getJSON();
//
//            Response<ResponseBody> result = call.execute();
//            json = result.body().string();
//
//        return json;
//    }
//
//
//
//
//    public static List<MediaItem> buildMedia(String json) throws JSONException, IOException {
//        if (null != mediaList) {
//            return mediaList;
//        }
//
//        mediaList = new ArrayList<>();
//        VideoFetcher.json = json;
//        Log.d("jsonData", json);
//
//        JSONObject jsonObject = new JSONObject(json);
//        JSONArray categories = jsonObject.getJSONArray("categories");
//        if (null != categories) {
//            //here, there is only one item in categories
//            JSONObject moviesBlob = categories.getJSONObject(0);
//            videoUrlPrefix = moviesBlob.getString("hls");
//            imageUrlPrefix = moviesBlob.getString("images");
//            JSONArray videos = moviesBlob.getJSONArray("videos");
//            //each item in videos is a video
//            if (null != videos) {
//                for (int j = 0;  j < videos.length(); j++) {
//                    JSONObject videoInfo = videos.getJSONObject(j);
//                    JSONArray sources = videoInfo.getJSONArray("sources");
//                    if (null != sources) {
//                        //isolate 0th item and fetch video link suffix and mime type
//                        JSONObject spec = sources.getJSONObject(0);
//                        videoUrl = videoUrlPrefix + spec.getString("url");
//                        mimeType = spec.getString("mime");
//                    }
//                    imageUrl = imageUrlPrefix + videoInfo.getString("image-480x270");
//                    title = videoInfo.getString("title");
//                    duration = videoInfo.getInt("duration");
//                    MediaItem current = new MediaItem.MediaItemBuilder(title, videoUrl, imageUrl, duration, mimeType).build();
//                    mediaList.add(current);
//                }
//            }
//
//        }
//        for (MediaItem mediaItem : mediaList) {
//            System.out.println(mediaItem.getTitle());
//        }
//        return mediaList;
//
//    }
//
//
//
//
////    public static MediaItem buildMediaInfo(String title, String videoUrl, String imageUrl, int duration, String mimeType){
////        MediaItem.MediaItemBuilder mediaItem = new MediaItem.MediaItemBuilder(title, videoUrl, imageUrl, duration, mimeType);
////        mediaItem.setTitle(title);
////        mediaItem.setVideoUrl(videoUrl);
////        mediaItem.setImageUrl(imageUrl);
////        mediaItem.setDuration(duration);
////        mediaItem.setMimeType(mimeType);
////
////        return mediaItem;
////    }
//
//    public static List<MediaItem> getVideoList(){
//        return mediaList;
//    }
//
//
//}
//
