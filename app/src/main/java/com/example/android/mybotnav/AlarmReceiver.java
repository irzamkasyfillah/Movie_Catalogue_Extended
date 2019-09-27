package com.example.android.mybotnav;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.android.mybotnav.API.MovieAPI;
import com.example.android.mybotnav.API.Network;
import com.example.android.mybotnav.Item.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    private final static int ID_RELEASE = 200;
    private ArrayList<Movie> arrayListMovie = new ArrayList<>();
    private final static int ID_DAILY= 100;

    public static final String TYPE_RELEASE= "type_release";
    public static final String TYPE_DAILY= "type_daily";

    private static final String EXTRA_MESSAGE = "extra_message" ;
    private static final String EXTRA_TYPE= "extra_type" ;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        final String message = intent.getStringExtra(EXTRA_MESSAGE);
        final String title = type.equalsIgnoreCase(TYPE_RELEASE) ? context.getResources().getString(R.string.release_notif_title) : null;
        if (type.equalsIgnoreCase(TYPE_RELEASE)) {
            //update data film setiap hari
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = sdf.format(new Date());
            arrayListMovie.clear();
            new MovieTask().execute(MovieAPI.getDiscoverURL(date));
            // countdowntimer untuk memberikan waktu untuk asyntask berjalan dan agar data film != null
            new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    int jumlah = arrayListMovie.size();
                    if (arrayListMovie.size() > 0) {
                        for (int i = 0; i < arrayListMovie.size(); i++) {
                            String message = arrayListMovie.get(--jumlah).getName() + " " + context.getResources().getString(R.string.release_notif_message);
                            int notifId = ID_RELEASE + i;
                            showAlarmNotification(context, title, message, notifId);
                        }
                    }
                }
            }.start();
        } else if (type.equalsIgnoreCase(TYPE_DAILY)) {
            showAlarmNotification(context, title, message, ID_DAILY);
        }
    }

    private final static String TIME_FORMAT = "HH:mm";

    // Metode ini digunakan untuk validasi date dan time
    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    // Metode ini digunakan untuk menjalankan alarm repeating
    public void setRepeatingAlarm(Context context, String time, String type, String message) {

        // Validasi inputan waktu terlebih dahulu
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if (type.equals(TYPE_RELEASE)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        } else if (type.equals(TYPE_DAILY)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public void cancelAlarm(final Context context, String type) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }

    // Gunakan metode ini untuk menampilkan notifikasi
    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    public class MovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            String teks = null;
            try {
                teks = Network.getFromNetwork(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return teks;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !TextUtils.isEmpty(s)) {
                try {
                    JSONObject jObject = new JSONObject(s);
                    JSONArray jArray = jObject.getJSONArray("results");
                    for (int i = 0; i < jArray.length(); i++) {
                        Movie movie = new Movie(jArray.getJSONObject(i));
                        arrayListMovie.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
