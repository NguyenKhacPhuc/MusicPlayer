package com.example.musicplayerv1.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicplayerv1.Model.Playlist;
import com.example.musicplayerv1.Model.Track;

import java.util.List;

@Dao
public interface PlaylistDAO {
    @Query("Select * from Playlist")
    public List<Playlist> getAllPlaylist();
    @Query("Select * from Playlist where ID  = :trackId")
    public List<Playlist> getAPlaylist(String trackId);
    @Query("Delete from Playlist where ID = :trackId")
    public void deleteAPlaylist(String trackId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAPlaylist(Playlist playlist);
    @Query("Update Playlist set title = :replacement where ID = :id ")
    public void updateNameOfThePlaylist(String id,String replacement);
    @Query("Update Playlist set url = :url where ID = :id ")
    public void updateUrlThumbnailOfThePlaylist(String id,String url);
}
