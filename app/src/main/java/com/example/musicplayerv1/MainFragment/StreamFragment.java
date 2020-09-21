package com.example.musicplayerv1.MainFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicplayerv1.APIQuery.YoutubeStreamQuery;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Adapters.StreamAdadpter;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Interfaces.IStreamItemCallBack;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class StreamFragment extends Fragment implements IStreamItemCallBack, PopupMenu.OnMenuItemClickListener {
    RecyclerView streamRs;
    ArrayList<Track> tracks;
    StreamAdadpter streamAdadpter;
    RequestQueue requestQueue;
    PopupMenu popupMenu;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.stream_frag,container,false);
         init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        streamRs.setLayoutManager(linearLayoutManager);
        streamAdadpter = new StreamAdadpter(tracks,getContext(),this);
        streamRs.setAdapter(streamAdadpter);
        addData();
        return v;
    }
    void init(){
        streamRs = v.findViewById(R.id.stream_recycler);
        requestQueue = Volley.newRequestQueue(getContext());
        tracks = new ArrayList<>();
    }




    private void addData(){
        YoutubeStreamQuery youtubeStreamQuery = new YoutubeStreamQuery(tracks,streamAdadpter,requestQueue);
        youtubeStreamQuery.run();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStreamItemClick(int position, ArrayList<Track> tracks, ImageView v) {
        if(v == null){
            Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(), MusicPlayService.class));
            Intent intent = new Intent(getContext(), PlayMusic.class);
            intent.putExtra("tracks", (Serializable)tracks);
            intent.putExtra("position",position);
            Objects.requireNonNull(getContext()).startActivity(intent);
        }
        else {
            switch (v.getId()) {
                case R.id.fav:
                    //TODO:Change status like
                    break;
                case R.id.add_to_playlist:
                    //TODO: add to database
                    break;
                case R.id.more:
                    showOptions(v);
                    popupMenu.setOnMenuItemClickListener(this);
                    break;
                default:
                    Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(), MusicPlayService.class));
                    Intent intent = new Intent(getContext(), PlayMusic.class);
                    intent.putExtra("tracks", (Serializable) tracks);
                    intent.putExtra("position", position);
                    Objects.requireNonNull(getContext()).startActivity(intent);
                    break;
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showOptions(View v){
         popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        popupMenu.setGravity(Gravity.END);
        inflater.inflate(R.menu.toolbar_button,popupMenu.getMenu());
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }
}
