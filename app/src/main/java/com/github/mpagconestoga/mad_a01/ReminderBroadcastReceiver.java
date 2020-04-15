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
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){ // ensures receiver
            Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
        }

    }
}
