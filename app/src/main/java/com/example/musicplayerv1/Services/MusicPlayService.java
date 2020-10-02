package com.example.musicplayerv1.Services;

import android.animation.Animator;
import android.app.Notification;
import android.app.NotificationManager;
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
import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Common.MyCountDownTimer;
import com.example.musicplayerv1.Interfaces.ICallBackBitmap;
import com.example.musicplayerv1.Interfaces.ICallBackModel;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.TestActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.musicplayerv1.App.CHANNEL_ID;
import static com.example.musicplayerv1.App.SERVICECONNECTION;
import static com.example.musicplayerv1.App.manager;

public class MusicPlayService extends Service {
    private IBinder iBinder = new MediaBinder();
    static MediaPlayer mediaPlayer;
    TrackBroadcastReceiver trackBroadcastReceiver;
    ServiceConnection serviceConnection;
    String title;
    String thumbnail;
    String author;
    String url;
    static ArrayList<Track> tracks;
    static int position;
    static PendingIntent pendingIntentPause;
    MusicPlayService musicPlayService;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Notification notification;
    private MediaSessionCompat mediaSessionCompat;
    private ICallBackBitmap iCallBackBitmap;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            url = intent.getStringExtra("streamLink");
            Log.d("streamLink",url);
            title = intent.getStringExtra("title");
            thumbnail = intent.getStringExtra("thumbnail");
            author = intent.getStringExtra("author");
            tracks = (ArrayList<Track>) intent.getSerializableExtra("tracks");
            position = intent.getIntExtra("position", 0);
            mediaSessionCompat = new MediaSessionCompat(this, "MediaSessionTag");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

            final Intent nextSong = new Intent(getApplicationContext(),NotificationBroadCast.class);
            nextSong.setAction("Next Song");
            final Intent pause = new Intent(getApplicationContext(),NotificationBroadCast.class);
            pause.setAction("Pause");
            final Intent fav = new Intent(getApplicationContext(),NotificationBroadCast.class);
            fav.setAction("Like");
           final Intent previous = new Intent(getApplicationContext(),NotificationBroadCast.class);
            previous.setAction("Previous Song");
           final Intent notificationIntent = new Intent(getApplicationContext(), PlayMusic.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.putExtra("tracks", (Serializable) tracks);
            notificationIntent.putExtra("position", position);
            notificationIntent.setAction("Start Activity");
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntentPause = PendingIntent.getBroadcast(this,1,pause,PendingIntent.FLAG_UPDATE_CURRENT);
            final PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this,2,nextSong,PendingIntent.FLAG_UPDATE_CURRENT);
            final PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this,3,previous,PendingIntent.FLAG_UPDATE_CURRENT);
            final PendingIntent pendingIntentFav = PendingIntent.getBroadcast(this,4,previous,PendingIntent.FLAG_UPDATE_CURRENT);
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

                     notification = new NotificationCompat.Builder(MusicPlayService.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.arrow_back_24)
                            .setContentTitle(title)
                            .setContentText(author)
                            .setLargeIcon(bitmap)
                            .addAction(R.drawable.previous_alt, "Previous", pendingIntentPrevious)
                            .addAction(R.drawable.pause, "Play", pendingIntentPause)
                            .addAction(R.drawable.next_alt, "Next", pendingIntentNext)
                            .addAction(R.drawable.ic_baseline_favorite_24, "Heart", pendingIntentFav)
                            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(1, 2, 3)
                                    .setMediaSession(mediaSessionCompat.getSessionToken()))
                            .setSubText("Music")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        stopForeground(STOP_FOREGROUND_REMOVE);
        unregisterReceiver(trackBroadcastReceiver);

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

    public void release() {
        mediaPlayer.stop();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void repeat() {
        mediaPlayer.setLooping(true);
    }

    public void dismissRepeat() {
        if (mediaPlayer.isLooping()) {
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
            if (!PlayMusic.isAlive ) {
                mediaPlayer.release();
                stopSelf();
                String channelNameStr = intent.getStringExtra("channelName");
                String trackNameStr = intent.getStringExtra("Title");
                long duration = intent.getLongExtra("duration", 0L);
                String urlThumbnail = intent.getStringExtra("urlThumbnail");
                int position = intent.getIntExtra("position",0);
                String streamLink = intent.getStringExtra("streamLink");
                tracks = (ArrayList<Track>) intent.getSerializableExtra("tracks");
                Intent intent1 = new Intent(MusicPlayService.this, MusicPlayService.class);
                intent1.putExtra("streamLink", streamLink);
                intent1.putExtra("title", trackNameStr);
                intent1.putExtra("thumbnail", urlThumbnail);
                intent1.putExtra("author", channelNameStr);
                intent1.putExtra("tracks", (Serializable) tracks);
                intent1.putExtra("position",position);
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
     public static class NotificationBroadCast extends BroadcastReceiver {
         com.example.musicplayerv1.Common.Timer timer;

         @Override
         public void onReceive(final Context context, Intent intent) {

             String message = intent.getAction();
             Log.d("tag", "run here");
             switch (message){
                 case "Pause":
                     if (mediaPlayer.isPlaying()) {
                     mediaPlayer.pause();

                 } else {
                     mediaPlayer.start();
                 }
                     break;
                 case "Next Song":
                   position+=1;

                    playSong(context,position);
                     break;
                 case "Previous Song":
                     position-=1;
                     playSong(context,position);
                     break;
                 default:
                     break;
             }
         }
         private void sendBroadcast(Context context,String streamLink, String urlThumbnail, long duration, String description, String trackName, String artist,int position) {
             Intent intent = new Intent();
             intent.setAction("Pass Track to Home Fragment");
             intent.putExtra("streamLink", streamLink);
             intent.putExtra("urlThumbnail", urlThumbnail);
             intent.putExtra("duration", duration);
             intent.putExtra("description", description);
             intent.putExtra("channelName", artist);
             intent.putExtra("Title", trackName);
             intent.putExtra("tracks", (Serializable) tracks);
             intent.putExtra("position",position);
             Log.d("check", String.valueOf(tracks == null));
             context.sendBroadcast(intent);
         }
         private void playSong(final Context context, final int positionH){
             timer = new com.example.musicplayerv1.Common.Timer(tracks,context,positionH);
             mediaPlayer.release();
             context.stopService(new Intent(context,MusicPlayService.class));
             QueryTrackUrl queryTrackUrl = new QueryTrackUrl(tracks.get(positionH).getId(),PlayMusic.requestQueue,context);
             queryTrackUrl.returnUrl(new IPassUrl() {
                 @Override
                 public void getUr(final Track url) {
                     sendBroadcast(context,url.getStreamLink()
                             , tracks.get(positionH).getUrlThumbnail()
                             , url.getDuration()
                             , tracks.get(positionH).getDescription()
                             , tracks.get(positionH).getTrackName()
                             , tracks.get(positionH).getArtist()
                                , positionH);

                     timer.countDown(null,0,url.getDuration()*1000,null,null,PlayMusic.requestQueue,true);

                 }
             });
         }
     }
}

