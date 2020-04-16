/*
 *	FILE			: MainActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Landing page for the application
 *
 */

package com.github.mpagconestoga.mad_a01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.mpagconestoga.mad_a01.adapters.TaskListAdapter;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.github.mpagconestoga.mad_a01.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskViewModel viewModel;


    @Override
    // FUNCTION   : onCreateView
    // DESCRIPTION: Initiate the UI elements
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create new task button creation
        FloatingActionButton newTaskButton = findViewById(R.id.button_new_task);
        newTaskButton.setOnClickListener(new NewTaskClickListener());

        // RecyclerView
        RecyclerView taskList = findViewById(R.id.task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setHasFixedSize(true);

        // Adapter
        final TaskListAdapter adapter = new TaskListAdapter();
        taskList.setAdapter(adapter);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(TaskViewModel.class);
        viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });
    }

    // FUNCTION   : onCreateOptionsMenu
    // DESCRIPTION: Inflates the main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // FUNCTION   : onOptionsItemSelected
    // DESCRIPTION: Opens the settings page if clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent newSettingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(newSettingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    // FUNCTION   : NewTaskClickListener
    // DESCRIPTION: Opens the CreateTaskActivity
    public class NewTaskClickListener implements FloatingActionButton.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: New task button clicked --> Proceding to Task Creation");
            Intent newTaskIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
            startActivity(newTaskIntent);
        }
    }
}