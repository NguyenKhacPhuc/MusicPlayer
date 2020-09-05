package com.example.musicplayerv1.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.musicplayerv1.Activities.PlayMusic;

import java.io.IOException;

import static com.example.musicplayerv1.App.CHANNEL_ID;

public class MusicPlayService extends Service {
    private IBinder iBinder = new MediaBinder();
    MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
        String url = intent.getStringExtra("streamLink");
        String title = intent.getStringExtra("title");
        Intent notificationIntent = new Intent(this, PlayMusic.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .build();
        mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            startForeground(1,notification);

        } catch (IOException | NullPointerException e) {
                mediaPlayer.release();
        }

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }
    public void play(){

            mediaPlayer.start();

    }
    public void paused(){

            mediaPlayer.pause();

    }
    public int getDuration(){
        return  mediaPlayer.getDuration();
    }
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
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

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

