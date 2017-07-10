package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Source {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("mime")
    @Expose
    public String mime;
    @SerializedName("url")
    @Expose
    public String url;
}
