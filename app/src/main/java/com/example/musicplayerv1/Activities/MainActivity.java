package com.example.musicplayerv1.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayerv1.Adapters.ViewPagerAdapter;
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
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        navigationView.setOnNavigationItemSelectedListener(this);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                viewPager.setCurrentItem(0);
                toolbar.setTitle("HOME");
                break;
            case R.id.stream:
               viewPager.setCurrentItem(1);
                toolbar.setTitle("STREAM");
                break;
            case R.id.search:
              viewPager.setCurrentItem(2);
                toolbar.setTitle("SEARCH");
                break;
            case R.id.playlist:
                viewPager.setCurrentItem(3);
                toolbar.setTitle("PLAYLIST");
                break;
            default:
                break;
        }
        return true;
    }
    void initView(){
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.fragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigationView.getMenu().findItem(R.id.home).setChecked(true);
                        toolbar.setTitle("HOME");
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.stream).setChecked(true);
                        toolbar.setTitle("STREAM");
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.search).setChecked(true);
                        toolbar.setTitle("SEARCH");
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.playlist).setChecked(true);
                        toolbar.setTitle("PLAYLIST");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
