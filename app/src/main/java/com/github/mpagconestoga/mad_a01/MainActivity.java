/*
 *	FILE			: MainActivity.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Landing page for the application
 *
 */

package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mpagconestoga.mad_a01.adapters.TaskListAdapter;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView title;
    private RecyclerView taskList;
    private FloatingActionButton newTaskButton;
    private TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.main_title);

        ArrayList<Task> tasks = Task.getAllTasks();

        if (tasks.size() == 0) {
            title.setText(getResources().getString(R.string.no_tasks));
        }

        taskList = findViewById(R.id.task_list);
        taskList.setHasFixedSize(true);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskListAdapter(this, tasks);
        taskList.setAdapter(adapter);

        newTaskButton = findViewById(R.id.button_new_task);
        newTaskButton.setOnClickListener(new NewTaskClickListener());
    }

    public class NewTaskClickListener implements FloatingActionButton.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: New task button clicked");

            Intent newTaskIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
            startActivity(newTaskIntent);
        }
    }

    public void ShowTask() {
        Intent newTaskIntent = new Intent(getApplicationContext(), TaskViewActivity.class);
        startActivity(newTaskIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
