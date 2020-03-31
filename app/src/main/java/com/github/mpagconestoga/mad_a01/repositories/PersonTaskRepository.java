/*
 *	FILE			: PersonTaskRepository.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the data repository for PersonTask. It interacts with the
 *                    PersonTask Data Access Object and stores data retrieved from it.
 */

package com.github.mpagconestoga.mad_a01.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.mpagconestoga.mad_a01.dao.PersonTaskDao;
import com.github.mpagconestoga.mad_a01.objects.Database;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.PersonTask;

import java.util.List;

/*
 *  CLASS: PersonTaskRepository
 *  DESCRIPTION: This class is a model in the MVVM framework. It used to hold data about the person_task table.
 *              Using the persontask DAO, it retrieves data from the database, sending it to the view model.
 */
public class PersonTaskRepository {

    private PersonTaskDao personTaskDao;

    //Constructor used to get database instance, and initialize the DAO
    public PersonTaskRepository(Application application){
        Database database = Database.getInstance(application);
        personTaskDao = database.personTaskDao();
    }
    /*
     *    METHOD      :     insert
     *    DESCRIPTION :     Inserts a new PersonTask into the database
     *    PARAMETERS  :     PersonTask personTask
     *    RETURNS     :     VOID
     * */
    public void insert(PersonTask personTask){
        new PersonTaskRepository.InsertPersonTaskAsyncTask(personTaskDao).execute(personTask);
    }

    /*
     *    METHOD      :     getPersonsByTaskId
     *    DESCRIPTION :     Returns a list of persons, based on the Id of a task
     *    PARAMETERS  :     int taskId -> Id of task
     *    RETURNS     :     List<Person>
     * */
    public List<Person> getPersonsByTaskId(int taskId){
        return personTaskDao.GetPersonsByTaskId(taskId);
    }

    //This class is an async task used to access the database in order to insert a new personTask row
    private static class InsertPersonTaskAsyncTask extends AsyncTask<PersonTask, Void, Void>{

        private PersonTaskDao personTaskDao;

        private InsertPersonTaskAsyncTask(PersonTaskDao personTaskDao){
            this.personTaskDao = personTaskDao;
        }
        @Override
        protected Void doInBackground(PersonTask... personTasks) {
            personTaskDao.insert(personTasks[0]);
            return null;
        }
    }
}
