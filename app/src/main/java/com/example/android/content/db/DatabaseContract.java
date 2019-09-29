package com.example.android.content.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String AUTHORITY = "com.example.android.content";
    private static final String SCHEME = "content";

    public static String TABLE_FAV = "fav";
    public static String TABLE_FAV2 = "fav2";

    private DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

    public static final class NoteColumns implements BaseColumns {

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAV)
                .build();
        public static final Uri CONTENT_URI_2 = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAV2)
                .build();
        public static String TITLE = "title";
        public static String DATE = "date";
        public static String RATING = "rating";
        public static String IMAGE = "image";
        public static String IMAGE2 = "image2";
        public static String OVERVIEW = "overview";
    }
}
