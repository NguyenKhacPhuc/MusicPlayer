package com.example.musicplayerv1.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicplayerv1.Model.Track;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface TrackDAO {
    @Query("Select * from Track")
    public List<Track> getAllTracks();
    @Query("Select * from Track where trackId  = :trackId")
    public List<Track> getATrack(String trackId);
    @Query("Delete from Track where trackId = :trackId")
    public void deleteATrack(String trackId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertATrack(Track track);
}
