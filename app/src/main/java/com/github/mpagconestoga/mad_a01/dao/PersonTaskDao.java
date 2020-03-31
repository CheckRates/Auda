/*
 *	FILE			: PersonTaskDao.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This is the Data Access Object for the PersonTask class
 */

package com.github.mpagconestoga.mad_a01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.PersonTask;
import com.github.mpagconestoga.mad_a01.objects.Task;

import java.util.List;

@Dao
public interface PersonTaskDao {

    @Insert
    void insert(PersonTask personTask);

    @Query("SELECT Id, CatId, Name, Category, StartTime, EndTime FROM Task INNER JOIN Person_Task ON Task.Id = Person_Task.TaskId WHERE Person_Task.PersonId = :personId")
    LiveData<List<Task>> GetTasksByUser(int personId);

    @Query("SELECT Id, Name FROM Person INNER JOIN Person_Task ON Person.Id = Person_Task.PersonId WHERE Person_Task.TaskId = :taskId")
    List<Person> GetPersonsByTaskId(int taskId);
}
