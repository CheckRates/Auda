/*
 *	FILE			: ReminderBroadcastReceiver.java
 *	PROJECT			: PROG3150 - Assignment-03
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 04 - 17
 *	DESCRIPTION		: This file holds construction of the reminder broadcast
 */
package com.github.mpagconestoga.mad_a01.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.mpagconestoga.mad_a01.R;

/*
        Class Name: ReminderBroadcastReceiver
        Purpose: This class is used establishing the receiver that will be triggered when a
        task is due to be completed.
 */
public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        //Constructs the details of the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTask")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Task Due")
                .setContentText("Planner task is due")
                .setPriority((NotificationCompat.PRIORITY_DEFAULT));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build()); // notifactionID is unique int for each notification that must be defined
    }
}
