package com.example.musicplayerv1.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicplayerv1.APIQuery.ModelQuery;
import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.APIQuery.YoutubeModelQuery;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Adapters.MainReAdapter;
import com.example.musicplayerv1.Common.Checking;
import com.example.musicplayerv1.Common.ProgressDialogSingleton;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.ICallBackModel;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Interfaces.IModelOnClick;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Model;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.Services.MusicPlayService;
import com.example.musicplayerv1.R;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment implements IItemPreviewClick, IModelOnClick {
    static boolean active = false;
    RecyclerView mainRe;
    MainReAdapter mainReAdapter;
    ArrayList<Model> models;
    RequestQueue requestQueue;
    ArrayList<Track> tracks;
    ConstraintLayout homeFragment;
    public static final int position = 1;


    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.home_frag,container,false);
            initView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mainReAdapter = new MainReAdapter(getContext(),models,this);
        mainRe.setLayoutManager(linearLayoutManager);
        mainRe.setAdapter(mainReAdapter);

            addModelData();

        return v;
    }
    void initView(){
        requestQueue = Volley.newRequestQueue(getContext());
        models = new ArrayList<>();
        mainRe = v.findViewById(R.id.mainRe);
        homeFragment = v.findViewById(R.id.home_fragment);


    }

    @Override
    public void onItemClick(int position,ArrayList<Track> tracks) {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(),MusicPlayService.class));
        Intent intent = new Intent(getContext(),PlayMusic.class);
        intent.putExtra("tracks", (Serializable) tracks);
        intent.putExtra("position", position);
        intent.putExtra("playPoint", 0L);
        Objects.requireNonNull(getContext()).startActivity(intent);

    }

    void addModelData(){
        final ModelQuery modelQuery = new ModelQuery();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor .execute(new Runnable() {
            @Override
            public void run() {
                modelQuery.getAllModel(new ICallBackModel() {
                    @Override
                    public void callBackModels(final ArrayList<Model> multipleModels) {

                        for(int i = 0; i <multipleModels.size();i++) {
                            executor.execute(new YoutubeModelQuery(models, mainReAdapter, multipleModels.get(i), HomeFragment.this, requestQueue) {
                            });
                        }
                        executor.shutdown();
            }
        });
            }
        });
    }


    @Override
    public void modelOnClick(final ArrayList<Track> tracks) {
        Collections.shuffle(tracks);
        this.tracks = tracks;
        Intent intent = new Intent(getContext(),PlayMusic.class);
        intent.putExtra("tracks", (Serializable)tracks);
        intent.putExtra("position",0);
        Objects.requireNonNull(getContext()).startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}
