package com.example.musicplayerv1.ModelLocalDataSource;

import androidx.room.Query;

import com.example.musicplayerv1.DAO.TrackDAO;
import com.example.musicplayerv1.Interfaces.LocalDataSource;
import com.example.musicplayerv1.Model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackLocalDataSource implements LocalDataSource<Track> {
    private TrackDAO trackDAO;
    public TrackLocalDataSource(TrackDAO trackDAO){
        this.trackDAO = trackDAO;
    }
    @Override
    public List<Track> getAll() {
        return trackDAO.getAllTracks();
    }

    @Override
    public Track getA(String id) {
        return trackDAO.getATrack(id);
    }

    @Override
    public void deleteA(String id) {
        trackDAO.deleteATrack(id);
    }

    @Override
    public void insert(Track track) {
        trackDAO.insertATrack(track);
    }
    public void updateLike(boolean like){
        trackDAO.updateLike(like);
    }
    public void updateStreamLink(String trackName,String streamLink){
        trackDAO.updateStreamLink(trackName,streamLink);
    }
    public void updateDownload(String trackName,boolean download){
        trackDAO.updateDownload(trackName,download);
    }
    public List<Boolean> getDownloaded(String trackName){
        return trackDAO.getDownloaded(trackName);
    }
}
