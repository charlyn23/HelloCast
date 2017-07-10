package com.example.charlynbuchanan.hellocast.api;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
