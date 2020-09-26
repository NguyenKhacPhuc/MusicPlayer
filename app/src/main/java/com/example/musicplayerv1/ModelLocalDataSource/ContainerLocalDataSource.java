package com.example.musicplayerv1.ModelLocalDataSource;

import com.example.musicplayerv1.DAO.ContainerDAO;
import com.example.musicplayerv1.Interfaces.LocalDataSource;
import com.example.musicplayerv1.Model.Container;
import com.example.musicplayerv1.Model.Track;

import java.util.List;

public class ContainerLocalDataSource implements LocalDataSource<Container> {
    private ContainerDAO containerDAO;
    public ContainerLocalDataSource(ContainerDAO containerDAO){
        this.containerDAO = containerDAO;
    }
    @Override
    public List<Container> getAll() {
        return null;
    }

    @Override
    public Container getA(String id) {
        return null;
    }

    public List<Container> getTracks(String id){
        return containerDAO.getTracksOfAPlaylist(id);
    }

    @Override
    public void deleteA(String id) {
            containerDAO.deleteATrack(id);
    }
    @Override
    public void insert(Container container) {
        containerDAO.insertAContainer(container);
    }
    public void deleteAContainer(String trackId, String playlistId){
        containerDAO.deleteAContainer(trackId,playlistId);
    }
}
