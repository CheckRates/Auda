/*
 *	FILE			: Task.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 06
 *	DESCRIPTION		: This class contains the class declaration and definition of Task
 */


package com.github.mpagconestoga.mad_a01.objects;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
/*
 *  CLASS: Task
 *  DESCRIPTION: This class represents a Task. Room uses this class
 *               definition to create a table with the members of this class being the
 *              columns of the Task Table
 */
@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "_id")
    private int Id;
    private int CatId;
    private String Name;
    private Category Category;
    private Date StartTime;
    private Date EndTime;
    private double Lattitude;
    private double Longitude;

    //ignore tells room to not include these values in the database
    @Ignore private ArrayList<Person> assignedPeople;   //holds the list of people assigned to the task
    @Ignore private ArrayList<Subtask> subtasks;        //holds the list of subtasks of the task

    // Constructor
    public Task(String Name, Category Category, Date EndTime, double Lattitude, double Longitude) {
        this.Name = Name;
        this.Category = Category;
        this.StartTime = new Date();
        this.EndTime = EndTime;
//        this.Lattitude = latLng.latitude;
//        this.Longitude = latLng.longitude;
        this.Lattitude = Lattitude;
        this.Longitude = Longitude;
    }
    @Ignore
    public Task(String Name){
    }

    public static Task fromContentValues(ContentValues values) {
        String name = "";
        if (values.containsKey("Name")) {
            name = values.getAsString("Name");
        }
        if(name != "") {
            final Task task = new Task(name);
            return task;
        }
        else{
            return null;
        }

    }

    // IDS Getters and Setters
    public void setId(int id) {
        this.Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setCatId(int catId) {
        this.CatId = catId;
    }

    public int getCatId() {
        return CatId;
    }

    // Attributes Getters and Setters

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        this.EndTime = endTime;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        this.StartTime = startTime;
    }

    public Category getCategory(){
        return Category;
    }

    public void setCategory(Category category) {
        this.Category = category;
    }

    public double getLattitude() {
        return Lattitude;
    }

    public void setLattitude(double Lattitude) {
        this.Lattitude = Lattitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(Lattitude, Longitude);
    }

    public ArrayList<Person> getAssignedPeople() {
        return assignedPeople;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setAssignedPeople(ArrayList<Person> assignedPeople) {
        this.assignedPeople = assignedPeople;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

}
