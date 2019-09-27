package com.example.android.mybotnav.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.example.android.mybotnav.AlarmReceiver;
import com.example.android.mybotnav.R;

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
        public static final String JAM8 = "07:59";
        //set ke 07:59 karna alarm release_reminder delay sekitar 15 detik

        private SwitchPreferenceCompat dailySwitch;
        private SwitchPreferenceCompat releaseSwitch;

        private AlarmReceiver alarmReceiver;

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
            dailySwitch = (SwitchPreferenceCompat) findPreference(DAILY_REMINDER_KEY);
            releaseSwitch = (SwitchPreferenceCompat) findPreference(RELEASE_REMINDER_KEY);

            setSummaries();

            alarmReceiver = new AlarmReceiver();

            dailySwitch.setOnPreferenceChangeListener(this);
            releaseSwitch.setOnPreferenceChangeListener(this);

        }

        public void setSummaries() {
            SharedPreferences sh = getPreferenceManager().getSharedPreferences();
            dailySwitch.setChecked(sh.getBoolean(DAILY_REMINDER_KEY, false));
            releaseSwitch.setChecked(sh.getBoolean(RELEASE_REMINDER_KEY, false));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            switch (preference.getKey()) {
                case DAILY_REMINDER_KEY:
                    if (o.equals(true)) {
                        alarmReceiver.setRepeatingAlarm(getContext(), JAM7, AlarmReceiver.TYPE_DAILY, getResources().getString(R.string.alarm_notif_message));
                    } else {
                        alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_DAILY);
                    }
                    break;

                case RELEASE_REMINDER_KEY:
                    if (o.equals(true)) {
                        alarmReceiver.setRepeatingAlarm(getContext(), JAM8, AlarmReceiver.TYPE_RELEASE, null);
                    } else {
                        alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_RELEASE);
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
