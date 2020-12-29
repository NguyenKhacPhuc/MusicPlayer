package com.example.musicplayerv1.MainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.musicplayerv1.SubFragment.BottomSheetFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamFragment extends Fragment implements IStreamItemCallBack, PopupMenu.OnMenuItemClickListener {
    RecyclerView streamRs;
    public static ArrayList<Track> tracksOfStream;
    public static int positionOfStream;
    StreamAdadpter streamAdadpter;
    RequestQueue requestQueue;
    ExecutorService executorService;
    PopupMenu popupMenu;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLiked;

    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.stream_frag,container,false);
         init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        streamRs.setLayoutManager(linearLayoutManager);
        streamAdadpter = new StreamAdadpter(tracksOfStream,getContext(),this);
        streamRs.setAdapter(streamAdadpter);
        addData();
        return v;
    }
    @SuppressLint("CommitPrefEdits")
    void init(){
        streamRs = v.findViewById(R.id.stream_recycler);
        requestQueue = Volley.newRequestQueue(getContext());
        tracksOfStream = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("LIKED", getContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        executorService = Executors.newSingleThreadExecutor();
    }

    private void addData(){
        YoutubeStreamQuery youtubeStreamQuery = new YoutubeStreamQuery(tracksOfStream,streamAdadpter,requestQueue);
        youtubeStreamQuery.run();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStreamItemClick(final int position, final ArrayList<Track> tracks, ImageView v) {
        tracksOfStream =tracks;
        positionOfStream = position;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Injection.getProvidedTrackLocalStorage(getContext()).insert(tracks.get(position));
            }
        });

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
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.like_anim);
                    v.startAnimation(animation);

                    if (isLiked) {
                        v.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        isLiked = false;
                        editor.remove(tracks.get(position).getId());
                    } else {
                        v.setImageResource(R.drawable.ic_baseline_favorite_24);
                        isLiked = true;

                        editor.putString(tracks.get(position).getId(), tracks.get(position).getTrackName());
                    }
                    editor.apply();
                    break;
                case R.id.add_to_playlist:
                    //TODO: add to database
                    try {
                        BottomSheetFragment bottomSheetDialogFragment = new BottomSheetFragment();
                        assert getFragmentManager() != null;
                        bottomSheetDialogFragment.show(getFragmentManager(), "playlist adding");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
