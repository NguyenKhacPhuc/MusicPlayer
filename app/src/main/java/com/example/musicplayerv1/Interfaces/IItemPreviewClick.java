package com.example.musicplayerv1.Interfaces;

import com.example.musicplayerv1.Model.Track;

import java.util.ArrayList;

public interface IItemPreviewClick {
    void onItemClick(int position, ArrayList<Track> tracks);
}
