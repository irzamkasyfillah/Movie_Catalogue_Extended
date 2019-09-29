package com.example.android.content.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.content.R;
import com.example.android.content.db.FavoriteHelper;
import com.example.android.content.item.TVShow;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TVShowStackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    FavoriteHelper favoriteHelper;
    private ArrayList<TVShow> tvShowArrayList = new ArrayList<>();

    public TVShowStackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        favoriteHelper = FavoriteHelper.getInstance(mContext);
        favoriteHelper.open();
        tvShowArrayList.clear();
        tvShowArrayList.addAll(favoriteHelper.getAllFavorites2());
        favoriteHelper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return tvShowArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (tvShowArrayList.size() > 0) {
            InputStream inputStream = null;
            try {
                inputStream = new URL("https://image.tmdb.org/t/p/w500" + tvShowArrayList.get(position).getPhoto1()).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rv.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeStream(inputStream));
        }
        Bundle extras = new Bundle();
        extras.putInt(FavoriteTVShowWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
