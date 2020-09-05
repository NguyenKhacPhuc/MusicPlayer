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

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewHolder> {
    Context context;
    ArrayList<Track> tracks;
    IItemPreviewClick iItemPreviewClick;
    String mTag;
    public PreviewAdapter(Context context,ArrayList<Track> tracks,IItemPreviewClick iItemPreviewClick,String mTag){
        this.context = context;
        this.tracks = tracks;
        this.iItemPreviewClick = iItemPreviewClick;
        this.mTag = mTag;
    }
    @NonNull
    @Override
    public PreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_preview,parent,false);
        return new PreviewHolder(v,iItemPreviewClick);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewHolder holder, int position) {
            holder.trackName.setText(tracks.get(position).getTrackName());
            holder.artist.setText(tracks.get(position).getArtist());
            Glide.with(context).load(tracks.get(position).getUrlThumbnail()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class PreviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView artist;
        TextView trackName;
        IItemPreviewClick iItemPreviewClick;
        public PreviewHolder(@NonNull View itemView,IItemPreviewClick iItemPreviewClick) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail);
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
