package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Track {

    @SerializedName("id")
    public String id;

    @SerializedName("type")
    public String type;

    @SerializedName("subtype")
    public String subtype;

    @SerializedName("contentId")
    public String contentId;

    @SerializedName("name")
    public String name;

    @SerializedName("language")
    public String language;
}
