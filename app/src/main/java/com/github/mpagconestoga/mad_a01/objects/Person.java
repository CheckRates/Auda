/*
 *	FILE			: Person.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Models a "Person". servers to simbolize a potential worker of a task
 *
 */

package com.github.mpagconestoga.mad_a01.objects;

import java.util.ArrayList;
import java.util.Arrays;

public class Person
{

    // Private Variables
    private String name;
    private int color;

    // Constructors
    public Person(String name)
    {
        this.name = name;
        this.color = validColors.get(colorIndex);

        if (colorIndex == validColors.size() - 1) {
            colorIndex = -1;
        }
        colorIndex++;

        // Register person in our data store
        registerPerson(this);
    }

    public Person(String name, boolean register) {
        this.name = name;
        this.color = validColors.get(colorIndex);

        if (colorIndex == validColors.size() - 1) {
            colorIndex = -1;
        }
        colorIndex++;

        if (register) {
            registerPerson(this);
        }
    }

    // Getters and Setters
    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    // STATIC VARIABLES & METHODS
    private static ArrayList<Person> allPeople = new ArrayList<Person>();
    private static ArrayList<Integer> validColors = new ArrayList<>(Arrays.asList(220220220, 303030,1090200, 2552030, 230255));

    private static int colorIndex = 0;


    public static void registerPerson(Person person) {
        if (allPeople.contains(person)) {
            return;
        }

        allPeople.add(person);
    }

    public static void unregisterPerson(Person person) {
        if (!allPeople.contains(person)) {
            return;
        }

        allPeople.remove(person);
    }

    public static ArrayList<Person> getAllPeople() {
        return allPeople;
    }

    public static boolean personExists(String name) {
        for (Person person : allPeople) {
            if (person.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static Person getPerson(String name) {
        for (Person person : allPeople) {
            if (person.getName().equals(name)) {
                return person;
            }
        }

        return null;
    }
}
