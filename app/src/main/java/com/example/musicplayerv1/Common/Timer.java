package com.example.musicplayerv1.Common;


import android.content.Context;
import android.content.Intent;


import android.os.CountDownTimer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;

import com.example.musicplayerv1.APIQuery.QueryTrackUrl;

import com.example.musicplayerv1.Interfaces.IDurationCallBack;
import com.example.musicplayerv1.Interfaces.IPassUrl;

import com.example.musicplayerv1.Model.Track;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Timer {
    long temp;
    long currentTemp;
    IDurationCallBack iDurationCallBack;
    MyCountDownTimer countDownTimer;
    ArrayList<Track> tempTracks;
    Context context;


    public Timer(final ArrayList<Track> tracks
            , final Context context) {
        this.tempTracks = tracks;
        this.context = context;

    }

    public void countDown(final SeekBar seekbar
            , final long currentProgress
            , final long duration
            , final TextView start
            , final TextView end
            , final RequestQueue requestQueue

    ) {

        seekbar.setProgress((int) (currentProgress/1000));
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
                int current =seekbar.getProgress();
                temp = temp - 1000;
                currentTemp = currentTemp + 1000;
                seekbar.setProgress(current+1);
                String strDuration = Converting.convertToSecond(temp);
                String strCurrent = Converting.convertToSecond(currentTemp);
                start.setText(strCurrent);
                end.setText(strDuration);
            }

            @Override
            public void onFinish() {
                if (!tempTracks.isEmpty() && context != null) {
                    Collections.shuffle(tempTracks);
                    Random ran = new Random();
                    int position = ran.nextInt(tempTracks.size());
                    final Track track = tempTracks.get(position);
                    QueryTrackUrl queryTrackUrl = new QueryTrackUrl(track.getId(),requestQueue,context);

                    queryTrackUrl.returnUrl(new IPassUrl() {
                        @Override
                        public void getUr(Track url) {
                            Intent intent = new Intent();
                            intent.setAction("Pass Track to Home Fragment");
                            intent.putExtra("streamLink", url.getStreamLink());
                            intent.putExtra("urlThumbnail", track.getUrlThumbnail());
                            intent.putExtra("duration", url.getDuration());
                            intent.putExtra("description", url.getDescription());
                            intent.putExtra("channelName", url.getTrackName());
                            intent.putExtra("Title", url.getArtist());
                            iDurationCallBack.passDuration(url.getDuration());
                            context.sendBroadcast(intent);

                        }
                    });
                }
                iDurationCallBack = new IDurationCallBack() {
                    @Override
                    public void passDuration(long duration) {
                            long milDuration = duration *1000;
                            int max = (int)duration;
                            Toast.makeText(context,String.valueOf(max),Toast.LENGTH_SHORT).show();
                        seekbar.setMax(max);
//                            temp= milDuration;
//                            currentTemp = 0;
//                            current = 0;
//                        start.setText(Converting.convertToSecond(currentTemp));
//                        end.setText(Converting.convertToSecond(temp));
//                        countDownTimer.setMillisInFuture(milDuration);
//                        countDownTimer.setCountdownInterval(1000);
//                        countDownTimer.start();
                        countDown(seekbar,0,milDuration,start,end,requestQueue);

                    }
                };
            }
        }.start();
    }

}

