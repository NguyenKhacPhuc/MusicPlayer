package com.example.musicplayerv1.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Adapters.PlaylistAdapter;
import com.example.musicplayerv1.Adapters.PreviewAdapter;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Model.Playlist;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.ModelLocalDataSource.PlaylistLocalDataSource;
import com.example.musicplayerv1.ModelLocalDataSource.TrackLocalDataSource;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistFragment extends Fragment implements IItemPreviewClick {
    RecyclerView playlist_re;
    RecyclerView recentPlay;
    ArrayList<Playlist> playlists;
    ArrayList<Track> tracks;
    PlaylistLocalDataSource playlistLocalDataSource;
    TrackLocalDataSource trackLocalDataSource;
    ExecutorService executorService;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.playlist_frag,container,false);
         init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        playlist_re.setLayoutManager(linearLayoutManager);
        recentPlay.setLayoutManager(linearLayoutManager2);
//        playlists = (ArrayList<Playlist>) playlistLocalDataSource.getAll();
        final PreviewAdapter previewAdapter = new PreviewAdapter(getContext(),tracks,this);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(playlists,getContext());
        recentPlay.setAdapter(previewAdapter);
       executorService.execute(new Runnable() {
           @Override
           public void run() {

               tracks.addAll(Injection.getProvidedTrackLocalStorage(getContext()).getAll());
               Collections.reverse(tracks);
               Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       previewAdapter.notifyDataSetChanged();
                   }
               });

           }
       });

        return  v;
    }
    void init(){
        playlist_re = v.findViewById(R.id.playlist_rec);
        recentPlay = v.findViewById(R.id.recent_re_playlist);
        playlists = new ArrayList<>();
        tracks = new ArrayList<>();
//        playlistLocalDataSource = Injection.getProvidedPlaylistLocalStorage(getContext());
        executorService = Executors.newSingleThreadExecutor();

    }

    @Override
    public void onItemClick(int position, ArrayList<Track> tracks) {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(), MusicPlayService.class));

        Intent intent = new Intent(getContext(), PlayMusic.class);
        intent.putExtra("tracks", (Serializable)tracks);
        intent.putExtra("position",position);
        Objects.requireNonNull(getContext()).startActivity(intent);
    }
}
