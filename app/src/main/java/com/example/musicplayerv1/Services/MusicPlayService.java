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


import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Broadcast.NotificationBroadcast;

import com.example.musicplayerv1.Interfaces.ICallBackBitmap;

import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.musicplayerv1.App.CHANNEL_ID;


public class MusicPlayService extends Service {
    private IBinder iBinder = new MediaBinder();
    public static MediaPlayer mediaPlayer;
    TrackBroadcastReceiver trackBroadcastReceiver;
    ServiceConnection serviceConnection;
    StopBroadCast stopBroadCast;
    String title;
    String thumbnail;
    String author;
    public static PendingIntent pendingIntentFav;
    String url;
    public static ArrayList<Track> tracks;
    public static int position;
    public static PendingIntent pendingIntentPause;
    MusicPlayService musicPlayService;
    boolean isLiked;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public  static Notification notification;
    int likeIc;
    private MediaSessionCompat mediaSessionCompat;
    private ICallBackBitmap iCallBackBitmap;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            isLiked = intent.getBooleanExtra("liked",false);
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
             play();

            final Intent nextSong = new Intent(getApplicationContext(), NotificationBroadcast.class);
            nextSong.setAction("Next Song");
            final Intent pause = new Intent(getApplicationContext(),NotificationBroadcast.class);
            pause.setAction("Pause");
            final Intent fav = new Intent(getApplicationContext(),NotificationBroadcast.class);
            fav.setAction("Like");
            fav.putExtra("liked",isLiked);
           final Intent previous = new Intent(getApplicationContext(),NotificationBroadcast.class);
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
          pendingIntentFav = PendingIntent.getBroadcast(this,4,fav,PendingIntent.FLAG_UPDATE_CURRENT);
          if(isLiked){
              likeIc = R.drawable.ic_baseline_favorite_24;
          }
          else{
              likeIc = R.drawable.ic_baseline_favorite_border_24;
          }
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
                            .addAction(likeIc, "Heart", pendingIntentFav)
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
        stopForeground(true);
        unregisterReceiver(trackBroadcastReceiver);
        unregisterReceiver(stopBroadCast);

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
        mediaPlayer.release();
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
    public void selfUnbind(){
        getApplicationContext().unbindService(serviceConnection);
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
        IntentFilter stopFilter = new IntentFilter();
        stopFilter.addAction("close service");
        trackBroadcastReceiver = new TrackBroadcastReceiver();
        stopBroadCast = new StopBroadCast();
        registerReceiver(trackBroadcastReceiver, intentFilter);
        registerReceiver(stopBroadCast,stopFilter);

    }

    public class TrackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
                stopForeground(true);
                mediaPlayer.release();
                stopSelf();
                String channelNameStr = intent.getStringExtra("channelName");
                String trackNameStr = intent.getStringExtra("Title");
                long duration = intent.getLongExtra("duration", 0L);
                String urlThumbnail = intent.getStringExtra("urlThumbnail");
                int position = intent.getIntExtra("position", 0);
                String streamLink = intent.getStringExtra("streamLink");
                tracks = (ArrayList<Track>) intent.getSerializableExtra("tracks");
                Intent intent1 = new Intent(getApplicationContext() ,MusicPlayService.class);
                intent1.putExtra("streamLink", streamLink);
                intent1.putExtra("title", trackNameStr);
                intent1.putExtra("thumbnail", urlThumbnail);
                intent1.putExtra("author", channelNameStr);
                intent1.putExtra("tracks", (Serializable) tracks);
                intent1.putExtra("position", position);
                Toast.makeText(getApplicationContext(), "Run here", Toast.LENGTH_SHORT).show();
                getApplicationContext().startService(intent1);
        }
    }
    class StopBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                stopForeground(true);
                stopSelf();
        }
    }
}

