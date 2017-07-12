package com.example.charlynbuchanan.hellocast.model;

/**
 * Created by charlynbuchanan on 7/10/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("name")
    
    public String name;

    @SerializedName("hls")
    public String hls;

    @SerializedName("dash")
    public String dash;

    @SerializedName("mp4")
    public String mp4;

    @SerializedName("images")
    public String images;

    @SerializedName("tracks")
    public String tracks;

    @SerializedName("videos")
    public List<Video> videos = null;

    public String getHls() {
        return hls;
    }

    public String getImages() {
        return images;
    }

    public Category(String hls, String images){
        this.hls = hls;         // videoBaseUrl
        this.images = images;   //imageBaseUrl
    }
}
