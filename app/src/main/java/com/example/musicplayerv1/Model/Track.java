package com.example.musicplayerv1.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Track")
public class Track implements Serializable {
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
    @ColumnInfo(name = "duration")
    private long duration;
    @ColumnInfo(name = "streamLink")
    private String streamLink;
    public String getDescription() {
        return description;
    }

    public Track(String id, String artist, String trackName, String tag, String urlThumbnail, boolean isLike,String description) {
        this.id = id;
        this.artist = artist;
        this.trackName = trackName;
        this.tag = tag;
        this.urlThumbnail = urlThumbnail;
        this.isLike = isLike;
        this.description = description;

    }
    @Ignore
    public Track(String id,String trackName, String artist,String urlThumbnail){
        this.trackName = trackName;
        this.artist = artist;
        this.urlThumbnail = urlThumbnail;
        this.id = id;
    }
   @Ignore
    public Track(String id,String trackName, String artist,String description,long duration,String streamLink){
        this.trackName = trackName;
        this.artist = artist;
        this.description = description;
        this.id = id;
        this.duration = duration;
        this.streamLink = streamLink;
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



    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStreamLink() {
        return streamLink;
    }

    public void setStreamLink(String streamLink) {
        this.streamLink = streamLink;
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
                '}';
    }
}
