package com.example.musicplayerv1.APIQuery;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicplayerv1.Adapters.StreamAdadpter;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YoutubeStreamQuery implements Runnable {
    private final ArrayList<Track> tracks;
    private final StreamAdadpter adapter;
    private final RequestQueue requestQueue;

    public YoutubeStreamQuery(ArrayList<Track> tracks, StreamAdadpter adapter, RequestQueue requestQueue){
        this.tracks = tracks;
        this.adapter = adapter;
        this.requestQueue = requestQueue;
    }
    @Override
    public void run() {
        String url = YoutubeConstant.SCHEME
                +YoutubeConstant.PLAYLIST_ITEM_QUERY
                +YoutubeConstant.PART
                +"snippet"
                +YoutubeConstant.PLAYLIST_ID
                +"PLDfKAXSi6kUZnATwAUfN6tg1dULU-7XcD"
                +YoutubeConstant.KEY
                +YoutubeConstant.API_KEY
                +"&maxResults=10"
                ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                ,url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray items = response.getJSONArray("items");
                    tracks.clear();
                    for (int i = 0; i < items.length();i++) {
                        JSONObject jsonObject = (JSONObject) items.get(i);
                        JSONObject snippet = jsonObject.getJSONObject("snippet");
                        JSONObject resourceId = snippet.getJSONObject("resourceId");
                        String trackName = snippet.getString("title");
                        String channelTitle = snippet.getString("channelTitle");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject defaultThumb = thumbnails.getJSONObject("high");
                        String thumbnailUrl = defaultThumb.getString("url");
                        String id = resourceId.getString("videoId");
                        Log.d("id",id);
                        Track track = new Track(id,trackName, channelTitle,thumbnailUrl);
                        tracks.add(track);
                    }

                    adapter.notifyItemInserted(tracks.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
