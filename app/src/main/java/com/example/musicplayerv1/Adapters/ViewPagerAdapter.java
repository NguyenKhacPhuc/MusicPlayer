package com.example.musicplayerv1.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayerv1.MainFragment.HomeFragment;
import com.example.musicplayerv1.MainFragment.PlaylistFragment;
import com.example.musicplayerv1.MainFragment.SearchFragment;
import com.example.musicplayerv1.MainFragment.StreamFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new StreamFragment();
            case 2:
                return new SearchFragment();
            case 3:
                return new PlaylistFragment();

            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
