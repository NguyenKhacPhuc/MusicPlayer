package com.example.musicplayerv1.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.musicplayerv1.MainFragment.HomeFragment;
import com.example.musicplayerv1.MainFragment.PlaylistFragment;
import com.example.musicplayerv1.MainFragment.SearchFragment;
import com.example.musicplayerv1.MainFragment.StreamFragment;
import com.example.musicplayerv1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        navigationView.setOnNavigationItemSelectedListener(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.mainhthene));
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
        if(savedInstanceState==null)
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment chosen = new Fragment();
        switch (item.getItemId()){

            case R.id.home:
                chosen = new HomeFragment();
                toolbar.setTitle("HOME");
                break;
            case R.id.stream:
                chosen = new StreamFragment();
                toolbar.setTitle("STREAM");
                break;
            case R.id.search:
                chosen = new SearchFragment();
                toolbar.setTitle("SEARCH");
                break;
            case R.id.playlist:
                chosen= new PlaylistFragment();
                toolbar.setTitle("PLAYLIST");
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,chosen).commit();
        return true;
    }
    void initView(){
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
    }
}
