package com.example.musicplayerv1.Model;

import com.example.musicplayerv1.Interfaces.IItemPreviewClick;

import java.util.ArrayList;

public class Model {
    private String title;
    private String description;
    private ArrayList<Track> tracks;
    private String mTag;
    private String idList;
    private IItemPreviewClick iItemPreviewClick;
    private String queryId;

    public Model(String title, String description, ArrayList<Track> tracks, String mTag,IItemPreviewClick iItemPreviewClick) {
        this.title = title;
        this.description = description;
        this.tracks = tracks;
        this.mTag = mTag;
        this.iItemPreviewClick = iItemPreviewClick;

    }

    public void setiItemPreviewClick(IItemPreviewClick iItemPreviewClick) {
        this.iItemPreviewClick = iItemPreviewClick;
    }

    public Model(String title, String description, String mTag, String idList,String queryId) {
        this.title = title;
        this.description = description;
        this.mTag = mTag;
        this.idList = idList;
        this.queryId = queryId;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getIdList() {
        return idList;
    }

    public String getmTag() {
        return mTag;
    }

    public IItemPreviewClick getiItemPreviewClick() {
        return iItemPreviewClick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}
