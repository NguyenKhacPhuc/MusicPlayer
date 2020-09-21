package com.example.musicplayerv1;

import android.content.Context;

import androidx.room.Database;

import com.example.musicplayerv1.DAO.ContainerDAO;
import com.example.musicplayerv1.Database.RootDatabase;
import com.example.musicplayerv1.ModelLocalDataSource.ContainerLocalDataSource;
import com.example.musicplayerv1.ModelLocalDataSource.PlaylistLocalDataSource;
import com.example.musicplayerv1.ModelLocalDataSource.TrackLocalDataSource;

public class Injection {
    public static TrackLocalDataSource getProvidedTrackLocalStorage(Context context){
       return new TrackLocalDataSource(RootDatabase.getInstance(context).trackDAO());
    }
    public static PlaylistLocalDataSource getProvidedPlaylistLocalStorage(Context context){
        return new PlaylistLocalDataSource(RootDatabase.getInstance(context).playlistDAO());
    }
    public static ContainerLocalDataSource getProvidedContainerLocalStorage(Context context){
        return new ContainerLocalDataSource(RootDatabase.getInstance(context).containerDAO()) {
        };
    }
}
