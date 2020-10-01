package com.example.musicplayerv1.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Activities.PlaylistActivity;
import com.example.musicplayerv1.Adapters.PlaylistAdapter;
import com.example.musicplayerv1.Adapters.PreviewAdapter;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Interfaces.IPlaylistClick;
import com.example.musicplayerv1.Model.Container;
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

public class PlaylistFragment extends Fragment implements IItemPreviewClick, IPlaylistClick {
    RecyclerView playlist_re;
    RecyclerView recentPlay;
    ArrayList<Playlist> playlists;
    ArrayList<Track> recentTracks;
    ArrayList<Track> tracks;
    ArrayList<Container> containers;
    public static final int position = 3;
    ExecutorService executorService;
     PlaylistAdapter playlistAdapter;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.playlist_frag, container, false);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        playlist_re.setLayoutManager(linearLayoutManager);
        recentPlay.setLayoutManager(linearLayoutManager2);
        final PreviewAdapter recentPlayAdapter = new PreviewAdapter(getContext(), recentTracks, this);
         playlistAdapter = new PlaylistAdapter(playlists, getContext(), this);
        recentPlay.setAdapter(recentPlayAdapter);
        playlist_re.setAdapter(playlistAdapter);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(playlist_re);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recentTracks.clear();
                recentTracks.addAll(Injection.getProvidedTrackLocalStorage(getContext()).getAll());
                Collections.reverse(recentTracks);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recentPlayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                playlists.clear();
                playlists.addAll(Injection.getProvidedPlaylistLocalStorage(getContext()).getAll());
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playlistAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        return v;
    }

    void init() {
        playlist_re = v.findViewById(R.id.playlist_rec);
        recentPlay = v.findViewById(R.id.recent_re_playlist);
        playlists = new ArrayList<>();
        containers = new ArrayList<>();
        recentTracks = new ArrayList<>();
        tracks = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();

    }

    @Override
    public void onItemClick(int position, ArrayList<Track> tracks) {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(), MusicPlayService.class));
        Intent intent = new Intent(getContext(), PlayMusic.class);
        intent.putExtra("tracks", (Serializable) tracks);
        intent.putExtra("position", position);
        Objects.requireNonNull(getContext()).startActivity(intent);
    }

    @Override
    public void onPlaylistClick(final int position) {
        try {
            Playlist playlist = playlists.get(position);
            final String name = playlist.getTitle();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    containers.clear();
                    containers.addAll(Injection.getProvidedContainerLocalStorage(getContext()).getTracks(name));
                    for (final Container container : containers) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                tracks.add(Injection.getProvidedTrackLocalStorage(getContext()).getA(container.getTrackID()));

                            }
                        });
                    }
                    Intent intent = new Intent(getContext(), PlaylistActivity.class);
                    intent.putExtra("containers", (Serializable) containers);
                    intent.putExtra("position", position);
                    intent.putExtra("title",name);
                    startActivity(intent);

                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    ItemTouchHelper.SimpleCallback itemSimpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final String id = playlists.get(viewHolder.getAdapterPosition()).getTitle();
            playlists.remove(viewHolder.getAdapterPosition());
            playlistAdapter.notifyItemRemoved(playlists.size());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Injection.getProvidedPlaylistLocalStorage(getContext()).deleteA(id);
                }
            });
        }
    };
}
