package com.example.musicplayerv1;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.Trace;

import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    public static final String CHANNEL_ID = "musicplayernoti";
    public static  NotificationManager manager;
    public static ServiceConnection SERVICECONNECTION;
    public static MusicPlayService MUSICPLAYSERVICE;
    @Override
    public void onCreate() {
        super.onCreate();
        SERVICECONNECTION = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                MUSICPLAYSERVICE = mediaBinder.getService();
                MUSICPLAYSERVICE.play();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Music Notification", NotificationManager.IMPORTANCE_DEFAULT);
             manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);

        }
    }
}
