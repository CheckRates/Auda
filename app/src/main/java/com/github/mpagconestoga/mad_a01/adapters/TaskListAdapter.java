/*
 *	FILE			: TaskListAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the RecyclerView to display the list of tasks
 *
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.CreateTaskActivity;
import com.github.mpagconestoga.mad_a01.MainActivity;
import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.TaskViewActivity;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private static final String TAG = "TaskListAdapter";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.CANADA);

    private ArrayList<Task> data;
    private LayoutInflater inflater;
    private Context context;

    public TaskListAdapter(Context context, ArrayList<Task> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = data.get(position);
        holder.name.setText(task.getName());
        holder.date.setText(dateFormat.format(task.getEndTime()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.task_list_item_name);
            date = itemView.findViewById(R.id.task_list_item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newTaskIntent = new Intent(context.getApplicationContext(), TaskViewActivity.class);
                    newTaskIntent.putExtra("task", data.get(position).hashCode());
                    context.startActivity(newTaskIntent);
                }
            });
        }
    }
}
