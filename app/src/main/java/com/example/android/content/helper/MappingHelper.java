package com.example.android.content.helper;

import android.database.Cursor;

import com.example.android.content.item.Movie;
import com.example.android.content.item.TVShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.android.content.db.DatabaseContract.NoteColumns.DATE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE2;
import static com.example.android.content.db.DatabaseContract.NoteColumns.OVERVIEW;
import static com.example.android.content.db.DatabaseContract.NoteColumns.RATING;
import static com.example.android.content.db.DatabaseContract.NoteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorMovieToArrayList(Cursor notesCursor) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String photo1 = notesCursor.getString(notesCursor.getColumnIndexOrThrow(IMAGE));
            String photo2 = notesCursor.getString(notesCursor.getColumnIndexOrThrow(IMAGE2));
            float rating = notesCursor.getFloat(notesCursor.getColumnIndexOrThrow(RATING));
            moviesList.add(new Movie(id, title, photo1, photo2, description, rating, date));
        }
        return moviesList;
    }

    public static ArrayList<TVShow> mapCursorTVShowToArrayList(Cursor notesCursor) {
        ArrayList<TVShow> tvShowsList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String photo1 = notesCursor.getString(notesCursor.getColumnIndexOrThrow(IMAGE));
            String photo2 = notesCursor.getString(notesCursor.getColumnIndexOrThrow(IMAGE2));
            float rating = notesCursor.getFloat(notesCursor.getColumnIndexOrThrow(RATING));
            tvShowsList.add(new TVShow(id, title, photo1, photo2, description, rating, date));
        }
        return tvShowsList;
    }
}
