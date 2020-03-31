/*
 *	FILE			: Converters.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This file contains declarations and definitions of the type converters needed
 */


// Reference: https://developer.android.com/training/data-storage/room/referencing-data
package com.github.mpagconestoga.mad_a01.converter;

import androidx.room.TypeConverter;

import com.github.mpagconestoga.mad_a01.objects.Category;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Category fromCategory(String value) { return value == null ? null : new Category(value, "",""); }

    @TypeConverter
    public static String categoryToString(Category category) { return category == null ? null : category.getName(); }
}

