/*
 *	FILE			: PersonSearchAdapter.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Adapter for the Recycler view of the assignment of members to a task
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonSearchAdapter extends RecyclerView.Adapter<PersonSearchAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "PersonSearchAdapter";

    private List<Person> data;
    private List<Person> dataFull;

    public int selectedPosition = -1;

    public PersonSearchAdapter() {
        data = new ArrayList<>();
        dataFull = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_person_list_item, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public int getItemCount() {
        return data.size();
    }
    
    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Person p: dataFull) {
            names.add(p.getName().toLowerCase());
        }
        return names;
    }
    public void setPosition(int position){
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String name = data.get(position).getName();
        holder.name.setText(name);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#B8B8B8"));
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

    public void setPersons(List<Person> persons){
        this.data = persons;
        this.dataFull = new ArrayList<>(persons);
        notifyDataSetChanged();
    }

    public List<Person> getFilteredList(){
        return data;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Person> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(dataFull);
            } else{
                String filteredPattern = constraint.toString().toLowerCase().trim();

                for (Person p : dataFull){
                    if(p.getName().toLowerCase().contains(filteredPattern)){
                        filteredList.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}

