/*
 *	FILE			: PersonSearchActivity.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Logic for adding and searching members that will be assigned to the task
 *
 */

package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.PersonSearchAdapter;
import com.github.mpagconestoga.mad_a01.objects.Person;

import java.util.ArrayList;

public class PersonSearchActivity extends AppCompatActivity {
    private static final String TAG = "PersonSearchActivity";

    private Intent returnIntent;
    private EditText searchBox;
    private RecyclerView results;
    private Button done;
    private ArrayList<Person> allPeople;
    private ArrayList<Person> filteredPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_search);

        returnIntent = new Intent();

        searchBox = findViewById(R.id.personSearchBox);
        searchBox.addTextChangedListener(new SearchBoxKeyListener());

        allPeople = Person.getAllPeople();
        filteredPeople = new ArrayList<>(allPeople);

        results = findViewById(R.id.results);
        results.setHasFixedSize(true);
        results.setLayoutManager(new LinearLayoutManager(this));
        results.setAdapter(new PersonSearchAdapter(this, filteredPeople));

        done = findViewById(R.id.button_done);
        done.setOnClickListener(new DoneClickListener());
    }

    public class SearchBoxKeyListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            String filter = searchBox.getText().toString();

            // Clear array to clear previous results
            filteredPeople.clear();

            // Determine which people match the input provided
            for (Person person : allPeople) {
                if (person.getName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredPeople.add(person);
                }
            }

            if ((filteredPeople.size() == 0 || !Person.personExists(filter)) && filter.length() > 0) {
                filteredPeople.add(new Person(getResources().getString(R.string.person_search_create) + " '" + searchBox.getText().toString() + "'", false));
            }

            if (results.getAdapter() == null) {
                return;
            }

            results.getAdapter().notifyDataSetChanged();
        }
    }

    public class DoneClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: Done button clicked");
            
            if (results.getAdapter() == null) {
                Log.e(TAG, "onClick: Adapter was null");
                return;
            }

            Log.d(TAG, "onClick: SELECTED POSITION IS CURRENTLY " + ((PersonSearchAdapter) results.getAdapter()).selectedPosition);

            if ((searchBox.getText().toString().length() == 0 && ((PersonSearchAdapter) results.getAdapter()).selectedPosition == -1) || ((PersonSearchAdapter) results.getAdapter()).selectedPosition == -1) {
                Toast.makeText(PersonSearchActivity.this, R.string.person_search_select, Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "onClick: filteredPeople size: " + filteredPeople.size());

            Person selected = filteredPeople.get(((PersonSearchAdapter) results.getAdapter()).selectedPosition);

            Log.d(TAG, "onClick: Selected person: " + selected.getName());

            if (!allPeople.contains(selected)) {
                returnIntent.putExtra("create", true);

                if (searchBox.getText().toString().trim().length() == 0) {
                    returnIntent.putExtra("selected", selected.getName());
                }
                else {
                    returnIntent.putExtra("selected", searchBox.getText().toString());
                }
            }
            else {
                returnIntent.putExtra("create", false);

                if (searchBox.getText().toString().trim().length() == 0) {
                    returnIntent.putExtra("selected", selected.getName());
                }
                else {
                    returnIntent.putExtra("selected", searchBox.getText().toString());
                }
            }

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
