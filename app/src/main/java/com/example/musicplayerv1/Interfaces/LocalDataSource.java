package com.example.musicplayerv1.Interfaces;

import com.example.musicplayerv1.Model.Track;

import java.util.ArrayList;
import java.util.List;

public interface LocalDataSource<T> {
    public List<T> getAll();
    public List<T> getA(String id);
    public void deleteA(String id);
    public void insert(T t);

}
