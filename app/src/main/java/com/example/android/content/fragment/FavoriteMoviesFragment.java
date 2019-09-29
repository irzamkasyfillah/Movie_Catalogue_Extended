package com.example.android.content.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.content.R;
import com.example.android.content.adapter.ListMovieAdapter;
import com.example.android.content.helper.MappingHelper;
import com.example.android.content.item.Movie;

import java.util.ArrayList;

import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI;

public class FavoriteMoviesFragment extends Fragment {
    public static final String KEY_MOVIES = "movies";
    public ArrayList<Movie> listMovies = new ArrayList<>();
    public RecyclerView rvMovie;
    private ListMovieAdapter listMovieAdapter;
    private Bundle saveState;

    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);

        listMovieAdapter = new ListMovieAdapter(getContext());
        rvMovie.setAdapter(listMovieAdapter);

        if (saveState == null) {
            listMovies.clear();
            listMovies.addAll(MappingHelper.mapCursorMovieToArrayList(cursor));
            if (listMovies != null) {
                listMovieAdapter.setListFavorite(listMovies);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.empty_data), Toast.LENGTH_SHORT).show();
            }
        } else {
            ArrayList<Movie> list = saveState.getParcelableArrayList(KEY_MOVIES);
            if (list != null) {
                listMovieAdapter.setListFavorite(list);
            }
        }
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_movie);
        if (savedInstanceState != null) {
            saveState = savedInstanceState;
        }
        listMovies = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIES, listMovieAdapter.getListMovie());
    }
}
