package com.example.android.mybotnav.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.mybotnav.Fragment.FavoriteFragment;
import com.example.android.mybotnav.Fragment.MovieFragment;
import com.example.android.mybotnav.Fragment.TvShowFragment;
import com.example.android.mybotnav.Item.Movie;
import com.example.android.mybotnav.R;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final String STATE_TITLE = "state_string";
    public String title = "Movie";
    private SearchView searchView;
    private ArrayList<Movie> listMovies;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    title = getResources().getString(R.string.movie);
                    setActionBarTitle(title);
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
                case R.id.navigation_tv_show:
                    title = getResources().getString(R.string.tv_show);
                    setActionBarTitle(title);
                    fragment = new TvShowFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
                case R.id.navigation_favorite:
                    title = getResources().getString(R.string.favorite);
                    setActionBarTitle(title);
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);

        final String movie = getResources().getString(R.string.movie);
        final String tvShow = getResources().getString(R.string.tv_show);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextChange(String query) {
                String actionBarTitle = (String) Objects.requireNonNull(getSupportActionBar()).getTitle();
                if (searchView.getQuery().length() != 0) {

                    assert actionBarTitle != null;
                    Fragment fragment;
                    if (actionBarTitle.equals(movie)) {
                        fragment = new MovieFragment(query);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    } else if (actionBarTitle.equals(tvShow)) {
                        fragment = new TvShowFragment(query);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    }
                }
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                String actionBarTitle = (String) Objects.requireNonNull(getSupportActionBar()).getTitle();
                Fragment fragment;
                assert actionBarTitle != null;
                if (actionBarTitle.equals(movie)) {
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                } else if (actionBarTitle.equals(tvShow)) {
                    fragment = new TvShowFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_setting) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } else {
            if (item.getItemId() == R.id.action_change_reminder) {
                Intent intent = new Intent(this, ChangeReminderSettingActivity.class );
//                intent.putExtra(ChangeReminderSettingActivity.RELEASE_TODAY_MOVIES, listMovies);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            setActionBarTitle(title);
            navView.setSelectedItemId(R.id.navigation_movie);
        } else {
            title = savedInstanceState.getString(STATE_TITLE);
            setActionBarTitle(title);
        }
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
