package com.example.musicplayerv1.ModelLocalDataSource;

import com.example.musicplayerv1.DAO.PlaylistDAO;
import com.example.musicplayerv1.Interfaces.LocalDataSource;
import com.example.musicplayerv1.Model.Playlist;

import java.util.List;

public class PlaylistLocalDataSource implements LocalDataSource<Playlist> {
    private PlaylistDAO playlistDAO;
    public PlaylistLocalDataSource(PlaylistDAO playlistDAO){
        this.playlistDAO = playlistDAO;
    }
    @Override
    public List<Playlist> getAll() {
        return playlistDAO.getAllPlaylist();
    }

    @Override
    public Playlist getA(String id) {
        return playlistDAO.getAPlaylist(id);
    }

    @Override
    public void deleteA(String id) {
        playlistDAO.deleteAPlaylist(id);
    }

    @Override
    public void insert(Playlist playlist) {
        playlistDAO.insertAPlaylist(playlist);
    }
    public void updateTitle(String id,String replacement){
        playlistDAO.updateNameOfThePlaylist(id,replacement);
    }
    public void updateThumbnailUrl(String id, String replacement){
        playlistDAO.updateUrlThumbnailOfThePlaylist(id,replacement);
    }
    public int getTotalTrack(String id){
        return playlistDAO.selectTotalTracks(id);
    }
    public void updateTotalTrack(String id,int totalTracks){
        playlistDAO.updateTotalTracks(id,totalTracks);
    }
}
