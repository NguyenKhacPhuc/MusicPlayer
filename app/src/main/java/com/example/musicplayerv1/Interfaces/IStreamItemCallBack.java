package com.example.musicplayerv1.Interfaces;

import android.view.View;
import android.widget.ImageView;

import com.example.musicplayerv1.Model.Track;

import java.util.ArrayList;

public interface IStreamItemCallBack {
    void onStreamItemClick(int position, ArrayList<Track> tracks, ImageView v);
}
