package com.example.musicplayerv1.Model;

import java.util.ArrayList;

public class Playlist {
    private  String id;
    private String title;
    private ArrayList<Track> tracks;
    private String urlImage;

    public Playlist(String id){
        this.id = id;
    }
    public Playlist(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
