/*
 *	FILE			: SubtaskRepository.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the data repository for Subtask. It interacts with the
 *                    Subtask Data Access Object and stores data retrieved from it.
 */

package com.github.mpagconestoga.mad_a01.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.mpagconestoga.mad_a01.dao.SubtaskDao;
import com.github.mpagconestoga.mad_a01.objects.Database;
import com.github.mpagconestoga.mad_a01.objects.Subtask;

import java.util.List;
/*
 *  CLASS: SubtaskRepository
 *  DESCRIPTION: This class is a model in the MVVM framework. It used to hold data about the subtasks.
 *              Using the subtask DAO, it retrieves data from the database, sending it to the view model.
 */
public class SubtaskRepository {

    private SubtaskDao subTaskDao;

    //Constructor used to get database instance, and initialize the DAO
    public SubtaskRepository(Application application) {
        Database database = Database.getInstance(application);
        subTaskDao = database.subtaskDao();
    }
    /*
     *    METHOD      :     insert
     *    DESCRIPTION :     Inserts a new subtask into the database
     *    PARAMETERS  :     Subtask subtask
     *    RETURNS     :     VOID
     * */
    public void insert(Subtask subtask) {
        new SubtaskRepository.InsertSubtaskAsyncTask(subTaskDao).execute(subtask);
    }
    /*
     *    METHOD      :     update
     *    DESCRIPTION :     Update a subtask in the database
     *    PARAMETERS  :     Subtask subtask
     *    RETURNS     :     VOID
     * */
    public void update(Subtask subtask) {
        new SubtaskRepository.UpdateSubtaskAsyncTask(subTaskDao).execute(subtask);
    }
    /*
     *    METHOD      :     delete
     *    DESCRIPTION :     Delete a subtask from the database
     *    PARAMETERS  :     Subtask subtask
     *    RETURNS     :     VOID
     * */
    public void delete(Subtask subtask) {
        new SubtaskRepository.DeleteSubtaskAsyncTask(subTaskDao).execute(subtask);
    }
    /*
     *    METHOD      :     deleteAllSubtasks
     *    DESCRIPTION :     Delete all subtasks from the database
     *    PARAMETERS  :     NONE
     *    RETURNS     :     VOID
     * */
    public void deleteAllSubtasks() {
        new SubtaskRepository.DeleteAllSubtasksAsyncTask(subTaskDao).execute();
    }
    /*
     *    METHOD      :     getSubtasksByTaskId
     *    DESCRIPTION :     Retrieve subtasks of a task, based on task Id
     *    PARAMETERS  :     int taskId
     *    RETURNS     :     List<Subtask>
     * */
    public List<Subtask> getSubtasksByTaskId(int taskId) {
        return subTaskDao.getSubtasksbyTaskId(taskId);
    }

    //This class is an async task used to access the database in order to insert a new subtask
    private static class InsertSubtaskAsyncTask extends AsyncTask<Subtask, Void, Void> {

        private SubtaskDao subtaskDao;

        private InsertSubtaskAsyncTask(SubtaskDao subtaskDao) {
            this.subtaskDao = subtaskDao;
        }

        @Override
        protected Void doInBackground(Subtask... subtasks) {
            subtaskDao.insert(subtasks[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to update a subtask
    private static class UpdateSubtaskAsyncTask extends AsyncTask<Subtask, Void, Void> {

        private SubtaskDao subtaskDao;

        private UpdateSubtaskAsyncTask(SubtaskDao subtaskDao) {
            this.subtaskDao = subtaskDao;
        }

        @Override
        protected Void doInBackground(Subtask... subtasks) {
            subtaskDao.update(subtasks[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete a subtask
    private static class DeleteSubtaskAsyncTask extends AsyncTask<Subtask, Void, Void> {

        private SubtaskDao subtaskDao;

        private DeleteSubtaskAsyncTask(SubtaskDao subtaskDao) {
            this.subtaskDao = subtaskDao;
        }

        @Override
        protected Void doInBackground(Subtask... subtasks) {
            subtaskDao.delete(subtasks[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete all subtasks from the database
    private static class DeleteAllSubtasksAsyncTask extends AsyncTask<Void, Void, Void> {

        private SubtaskDao subtaskDao;

        private DeleteAllSubtasksAsyncTask(SubtaskDao subtaskDao) {
            this.subtaskDao = subtaskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subtaskDao.deleteAllSubtasks();
            return null;
        }
    }
}