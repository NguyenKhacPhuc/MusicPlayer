package com.example.musicplayerv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Interfaces.IItemPlaylisClick;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.TrackHolder> {
    Context context;
    ArrayList<Track> tracks;
    IItemPlaylisClick iItemPlaylisClick;
    public PlaylistAdapter(Context context, ArrayList<Track> tracks,IItemPlaylisClick iItemPlaylisClick){
        this.context = context;
        this.tracks = tracks;
        this.iItemPlaylisClick = iItemPlaylisClick;
    }
    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_playlist,parent,false);
        return new TrackHolder(v,iItemPlaylisClick);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHolder holder, int position) {
        holder.artist.setText(tracks.get(position).getArtist());
        holder.title.setText(tracks.get(position).getTrackName());
        Glide.with(context).load(tracks.get(position).getUrlThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class TrackHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView artist;
        ImageButton play;
        CircleImageView thumbnail;
        IItemPlaylisClick iItemPlaylisClick;
        public TrackHolder(@NonNull View itemView,IItemPlaylisClick iItemPlaylisClick) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            play = itemView.findViewById(R.id.play);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
            this.iItemPlaylisClick = iItemPlaylisClick;

        }

        @Override
        public void onClick(View v) {
            iItemPlaylisClick.onClick(getAdapterPosition(),play);
        }
    }
}
