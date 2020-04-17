/*
 *	FILE			: ConnectionBroadcastReceiver.java
 *	PROJECT			: PROG3150 - Assignment-03
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 04 - 17
 *	DESCRIPTION		: This file holds construction of the connection state broadcast
 */
package com.github.mpagconestoga.mad_a01.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/*
        Class Name: ConnectionBroadcastReceiver
        Purpose: This class is used establishing the receiver that will be displayed
                during connection state changes in the Task view.
 */
public class ConnectionBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){                          //
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){    // if statement checks for change in connectivity state
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
