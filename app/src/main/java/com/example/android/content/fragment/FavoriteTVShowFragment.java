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
import com.example.android.content.adapter.ListTVShowAdapter;
import com.example.android.content.helper.MappingHelper;
import com.example.android.content.item.TVShow;

import java.util.ArrayList;

import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI_2;

public class FavoriteTVShowFragment extends Fragment {
    public static final String KEY_TVSHOW = "tvshow";
    public ArrayList<TVShow> listTVShow = new ArrayList<>();
    public RecyclerView rvTVShow;
    private ListTVShowAdapter listTVShowAdapter;
    private Bundle saveState;

    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTVShow = view.findViewById(R.id.rv_tv_show);
        if (savedInstanceState != null) {
            saveState = savedInstanceState;
        }
        listTVShow = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI_2, null, null, null);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTVShow.setHasFixedSize(true);

        listTVShowAdapter = new ListTVShowAdapter(getContext());
        rvTVShow.setAdapter(listTVShowAdapter);

        if (saveState == null) {
            listTVShow.clear();
            listTVShow.addAll(MappingHelper.mapCursorTVShowToArrayList(cursor));
            if (listTVShow != null) {
                listTVShowAdapter.setListFavorite(listTVShow);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.empty_data), Toast.LENGTH_SHORT).show();
            }
        } else {
            ArrayList<TVShow> list = saveState.getParcelableArrayList(KEY_TVSHOW);
            if (list != null) {
                listTVShowAdapter.setListFavorite(list);
            }
        }
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TVSHOW, listTVShowAdapter.getListTVShow());
    }
}
