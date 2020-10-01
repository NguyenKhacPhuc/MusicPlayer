package com.example.musicplayerv1.SubFragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Adapters.CardStackAdapter;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IPlaylistAddingClick;
import com.example.musicplayerv1.MainFragment.StreamFragment;
import com.example.musicplayerv1.Model.Container;
import com.example.musicplayerv1.Model.Playlist;

import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firestore.v1.TargetOrBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.StatementEvent;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, IPlaylistAddingClick {
    View v;
    RecyclerView cardStackView;
    LinearLayoutManager cardStackLayoutManager;
    CardStackAdapter cardStackAdapter;
    Container container;
    ExecutorService executorService;
    Button create;
    Dialog dialog;
    Track track;
    ArrayList<Playlist> models;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.bottom_sheet_add_playlist,container,false);
        initView();

        create.setOnClickListener(this);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        addData();
        return v;
    }

    private void addData() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                models.clear();
                models.addAll(Injection.getProvidedPlaylistLocalStorage(getContext()).getAll());
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardStackView.setAdapter(cardStackAdapter);
                    }
                });
            }
        });
    }

    void initView(){
        cardStackView = v.findViewById(R.id.stack_view);
        create = v.findViewById(R.id.create_btn);
        models = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
        cardStackAdapter = new CardStackAdapter(models,getContext(),this);
        cardStackLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.create_btn){
              dialog = new Dialog(Objects.requireNonNull(getContext()));
            dialog.setContentView(R.layout.create_playlist_layout);

            final EditText nameOfThePlaylst = dialog.findViewById(R.id.playlist_name);
            final Button confirmBtn =dialog.findViewById(R.id.confirm_button);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameOfThePlaylst.getText().toString();
                    if(getActivity() instanceof PlayMusic){
                        track = PlayMusic.getTracks().get(PlayMusic.position);
                    }
                    else{
                        track = StreamFragment.tracksOfStream.get(StreamFragment.positionOfStream);
                        Log.d("check null",track.getTrackName());
                    }

                    final Playlist playlist = new Playlist(name,track.getUrlThumbnail(),0);

                    models.add(playlist);
                    Toast.makeText(getContext(),String.valueOf(models.size()),Toast.LENGTH_SHORT).show();
                    cardStackAdapter.notifyItemInserted(models.size());

                    final Container container = new Container(track.getId(),playlist.getTitle());
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            Injection.getProvidedPlaylistLocalStorage(getContext()).insert(playlist);
                            Injection.getProvidedContainerLocalStorage(getContext()).insert(container);
                            Injection.getProvidedPlaylistLocalStorage(getContext()).updateTotalTrack(playlist.getTitle(),1);
                        }
                    });

                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    }

    @Override
    public void onPlaylistItemAddingClick(final String playlistID) {
        if(getContext() instanceof  PlayMusic){
            container = new Container(PlayMusic.getTracks().get(PlayMusic.position).getId(),playlistID);
        }
        else {
            container = new Container(StreamFragment.tracksOfStream.get(StreamFragment.positionOfStream).getId(), playlistID);

        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Injection.getProvidedContainerLocalStorage(getContext()).insert(container);
                int totalTracks = Injection.getProvidedPlaylistLocalStorage(getContext()).getTotalTrack(playlistID);
               totalTracks += 1;
               Injection.getProvidedPlaylistLocalStorage(getContext()).updateTotalTrack(playlistID,totalTracks);
            }
        });
       dismiss();

    }

}
