/*
 *	FILE			: CreateTaskFragment.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: This file contains the fragment of the screen used to create a task and
 *                   add members to the task.
 *
 */

package com.github.mpagconestoga.mad_a01;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
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
import com.github.mpagconestoga.mad_a01.objects.CategoryItem;
import com.github.mpagconestoga.mad_a01.objects.MemberListItem;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreateTaskFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private ArrayList<MemberListItem> memberList;               // Member List
    private RecyclerView memberListRecyclerView;                //
    private MemberListAdapter mListAdapter;                     //
    private RecyclerView.LayoutManager memberListLayoutManager; //
    private Button buttonInsert;                                // Task Info
    private Button buttonCreateTask;                            //
    private EditText nameEditText;                              //
    private EditText taskNameEditText;                          //
    private ArrayList<Person> assignedPeople;                   //
    private ArrayList<CategoryItem> categoryList;               //
    private Spinner spinnerCategories;                          //
    private CategoryAdapter categoryAdapter;                    //
    private Button buttonDateTime;                              // Date Time
    private DatePickerDialog dateDialog;                        //
    private TimePickerDialog timePicker;                        //
    private Date endTime;                                       //
    private View view;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.CANADA);

    public CreateTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        createMemberList();
        buildRecyclerView();
        setButtons();
        endTime = null;

        // CHECK INTO LATER
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        initList();
        dateTimeDialogBuilder();
        assignedPeople = new ArrayList<Person>();
        spinnerCategories = view.findViewById(R.id.selectTaskSpinner);
        taskNameEditText = view.findViewById(R.id.addTaskEditText);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem selectedItem = (CategoryItem)parent.getItemAtPosition(position);
                String selectedItemName = selectedItem.getCategoryName();
                Toast.makeText(getActivity(), selectedItemName + " " + R.string.selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;

    }

    /*
     *    METHOD      :   initList
     *    DESCRIPTION :   set up the categoryList. Using static categories for now
     *    PARAMETERS  :
     *    RETURNS     :   void
     * */
    private void initList()
    {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryItem("Test 1"));
        categoryList.add(new CategoryItem("Test 2"));
        categoryList.add(new CategoryItem("Test 3"));

    }

    private void dateTimeDialogBuilder() {
        buttonDateTime = view.findViewById(R.id.button_dateTime);

        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up calendar -> used to initialized values for the time & date picker
                final Calendar calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH);
                final int year = calendar.get(Calendar.YEAR);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                // Create date dialog
                dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // When date is set -> save the values and popup the time dialog
                    public void onDateSet(DatePicker datePicker, final int endYear, final int endMonth, final int endDay) {
                        timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int endHour, int endMinute) {
                                // Construct Date value for EndDate Task attribute
                                calendar.set(endYear, (endMonth  - 1), endDay, endHour, endMinute);
                                endTime = calendar.getTime();
                                buttonDateTime.setText(dateFormat.format(endTime));
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

    /*
     *    METHOD      :   insertItem
     *    DESCRIPTION :   Add a new memberListItem object to the recyclerView
     *    PARAMETERS  :   int position - position where it should be added
     *                    String name -> name of the person to be added to the list
     *    RETURNS     :   void
     * */
    private void insertItem(int position, String name)
    {
        for (MemberListItem mli : memberList) {
            if (mli.getmName().equals(name)) {
                Toast.makeText(this.getContext(), getResources().getString(R.string.person_already_added), Toast.LENGTH_LONG).show();
                return;
            }
        }

        memberList.add(position, new MemberListItem(R.drawable.user, name, R.drawable.ic_delete));
        mListAdapter.notifyItemInserted(position);
    }

    private void removeItem(int position)
    {
        memberList.remove(position);
        mListAdapter.notifyItemRemoved(position);
    }

    /*
     *    METHOD      :   createMemberList
     *    DESCRIPTION :   Initialize the memberList that holds the
     *                    list of members currently assigned to a task
     *    PARAMETERS  :
     *    RETURNS     :   void
     * */
    private void createMemberList()
    {
        memberList = new ArrayList<>();
    }

    /*
     *    METHOD      :   buildRecyclerView
     *    DESCRIPTION :   set up the recyclerView logic, set adapters and listeners
     *    PARAMETERS  :
     *    RETURNS     :   void
     * */
    private void buildRecyclerView()
    {
        memberListRecyclerView = view.findViewById(R.id.memberList);
        memberListRecyclerView.setHasFixedSize(true); //true if wont change in size
        memberListLayoutManager = new LinearLayoutManager(getActivity());
        mListAdapter = new MemberListAdapter(memberList);


        memberListRecyclerView.setLayoutManager(memberListLayoutManager);
        memberListRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setOnItemClickListener(new MemberListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position)
            {
                removeItem(position);
            }

        });
    }

    /*
     *    METHOD      :   setButtons
     *    DESCRIPTION :   set up the buttons, finding Views and setting the onclick listeners
     *    PARAMETERS  :
     *    RETURNS     :   void
     * */
    private void setButtons()
    {
        buttonCreateTask = view.findViewById(R.id.CreateTaskButton);
        buttonInsert = view.findViewById(R.id.button_insert);

        buttonCreateTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CategoryItem selectedTask = (CategoryItem)spinnerCategories.getSelectedItem();
                String taskCategory = selectedTask.getCategoryName();
                String taskName = taskNameEditText.getText().toString().trim();

                if (taskName.trim().length() == 0) {
                    Toast.makeText(getActivity(), R.string.enter_task_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (memberList.size() <= 0) {
                    Toast.makeText(getActivity(), R.string.enter_team_member, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(endTime == null) {
                    Toast.makeText(getActivity(), R.string.enter_datetime, Toast.LENGTH_SHORT).show();
                    return;
                }


                // Create Task
                CreateTaskActivity.newTask = new Task(taskName, taskCategory, endTime, assignedPeople);
                Log.d(TAG, "----------> Task Created:" + CreateTaskActivity.newTask.getName() + "\n" + assignedPeople);

                // Change Fragment
                FragmentManager fragManager = getFragmentManager();
                fragManager.beginTransaction().replace(R.id.TaskCreationFragment, new CreateSubtasksFragment())
                        .commit();
            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent searchIntent = new Intent(getActivity(), PersonSearchActivity.class);
                startActivityForResult(searchIntent, 1);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case -1:
                String personName = data.getStringExtra("selected");

                Log.d(TAG, "onActivityResult: " + personName);

                Person selected = null;

                // Check if we need to create this person or if they already exist
                if (data.getBooleanExtra("create", false)) {
                    Log.d(TAG, "onActivityResult: Created new person '" + personName + "'");
                    selected = new Person(personName);
                } else {
                    Log.d(TAG, "onActivityResult: Retrieved person: " + personName);
                    selected = Person.getPerson(personName);
                }

                if (selected == null) {
                    return;
                }

                Log.d(TAG, "onActivityResult: Selected Name: " + selected.getName());
                assignedPeople.add(selected);
                insertItem(0, selected.getName());

                break;
        }
    }
}
