package com.example.musicplayerv1.Model;

public class StackModel {
    private String title;
    private String urlThumbnail;
    private String numberOfTracks;

    public StackModel(String title, String urlThumbnail,String numberOfTracks) {
        this.title = title;
        this.urlThumbnail = urlThumbnail;
        this.numberOfTracks = numberOfTracks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(String numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }
}
