/*
 *	FILE			: SubtaskAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the Recycler view of the assignment of the sub-tasks 
 *
 */
package com.github.mpagconestoga.mad_a01.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.MainActivity;
import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.github.mpagconestoga.mad_a01.objects.WeightFilter;

import java.util.ArrayList;
import java.util.Arrays;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ViewHolder> {
    private static final String TAG = "SubtaskAdapter";

    private ArrayList<Subtask> data;
    private LayoutInflater inflater;
    private Task parent;

    public SubtaskAdapter(Context context, ArrayList<Subtask> data, Task parent) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.parent = parent;
    }

    public void setData(ArrayList<Subtask> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateParentData(int position) {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subtask_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subtask subtask = data.get(position);

        Log.d(TAG, "onBindViewHolder: Name: " + holder.name.getText().toString());

        holder.name.setText(subtask.getName());
        holder.weight.setText(String.valueOf(subtask.getPriority()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText name;
        EditText weight;
        Button delete;
        Button assign;
        int position;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.subtask_name);
            weight = view.findViewById(R.id.subtask_weight);
            delete = view.findViewById(R.id.subtask_delete_button);
            assign = view.findViewById(R.id.subtask_assign_people);

            weight.setFilters(new InputFilter[]{ new WeightFilter(1, 5) });

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    data.get(position).setName(name.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String newVal = weight.getText().toString();
                    int weight = 1;

                    try {
                        weight = Integer.parseInt(newVal);
                    } catch (NumberFormatException ignored) { }

                    data.get(position).setPriority(weight);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            delete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            assign.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<Person> assigned = parent.getAssignedPeople();
                    final String[] listWorkers = new String[assigned.size()];
                    Log.d(TAG, "onClick: LIST WORKERS: " + listWorkers);
                    final boolean[] checkedWorkers = new boolean[assigned.size()];

                    final ArrayList<Integer> selected = new ArrayList<>();

                    for (int i = 0; i < assigned.size(); i++) {
                        Person person = assigned.get(i);
                        listWorkers[i] = person.getName();

                        if (data.get(position).getAssignedPeople().contains(person)) {
                            checkedWorkers[i] = true;
                            selected.add(i);
                        }
                        else {
                            checkedWorkers[i] = false;
                        }
                    }

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(inflater.getContext());  // alert dialog for the presentation of worker selection
                    mBuilder.setTitle("Workers available for task");    // dialog for the builder <--************ 27:38
                    mBuilder.setMultiChoiceItems(listWorkers, checkedWorkers, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) { // handles the checking of an item within the list
                            if (isChecked) {
                                if (!selected.contains(which)){ // if the current worker is not on the list, adds them to the list
                                    selected.add(which);
                                }
                            }
                            else if (selected.contains(which)) {  // removes worker on unchecking box
                                // We use removeAll since if you call remove on an ArrayList<Integer> with an int,
                                // it will assume that you want to remove an index and not a value.
                                selected.removeAll(Arrays.asList(which));
                            }
                        }
                    });
                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<Person> ass = data.get(position).getAssignedPeople();

                            Log.d(TAG, "onClick: assigned name is: " + data.get(position).getName());

                            // Add all selected workers to the subtask's list
                            for (int i = 0; i < checkedWorkers.length; i++) {
                                if (checkedWorkers[i] && !ass.contains(assigned.get(i))) {
                                    Log.d(TAG, "onClick: Assigned person: " + assigned.get(i).getName());
                                    ass.add(assigned.get(i));
                                }
                                else if (!checkedWorkers[i] && ass.contains(assigned.get(i))) {
                                    Log.d(TAG, "onClick: Unassigned person: " + assigned.get(i).getName());
                                    ass.remove(assigned.get(i));
                                }
                            }

                            Log.d(TAG, "onClick: Ass size: " + ass.size());

                            data.get(position).setAssignedPeople(ass);

                            Log.d(TAG, "onClick: getAssignedPeople size: " + data.get(position).getAssignedPeople().size());
                        }
                    });

                    mBuilder.create().show();

                    for (Person p : data.get(position).getAssignedPeople()) {
                        Log.d(TAG, "onClick: Assigned: " + p.getName());
                    }
                }
            });
        }
    }
}
