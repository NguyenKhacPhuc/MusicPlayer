package com.example.musicplayerv1.MainFragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicplayerv1.APIQuery.ModelQuery;
import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.APIQuery.YoutubeQuery;
import com.example.musicplayerv1.Adapters.MainReAdapter;
import com.example.musicplayerv1.Adapters.PreviewAdapter;
import com.example.musicplayerv1.Common.ProgressDialogSingleton;
import com.example.musicplayerv1.Constant;
import com.example.musicplayerv1.Interfaces.ICallBackModel;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Interfaces.IYoutubePlaylistCallBack;
import com.example.musicplayerv1.Model.Model;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment implements IItemPreviewClick {
    RecyclerView mainRe;
    MainReAdapter mainReAdapter;
    ArrayList<Track> recentData;
    ArrayList<Track> relaxData;
    ArrayList<Track> motivationData;
    ArrayList<Track> illeniumData;
    ArrayList<Model> models;
    RequestQueue requestQueue;
    MediaPlayer mediaPlayer;

    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.home_frag,container,false);
            initView();
            mediaPlayer= new MediaPlayer();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mainReAdapter = new MainReAdapter(getContext(),models);
        mainRe.setLayoutManager(linearLayoutManager);
        mainRe.setAdapter(mainReAdapter);

        addModelData();

        return v;
    }
    void initView(){
        requestQueue = Volley.newRequestQueue(getContext());
        recentData  = new ArrayList<>();
        relaxData  = new ArrayList<>();
        motivationData  = new ArrayList<>();
        illeniumData  = new ArrayList<>();
        models = new ArrayList<>();
        mainRe = v.findViewById(R.id.mainRe);
    }

    @Override
    public void onItemClick(int position, String trackId) {
        QueryTrackUrl queryTrackUrl = new QueryTrackUrl(trackId,requestQueue,getContext());
        queryTrackUrl.returnUrl(new IPassUrl() {
            @Override
            public void getUr(String url)  {
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    void addModelData(){
        final ModelQuery modelQuery = new ModelQuery();

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        modelQuery.getAllModel(new ICallBackModel() {
            @Override
            public void callBackModels(final ArrayList<Model> multipleModels) {

                    for(int i = 0; i <multipleModels.size();i++){
                        executor.execute(new YoutubeQuery(models,mainReAdapter,multipleModels.get(i),HomeFragment.this, requestQueue) {
                        });
                        }
                executor.shutdown();

            }
        });
    }
}
