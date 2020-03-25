// FILE         Task.java
// ASSIGNMENT   MAD A01
// DATE         Feb 6 2020
// PROGRAMMERS  Gabriel Gurgel & Duncan Snider
// DESCRIPTION  This class models a task with many data members we need
//              for the program to function.

package com.github.mpagconestoga.mad_a01.objects;

import java.util.ArrayList;
import java.util.Date;

public class Task
{
    private String name;
    private String category;
    private Date startTime;
    private Date endTime;
    private ArrayList<Person> assignedPeople;
    private ArrayList<Subtask> subtasks;

    // Constructor
    public Task(String name, String category, Date endTime, ArrayList<Person> assignedPeople) {
        this.name = name;
        this.category = category;
        this.startTime = new Date();
        this.endTime = endTime;
        this.assignedPeople = assignedPeople;
        this.subtasks = new ArrayList<>();
    }

    // FUNCTION         registerTask()
    // DESCRIPTION      Adds a task to our pool of tasks
    public void registerTask() {
        registerTask(this);
    }

    // FUNCTION         setName()
    // PARAMETERS       String name
    // DESCRIPTION      Setter for the name
    public void setName(String name) {
        this.name = name;
    }

    // FUNCTION         getName()
    // DESCRIPTION      Getter for the name
    public String getName() {
        return name;
    }

    // FUNCTION         getEndTime()
    // RETURNS          Date
    // DESCRIPTION      Setter for the name
    public Date getEndTime() {
        return endTime;
    }

    // FUNCTION         getCategory()
    // RETURNS          String
    // DESCRIPTION      Getter for the category
    public String getCategory(){
        return category;
    }

    // FUNCTION         getAssignedPeople()
    // RETURNS          ArrayList<Person>
    // DESCRIPTION      Getter for assigned people
    public ArrayList<Person> getAssignedPeople() {
        return assignedPeople;
    }

    // FUNCTION         setAssignedPeople()
    // DESCRIPTION      Setter for assigned people
    public void setAssignedPeople(ArrayList<Person> people) {
        this.assignedPeople = people;
    }

    // FUNCTION         getSubtasks()
    // RETURNS          ArrayList<Subtask>
    // DESCRIPTION      Getter for subtasks
    public ArrayList<Subtask> getSubtasks() { return subtasks; }

    // assignedPeople manipulation
    public void assignPerson(Person person) {
        if (assignedPeople.contains(person)) {
            return;
        }

        assignedPeople.add(person);
    }

    public void unassignPerson(Person person) {
        if (!assignedPeople.contains(person)) {
            return;
        }

        assignedPeople.remove(person);
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public boolean hasPersonAssigned(Person person) {
        return assignedPeople.contains(person);
    }

    // STATIC VARIABLES & METHODS
    private static ArrayList<Task> allTasks = new ArrayList<Task>();

    public static void registerTask(Task task) {
        if (allTasks.contains(task)) {
            return;
        }

        allTasks.add(task);
    }

    public static void unregisterTask(Task task) {
        if (!allTasks.contains(task)) {
            return;
        }

        allTasks.remove(task);
    }

    public static Task getTaskByHashcode(int hashcode) {
        for (Task task : allTasks) {
            if (task.hashCode() == hashcode) {
                return task;
            }
        }

        return null;
    }

    public static ArrayList<Task> getAllTasks() {
        return allTasks;
    }
}

