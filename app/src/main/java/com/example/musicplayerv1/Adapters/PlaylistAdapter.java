package com.example.musicplayerv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Model.Playlist;
import com.example.musicplayerv1.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    ArrayList<Playlist> playlists;
    Context context;
    public PlaylistAdapter(ArrayList<Playlist> playlists,Context context){
        this.playlists = playlists;
        this.context = context;

    }
    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist_layout,parent,false);

        return new PlaylistHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
            holder.playlistName.setText(playlists.get(position).getTitle());
            Glide.with(context).load(playlists.get(position).getUrlImage()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class PlaylistHolder extends RecyclerView.ViewHolder{
        CircleImageView thumbnail;
        TextView playlistName;
        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.playlist_thumbnail);
            playlistName = itemView.findViewById(R.id.playlist_name);
        }
    }
}
