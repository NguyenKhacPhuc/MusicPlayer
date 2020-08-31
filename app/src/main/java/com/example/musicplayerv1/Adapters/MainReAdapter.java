package com.example.musicplayerv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerv1.Model.Model;
import com.example.musicplayerv1.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainReAdapter extends RecyclerView.Adapter<MainReAdapter.LayoutHolder> {
    ArrayList<Model> models;
    Context context;
    public MainReAdapter(Context context, ArrayList<Model> models){
        this.context = context;
        this.models = models;

    }
    @NonNull
    @Override
    public LayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new LayoutHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LayoutHolder holder, int position) {
        holder.recent_lable.setText(models.get(position).getTitle());
        holder.description.setText(models.get(position).getDescription());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        Collections.shuffle(models.get(position).getTracks());
        holder.recyclerView.setAdapter(new PreviewAdapter(context
                ,models.get(position).getTracks()
                ,models.get(position).getiItemPreviewClick()
                ,models.get(position).getmTag()));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class LayoutHolder extends RecyclerView.ViewHolder {
        TextView recent_lable;
        TextView description;
        RecyclerView recyclerView;
        public LayoutHolder(@NonNull View itemView) {
            super(itemView);
            recent_lable = itemView.findViewById(R.id.recent_label);
            description = itemView.findViewById(R.id.description_label);
            recyclerView = itemView.findViewById(R.id.recent_re);
        }
    }
}
