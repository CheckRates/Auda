package com.github.mpagconestoga.mad_a01;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Chronometer;

import androidx.annotation.Nullable;

public class TaskTimerService extends Service {
    private static final String TAG = "TaskService";

    private IBinder binder = new ServBinder();
    private Handler servHandler;
    private long totalTime;
    private final long maxHours = 5 * (3600) * 1000;        // in hours
    private Boolean isPaused;
    private long start;

    @Override
    public void onCreate() {
        super.onCreate();
        servHandler = new Handler();
        totalTime = 0;
        start = 0;
        isPaused = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Get a instance of a service to a client (activity/fragment)
    public class ServBinder extends  Binder {
        TaskTimerService getService() {
            return TaskTimerService.this;
        }
    }

    public void startTimer() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(totalTime >= maxHours || isPaused) {             // DEBUG: Handle this propely
                    Log.d(TAG, "--> Stopped task service");
                    servHandler.removeCallbacks(this);
                    pauseTaskTimer();
                }
                else {
                    Log.d(TAG, "&--> Current time: " + totalTime);
                    long start = System.currentTimeMillis();
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalTime += (System.currentTimeMillis() - start);
                    servHandler.postDelayed(this, 100);
                }
            }
        };
        servHandler.postDelayed(runnable, 100);
    }

    public int getElapsedTime() {
        return (int)totalTime/1000;
    }

    public void pauseTaskTimer() {
        isPaused = true;
    }

    public void resumeTaskTimer() {
        isPaused = false;
        startTimer();
    }

    public Boolean getIsPaused() {
        return isPaused;
    }

    public long getMaxHours() {
        return maxHours;
    }

    public void resetTimer() {
        totalTime = 0;
    }

    // Activated when application is removed
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}
