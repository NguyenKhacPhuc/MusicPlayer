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
import com.example.musicplayerv1.Interfaces.IPlaylistAddingClick;
import com.example.musicplayerv1.Model.Playlist;

import com.example.musicplayerv1.R;

import java.util.ArrayList;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ImageHolder> {
    ArrayList<Playlist> models;
    Context context;
    IPlaylistAddingClick iPlaylistAddingClick;
    public CardStackAdapter(ArrayList<Playlist> models, Context context, IPlaylistAddingClick iPlaylistAddingClick){
        this.models=models;
        this.context =context;
        this.iPlaylistAddingClick = iPlaylistAddingClick;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_adding,parent,false);
        return new ImageHolder(v,iPlaylistAddingClick);
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

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        TextView name;
        TextView noOfTracks;
        IPlaylistAddingClick iPlaylistAddingClick;
        public ImageHolder(@NonNull View itemView,IPlaylistAddingClick iPlaylistAddingClick) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.playlist_name);
            noOfTracks = itemView.findViewById(R.id.noOfTracks);
            itemView.setOnClickListener(this);
            this.iPlaylistAddingClick = iPlaylistAddingClick;
        }

        @Override
        public void onClick(View v) {
            iPlaylistAddingClick.onPlaylistItemAddingClick(models.get(getAdapterPosition()).getTitle());
        }
    }
}
