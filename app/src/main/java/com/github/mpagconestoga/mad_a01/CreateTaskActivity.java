/*
 *	FILE			: CreateTaskActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 06
 *	DESCRIPTION		: This is the activity class for creating tasks
 */

package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class CreateTaskActivity extends AppCompatActivity {
    private static final String TAG = "TaskCreation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        createNotificationChannel();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.TaskCreationFragment, new TaskCreationFragment()).commit();
    }

    private void createNotificationChannel(){

        // if statement for the handling of newer API versions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotifyTaskChannel";
            String description = "Channel for Notification Reminder";
            int importance = IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyTask", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
