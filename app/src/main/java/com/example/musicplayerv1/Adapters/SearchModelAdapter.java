package com.example.musicplayerv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;

import java.util.ArrayList;

public class SearchModelAdapter extends RecyclerView.Adapter<SearchModelAdapter.SearchModel> {
    ArrayList<Track> tracks;
    Context context;
    IItemPreviewClick iItemPreviewClick;
    public SearchModelAdapter(ArrayList<Track> tracks, Context context,IItemPreviewClick iItemPreviewClick){
        this.tracks = tracks;
        this.context =context;
        this.iItemPreviewClick = iItemPreviewClick;
    }
    @NonNull
    @Override
    public SearchModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new SearchModel(v,iItemPreviewClick);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchModel holder, int position) {
        Glide.with(context).load(tracks.get(position).getUrlThumbnail()).into(holder.thumbnail);
        holder.artist.setText(tracks.get(position).getArtist());
        holder.trackName.setText(tracks.get(position).getTrackName());
    }

    public class SearchModel extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        TextView artist;
        TextView trackName;
        IItemPreviewClick iItemPreviewClick;
        public SearchModel(@NonNull View itemView,IItemPreviewClick iItemPreviewClick) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            artist = itemView.findViewById(R.id.artist);
            trackName = itemView.findViewById(R.id.track_name);
            itemView.setOnClickListener(this);
            this.iItemPreviewClick = iItemPreviewClick;
        }

        @Override
        public void onClick(View v) {
            iItemPreviewClick.onItemClick(getAdapterPosition(),tracks);
        }
    }
}
