package com.example.charlynbuchanan.hellocast.api;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by charlynbuchanan on 6/30/17.
 */

public interface ApiService {
        @GET("gtv-videos-bucket/CastVideos/f.json")
        Call<ResponseBody> getJSON();
}
