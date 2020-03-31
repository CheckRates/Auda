/*
 *	FILE			: CategoryDao.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This is the Data Access Object for the Category class
 */

package com.github.mpagconestoga.mad_a01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.mpagconestoga.mad_a01.objects.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);
    @Update
    void update(Category category);
    @Delete
    void delete(Category category);

    @Query("DELETE FROM Category")
    void deleteAllCategories();

    @Query("SELECT * FROM Category ORDER BY Id DESC")
    List<Category> getAllCategories();

    @Query("SELECT BackgroundURL FROM Category Where Name = :catName")
    String getBackground(String catName);

    @Query("SELECT WebURL FROM Category Where Name = :catName")
    String getWebUrl(String catName);
}
