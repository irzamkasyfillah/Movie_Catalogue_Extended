package com.example.android.content.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.content.R;
import com.example.android.content.db.FavoriteHelper;
import com.example.android.content.item.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieStackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    FavoriteHelper favoriteHelper;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();

    public MovieStackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        favoriteHelper = FavoriteHelper.getInstance(mContext);
        favoriteHelper.open();
        movieArrayList.clear();
        movieArrayList.addAll(favoriteHelper.getAllFavorites());
        favoriteHelper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (movieArrayList.size() > 0) {
            InputStream inputStream = null;
            try {
                inputStream = new URL("https://image.tmdb.org/t/p/w500" + movieArrayList.get(position).getPhoto1()).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rv.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeStream(inputStream));
        }
        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);
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
