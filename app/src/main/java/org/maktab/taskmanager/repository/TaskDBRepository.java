package org.maktab.taskmanager.repository;

import android.content.Context;

import androidx.room.Room;

import org.maktab.taskmanager.database.TaskDatabase;
import org.maktab.taskmanager.database.TaskDatabaseDao;
import org.maktab.taskmanager.model.Task;

import java.io.File;
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
    public List<Task> searchTasks(String searchValue, long userId) {
        return mTaskDao.searchTasks(searchValue, userId);
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
    public void insertTasks(List<Task> tasks) {
        mTaskDao.insertTasks(tasks);
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
    public List<Task> getTodoTask(long userId) {
       return mTaskDao.getTodoTask(userId);
    }

    @Override
    public List<Task> getDoingTask(long userId) {
        return mTaskDao.getDoingTask(userId);
    }

    @Override
    public List<Task> getDoneTask(long userId) {
        return mTaskDao.getDoneTask(userId);
    }

    @Override
    public File getPhotoFile(Task task) {
        File filesDir = mContext.getFilesDir();

        File photoFile = new File(filesDir, task.getPhotoFileName());
        return photoFile;
    }
}
