package com.github.mpagconestoga.mad_a01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent){

        //Constructs the details of the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTask")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Remind Task")
                .setContentText("Hello, fellow students. I'm going to get you.")
                .setPriority((NotificationCompat.PRIORITY_DEFAULT));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build()); // notifactionID is unique int for each notification that must be defined
    }
}
