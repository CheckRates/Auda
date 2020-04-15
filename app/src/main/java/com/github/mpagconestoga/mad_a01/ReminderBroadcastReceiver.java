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
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(                        // true if there is no connectivity
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity){
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
