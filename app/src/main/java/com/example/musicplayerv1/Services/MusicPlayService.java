package com.example.musicplayerv1.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Interfaces.ICallBackBitmap;
import com.example.musicplayerv1.Interfaces.ICallBackModel;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.TestActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.musicplayerv1.App.CHANNEL_ID;
import static com.example.musicplayerv1.App.manager;

public class MusicPlayService extends Service {
    private IBinder iBinder = new MediaBinder();
    MediaPlayer mediaPlayer;
    TrackBroadcastReceiver trackBroadcastReceiver;
    ServiceConnection serviceConnection;
    String title;
    String thumbnail;
    String author;
    String url;
    ArrayList<Track> tracks;
    int position;
    MusicPlayService musicPlayService;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    ;
    private MediaSessionCompat mediaSessionCompat;
    private ICallBackBitmap iCallBackBitmap;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            url = intent.getStringExtra("streamLink");
            title = intent.getStringExtra("title");
            thumbnail = intent.getStringExtra("thumbnail");
            author = intent.getStringExtra("author");
            tracks = (ArrayList<Track>) intent.getSerializableExtra("tracks");
            position = intent.getIntExtra("position", 0);
            mediaSessionCompat = new MediaSessionCompat(this, "MediaSessionTag");

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

            Intent notificationIntent = new Intent(getApplicationContext(), PlayMusic.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.putExtra("tracks", (Serializable) tracks);
            notificationIntent.putExtra("position", position);
            notificationIntent.setAction("Start Activity");
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = new URL(thumbnail).openStream();
                        Bitmap bmp = BitmapFactory.decodeStream(in);
                        iCallBackBitmap.callBackBitmap(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            iCallBackBitmap = new ICallBackBitmap() {
                @Override
                public void callBackBitmap(Bitmap bitmap) {
                    Notification notification = new NotificationCompat.Builder(MusicPlayService.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.arrow_back_24)
                            .setContentTitle(title)
                            .setContentText(author)
                            .setLargeIcon(bitmap)
                            .addAction(R.drawable.previous_alt, "Previous", null)
                            .addAction(R.drawable.pause, "Play", null)
                            .addAction(R.drawable.next_alt, "Next", null)
                            .addAction(R.drawable.ic_baseline_favorite_24, "Heart", null)
                            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(1, 2, 3)
                                    .setMediaSession(mediaSessionCompat.getSessionToken()))
                            .setSubText("Music")
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)

                            .build();
                    startForeground(1, notification);
                }
            };


        } catch (IOException | NullPointerException e) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        }

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
        unbindService(serviceConnection);
        unregisterReceiver(trackBroadcastReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }

    public void play() {

        mediaPlayer.start();

    }

    public void paused() {

        mediaPlayer.pause();

    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void repeat(){
        mediaPlayer.setLooping(true);
    }
    public void dismissRepeat(){
        if(mediaPlayer.isLooping()){
            mediaPlayer.setLooping(false);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void seekTo(long milisec) {
        mediaPlayer.seekTo(milisec, MediaPlayer.SEEK_CLOSEST);
    }

    public class MediaBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Pass Track to Home Fragment");
        trackBroadcastReceiver = new TrackBroadcastReceiver();
        registerReceiver(trackBroadcastReceiver, intentFilter);
    }

    class TrackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(!PlayMusic.isAlive) {
                stopService(new Intent(getApplicationContext(), MusicPlayService.class));
                String channelNameStr = intent.getStringExtra("channelName");
                String trackNameStr = intent.getStringExtra("Title");
                long duration = intent.getLongExtra("duration", 0L);
                String urlThumbnail = intent.getStringExtra("urlThumbnail");
                String streamLink = intent.getStringExtra("streamLink");
                Intent intent1 = new Intent(getApplicationContext(), MusicPlayService.class);
                intent1.putExtra("streamLink", streamLink);
                intent1.putExtra("title", trackNameStr);
                intent1.putExtra("thumbnail", urlThumbnail);
                intent1.putExtra("author", channelNameStr);

                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();
                        musicPlayService.play();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                getApplicationContext().startService(intent1);
                Objects.requireNonNull(getApplicationContext()).bindService(intent1, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        }
    }

}

