/*
 *	FILE			: TaskListAdapter.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the RecyclerView to display the list of tasks
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.TaskViewActivity;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {
    // Formats and TAG
    private static final String TAG = "TaskListAdapter";
    private static final DateFormat dateFormat = new SimpleDateFormat
                            ("yyyy-mm-dd hh:mm", Locale.CANADA);
    // Attributes
    private List<Task> tasks = new ArrayList<>();

    // Methods
    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);

        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.TaskName.setText(currentTask.getName());
        holder.TaskEndTime.setText(String.valueOf(currentTask.getEndTime()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    // Class ViewHolder
    // Class representing the Task that is going to be display in the Recycler View
    // In this case it will be the landing page of our application
    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView TaskName;
        private TextView TaskEndTime;
        int position;

        public TaskHolder(@NonNull final View itemView) {
            super(itemView);
            TaskName = itemView.findViewById(R.id.task_list_item_name);
            TaskEndTime = itemView.findViewById(R.id.task_list_item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newTaskIntent = new Intent(itemView.getContext().getApplicationContext(), TaskViewActivity.class);
                    newTaskIntent.putExtra("taskid", tasks.get(position).getId());
                    itemView.getContext().startActivity(newTaskIntent);
                }
            });
        }
    }
}
