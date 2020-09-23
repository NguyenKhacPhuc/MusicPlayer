package com.example.musicplayerv1.Common;


import android.content.Context;
import android.content.Intent;


import android.os.CountDownTimer;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;

import com.example.musicplayerv1.APIQuery.QueryTrackUrl;

import com.example.musicplayerv1.Interfaces.IDurationCallBack;
import com.example.musicplayerv1.Interfaces.IPassUrl;

import com.example.musicplayerv1.Model.Track;


import java.io.Serializable;
import java.util.ArrayList;


public class Timer {
    long temp;
    long currentTemp;
    IDurationCallBack iDurationCallBack;
    MyCountDownTimer countDownTimer;
    ArrayList<Track> tempTracks;
    int index;
    Context context;

    public Timer(final ArrayList<Track> tracks
            , final Context context, int index) {
        this.tempTracks = tracks;
        this.context = context;
        this.index = index;
    }

    public void countDown(final SeekBar seekbar
            , final long currentProgress
            , final long duration
            , final TextView start
            , final TextView end
            , final RequestQueue requestQueue
            , boolean isNotRewind

    ) {
        if (isNotRewind) {
            ++index;
        }


        seekbar.setProgress((int) (currentProgress / 1000));
        temp = duration;
        currentTemp = currentProgress;

        start.setText(Converting.convertToSecond(currentTemp));
        end.setText(Converting.convertToSecond(temp));
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new MyCountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int current = seekbar.getProgress();
                temp = temp - 1000;
                currentTemp = currentTemp + 1000;
                seekbar.setProgress(current + 1);
                String strDuration = Converting.convertToSecond(temp);
                String strCurrent = Converting.convertToSecond(currentTemp);
                start.setText(strCurrent);
                end.setText(strDuration);
            }

            @Override
            public void onFinish() {

                if (!tempTracks.isEmpty() && context != null && index < tempTracks.size()) {
                    final Track track = tempTracks.get(index);
                    if (track.isDownloaded()) {
                        sendBroadcast(track.getStreamLink(), track.getUrlThumbnail(), track.getDuration(), track.getDescription(), track.getTrackName(), track.getArtist());
                    }
                    QueryTrackUrl queryTrackUrl = new QueryTrackUrl(track.getId(), requestQueue, context);
                    queryTrackUrl.returnUrl(new IPassUrl() {
                        @Override
                        public void getUr(Track url) {
                            sendBroadcast(url.getStreamLink(), track.getUrlThumbnail(), url.getDuration(), track.getDescription(), track.getTrackName(), track.getArtist());
                            iDurationCallBack.passDuration(url.getDuration(), index);

                        }
                    });
                }
                iDurationCallBack = new IDurationCallBack() {
                    @Override
                    public void passDuration(long duration, int index) {
                        long milDuration = duration * 1000;
                        int max = (int) duration;
                        seekbar.setMax(max);
//                            temp= milDuration;
//                            currentTemp = 0;
//                            current = 0;
//                        start.setText(Converting.convertToSecond(currentTemp));
//                        end.setText(Converting.convertToSecond(temp));
//                        countDownTimer.setMillisInFuture(milDuration);
//                        countDownTimer.setCountdownInterval(1000);
//                        countDownTimer.start();
                        countDown(seekbar, 0, milDuration, start, end, requestQueue, true);

                    }
                };
            }
        }.start();
    }

    private void sendBroadcast(String streamLink, String urlThumbnail, long duration, String description, String trackName, String artist) {
        Intent intent = new Intent();
        intent.setAction("Pass Track to Home Fragment");
        intent.putExtra("streamLink", streamLink);
        intent.putExtra("urlThumbnail", urlThumbnail);
        intent.putExtra("duration", duration);
        intent.putExtra("description", description);
        intent.putExtra("channelName", artist);
        intent.putExtra("Title", trackName);
        intent.putExtra("tracks", (Serializable) tempTracks);
        context.sendBroadcast(intent);
    }

    public void cancel() {
        countDownTimer.cancel();
    }
}


