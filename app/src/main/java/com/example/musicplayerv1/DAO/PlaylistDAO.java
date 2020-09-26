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
    @Query("Select totalTracks from Playlist where title = :id")
    public int selectTotalTracks(String id);
    @Query("Select * from Playlist where title  = :name")
    public Playlist getAPlaylist(String name);
    @Query("Update Playlist set totalTracks = :totalTracks where title = :id")
    public void updateTotalTracks(String id,int totalTracks);
    @Query("Delete from Playlist where title = :name")
    public void deleteAPlaylist(String name);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAPlaylist(Playlist playlist);
    @Query("Update Playlist set title = :replacement where title = :oldname ")
    public void updateNameOfThePlaylist(String oldname,String replacement);
    @Query("Update Playlist set url = :url where title = :name ")
    public void updateUrlThumbnailOfThePlaylist(String name,String url);

}
