/*
 *	FILE			: CategoryItem.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		:This file contains the class definition for the CategoryItem
 *
 */
package com.github.mpagconestoga.mad_a01.objects;

/*
        Class Name: memberListAdapter
        Purpose: This class is used for an item in the spinner showing the list of task categories
 */
public class CategoryItem {
    private String categoryName;

    public CategoryItem(String name)
    {
        categoryName = name;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
}
