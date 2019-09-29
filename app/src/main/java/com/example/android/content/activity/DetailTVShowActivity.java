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
import com.example.android.content.item.TVShow;
import com.example.android.content.widget.FavoriteTVShowWidget;

import static android.provider.BaseColumns._ID;
import static com.example.android.content.db.DatabaseContract.NoteColumns.CONTENT_URI_2;
import static com.example.android.content.db.DatabaseContract.NoteColumns.DATE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE;
import static com.example.android.content.db.DatabaseContract.NoteColumns.IMAGE2;
import static com.example.android.content.db.DatabaseContract.NoteColumns.OVERVIEW;
import static com.example.android.content.db.DatabaseContract.NoteColumns.RATING;
import static com.example.android.content.db.DatabaseContract.NoteColumns.TITLE;

public class DetailTVShowActivity extends AppCompatActivity {
    public static final String EXTRA_TV_SHOW = "string_extra";
    public String name, description, date, photo2, genre, status, production;
    private ImageView imgPhoto;
    private TextView tvName, tvDescription, tvGenre, tvDate, tvStatus, tvProduction;
    private TVShow tvShow;
    private FavoriteHelper favoriteHelper;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_favorite) {
            if (!favoriteHelper.isExist2(tvShow)) {
                ContentValues args = new ContentValues();
                args.put(_ID, tvShow.getId());
                args.put(TITLE, tvShow.getName());
                args.put(RATING, tvShow.getRating());
                args.put(DATE, tvShow.getDate());
                args.put(IMAGE, tvShow.getPhoto1());
                args.put(IMAGE2, tvShow.getPhoto2());
                args.put(OVERVIEW, tvShow.getDescription());
                getContentResolver().insert(CONTENT_URI_2, args);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star_clicked));
                Toast.makeText(DetailTVShowActivity.this, getResources().getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailTVShowActivity.this, getResources().getString(R.string.favorite_is_exist), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (item.getItemId() == R.id.action_delete_favorite) {
                Uri uri = Uri.parse(CONTENT_URI_2 + "/" + tvShow.getId());
                getContentResolver().delete(uri, null, null);
                item.setIcon(getResources().getDrawable(R.drawable.ic_star));
                Toast.makeText(DetailTVShowActivity.this, getResources().getString(R.string.success_delete_favorite), Toast.LENGTH_SHORT).show();
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName component = new ComponentName(this, FavoriteTVShowWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(component);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (favoriteHelper.isExist2(tvShow)) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        tvName = findViewById(R.id.detail_name);
        tvDescription = findViewById(R.id.detail_description);
        tvGenre = findViewById(R.id.detail_genre);
        tvDate = findViewById(R.id.detail_date);
        tvStatus = findViewById(R.id.detail_status);
        imgPhoto = findViewById(R.id.detail_photo);
        tvProduction = findViewById(R.id.detail_production_comp);

        tvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);

        name = tvShow.getName();
        description = tvShow.getDescription();
        date = tvShow.getDate();
        photo2 = tvShow.getPhoto2();
        genre = tvShow.getAllgenre();
        status = tvShow.getStatus();
        production = tvShow.getAllproduction();

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

        if (status != null && !TextUtils.isEmpty(status))
            tvStatus.setText(status);
        else
            tvStatus.setText("-");

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();
    }
}
