package com.example.musicplayerv1.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Playlist")
public class Playlist {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    private  int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "url")
    private String urlImage;

    public Playlist(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
