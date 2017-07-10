package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Track {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("subtype")
    @Expose
    public String subtype;
    @SerializedName("contentId")
    @Expose
    public String contentId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("language")
    @Expose
    public String language;
}
