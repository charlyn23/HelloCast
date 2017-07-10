package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Video {

    @SerializedName("subtitle")
    @Expose
    public String subtitle;

    @SerializedName("sources")
    @Expose
    public List<Source> sources = null;

    @SerializedName("thumb")
    @Expose
    public String thumb;

    @SerializedName("image-480x270")
    @Expose
    public String image480x270;

    @SerializedName("image-780x1200")
    @Expose
    public String image780x1200;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("studio")
    @Expose
    public String studio;

    @SerializedName("duration")
    @Expose
    public Integer duration;

    @SerializedName("tracks")
    @Expose
    public List<Track> tracks = null;
}
