/*
 *	FILE			: SubtaskDao.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This is the Data Access Object for the Subtask class
 */

package com.github.mpagconestoga.mad_a01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.mpagconestoga.mad_a01.objects.Subtask;

import java.util.List;

@Dao
public interface SubtaskDao {

    @Insert
    void insert(Subtask subtask);
    @Update
    void update(Subtask subtask);
    @Delete
    void delete(Subtask subtask);

    @Query("DELETE FROM Subtask")
    void deleteAllSubtasks();

    @Query("SELECT * FROM Subtask ORDER BY Id DESC")
    LiveData<List<Subtask>> getAllSubtasks();

    @Query("SELECT * FROM Subtask WHERE TaskId = :taskId")
    List<Subtask> getSubtasksbyTaskId(int taskId);
}
