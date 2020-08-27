package com.example.musicplayerv1.Model;

import java.util.ArrayList;

public class Account {
    private String id;
    private String username;
    private String password;
    private ArrayList<Playlist> playlists;
    private String name;

    public Account(String id, String username, String password, ArrayList<Playlist> playlists, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.playlists = playlists;
        this.name = name;
    }
    public Account(){

    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
