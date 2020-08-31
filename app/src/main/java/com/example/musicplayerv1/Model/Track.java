package com.example.musicplayerv1.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Track")
public class Track {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "trackId")
    private  String id;
    @ColumnInfo(name = "artist")
    private String artist;
    @ColumnInfo(name = "trackName")
    private String trackName;
    @ColumnInfo(name = "tag")
    private String tag;
    @ColumnInfo(name = "urlThumbnail")
    private String urlThumbnail;
    @ColumnInfo(name="isLike")
    private boolean isLike;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name="downloaded")
    private boolean downloaded;

    public String getDescription() {
        return description;
    }
    @Ignore
    public Track(String id, String artist, String trackName, String tag, String urlThumbnail, boolean isLike,boolean downloaded, String description) {
        this.id = id;
        this.artist = artist;
        this.trackName = trackName;
        this.tag = tag;
        this.urlThumbnail = urlThumbnail;
        this.isLike = isLike;
        this.description = description;
        this.downloaded = downloaded;
    }
    public Track(String id,String trackName, String artist,String urlThumbnail){
        this.trackName = trackName;
        this.artist = artist;
        this.urlThumbnail = urlThumbnail;
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", artist='" + artist + '\'' +
                ", trackName='" + trackName + '\'' +
                ", tag='" + tag + '\'' +
                ", urlThumbnail='" + urlThumbnail + '\'' +
                ", isLike=" + isLike +
                ", description='" + description + '\'' +
                ", downloaded=" + downloaded +
                '}';
    }
}
