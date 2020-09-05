package com.example.musicplayerv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv1.Interfaces.IItemPreviewClick;
import com.example.musicplayerv1.Interfaces.IStreamItemCallBack;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;

import java.util.ArrayList;

public class StreamAdadpter extends RecyclerView.Adapter<StreamAdadpter.StreamHolder> {
    ArrayList<Track> tracks;
    Context context;
    IStreamItemCallBack iStreamItemCallBack;
    public StreamAdadpter(ArrayList<Track> tracks,Context context, IStreamItemCallBack iStreamItemCallBack){
        this.tracks = tracks;
        this.context = context;
        this.iStreamItemCallBack = iStreamItemCallBack;
    }
    @NonNull
    @Override
    public StreamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_stream,parent,false);
        return new StreamHolder(v,iStreamItemCallBack);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull StreamHolder holder, int position) {
            holder.track_name.setText(tracks.get(position).getTrackName());
        Glide.with(context).load(tracks.get(position).getUrlThumbnail()).into(holder.thumbnail);
        holder.artist.setText(tracks.get(position).getArtist());
    }

    public class StreamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView track_name;
        TextView artist;
        ImageView thumbnail;
        ImageView like;
        ImageView playlistAdding;
        ImageView more;
        IStreamItemCallBack iStreamItemCallBack;
        public StreamHolder(@NonNull View itemView,IStreamItemCallBack iStreamItemCallBack) {
            super(itemView);
            track_name = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            like = itemView.findViewById(R.id.fav);
            playlistAdding = itemView.findViewById(R.id.add_to_playlist);
            more = itemView.findViewById(R.id.more);
            like.setOnClickListener(this);
            playlistAdding.setOnClickListener(this);
            more.setOnClickListener(this);
            this.iStreamItemCallBack = iStreamItemCallBack;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            ImageView view = null;
            switch (v.getId()){
                case R.id.fav:
                    view = like;
                    Toast.makeText(context,"Liked",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.add_to_playlist:
                    view = playlistAdding;
                    Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.more:
                    view = more;
                    Toast.makeText(context,"Show more",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            iStreamItemCallBack.onStreamItemClick(getAdapterPosition(),tracks,view);
        }
    }
}
