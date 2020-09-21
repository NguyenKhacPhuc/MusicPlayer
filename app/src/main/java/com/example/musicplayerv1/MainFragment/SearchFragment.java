package com.example.musicplayerv1.MainFragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicplayerv1.APIQuery.YoutubeSearchQuery;
import com.example.musicplayerv1.Activities.PlayMusic;
import com.example.musicplayerv1.Adapters.SearchModelAdapter;
import com.example.musicplayerv1.Adapters.StreamAdadpter;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment implements View.OnClickListener, IItemPreviewClick {
    AutoCompleteTextView searchBar;
    ImageButton searchBtn;
    RecyclerView result;
    ArrayList<Track> tracks;
    SearchModelAdapter searchModelAdapter;
    TextView quoteApp;
    TextView purpose;
    ImageView search_ic;
    RequestQueue requestQueue;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.search,container,false);
         init();
         searchBtn.setOnClickListener(this);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
         result.setLayoutManager(linearLayoutManager);
         result.setAdapter(searchModelAdapter);
        return v;
    }
    void init(){
        searchBar = v.findViewById(R.id.search_bar);
        searchBtn = v.findViewById(R.id.searchBtn);
        result = v.findViewById(R.id.search_history);
        quoteApp = v.findViewById(R.id.quote_app);
        purpose = v.findViewById(R.id.purpose);
        search_ic = v.findViewById(R.id.search_ic);
        tracks = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        searchModelAdapter = new SearchModelAdapter(tracks,getContext(),this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.searchBtn && !searchBar.getText().toString().isEmpty()){
            result.setVisibility(View.VISIBLE);
            quoteApp.setVisibility(View.INVISIBLE);
            purpose.setVisibility(View.INVISIBLE);
            search_ic.setVisibility(View.INVISIBLE);

            String query = searchBar.getText().toString();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute( new YoutubeSearchQuery(tracks,searchModelAdapter,requestQueue,query));
        }
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
