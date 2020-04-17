package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Subtask;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ViewSubtaskAdapter extends RecyclerView.Adapter<ViewSubtaskAdapter.ViewHolder>  {
    private static final String TAG = "TaskViewSubtaskAdapter";

    private List<Subtask> Subtasks;
    private ProgressBar progressBar;
    private int completionWeight = 0;
    private int currentWeight = 0;
    private MutableLiveData<Boolean> taskDone;

    public ViewSubtaskAdapter(Context context, ProgressBar progressBar) {
        Subtasks = new ArrayList<Subtask>();
        this.progressBar = progressBar;
        taskDone = new MutableLiveData<>();
        taskDone.setValue(false);
    }

    @NonNull
    @Override
    public ViewSubtaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewsubtask_item, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<Subtask> subtasks) {
        this.Subtasks = subtasks;
        notifyDataSetChanged();

        int maxWeight = 0;

        for (Subtask subtask : subtasks) {
            maxWeight += subtask.getWeight();
        }

        completionWeight = maxWeight;
        progressBar.setMax(completionWeight);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSubtaskAdapter.ViewHolder holder, int position) {
        Subtask subtask = Subtasks.get(position);

        Log.d(TAG, "onBindViewHolder: Name: " + holder.name.getText().toString());
        holder.name.setText(subtask.getName());
        holder.weight.setText(String.valueOf(subtask.getWeight()));
        holder.position = position;
        Log.d(TAG, "&--> onBindViewHolder: Position: " + position + "Name: " + subtask.getName());
    }

    public LiveData<Boolean> getTaskDone() {
        return taskDone;
    }

    @Override
    public int getItemCount() {
        return Subtasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView weight;
        CheckBox checkBox;
        int position;

        public ViewHolder(View view) {
            super(view);
            // Set UI buttons
            name = view.findViewById(R.id.viewSubtask_name);
            weight = view.findViewById(R.id.viewSubtask_weight);
            checkBox = view.findViewById(R.id.checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                    if (isChecked) {
                        currentWeight += Subtasks.get(position).getWeight();
                    }
                    else {
                        currentWeight -= Subtasks.get(position).getWeight();
                    }
                    progressBar.setProgress(currentWeight);
                    
                    if(currentWeight == completionWeight)
                    {
                        taskDone.setValue(true);
                    }
                }
            });
        }
    }
}