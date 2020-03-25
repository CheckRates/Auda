/*
 *	FILE			: memberListItem.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		:This file contains the class definition for the MemberListItem
 *
 */
package com.github.mpagconestoga.mad_a01.objects;

/*
        Class Name: memberListItem
        Purpose: This class is used for an item in the recycler view on the page
                where people can create a task and assign people to it. The class contains
                2 images(user icon and delete) and a string holding the name of the person
 */
public class MemberListItem {
    private int mImage;
    private String mName;
    private int mDeleteImage;

    public MemberListItem(int image, String memberName, int delete)
    {
        mImage = image;
        mName = memberName;
        mDeleteImage = delete;
    }

    public int getmImage()
    {
        return mImage;
    }
    public String getmName()
    {
        return mName;
    }

    public int getmDeleteImage()
    {
        return mDeleteImage;
    }
}
