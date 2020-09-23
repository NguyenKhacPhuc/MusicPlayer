package com.example.musicplayerv1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Model.Playlist;

import com.example.musicplayerv1.R;

import java.util.ArrayList;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ImageHolder> {
    ArrayList<Playlist> models;
    Context context;
    public CardStackAdapter(ArrayList<Playlist> models, Context context){
        this.models=models;
        this.context =context;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_adding,parent,false);
        return new ImageHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Glide.with(context).load(models.get(position).getUrlImage()).into(holder.thumbnail);
        holder.name.setText(models.get(position).getTitle());
        holder.noOfTracks.setText(String.valueOf(models.get(position).getTotalTracks()));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView name;
        TextView noOfTracks;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.playlist_name);
            noOfTracks = itemView.findViewById(R.id.noOfTracks);
        }
    }
}
