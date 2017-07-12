package com.example.charlynbuchanan.hellocast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by charlynbuchanan on 7/10/17.
 */

public class Movie {

    @SerializedName("categories")
    public List<Category> categories = null;

}
