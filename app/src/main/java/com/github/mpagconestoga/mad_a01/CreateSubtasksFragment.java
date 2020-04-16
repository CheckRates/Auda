/*
 *	FILE			: CreateSubtasksFragment.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Fragment screen to add subtasks to the task, defining their workers, weight and name
 *
 */

package com.github.mpagconestoga.mad_a01;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.SubtaskAdapter;
import com.github.mpagconestoga.mad_a01.objects.HideKeyBoardUtility;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.viewmodel.CreateTaskViewModel;

import java.util.ArrayList;


public class CreateSubtasksFragment extends Fragment {
    // TAG
    private static final String TAG = "CreateSubtasksFragment";

    //---------- Attributes ----------//
    private CreateTaskViewModel viewModel;
    private Activity parentActivity;
    private View view;

    // UI Elements
    private Button addSubtask;
    private NumberPicker weight;
    private SubtaskAdapter adapter;
    private RecyclerView subtasks;
    private Button finishTaskCreation;
    private EditText subtaskNameEditText;

    //---------- Lifecycle methods ----------//
    // FUNCTION   : onCreateView
    // DESCRIPTION: Initiate the UI elements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_subtasks, container, false);

        // Set up RecyclerView and Adapter for the Subtasks list
        subtasks = view.findViewById(R.id.subtasks);
        subtasks.setHasFixedSize(true);
        subtasks.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new SubtaskAdapter(view.getContext());
        subtasks.setAdapter(adapter);

        // Add subtask button
        addSubtask = view.findViewById(R.id.subtasks_button_add);
        addSubtask.setOnClickListener(new AddSubtaskClickListener());

        subtaskNameEditText = view.findViewById(R.id.subtask_name_edittext);
        weight = view.findViewById(R.id.subtask_number_picker);
        weight.setMinValue(1);
        weight.setMaxValue(5);
        weight.setWrapSelectorWheel(true);
        weight.setValue(2);

        // Finilize Task Button Listener(Complete Task)
        finishTaskCreation = view.findViewById(R.id.button_create_finalTask);
        finishTaskCreation.setOnClickListener(new CreateFinalTaskClickListener());

        return view;
    }

    // FUNCTION   : onActivityCreated
    // DESCRIPTION: Gets the Activity and initiates or grab the viewModel
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get Parent activity
        parentActivity = getActivity();
        if (parentActivity == null) {
            Log.d(TAG, "Unexpected error: Fragment Parent activity does not exists");
            return;
        }

        // Create/Get Shared ViewModel
        viewModel = new ViewModelProvider((ViewModelStoreOwner) parentActivity).get(CreateTaskViewModel.class);
        Log.d(TAG, "&--> Subtask Creation Address: " + viewModel);
    }


    //---------- OnClick Listeners & Handlers----------//
    // FUNCTION   : AddSubtaskClickListener
    // DESCRIPTION: Creates a subtask object and adds to the list
    public class AddSubtaskClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: AddSubtask button clicked");

            // Input Validation
            String subtaskName = subtaskNameEditText.getText().toString().trim();
            if(subtaskName.isEmpty()){
                Toast.makeText(view.getContext(), "Please fill out the name of the subtask", Toast.LENGTH_LONG).show();
                return;
            }
            int subtaskWeight = weight.getValue();
            if (subtaskWeight < 1){
                Toast.makeText(view.getContext(), "Please fill out the weight of the subtask", Toast.LENGTH_LONG).show();
                return;
            }

            HideKeyBoardUtility.hideKeyboard(v);

            // Add subtask to adapter
            adapter.addSubtask(new Subtask(subtaskWeight, subtaskName));
            Log.d(TAG, "onClick: currentSubtasks size: " + adapter.getItemCount());

            // Reset input fields
            subtaskNameEditText.setText("");
            weight.setValue(1);
            subtaskNameEditText.requestFocus();
        }
    }

    // FUNCTION   : CreateFinalTaskClickListener
    // DESCRIPTION: Validates that there at least one subtask and creates a task if so
    private class CreateFinalTaskClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            if(adapter.getItemCount() == 0){
                Toast.makeText(viewModel.getApplication(), "Please enter at least one subtask", Toast.LENGTH_SHORT).show();
                return;
            }

            // Assign subtask to current task
            openConfirmation();
            viewModel.setCurrentSubtasks(adapter.getSubtasks());


        }
    }
    // FUNCTION   : createTaskAndFinish
    // DESCRIPTION: Submit the task to the database and close the activity
    public void createTaskAndFinish()
    {
        // Add task to database and quit activity
        viewModel.createTask();
        parentActivity.finish();
    }
    // FUNCTION   : openConfirmation
    // DESCRIPTION: open the confirmation dialog when the user clicks
    //              button to submit the task
    private void openConfirmation(){
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.show(getChildFragmentManager(), "Confirm Task Creation");
    }
}
