/*
 *	FILE			: App.java
 *	PROJECT			: PROG3150 - Assignment-03
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 04 - 17
 *	DESCRIPTION		: This file holds construction of the notification channels
 */
package com.github.mpagconestoga.mad_a01;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

/*
        Class Name: App
        Purpose: This class is used for establishing the notification channels
        that will be utilized throughout the app.
 */public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {    // if statement for ensuring that all relevant versions are supported
            CharSequence name = "NotifyTaskChannel";
            String description = "Channel for Notification Reminder";
            int importance = IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyTask", name, importance);  // establishes the broadcast to be received
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // initializes notification manager
            notificationManager.createNotificationChannel(channel); // catches the applied broadcast
        }
    }
}
