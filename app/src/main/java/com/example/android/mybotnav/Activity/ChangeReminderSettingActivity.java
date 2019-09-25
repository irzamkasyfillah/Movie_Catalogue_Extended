package com.example.android.mybotnav.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.example.android.mybotnav.AlarmReceiver;
import com.example.android.mybotnav.Item.Movie;
import com.example.android.mybotnav.R;

import java.util.ArrayList;

public class ChangeReminderSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_reminder_setting);

        setActionBarTitle(getResources().getString(R.string.reminder_setting));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.reminder_fragment, new SettingReminderFragment())
                .commit();
    }

    public static class SettingReminderFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        public static final String DAILY_REMINDER_KEY = "daily_reminder";
        public static final String RELEASE_REMINDER_KEY = "release_reminder";
        public static final String JAM7 = "07:00";
        public static final String JAM8 = "08:00";

        private SwitchPreferenceCompat dailySwitch;
        private SwitchPreferenceCompat releaseSwitch;

        private AlarmReceiver alarmReceiver;

        private ArrayList<Movie> arrayListMovie = new ArrayList<>();

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
            dailySwitch = (SwitchPreferenceCompat) findPreference(DAILY_REMINDER_KEY);
            releaseSwitch = (SwitchPreferenceCompat) findPreference(RELEASE_REMINDER_KEY);

            setSummaries();

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            String date = sdf.format(new Date());
//            arrayListMovie.clear();
//            new MovieTask().execute(MovieAPI.getDiscoverURL(date));

            alarmReceiver = new AlarmReceiver();

            dailySwitch.setOnPreferenceChangeListener(this);
            releaseSwitch.setOnPreferenceChangeListener(this);

        }

        public void setSummaries() {
            SharedPreferences sh = getPreferenceManager().getSharedPreferences();
            dailySwitch.setChecked(sh.getBoolean(DAILY_REMINDER_KEY, false));
            releaseSwitch.setChecked(sh.getBoolean(RELEASE_REMINDER_KEY, false));
        }

//        public class MovieTask extends AsyncTask<URL, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected String doInBackground(URL... urls) {
//                String teks = null;
//                try {
//                    teks = Network.getFromNetwork(urls[0]);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return teks;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                if (s != null && !TextUtils.isEmpty(s)) {
//                    try {
//                        JSONObject jObject = new JSONObject(s);
//                        JSONArray jArray = jObject.getJSONArray("results");
//                        for (int i = 0; i < jArray.length(); i++) {
//                            Movie movie = new Movie(jArray.getJSONObject(i));
//                            arrayListMovie.add(movie);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            switch (preference.getKey()) {
                case DAILY_REMINDER_KEY:
                    if (o.equals(true)) {
                        alarmReceiver.setRepeatingAlarm(getContext(), JAM7, AlarmReceiver.TYPE_DAILY, 0, getResources().getString(R.string.alarm_notif_message));
                    } else {
                        alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_DAILY, 0);
                        dailySwitch.setChecked(false);
                    }
                    break;

                case RELEASE_REMINDER_KEY:
                    if (o.equals(true)) {
                        int jumlah = arrayListMovie.size();
                        if (jumlah > 0) {
                            for (int i = 0; i < arrayListMovie.size(); i++) {
                                String message;
                                message = arrayListMovie.get(i).getName();
                                if (message != null) {
                                    message += " " + getResources().getString(R.string.release_notif_message);
                                    alarmReceiver.setRepeatingAlarm(getContext(), JAM8, AlarmReceiver.TYPE_RELEASE, jumlah, message);
                                    jumlah--;
                                }
                            }
                            dailySwitch.setChecked(true);
                        }
                    } else {
                        int jumlah = arrayListMovie.size();
                        if (jumlah > 0) {
                            for (int i = 0; i < arrayListMovie.size(); i++) {
                                alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_RELEASE, jumlah);
                                jumlah--;
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
