package com.example.musicplayerv1.APIQuery;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicplayerv1.Adapters.SearchModelAdapter;
import com.example.musicplayerv1.Adapters.StreamAdadpter;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YoutubeSearchQuery implements Runnable {
    private final ArrayList<Track> tracks;
    private final SearchModelAdapter adapter;
    private final RequestQueue requestQueue;
    private final String query;

    public YoutubeSearchQuery(ArrayList<Track> tracks, SearchModelAdapter adapter, RequestQueue requestQueue, String query){
        this.tracks = tracks;
        this.adapter = adapter;
        this.requestQueue = requestQueue;
        this.query = query;
    }
    @Override
    public void run() {
        String url = YoutubeConstant.SCHEME
                +YoutubeConstant.PLAYLIST_SEARCH_QUERY
                +YoutubeConstant.PART
                +"snippet"
                +YoutubeConstant.Q
                +query
                +YoutubeConstant.KEY
                +YoutubeConstant.API__SEARCH_KEY
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
                        JSONObject resourceId = jsonObject.getJSONObject("id");
                        String trackName = snippet.getString("title");
                        String channelTitle = snippet.getString("channelTitle");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject defaultThumb = thumbnails.getJSONObject("high");
                        String thumbnailUrl = defaultThumb.getString("url");
                        String id = resourceId.getString("videoId");
                        Track track = new Track(id,trackName, channelTitle,thumbnailUrl);
                        tracks.add(track);
                    }
                    adapter.notifyDataSetChanged();

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

