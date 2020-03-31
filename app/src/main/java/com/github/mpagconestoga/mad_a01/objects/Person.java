/*
 *	FILE			: Person.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class contains the class declaration and definition of Person
 */

package com.github.mpagconestoga.mad_a01.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Constructor;

/*
 *  CLASS: Person
 *  DESCRIPTION: This class represents a person that is assigned to a task. Room uses this class
 *               definition to create a table with the members of this class being the
 *              columns of the Person Table
 */
@Entity
public class Person implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Name;

    //Constructor
    public Person(String Name) {
        this.Name = Name;
    }
    //Constructor - Parcel is used to pass objects between Activities
    public Person(Parcel in) {
        this.Name = in.readString();
        this.Id = in.readInt();
    }

    //Setters and getters
    public void setId(int id) {
        this.Id = id;
    }
    public int getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Write the Person's name and Id into a parcel, to be passed to another activity
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeInt(Id);
    }


    //Create person from parceable
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
