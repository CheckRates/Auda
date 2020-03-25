/*
 *	FILE			: OnGoingSubtaskAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the Recycler view of the display of the in progress subtasks of one task
 *
 */


package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.util.ArrayList;

public class OnGoingSubtaskAdapter extends RecyclerView.Adapter<OnGoingSubtaskAdapter.ViewHolder> {
    private static final String TAG = "OnGoingSubtask";

    private ArrayList<Subtask> data;
    private LayoutInflater inflater;
    private Context context;
    private Task parent;
    private ProgressBar progressBar;

    private int completionWeight = 0;
    private int currentWeight = 0;

    public OnGoingSubtaskAdapter(Context context, ArrayList<Subtask> data, Task parent, ProgressBar progressBar) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.parent = parent;
        this.progressBar = progressBar;

        for (Subtask st : data) {
            Log.d(TAG, "OnGoingSubtaskAdapter: SUBTASK " + st.getName());
            completionWeight += st.getPriority();
        }

        progressBar.setMax(completionWeight);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_subtask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Name: " + holder.name.getText().toString());

        Subtask subtask = data.get(position);
        holder.name.setText(subtask.getName());
        holder.weight.setText(String.format("%s %s", context.getResources().getString(R.string.weight_view), String.valueOf(subtask.getPriority())));
        holder.position = position;

        // Display list of people from that subtask
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView weight;
        CheckBox done;
        int position;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txt_subTask);
            weight = view.findViewById(R.id.txtWeight);
            done = view.findViewById(R.id.subTaskcheckBox);

            done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                    if (isChecked) {
                        currentWeight += data.get(position).getPriority();
                    }
                    else {
                        currentWeight -= data.get(position).getPriority();
                    }

                    progressBar.setProgress(currentWeight);
                }
            });
        }
    }
}