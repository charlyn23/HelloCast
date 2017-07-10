package com.example.charlynbuchanan.hellocast.model;

/**
 * Created by charlynbuchanan on 7/10/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("hls")
    @Expose
    public String hls;

    @SerializedName("dash")
    @Expose
    public String dash;

    @SerializedName("mp4")
    @Expose
    public String mp4;

    @SerializedName("images")
    @Expose
    public String images;

    @SerializedName("tracks")
    @Expose
    public String tracks;

    @SerializedName("videos")
    @Expose
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
