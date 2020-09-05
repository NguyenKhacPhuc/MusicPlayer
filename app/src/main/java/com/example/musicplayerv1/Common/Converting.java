package com.example.musicplayerv1.Common;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class Converting {
    public static String convertToSecond(Long duration){
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return hms;
    }
}
