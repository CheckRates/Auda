/*
 *	FILE			: SettingsViewModel.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the ViewModel for searching for Settings page
 */

package com.github.mpagconestoga.mad_a01.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.github.mpagconestoga.mad_a01.objects.Category;
import com.github.mpagconestoga.mad_a01.repositories.CategoryRepository;
import com.github.mpagconestoga.mad_a01.repositories.PersonRepository;
import com.github.mpagconestoga.mad_a01.repositories.SubtaskRepository;
import com.github.mpagconestoga.mad_a01.repositories.TaskRepository;

/*
 *   CLASS       : SettingsViewModel,java
 *   DESCRIPTION : ViewModel for the activity responsible for the Settings Activity page.
 *                 In the settings page we are able to delete all the data in the database
 *                  and there is information about the app
 */
public class SettingsViewModel extends AndroidViewModel {
    private Application application;

    // Constructor
    public SettingsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    // FUNCTION   : deleteAllData
    // DESCRIPTION: Deletes all the data from the app
    public void deleteAllData(){
        new PersonRepository(application).deleteAllPersons();
        new TaskRepository(application).deleteAllTasks();
        new SubtaskRepository(application).deleteAllSubtasks();
        Toast.makeText(application, "All Data Deleted", Toast.LENGTH_LONG).show();

    }
}
