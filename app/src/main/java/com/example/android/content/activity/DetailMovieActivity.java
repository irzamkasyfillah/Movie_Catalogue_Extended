package com.example.android.content.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.content.R;
import com.example.android.content.db.FavoriteHelper;
import com.example.android.content.item.Movie;
import com.example.android.content.widget.FavoriteMovieWidget;

import static android.provider.BaseColumns._ID;
import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.android.content.db.DatabaseContract.NoteColumns.DATE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE2;
import static com.example.android.content.db.DatabaseContract.NoteColumns.OVERVIEW;
import static com.example.android.content.db.DatabaseContract.NoteColumns.RATING;
import static com.example.android.content.db.DatabaseContract.NoteColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "string_extra";
    public String name, description, date, photo2, genre, tagline, production;
    private ImageView imgPhoto;
    private TextView tvName, tvDescription, tvGenre, tvDate, tvTagline, tvProduction;
    private Movie movie;
    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        tvName = findViewById(R.id.detail_name);
        tvDescription = findViewById(R.id.detail_description);
        tvGenre = findViewById(R.id.detail_genre);
        tvDate = findViewById(R.id.detail_date);
        imgPhoto = findViewById(R.id.detail_photo);
        tvTagline = findViewById(R.id.detail_tagline);
        tvProduction = findViewById(R.id.detail_production_comp);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        name = movie.getName();
        description = movie.getDescription();
        date = movie.getDate();
        photo2 = movie.getPhoto2();
        genre = movie.getAllgenre();
        tagline = movie.getTagline();
        production = movie.getAllproduction();

        Glide.with(getApplicationContext()).load(getResources().getString(R.string.image_url) + photo2).into(imgPhoto);

        tvName.setText(name);
        if (description != null && !TextUtils.isEmpty(description))
            tvDescription.setText(description);
        else
            tvDescription.setText("-");
        if (date != null && !TextUtils.isEmpty(date))
            tvDate.setText(date);
        else
            tvDate.setText("-");
        if (production != null && !TextUtils.isEmpty(production))
            tvProduction.setText(production);
        else
            tvProduction.setText("-");
        if (genre != null && !TextUtils.isEmpty(genre))
            tvGenre.setText(genre);
        else
            tvGenre.setText("-");

        if (!tagline.isEmpty())
            tvTagline.setText("\"" + tagline + "\"");
        else
            tvTagline.setHeight(0);

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (favoriteHelper.isExist(movie)) {
            getMenuInflater().inflate(R.menu.menu_already_favorite, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_add_favorite, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_favorite) {
            if (!favoriteHelper.isExist(movie)) {
                ContentValues args = new ContentValues();
                args.put(_ID, movie.getId());
                args.put(TITLE, movie.getName());
                args.put(RATING, movie.getRating());
                args.put(DATE, movie.getDate());
                args.put(IMAGE, movie.getPhoto1());
                args.put(IMAGE2, movie.getPhoto2());
                args.put(OVERVIEW, movie.getDescription());
                getContentResolver().insert(CONTENT_URI, args);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star_clicked));
                Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.favorite_is_exist), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (item.getItemId() == R.id.action_delete_favorite) {
                Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                getContentResolver().delete(uri, null, null);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star));
                Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.success_delete_favorite), Toast.LENGTH_SHORT).show();
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName component = new ComponentName(this, FavoriteMovieWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(component);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        return super.onOptionsItemSelected(item);
    }
}
