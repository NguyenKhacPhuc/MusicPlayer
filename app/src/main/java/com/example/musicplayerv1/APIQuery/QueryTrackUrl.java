package com.example.musicplayerv1.APIQuery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;

import com.android.volley.RequestQueue;
import com.example.musicplayerv1.Constant;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class QueryTrackUrl {

    private String id;
    private RequestQueue requestQueue;
    private Context context;

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
                YtFile ytFile = ytFiles.get(140);
                String finalUrl = ytFile.getUrl().replace("\\","");
                iPassUrl.getUr(finalUrl);
            }
        }.extract(trackUrl,true,true);

    }
}
