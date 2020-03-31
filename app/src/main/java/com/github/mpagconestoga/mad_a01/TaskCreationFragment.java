/*
 *	FILE			: TaskCreationFragment.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 06
 *	DESCRIPTION		: This is the fragment class for creating tasks
 */

package com.github.mpagconestoga.mad_a01;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.mpagconestoga.mad_a01.adapters.CategoryAdapter;
import com.github.mpagconestoga.mad_a01.adapters.MemberListAdapter;
import com.github.mpagconestoga.mad_a01.objects.Category;
import com.github.mpagconestoga.mad_a01.objects.HideKeyBoardUtility;
import com.github.mpagconestoga.mad_a01.objects.MemberListItem;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.viewmodel.CreateTaskViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TaskCreationFragment extends Fragment {
    // Date Format & TAG
    private static final String TAG = "TaskCreation";
    private static final DateFormat dateFormat = new SimpleDateFormat           // TODO: Change to a config file
                                ("yyyy-mm-dd hh:mm", Locale.CANADA);

    //---------- Attributes ----------//
    private CreateTaskViewModel viewModel;
    private Activity parentActivity;

    // UI Elements and Adapters
    private Button buttonDateTime;                  // Date and Time Picker
    private DatePickerDialog dateDialog;
    private TimePickerDialog timePicker;
    private Spinner categorySpinner;                // Category Drop-down
    private CategoryAdapter categoryAdapter;
    private ArrayList<MemberListItem> memberList;
    private ArrayList<Category> categoryList;
    private RecyclerView memberListRecyclerView;    // Task Member List
    private MemberListAdapter memberListAdapter;
    private RecyclerView.LayoutManager memberListLayoutManager;

    // Task Attributes
    private EditText taskNameEditText;
    private Category taskCategory;
    private Date taskEndTime = null;

    //---------- Lifecycle methods ----------//
    // FUNCTION   : onCreateView
    // DESCRIPTION: Initiate the UI elements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        // Get parent activity
        parentActivity = getActivity();
        if(parentActivity == null) {
            Log.d(TAG, "Unexpected error: Fragment Parent activity does not exists");
            return null;
        }

        memberList = new ArrayList<>();
        categoryList = new ArrayList<>();

        // Setup UI elements
        taskNameEditText = view.findViewById(R.id.newTaskName);

        // Category Drop-down builder
        categorySpinner = view.findViewById(R.id.spinner_select_category);
        categoryAdapter = new CategoryAdapter(parentActivity, categoryList);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskCategory = (Category) parent.getItemAtPosition(position);
                HideKeyBoardUtility.hideKeyboard(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Assigned People to task (Member List viewer)
        memberListRecyclerView = view.findViewById(R.id.memberList);
        memberListRecyclerView.setHasFixedSize(true); //true if wont change in size
        memberListLayoutManager = new LinearLayoutManager(parentActivity);
        memberListAdapter = new MemberListAdapter(memberList);

        memberListRecyclerView.setLayoutManager(memberListLayoutManager);
        memberListRecyclerView.setAdapter(memberListAdapter);

        memberListAdapter.setOnItemClickListener(new MemberListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position)
            {
                removeItem(position);
            }

        });

        // Date & Time picker builder
        buttonDateTime = view.findViewById(R.id.button_dateTime);
        dateTimeDialogBuilder(view);

        // Insert/Search Person Activity
        Button buttonAddPerson = view.findViewById(R.id.button_insert_person);
        buttonAddPerson.setOnClickListener(new AddMemberClickListener());

        // Create Current Task Button
        Button buttonCreateTask = view.findViewById(R.id.button_create_task);
        buttonCreateTask.setOnClickListener(new CreateTaskClickListener());

        return view;
    }

    // FUNCTION   : onActivityCreated
    // DESCRIPTION: Initializes or grabs a existing the viewModel
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get or Create ViewModel
        viewModel = new ViewModelProvider((ViewModelStoreOwner) parentActivity).get(CreateTaskViewModel.class);
        populateCategoryList();
        Log.d(TAG, "&--> Task Creation Address: " + viewModel);
    }

    // FUNCTION   : onActivityCreated
    // DESCRIPTION: Initializes or grabs a existing the viewModel
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            Person selectedPerson = data.getParcelableExtra("selected");

            assert selectedPerson != null;
            if(insertItem(0, selectedPerson.getName())){
                viewModel.addPerson(selectedPerson);
                //only add person if they are not in the list.
            }
        }
    }

    //---------- Other Methods----------//
    // FUNCTION   : CreateTaskClickListener
    // DESCRIPTION: Validates the task fields, creates a task in the viewModel, moves to the next
    //              screen if successful
    private class CreateTaskClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            // Get task attributes
            Category taskCategory = (Category)categorySpinner.getSelectedItem();
            String taskName = taskNameEditText.getText().toString().trim();

            // Input-Field Validation
            if (taskName.trim().length() == 0) {
                Toast.makeText(parentActivity, R.string.enter_task_name, Toast.LENGTH_SHORT).show();
                return;
            }
            else if (memberList.size() <= 0) {
                Toast.makeText(parentActivity, R.string.enter_team_member, Toast.LENGTH_SHORT).show();
                return;
            }
            else if(taskEndTime == null) {
                Toast.makeText(parentActivity, R.string.enter_datetime, Toast.LENGTH_SHORT).show();
                return;
            }

            // Set the current task in the viewModel to prepare for subtask creation
            viewModel.setCurrentTask(taskName, taskCategory, taskEndTime);
            Log.d(TAG, "--> Current Task Created -- Name: " + viewModel.getCurrentTask().getName());

            // Move to Sub-Task Fragment
            FragmentManager manager = getParentFragmentManager();
            manager.beginTransaction().replace(R.id.TaskCreationFragment, new CreateSubtasksFragment())
                    .commit();
        }
    }

    // FUNCTION   : AddMemberClickListener
    // DESCRIPTION: Add a person to the member list adapter and therefore in the task's assigned people
    private class AddMemberClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent searchIntent = new Intent(parentActivity, PersonSearchActivity.class);
            startActivityForResult(searchIntent, 1);
        }
    }

    // FUNCTION   : dateTimeDialogBuilder
    // DESCRIPTION: Sets up a date and time picker for the task endtime
    private void dateTimeDialogBuilder(View view) {
        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyBoardUtility.hideKeyboard(view);
                // Set up calendar -> used to initialized values for the time & date picker
                final Calendar calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH);
                final int year = calendar.get(Calendar.YEAR);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                // Create date dialog
                dateDialog = new DatePickerDialog(parentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // When date is set -> save the values and popup the time dialog
                    public void onDateSet(DatePicker datePicker, final int endYear, final int endMonth, final int endDay) {
                        timePicker = new TimePickerDialog(parentActivity, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int endHour, int endMinute) {
                                // Construct Date value for EndDate Task attribute
                                calendar.set(endYear, (endMonth  - 1), endDay, endHour, endMinute);
                                taskEndTime = calendar.getTime();
                                buttonDateTime.setText(dateFormat.format(taskEndTime));
                            }
                        }, hour, minute, true);
                        timePicker.show();
                    }
                }, day, month, year);

                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });
    }


    // FUNCTION    : insertItem
    // DESCRIPTION :  Add a new memberListItem object to the recyclerView
    private boolean insertItem(int position, String name) {
        for (MemberListItem mli : memberList) {
            if (mli.getmName().equals(name)) {
                Toast.makeText(this.getContext(), getResources().getString(R.string.person_already_added), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        memberList.add(position, new MemberListItem(R.drawable.user, name, R.drawable.ic_delete));
        memberListAdapter.notifyItemInserted(position);
        return true;
    }

    // FUNCTION    : removeItem
    // DESCRIPTION : Removes the object from the recyclerView
    private void removeItem(int position) {
        memberList.remove(position);
        memberListAdapter.notifyItemRemoved(position);
        viewModel.removePerson(position);

    }

    // FUNCTION    : removeItem
    // DESCRIPTION : Removes the object from the recyclerView
    private void populateCategoryList(){
        categoryList.addAll(viewModel.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
    }

}
