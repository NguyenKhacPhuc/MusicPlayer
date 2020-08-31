package com.example.musicplayerv1.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Container")
public class Container {
    @ColumnInfo(name = "trackID")
    private String trackID;
    @ColumnInfo(name="PlaylistID")
    private String playlistID;

    public Container(String trackID, String playlistID) {
        this.trackID = trackID;
        this.playlistID = playlistID;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(String playlistID) {
        this.playlistID = playlistID;
    }
}
