package com.example.musicplayerv1.Package;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;

public class MusicPlayService extends Service {
    private IBinder iBinder = new MediaBinder();
    MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String url = intent.getStringExtra("url");
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iBinder;
    }
    public void play(){

            mediaPlayer.start();

    }
    public void paused(){

            mediaPlayer.pause();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void seekTo(long milisec){
        mediaPlayer.seekTo(milisec,MediaPlayer.SEEK_CLOSEST);
    }
    public class MediaBinder extends Binder{
        public MusicPlayService getService(){
            return MusicPlayService.this;
        }
    }
}

