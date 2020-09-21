package com.example.musicplayerv1.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicplayerv1.Model.Container;
import com.example.musicplayerv1.Model.Track;

import java.util.List;

@Dao
public interface ContainerDAO {
    @Query("Select * from Container")
    public List<Container> getAllContainer();
    @Query("Select * from Container where playlistID  = :playlistId")
    public List<Container> getTracksOfAPlaylist(String playlistId);
    @Query("Delete from Container where trackId = :trackId")
    public void deleteATrack(String trackId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAContainer(Container container);
    @Query("Delete from Container where playlistID = :playlistId")
    public void deleteAPlaylist(String playlistId);
}
