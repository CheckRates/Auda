/*
 *	FILE			: Subtask.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class contains the class declaration and definition of Subtask
 */

package com.github.mpagconestoga.mad_a01.objects;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
 *  CLASS: Subtask
 *  DESCRIPTION: This class represents a subtask that is linked to a task. Room uses this class
 *               definition to create a table with the members of this class being the
 *              columns of the subtask Table
 */
@Entity(foreignKeys = {
        @ForeignKey(onDelete = 5/*CASCADE*/, entity = Task.class,
                parentColumns = "Id",
                childColumns = "TaskId")},
        indices=@Index("TaskId"))
public class Subtask {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private int Weight;
    private int TaskId;
    private String Name;

    public Subtask(int Weight, String Name) {
        this.Weight = Weight;
        this.Name = Name;
    }


    //Getters and Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setTaskId(int id){
        this.TaskId = id;
    }

    public int getTaskId(){
        return this.TaskId;
    }

    public int getWeight() {
        return Weight;
    }

    public void setPriority(int priority) { this.Weight = priority; }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}