/*
 *	FILE			: PersonRepository.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the data repository for Person. It interacts with the
 *                    Person Data Access Object and stores data retrieved from it.
 */

package com.github.mpagconestoga.mad_a01.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.mpagconestoga.mad_a01.dao.PersonDao;
import com.github.mpagconestoga.mad_a01.objects.Database;
import com.github.mpagconestoga.mad_a01.objects.Person;

import java.util.List;

/*
 *  CLASS: PersonRepository
 *  DESCRIPTION: This class is a model in the MVVM framework. It used to hold data about a person.
 *              Using the person DAO, it retrieves data from the database, sending it to the view model.
 */
public class PersonRepository {

    private PersonDao personDao;
    private LiveData<List<Person>> allPersons;

    //Constructor used to initalize the DAO and get all the persons in the database
    public PersonRepository(Application application) {
        Database database = Database.getInstance(application);
        personDao = database.personDao();
        allPersons = personDao.getAllPersons();
    }

    /*
     *    METHOD      :     insert
     *    DESCRIPTION :     Inserts a new Category into the database
     *    PARAMETERS  :     Person person
     *    RETURNS     :     VOID
     * */
    public void insert(Person person) {
        new InsertPersonAsyncTask(personDao).execute(person);
    }
    /*
     *    METHOD      :     update
     *    DESCRIPTION :     Update a Person in the database
     *    PARAMETERS  :     Person person
     *    RETURNS     :     VOID
     * */
    public void update(Person person) {
        new UpdatePersonAsyncTask(personDao).execute(person);
    }
    /*
     *    METHOD      :     delete
     *    DESCRIPTION :     Delete a Person from the database
     *    PARAMETERS  :     Person person
     *    RETURNS     :     VOID
     * */
    public void delete(Person person) {
        new DeletePersonAsyncTask(personDao).execute(person);
    }
    /*
     *    METHOD      :     deleteAllPersons
     *    DESCRIPTION :     Delete all Persons from the database
     *    PARAMETERS  :     Person person
     *    RETURNS     :     VOID
     * */
    public void deleteAllPersons() {
        new DeleteAllPersonsAsyncTask(personDao).execute();
    }
    /*
     *    METHOD      :     getAllPersons
     *    DESCRIPTION :     Get a list of all the persons in the database
     *    PARAMETERS  :     NONE
     *    RETURNS     :     LiveData<List<Person>> ->list of all persons
     * */
    public LiveData<List<Person>> getAllPersons() {
        return allPersons;
    }

    /*
     *    METHOD      :     getPersonByName
     *    DESCRIPTION :     Retrieve person from the database, based on name input
     *    PARAMETERS  :     String name
     *    RETURNS     :     LiveData<List<Person>> ->list of all persons
     * */
    public LiveData<List<Person>> getPersonByName(String name){
        return personDao.getPersonByName(name);
    }

    //This class is an async task used to access the database in order to insert a new person
    private static class InsertPersonAsyncTask extends AsyncTask<Person, Void, Void> {

        private PersonDao personDao;

        private InsertPersonAsyncTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... persons) {
            personDao.insert(persons[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to update a Person
    private static class UpdatePersonAsyncTask extends AsyncTask<Person, Void, Void> {

        private PersonDao personDao;

        private UpdatePersonAsyncTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... persons) {
            personDao.update(persons[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete a Person
    private static class DeletePersonAsyncTask extends AsyncTask<Person, Void, Void> {

        private PersonDao personDao;

        private DeletePersonAsyncTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... persons) {
            personDao.delete(persons[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete all persons from the database
    private static class DeleteAllPersonsAsyncTask extends AsyncTask<Void, Void, Void> {

        private PersonDao personDao;

        private DeleteAllPersonsAsyncTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            personDao.deleteAllPersons();
            return null;
        }
    }
}
