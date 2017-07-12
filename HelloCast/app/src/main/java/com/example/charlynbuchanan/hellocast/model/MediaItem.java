package com.example.charlynbuchanan.hellocast.model;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class MediaItem  {

    private final String title;
    private final String videoUrl;
    private final String imageUrl;
    private final int duration;
    private final String mimeType;

    public MediaItem(MediaItemBuilder mediaItemBuilder){
        this.title = mediaItemBuilder.title;
        this.videoUrl = mediaItemBuilder.videoUrl;
        this.imageUrl = mediaItemBuilder.imageUrl;
        this.duration = mediaItemBuilder.duration;
        this.mimeType = mediaItemBuilder.mimeType;
    }

    public String getTitle(){
        return title;
    }

    public String getVideoUrl(){
         return videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDuration(){
        return duration;
    }
    public String getMimeType(){
        return mimeType;
    }

    public MediaItem getMediaItem(){
        return new MediaItem.MediaItemBuilder(title, videoUrl, imageUrl, duration, mimeType).build();
    }


    public static class MediaItemBuilder {
        private String title;
        private  String videoUrl;
        private  String imageUrl;
        private  int duration;
        private  String mimeType;

        public MediaItemBuilder(String title, String videoUrl, String imageUrl, int duration, String mimeType){
            this.title = title;
            this.videoUrl = videoUrl;
            this.imageUrl = imageUrl;
            this.duration = duration;
            this.mimeType = mimeType;
        }

        public MediaItemBuilder title(String title){
            this.title = title;
            return this;
        }

        public MediaItemBuilder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public MediaItemBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }
        public MediaItemBuilder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public MediaItem build(){
            return new MediaItem(this);
        }
    }


}

