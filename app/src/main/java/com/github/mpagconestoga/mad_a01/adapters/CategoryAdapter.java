/*
 *	FILE			: CategoryAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		:This file contains the class definition for the CategoryAdapter
 *
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.CategoryItem;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    //Constructor
    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList)
    {
        super(context, 0, categoryList);
    }

    /*
     *    METHOD      :   getView
     *    DESCRIPTION :   Used to get a view at the specified position, by inflating
     *                    it from the xml file(done in initalizeView method called within
     *                    this method
     *    PARAMETERS  :   int position
     *                    View convertView
     *                    ViewGroup parent
     *    RETURNS     :   View
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initializeView(position, convertView, parent);
    }

    /*
     *    METHOD      :   getDropDownView
     *    DESCRIPTION :   Return the view of the item passed to this method
     *    PARAMETERS  :   int position
     *                    View convertView
     *                    ViewGroup parent
     *    RETURNS     :   View
     * */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initializeView(position, convertView, parent);
    }

    //initalize the view, first checking that convertView is not null
    //inflate the view from the xml file.
    private View initializeView(int pos, View convert, ViewGroup parent)
    {
        if (convert == null)
        {
            convert = LayoutInflater.from(getContext()).inflate(
                    R.layout.task_categories_spinner, parent, false);
        }
        TextView categoryViewName = convert.findViewById(R.id.categoryName);
        CategoryItem currentItem = getItem(pos);
        if(currentItem != null) {
            categoryViewName.setText(currentItem.getCategoryName());
        }
        return convert;
    }
}
