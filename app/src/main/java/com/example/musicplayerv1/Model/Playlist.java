package com.example.musicplayerv1.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "Playlist")
public class Playlist {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "url")
    private String urlImage;
    @ColumnInfo(name = "totalTracks")
    private int totalTracks;
    @Ignore
    public Playlist(){
    }

    public Playlist(String title, String urlImage, int totalTracks) {
        this.title = title;
        this.urlImage = urlImage;
        this.totalTracks = totalTracks;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
