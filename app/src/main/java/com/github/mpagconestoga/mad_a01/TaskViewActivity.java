/*
 *	FILE			: TaskViewActivity.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Activity that allows the display of a task info
 *
 */
package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.OnGoingSubtaskAdapter;
import com.github.mpagconestoga.mad_a01.adapters.TaskListAdapter;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class TaskViewActivity extends AppCompatActivity {
    private static final String TAG = "TaskViewActivity";

    private RecyclerView subTaskList;
    private TextView header;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        int hashCode = this.getIntent().getIntExtra("task", 0);

        Task task = Task.getTaskByHashcode(hashCode);

        if (task == null) {
            Log.e(TAG, "onCreate: Hash code received was invalid!");
            finish();
        }

        progressBar = findViewById(R.id.task_progress);

        subTaskList = findViewById(R.id.subtask_list);
        subTaskList.setLayoutManager(new LinearLayoutManager(this));
        subTaskList.setHasFixedSize(true);

        subTaskList.setAdapter(new OnGoingSubtaskAdapter(this, task.getSubtasks(), task, progressBar));

        header = findViewById(R.id.task_title);
        header.setText(String.format("%s: %s", "Task", task.getName()));
    }
}
