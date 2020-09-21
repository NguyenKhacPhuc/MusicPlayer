package com.example.musicplayerv1.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musicplayerv1.DAO.ContainerDAO;
import com.example.musicplayerv1.DAO.PlaylistDAO;
import com.example.musicplayerv1.DAO.TrackDAO;
import com.example.musicplayerv1.Model.Container;
import com.example.musicplayerv1.Model.Playlist;
import com.example.musicplayerv1.Model.Track;

@androidx.room.Database(entities = {Track.class, Playlist.class, Container.class},version = 2)
public abstract class RootDatabase extends RoomDatabase {
    private static volatile RootDatabase INSTANCE;
    public abstract TrackDAO trackDAO();
    public abstract PlaylistDAO playlistDAO();
    public abstract ContainerDAO containerDAO();

    public static RootDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RootDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RootDatabase.class, "Database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
