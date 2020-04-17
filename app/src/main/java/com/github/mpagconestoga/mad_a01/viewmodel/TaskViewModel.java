/*
 *	FILE			: TaskViewModel.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the ViewModel for tasks
 */

package com.github.mpagconestoga.mad_a01.viewmodel;

import android.app.Application;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.TaskTimerService;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.Subtask;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.github.mpagconestoga.mad_a01.repositories.PersonTaskRepository;
import com.github.mpagconestoga.mad_a01.repositories.SubtaskRepository;
import com.github.mpagconestoga.mad_a01.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.System.getString;

/*
 *   CLASS       : TaskViewModel.java
 *   DESCRIPTION : ViewModel for the task viewing screen, displaying all the information regarding a task.
 *                 Contains repository for Task, PersonTask, and Subtask.
 */
public class TaskViewModel extends AndroidViewModel {
    // Repositories
    private TaskRepository taskRepository;
    private PersonTaskRepository personTaskRepository;
    private SubtaskRepository subtaskRepository;

    // Task info
    private Task task;
    private LiveData<List<Task>> allTasks;
    private List<Subtask> subtasks;

    // Service
    private MutableLiveData<Boolean> isProgressUpdating = new MutableLiveData<>();
    private MutableLiveData<TaskTimerService.ServBinder> Binder = new MutableLiveData<>();
    private String timerStatus;

    //= Constructor
    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        personTaskRepository = new PersonTaskRepository(application);
        subtaskRepository = new SubtaskRepository(application);
        allTasks = taskRepository.getAllTasks();
        timerStatus =  "Start ";
    }

    // FUNCTION   : insert
    // DESCRIPTION: Inserts task into the database
    public void insert(Task task) {
        taskRepository.insert(task);
    }

    // FUNCTION   : delete
    // DESCRIPTION: Deletes the task in the database
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    // FUNCTION   : getAllTasks
    // DESCRIPTION: Returns all tasks from the database
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // FUNCTION   : setTaskById
    // DESCRIPTION: Returns the task by its TaskId
    public void setTaskById(int taskId) {
        task = taskRepository.getTaskById(taskId);
        task.setAssignedPeople((ArrayList<Person>) personTaskRepository.getPersonsByTaskId(taskId));
        task.setSubtasks((ArrayList<Subtask>) subtaskRepository.getSubtasksByTaskId(taskId));
    }

    // FUNCTION   : getTask
    // DESCRIPTION: Returns the task by its TaskId
    public Task getTask() {
        return task;
    }

    // Service Methods
    // Fragment <-> Service connection
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TaskTimerService.ServBinder binder = (TaskTimerService.ServBinder) service;
            Binder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Binder.postValue(null);
        }
    };

    public LiveData<Boolean> getIsProgress() {
        return isProgressUpdating;
    }

    public LiveData<TaskTimerService.ServBinder> getBinder() {
        return Binder;
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public void setIsUpdating(Boolean updating) {
        isProgressUpdating.postValue(updating);
    }
}
