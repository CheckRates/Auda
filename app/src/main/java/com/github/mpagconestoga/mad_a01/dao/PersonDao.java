/*
 *	FILE			: PersonDao.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This is the Data Access Object for the Person class
 */

package com.github.mpagconestoga.mad_a01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.mpagconestoga.mad_a01.objects.Person;

import java.util.List;

@Dao
public interface PersonDao {

    @Insert
    void insert(Person person);
    @Update
    void update(Person person);
    @Delete
    void delete(Person person);

    @Query("DELETE FROM Person")
    void deleteAllPersons();

    @Query("SELECT * FROM Person ORDER BY Id DESC")
    LiveData<List<Person>> getAllPersons();

    @Query(("SELECT * FROM PERSON WHERE Name = :personName"))
    LiveData<List<Person>> getPersonByName(String personName);
}
