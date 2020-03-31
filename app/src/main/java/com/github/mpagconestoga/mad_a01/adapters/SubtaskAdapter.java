/*
 *	FILE			: SubtaskAdapter.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the Recycler view of the assignment of the sub-tasks
 */
package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Subtask;

import java.util.ArrayList;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ViewHolder> {
    private static final String TAG = "SubtaskAdapter";

    private ArrayList<Subtask> Subtasks;
    private LayoutInflater inflater;

    public SubtaskAdapter(Context context) {
        Subtasks = new ArrayList<Subtask>();
        this.inflater = LayoutInflater.from(context);
    }

    public void updateParentData(int position) {}

    public ArrayList<Subtask> getSubtasks() {
        return Subtasks;
    }

    public void addSubtask(Subtask subtask) {
        Subtasks.add(subtask);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subtask_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Subtasks.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subtask subtask = Subtasks.get(position);

        Log.d(TAG, "onBindViewHolder: Name: " + holder.name.getText().toString());
        holder.name.setText(subtask.getName());
        holder.weight.setText(String.valueOf(subtask.getWeight()));
        holder.position = position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView weight;
        Button delete;
        int position;

        public ViewHolder(View view) {
            super(view);

            // Set UI buttons
            delete = view.findViewById(R.id.subtask_delete_button);
            name = view.findViewById(R.id.subtask_name);
            weight = view.findViewById(R.id.subtask_weight);

            // Delete subtask button handler
            delete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subtasks.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
