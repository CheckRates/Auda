/*
 *	FILE			: PersonSearchViewModel.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the ViewModel for searching for people. Specifically the
 *                    PersonSearchActivity
 */

package com.github.mpagconestoga.mad_a01.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.repositories.PersonRepository;

import java.util.List;

/*
 *   CLASS       : PersonSearchViewModel.java
 *   DESCRIPTION : ViewModel for the activity responsible for searching the database for adding to a task.
 *                 Contains Repositories for Person.
 */
public class PersonSearchViewModel extends AndroidViewModel {
    private PersonRepository peopleRepository;
    private LiveData<List<Person>> allPeople;

    // Constructor
    public PersonSearchViewModel(@NonNull Application application) {
        super(application);
        peopleRepository = new PersonRepository(application);
        allPeople = peopleRepository.getAllPersons();
    }

    // Getter and Setter
    public LiveData<List<Person>> getAllPersons() {
        return allPeople;
    }
    
    public void addPerson(String name){
        peopleRepository.insert(new Person(name));
    }
}
