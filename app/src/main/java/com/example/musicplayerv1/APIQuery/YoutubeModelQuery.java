package com.example.musicplayerv1.APIQuery;



import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicplayerv1.Adapters.MainReAdapter;

import com.example.musicplayerv1.Interfaces.IItemPreviewClick;

import com.example.musicplayerv1.Model.Model;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YoutubeModelQuery implements Runnable {
    private ArrayList<Track> tracks;
    private ArrayList<Model> models;
    private MainReAdapter mainReAdapter;
    private Model model;
    private IItemPreviewClick iItemPreviewClick;
    private RequestQueue requestQueue;
    private static String nextPageToken;
    private boolean haveNextPage;
     String url ;

    public YoutubeModelQuery(ArrayList<Model> models, MainReAdapter mainReAdapter, Model model, IItemPreviewClick iItemPreviewClick, RequestQueue requestQueue) {
        this.models = models;
        this.mainReAdapter = mainReAdapter;
        this.model = model;
        this.iItemPreviewClick = iItemPreviewClick;
        this.requestQueue = requestQueue;
        tracks = new ArrayList<>();
        url = YoutubeConstant.SCHEME
                + YoutubeConstant.PLAYLIST_ITEM_QUERY
                + YoutubeConstant.PART
                + "snippet"
                + YoutubeConstant.PLAYLIST_ID
                + model.getIdList()
                + YoutubeConstant.KEY
                + model.getQueryId()
                + "&maxResults=50";
    }

    @Override
    public void run() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray items = response.getJSONArray("items");
                    tracks.clear();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonObject = (JSONObject) items.get(i);
                        JSONObject snippet = jsonObject.getJSONObject("snippet");
                        JSONObject resourceId = snippet.getJSONObject("resourceId");
                        String channelTitle = snippet.getString("channelTitle");
                        String trackName = snippet.getString("title");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject defaultThumb = thumbnails.getJSONObject("high");
                        String thumbnailUrl = defaultThumb.getString("url");
                        String id = resourceId.getString("videoId");
                        Track track = new Track(id, trackName, channelTitle, thumbnailUrl);
                        tracks.add(track);
                    }
                    model.setTracks(tracks);
                    model.setiItemPreviewClick(iItemPreviewClick);
                    models.add(model);
                    mainReAdapter.notifyItemInserted(models.size());

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
