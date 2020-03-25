/*
 *	FILE			: PersonSearchAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the Recycler view of the assignment of members to a task
 *
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Person;

import java.util.ArrayList;

public class PersonSearchAdapter extends RecyclerView.Adapter<PersonSearchAdapter.ViewHolder> {
    private static final String TAG = "PersonSearchAdapter";

    private ArrayList<Person> data;
    private LayoutInflater inflater;

    public int selectedPosition = -1;

    public PersonSearchAdapter(Context context, ArrayList<Person> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_person_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String name = data.get(position).getName();
        holder.name.setText(name);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f0f0f0"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: selectedPosition was set to " + position);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
        }
    }
}

