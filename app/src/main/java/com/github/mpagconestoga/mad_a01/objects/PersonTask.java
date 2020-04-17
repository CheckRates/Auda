/*
 *	FILE			: PersonTask.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class contains the class declaration and definition of PersonTask
 *                    PersonTask links a person to a task.
 */

package com.github.mpagconestoga.mad_a01.objects;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
/*
 *  CLASS: PersonTask
 *  DESCRIPTION: This class is used by room to create the Person_Task table in the database.
 *               The table is used to link the Persons associated with a task
 */
@Entity(tableName = "Person_Task",
        primaryKeys = {"TaskId","PersonId"},
        foreignKeys = {
                @ForeignKey(onDelete = 5/*CASCADE*/, entity = Person.class,
                        parentColumns = "Id",
                        childColumns = "PersonId"),
                @ForeignKey(onDelete = 5/*CASCADE*/, entity = Task.class,
                        parentColumns = "_id",
                        childColumns = "TaskId")
        },
        indices = {@Index("PersonId")})
public class PersonTask {

    public int TaskId;
    public int PersonId;

    public PersonTask(int TaskId, int PersonId) {
        this.TaskId = TaskId;
        this.PersonId = PersonId;
    }
}