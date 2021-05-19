package org.maktab.taskmanager.repository;

import android.text.format.DateUtils;
import android.util.Log;

import org.maktab.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TaskRepository implements IRepository {

    private List<Task> mTasks;
    private static TaskRepository sInstance;

    private TaskRepository() {
        mTasks = new ArrayList<>();
        Log.d("TaskSize_constructor" , " " + mTasks.size());
    }

    @Override
    public List<Task> getTasks() {
        return mTasks;
    }

    @Override
    public Task getTask(UUID taskId) {
        for (Task task : mTasks)
            if (task.getId().equals(taskId)) {
                return task;
            }
        return null;
    }

    @Override
    public void insertTask(Task task) {
        mTasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        Task findTask = getTask(task.getId());
        findTask.setTitle(task.getTitle());
        findTask.setDescription(task.getDescription());
        findTask.setDate(task.getDate());
        findTask.setState(task.getState());
    }

    @Override
    public void deleteTask(Task task) {

        for (int i=0 ; i<mTasks.size() ; i++){
            if (mTasks.get(i).getId().equals(task.getId())){
                mTasks.remove(i);
                return;
            }
        }
    }

    public void deleteAllTasks(){
        mTasks.clear();
    }

    @Override
    public List<Task> getTodoTask() {
        List<Task> todoTask = new ArrayList<>();
        for (Task task : mTasks){
            if (task.getState().equalsIgnoreCase("TODO")){
                todoTask.add(task);
            }
        }
        return todoTask;
    }

    @Override
    public List<Task> getDoingTask() {
        List<Task> doingTask = new ArrayList<>();
        for (Task task : mTasks){
            if(task.getState().equalsIgnoreCase("DOING")){
                doingTask.add(task);
            }
        }
        return doingTask;
    }

    @Override
    public List<Task> getDoneTask() {
        List<Task> doneTask = new ArrayList<>();
        for (Task task : mTasks){
            if (task.getState().equalsIgnoreCase("DONE")){
                doneTask.add(task);
            }
        }
        return doneTask;
    }

    public static TaskRepository getInstance() {
        if (sInstance == null)
            sInstance = new TaskRepository();

        return sInstance;
    }
}
