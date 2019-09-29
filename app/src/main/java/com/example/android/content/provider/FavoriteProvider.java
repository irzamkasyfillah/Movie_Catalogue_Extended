package com.example.android.content.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.content.db.FavoriteHelper;

import static com.example.android.content.db.DatabaseContract.AUTHORITY;
import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI_2;
import static com.example.android.content.db.DatabaseContract.TABLE_FAV;
import static com.example.android.content.db.DatabaseContract.TABLE_FAV2;

public class FavoriteProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV = 3;
    private static final int TV_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TABLE_FAV2, TV);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV2 + "/#", TV_ID);
    }

    private FavoriteHelper favoriteHelper;

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV:
                cursor = favoriteHelper.queryProvider2();
                break;
            case TV_ID:
                cursor = favoriteHelper.queryByIdProvider2(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long added;
        Uri uri1;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favoriteHelper.insertProvider(values);
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                uri1 = Uri.parse(CONTENT_URI + "/" + added);
                break;
            case TV:
                added = favoriteHelper.insertProvider2(values);
                getContext().getContentResolver().notifyChange(CONTENT_URI_2, null);
                uri1 = Uri.parse(CONTENT_URI_2 + "/" + added);
                break;
            default:
                added = 0;
                uri1 = null;
                break;
        }
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                break;
            case TV_ID:
                deleted = favoriteHelper.deleteProvider2(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_2, null);
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
