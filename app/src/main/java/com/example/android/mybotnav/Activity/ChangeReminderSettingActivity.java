package com.example.android.mybotnav.Activity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.mybotnav.API.MovieAPI;
import com.example.android.mybotnav.API.Network;
import com.example.android.mybotnav.AlarmReceiver;
import com.example.android.mybotnav.Item.Movie;
import com.example.android.mybotnav.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChangeReminderSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private AlarmReceiver alarmReceiver;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    public static final String RELEASE_TODAY_MOVIES = "release_today_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_reminder_setting);

        setActionBarTitle(getResources().getString(R.string.reminder_setting));

        Switch swRelease = findViewById(R.id.sw_release_reminder);
        Switch swDaily = findViewById(R.id.sw_daily_reminder);
        swRelease.setOnCheckedChangeListener(this);
        swDaily.setOnCheckedChangeListener(this);

        listMovies = getIntent().getParcelableArrayListExtra(RELEASE_TODAY_MOVIES);
        alarmReceiver = new AlarmReceiver();

    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_daily_reminder :
                if (isChecked) {
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), "16:21", AlarmReceiver.TYPE_DAILY, 0, getResources().getString(R.string.alarm_notif_message));
                } else {
                    alarmReceiver.cancelAlarm(getApplicationContext(), AlarmReceiver.TYPE_DAILY, 0);
                }
                break;
            case R.id.sw_release_reminder :
                if (isChecked) {
                    int jumlah = listMovies.size();
                    for (int i=0; i<listMovies.size(); i++) {
                        String message = "";
                        message = listMovies.get(i).getName();
                        if (message != null) {
                            message += " " +  getResources().getString(R.string.release_notif_message);
                            alarmReceiver.setRepeatingAlarm(getApplicationContext(), "16:21", AlarmReceiver.TYPE_RELEASE, jumlah , message);
                            jumlah--;
                        }
                    }
                } else {
                    int jumlah = listMovies.size();
                    for (int i=0; i<listMovies.size(); i++) {
                        alarmReceiver.cancelAlarm(getApplicationContext(), AlarmReceiver.TYPE_RELEASE, jumlah );
                        jumlah--;
                    }
                }
                break;
        }
    }
}
