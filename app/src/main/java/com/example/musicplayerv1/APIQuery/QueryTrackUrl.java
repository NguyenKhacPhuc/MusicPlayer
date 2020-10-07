package com.example.musicplayerv1.APIQuery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.Services.MusicPlayService;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class  QueryTrackUrl  {

    private String id;
    private RequestQueue requestQueue;
    private Context context;
    private YtFile ytFile;

    public QueryTrackUrl(String id, RequestQueue requestQueue, Context context) {
        this.id = id;
        this.requestQueue = requestQueue;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    public void returnUrl(final IPassUrl iPassUrl){
        String trackUrl = YoutubeConstant.YOUTUBE_LINK+id;
        new YouTubeExtractor(context) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                try {
                    ytFile = ytFiles.get(140);

                    String finalUrl = ytFile.getUrl().replace("\\", "");
                    String author = videoMeta.getTitle();
                    String title = videoMeta.getAuthor();
                    long duration = videoMeta.getVideoLength();
                    String description = videoMeta.getShortDescription();
                    String videoId = videoMeta.getVideoId();
                    Track track = new Track(videoId, title, author, description, duration, finalUrl);
                    iPassUrl.getUr(track);
                }catch (NullPointerException n){
                   context.stopService(new Intent(context, MusicPlayService.class));
                }
            }
        }.extract(trackUrl,true,true);

    }

}
