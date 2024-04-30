package com.example.WeatherCheck.oman;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.WeatherCheck.R;

public class WeatherNotification {
    private static final String CHANNEL_ID = "weather_notification_channel";
    private static final int NOTIFICATION_ID = 123;

    private Context mContext;
    private NotificationManager mNotificationManager;

    public WeatherNotification(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Weather Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.hot)
                .setContentTitle("Weather Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        try {
            // Show the notification
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
            Log.d("WeatherNotification", "Notification sent: " + message); // Add this log statement
        } catch (Exception e) {
            // Log any errors or exceptions
            e.printStackTrace();
            Log.e("WeatherNotification", "Error sending notification: " + e.getMessage()); // Add this log statement
        }
    }

}
