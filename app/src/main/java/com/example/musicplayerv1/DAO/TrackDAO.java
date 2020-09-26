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
    public Track getATrack(String trackId);
    @Query("Delete from Track where trackId = :trackId")
    public void deleteATrack(String trackId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertATrack(Track track);
    @Query("Update Track set isLike =:like")
    public void updateLike(boolean like);
    @Query("Update Track set downloaded =:download where trackName = :trackName")
    public void updateDownload(String trackName,boolean download);
    @Query("Update Track set streamLink =:streamLink where trackName =:trackName")
    public void updateStreamLink(String trackName,String streamLink);
    @Query("Select downloaded from Track where trackName =:trackName")
    public List<Boolean> getDownloaded(String trackName);

}
