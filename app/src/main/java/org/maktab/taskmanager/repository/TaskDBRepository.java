package org.maktab.taskmanager.repository;

import android.content.Context;

import androidx.room.Room;

import org.maktab.taskmanager.database.TaskDatabase;
import org.maktab.taskmanager.database.TaskDatabaseDao;
import org.maktab.taskmanager.model.Task;

import java.util.List;
import java.util.UUID;

public class TaskDBRepository implements IRepository {

    private static TaskDBRepository sInstance;

    private TaskDatabaseDao mTaskDao;
    private Context mContext;
    private List<Task> mTasks;

    public static TaskDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskDBRepository(context);

        return sInstance;
    }

    private TaskDBRepository(Context context) {
        mContext = context.getApplicationContext();
        TaskDatabase taskDatabase = Room.databaseBuilder(mContext,
                TaskDatabase.class,
                "task.db")
                .allowMainThreadQueries()
                .build();

        mTaskDao = taskDatabase.getTaskDatabaseDAO();
    }

    @Override
    public List<Task> getTasks() {
        return mTaskDao.getTasks();
    }

    @Override
    public List<Task> searchTasks(String searchValue) {
        return mTaskDao.searchTasks(searchValue);
    }

    @Override
    public Task getTask(UUID taskId) {
        return mTaskDao.getTask(taskId);
    }

    @Override
    public void insertTask(Task task) {
        mTaskDao.insertTask(task);
    }

    @Override
    public void updateTask(Task task) {
        mTaskDao.updateTask(task);
    }

    @Override
    public void deleteTask(Task task) {
       mTaskDao.deleteTask(task);
    }

    @Override
    public void deleteAllTasks() {
        mTaskDao.deleteAllTasks();
    }

    @Override
    public List<Task> getTodoTask() {
       return mTaskDao.getTodoTask();
    }

    @Override
    public List<Task> getDoingTask() {
        return mTaskDao.getDoingTask();
    }

    @Override
    public List<Task> getDoneTask() {
        return mTaskDao.getDoneTask();
    }
}
