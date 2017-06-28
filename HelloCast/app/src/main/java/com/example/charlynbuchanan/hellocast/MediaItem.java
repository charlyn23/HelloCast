package com.example.charlynbuchanan.hellocast;

import android.os.Bundle;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class MediaItem {

    private String title;
    private String videoUrl;
    private String imageUrl;
    private int duration;
    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static final MediaItem fromBundle(Bundle wrapper) {
        if (null == wrapper) {
            return null;
        }
        MediaItem media = new MediaItem();
        media.setVideoUrl(wrapper.getString("videoUrl"));
        media.setTitle(wrapper.getString("title"));
        media.setImageUrl(wrapper.getString("imgUrl"));
        media.setDuration(wrapper.getInt("duration") * 1000);
        return media;
    }
}

