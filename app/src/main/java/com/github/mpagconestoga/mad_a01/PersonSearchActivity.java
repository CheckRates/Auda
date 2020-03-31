/*
 *	FILE			: PersonSearchActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Logic for adding and searching members that will be assigned to the task
 *
 */

package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.PersonSearchAdapter;
import com.github.mpagconestoga.mad_a01.objects.HideKeyBoardUtility;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.viewmodel.PersonSearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class PersonSearchActivity extends AppCompatActivity {
    private static final String TAG = "PersonSearchActivity";

    private final PersonSearchAdapter adapter = new PersonSearchAdapter();
    private PersonSearchViewModel viewModel;
    private Intent returnIntent;
    private SearchView searchBox;
    private RecyclerView results;
    private Button done;
    private Button addNewPerson;

    // FUNCTION   : onCreate
    // DESCRIPTION: Initiates UI components and grabs the viewModel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_search);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(PersonSearchViewModel.class);
        returnIntent = new Intent();

        addNewPerson = findViewById(R.id.addNewPersonButton);
        addNewPerson.setVisibility(View.GONE);
        searchBox = findViewById(R.id.personSearchBox);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d(TAG, "filtered empty");
                if (!adapter.getNames().contains(newText.toLowerCase().trim()) && searchBox.getQuery().toString().trim().length() != 0){
                    addNewPerson.setVisibility(View.VISIBLE);

                }
                else{
                    addNewPerson.setVisibility(View.GONE);

                }
                return false;
            }
        });
        addNewPerson.setOnClickListener(new NewPersonClickListener());
        Log.d(TAG, "onCreate: GETTING PASSED IN PEOPLE NOW");

        results = findViewById(R.id.results);
        results.setHasFixedSize(true);
        results.setLayoutManager(new LinearLayoutManager(this));

        results.setAdapter(adapter);
        viewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                adapter.setPersons(people);
            }
        });

        done = findViewById(R.id.button_done);
        done.setOnClickListener(new DoneClickListener());
    }

    // FUNCTION   : NewPersonClickListener
    // DESCRIPTION: Adds new person to the Person database
    public class NewPersonClickListener implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            String name = searchBox.getQuery().toString().trim();
            if (name.length() > 0){
                viewModel.addPerson(name);
                searchBox.setQuery("", false);
                adapter.setPosition(0);
                HideKeyBoardUtility.hideKeyboard(v);
            }

        }
    }

    // FUNCTION   : DoneClickListener
    // DESCRIPTION: Returns to the calling activity, proving the selected person
    public class DoneClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: Done button clicked");
            RecyclerView results;
            results = findViewById(R.id.results);
            int selectedItemPosition = ((PersonSearchAdapter) results.getAdapter()).selectedPosition;

            if ( selectedItemPosition == -1) {
                Toast.makeText(PersonSearchActivity.this, R.string.person_search_select, Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<Person> list = new ArrayList<>(adapter.getFilteredList());
            Person selectedPerson = list.get(selectedItemPosition);
            returnIntent.putExtra("selected", selectedPerson);
            setResult(Activity.RESULT_OK, returnIntent);
            HideKeyBoardUtility.hideKeyboard(v);
            finish();
        }
    }
}
