/*
 *	FILE			: TaskDao.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This is the Data Access Object for the Task class
 */

package com.github.mpagconestoga.mad_a01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.PersonTask;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class TaskDao {

    @Insert
    public abstract long insert(Task task);
    @Update
    public abstract void update(Task task);
    @Delete
    public abstract void delete(Task task);

    @Insert
    public abstract void insertPersonTask(PersonTask personTask);

    @Insert
    public abstract void insertSubtask(Subtask subtask);

    @Query("DELETE FROM Task")
    public abstract void deleteAllTasks();

    @Query("SELECT * FROM Task WHERE Id = :taskId")
    public abstract Task getTasksById(int taskId);

    @Query("SELECT * FROM Task ORDER BY Id DESC")
    public abstract LiveData<List<Task>> getAllTasks();

    @Transaction
    public void insertTask(Task task, List<Person> assignedPeople, ArrayList<Subtask> subtasks){
        long taskId = insert(task);

        for (Person person : assignedPeople) {
            insertPersonTask(new PersonTask((int)taskId, person.getId()));
        }

        for(Subtask subtask : subtasks){
            subtask.setTaskId((int) taskId);

            insertSubtask(subtask);
        }
    }
}
