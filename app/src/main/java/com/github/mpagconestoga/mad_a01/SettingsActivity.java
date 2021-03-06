/*
 *	FILE			: SettingsActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This file holds the activity used in the Settings page of the app
 */
package com.github.mpagconestoga.mad_a01;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.github.mpagconestoga.mad_a01.objects.SwipeButton;
import com.github.mpagconestoga.mad_a01.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {
    private Button deleteDataButton;
    private SwipeButton swipeButton;
    private TextView confirmationText;
    private SettingsViewModel settingsViewModel;
    private boolean deleted = false;

    /*
     *    METHOD      :     onCreate
     *    DESCRIPTION :     When the activity is created, widget members
     *                      are assigned. onclick listener is set for delete button data,
     *                      Delete data slider is hidden
     *    PARAMETERS  :     Bundle savedInstanceState
     *    RETURNS     :     VOID
     * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Get a list of task names using the content provider
        //populate list with names
        Uri tasksUri = Uri.parse("content://" + TaskContentProvider.AUTHORITY + "/Task");
        Log.d("MyApp", tasksUri.toString());
        Cursor cursor = getContentResolver().query(tasksUri,null,null,null,null);
        String[] columns = {"Name"};
        int[] to = {R.id.task_name};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.tasks,
                cursor,
                columns, to, 0
        );
        ListView myList = (ListView)findViewById(R.id.tasks_content_provider);
        myList.setAdapter(adapter);

        swipeButton = findViewById(R.id.swipeButton);
        confirmationText = findViewById(R.id.delete_data_textview);
        confirmationText.setVisibility(View.GONE);
        swipeButton.setVisibility(View.GONE);
        deleteDataButton = findViewById(R.id.deleteDataButton);
        deleteDataButton.setOnClickListener(new DeleteDataClickListener());
        settingsViewModel = new SettingsViewModel(this.getApplication());
    }

    /*
     *    METHOD      :     deleteAllData
     *    DESCRIPTION :     Calls the viewmodel to delete all the data in the databse
     *    PARAMETERS  :
     *    RETURNS     :     VOID
     * */
    public void deleteAllData(){
        settingsViewModel.deleteAllData();
    }

    //this onclick listener is used by the delete button on the settings page.
    //It sets the visibility of the button slider.
    private class DeleteDataClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            if(!deleted) {
                //first time clicking, show button slider
                swipeButton.setVisibility(View.VISIBLE);
                confirmationText.setVisibility(View.VISIBLE);
            }
            else {
                //Button has been clicked alread, reset position of button slider
                swipeButton.collapseButton();
            }
            deleted = true;
        }
    }
}



