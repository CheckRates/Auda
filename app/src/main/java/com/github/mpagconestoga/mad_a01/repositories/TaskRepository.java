/*
 *	FILE			: TaskRepository.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the data repository for Task. It interacts with the
 *                    Task Data Access Object and stores data retrieved from it.
 */

package com.github.mpagconestoga.mad_a01.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.github.mpagconestoga.mad_a01.dao.TaskDao;
import com.github.mpagconestoga.mad_a01.objects.Database;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.PersonTask;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.util.ArrayList;
import java.util.List;
/*
 *  CLASS: TaskRepository
 *  DESCRIPTION: This class is a model in the MVVM framework. It used to hold data about the Task.
 *              Using the subtask DAO, it retrieves data from the database, sending it to the view model.
 */
public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    public Task task;

    //Constructor used to get database instance, and initialize the DAO, get all Tasks
    public TaskRepository(Application application){
        Database database = Database.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    /*
     *    METHOD      :     insert
     *    DESCRIPTION :     Inserts a new task into the database
     *    PARAMETERS  :     Task task
     *    RETURNS     :     VOID
     * */
    public void insert(Task task){
        new TaskRepository.InsertTaskAsyncTask(taskDao).execute(task);
    }

    /*
     *    METHOD      :     insertTask
     *    DESCRIPTION :     Inserts a new task into the database, including
     *                      subtask and persons attached to task
     *    PARAMETERS  :     Task task
     *    RETURNS     :     VOID
     * */
    public void insertTask(Task task) {new TaskRepository.InsertFullTaskAsyncTask(taskDao).execute(task);}

    /*
     *    METHOD      :     update
     *    DESCRIPTION :     Update a task in the database
     *    PARAMETERS  :     Task task
     *    RETURNS     :     VOID
     * */
    public void update(Task task){
        new TaskRepository.UpdateTaskAsyncTask(taskDao).execute(task);
    }

    /*
     *    METHOD      :     delete
     *    DESCRIPTION :     Task a task from the database
     *    PARAMETERS  :     Subtask task
     *    RETURNS     :     VOID
     * */
    public void delete(Task task){
        new TaskRepository.DeleteTaskAsyncTask(taskDao).execute(task);
    }

    /*
     *    METHOD      :     deleteAllTasks
     *    DESCRIPTION :     Delete all tasks from the database
     *    PARAMETERS  :     NONE
     *    RETURNS     :     VOID
     * */
    public void deleteAllTasks() {
        new TaskRepository.DeleteAllTasksAsyncTask(taskDao).execute();
    }

    /*
     *    METHOD      :     getTaskById
     *    DESCRIPTION :     Get a Task object, based on the id passed into method
     *    PARAMETERS  :     int id
     *    RETURNS     :     Task
     * */
    public Task getTaskById(int id){
        return taskDao.getTasksById(id);
    }

    /*
     *    METHOD      :     getAllTasks
     *    DESCRIPTION :     Get all tasks from the database, in a livedata list
     *    PARAMETERS  :     None
     *    RETURNS     :     LiveData<List<Task>>
     * */
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    //This class is an async task used to access the database in order to insert a new task
    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Contrived and nonsensical use of chained async tasks.
            // We know how to do it, but we don't have a second use case since
            // we're using Room for our database manipulation.
            new InsertTaskVerifyIntegrityTask().execute();
        }
    }

    //Async task used to log the integrity of the newly inserted task information
    private static class InsertTaskVerifyIntegrityTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.d("Task logging", "doInBackground: A new task was inserted successfully");

            for (Task task : tasks) {
                Log.d("Task logging", "doInBackground: Task found: " + task.getName() + " (" + task.getId() + ")");
            }

            return null;
        }
    }

    //This class is an async task used to access the database in order to update a task
    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete a btask
    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void>{

        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete all tasks from the database
    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
    //This class is an async task used to access the database in order to insert into the task, subtask, and person_task tables
    private static class InsertFullTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private InsertFullTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0], tasks[0].getAssignedPeople(), tasks[0].getSubtasks());
            return null;
        }
    }
}
