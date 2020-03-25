// FILE         Subtask.java
// ASSIGNMENT   MAD A01
// DATE         Feb 6 2020
// PROGRAMMERS  Duncan Snider & Gabriel Gurgel
// DESCRIPTION  This class inherits from Task and implements some functionality
//              that models a subtask.

package com.github.mpagconestoga.mad_a01.objects;

import java.util.ArrayList;

public class Subtask extends Task
{
    private Task parent;
    private int weight;

    // Constructor
    public Subtask(Task parent, String name, int weight) {
        super(name, parent.getCategory() , parent.getEndTime(), new ArrayList<Person>());

        this.parent = parent;
        this.weight = weight;
    }

    // FUNCTION         getParent()
    // RETURNS          Task
    // DESCRIPTION      Getter for the parent class
    public Task getParent() {
        return parent;
    }

    // FUNCTION         setPriority()
    // PARAMETERS       int priority
    // DESCRIPTION      Setter for the priority
    public void setPriority(int priority) {
        this.weight = priority;
    }

    // FUNCTION         getPriority()
    // RETURNS          int
    // DESCRIPTION      Getter for the priority
    public int getPriority() {
        return weight;
    }
}
