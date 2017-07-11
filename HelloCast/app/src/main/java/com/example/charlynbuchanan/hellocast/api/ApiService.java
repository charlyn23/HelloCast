package com.example.charlynbuchanan.hellocast.api;

import com.example.charlynbuchanan.hellocast.model.ApiAnswerResponse;
import com.example.charlynbuchanan.hellocast.model.Category;
import com.example.charlynbuchanan.hellocast.model.Source;
import com.example.charlynbuchanan.hellocast.model.Video;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by charlynbuchanan on 6/30/17.
 */

public interface ApiService {
        @GET("gtv-videos-bucket/CastVideos/f.json")
        Call<ApiAnswerResponse> getJSON();

        @GET("gtv-videos-bucket/CastVideos/f.json")
        Call<Category>getBaseUrls();

        @GET("gtv-videos-bucket/CastVideos/f.json")
        Call<List<Source>>getVideoUrl();

        @GET("gtv-videos-bucket/CastVideos/f.json")
        Call<List<Video>>getVideos();


}
