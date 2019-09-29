package com.example.android.content.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.content.fragment.FavoriteMoviesFragment;
import com.example.android.content.fragment.FavoriteTVShowFragment;

public class FavoriteTabAdapter extends FragmentPagerAdapter {

    public FavoriteTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new FavoriteMoviesFragment();
        }
        return new FavoriteTVShowFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Movies";
        }
        return "TV Show";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
