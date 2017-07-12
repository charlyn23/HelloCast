package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Video {

    @SerializedName("subtitle")
    public String subtitle;

    @SerializedName("sources")
    public List<Source> sources = null;

    @SerializedName("thumb")
    public String thumb;

    @SerializedName("image-480x270")
    public String image480x270;

    @SerializedName("image-780x1200")
    public String image780x1200;

    @SerializedName("title")
    public String title;

    @SerializedName("studio")
    public String studio;

    @SerializedName("duration")
    public Integer duration;

    @SerializedName("tracks")
    public List<Track> tracks = null;

    public Video(String title, int duration, String imageTail){
        this.title = title;
        this.duration = duration;
        this.image480x270 = imageTail;

    }
}
