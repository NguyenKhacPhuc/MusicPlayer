package com.example.musicplayerv1;

import android.content.Context;

import androidx.room.Database;

import com.example.musicplayerv1.Database.RootDatabase;
import com.example.musicplayerv1.ModelLocalDataSource.TrackLocalDataSource;

public class Injection {
    public static TrackLocalDataSource getProvidedTrackLocalStorage(Context context){
       return new TrackLocalDataSource(RootDatabase.getInstance(context).trackDAO());
    }
}
