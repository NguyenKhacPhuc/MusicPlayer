package com.example.musicplayerv1.Broadcast;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Common.MyCountDownTimer;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class NotificationBroadcast extends BroadcastReceiver {
    com.example.musicplayerv1.Common.Timer timer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> keyLst;
    static boolean  isLiked;
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        sharedPreferences = context.getSharedPreferences("LIKED", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLiked = intent.getBooleanExtra("liked",false);
        Set<String> keySet = sharedPreferences.getAll().keySet();
        for(String string:keySet){
            Log.d("id",string);

        }

        String message = intent.getAction();
        assert message != null;
        switch (message){
            case "Pause":
                if (MusicPlayService.mediaPlayer.isPlaying()) {
                    MusicPlayService.mediaPlayer.pause();
                    MusicPlayService.notification.actions[1] = new Notification.Action(R.drawable.play, "play", MusicPlayService.pendingIntentPause);
                    ((NotificationManager) Objects.requireNonNull(context.getSystemService(Context.NOTIFICATION_SERVICE))).notify(1, MusicPlayService.notification);
                    MyCountDownTimer timer = new MyCountDownTimer(5000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            context.sendBroadcast(new Intent("close service"));
                        }
                    }.start();

                } else {
                    MusicPlayService.mediaPlayer.start();
                    MusicPlayService.notification.actions[1] = new Notification.Action(R.drawable.pause, "play", MusicPlayService.pendingIntentPause);
                    ((NotificationManager) Objects.requireNonNull(context.getSystemService(Context.NOTIFICATION_SERVICE))).notify(1, MusicPlayService.notification);
                }
                break;
            case "Next Song":

                MusicPlayService.position+=1;
                PlayMusic.position+=1;
                playSong(context,MusicPlayService.position);
                break;
            case "Previous Song":
                MusicPlayService.position-=1;
                PlayMusic.position-=1;
                playSong(context,MusicPlayService.position);
                break;
            case "Like":
                if(isLiked) {
                    MusicPlayService.notification.actions[3] = new Notification.Action(R.drawable.ic_baseline_favorite_border_24, "unlike", MusicPlayService.pendingIntentFav);
                    ((NotificationManager) Objects.requireNonNull(context.getSystemService(Context.NOTIFICATION_SERVICE))).notify(1, MusicPlayService.notification);
                    editor.remove(MusicPlayService.tracks.get(MusicPlayService.position).getId());
                    isLiked = false;
                }
                else{
                    MusicPlayService.notification.actions[3] = new Notification.Action(R.drawable.ic_baseline_favorite_24, "like", MusicPlayService.pendingIntentFav);
                    ((NotificationManager) Objects.requireNonNull(context.getSystemService(Context.NOTIFICATION_SERVICE))).notify(1, MusicPlayService.notification);
                    editor.putString(MusicPlayService.tracks.get(MusicPlayService.position).getId(), MusicPlayService.tracks.get(MusicPlayService.position).getTrackName());
                    Log.d("after put",MusicPlayService.tracks.get(MusicPlayService.position).getId());
                    isLiked = true;

                }
                editor.apply();
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
        intent.putExtra("tracks", (Serializable) MusicPlayService.tracks);
        intent.putExtra("position",position);
        Log.d("check", String.valueOf(MusicPlayService.tracks == null));
        context.sendBroadcast(intent);
    }
    private void playSong(final Context context, final int positionH){
        timer = new com.example.musicplayerv1.Common.Timer(MusicPlayService.tracks,context,positionH);
        MusicPlayService.mediaPlayer.release();
        QueryTrackUrl queryTrackUrl = new QueryTrackUrl(MusicPlayService.tracks.get(positionH).getId(), PlayMusic.requestQueue,context);
        queryTrackUrl.returnUrl(new IPassUrl() {
            @Override
            public void getUr(final Track url) {
                if(!PlayMusic.isAlive) {
                    sendBroadcast(context, url.getStreamLink()
                            , MusicPlayService.tracks.get(positionH).getUrlThumbnail()
                            , url.getDuration()
                            , MusicPlayService.tracks.get(positionH).getDescription()
                            , MusicPlayService.tracks.get(positionH).getTrackName()
                            , MusicPlayService.tracks.get(positionH).getArtist()
                            , positionH);

                    timer.countDown(null, 0, url.getDuration() * 1000, null, null, PlayMusic.requestQueue, true);
                }else{
                    sendBroadcast(context, url.getStreamLink()
                            , MusicPlayService.tracks.get(positionH).getUrlThumbnail()
                            , url.getDuration()
                            , MusicPlayService.tracks.get(positionH).getDescription()
                            , MusicPlayService.tracks.get(positionH).getTrackName()
                            , MusicPlayService.tracks.get(positionH).getArtist()
                            , positionH);
                }

            }
        });
    }
}
