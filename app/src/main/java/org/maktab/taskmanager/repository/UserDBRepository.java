package org.maktab.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import org.maktab.taskmanager.database.TaskDatabase;
import org.maktab.taskmanager.database.TaskDatabaseDao;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements IUserRepository {
    private static UserDBRepository sInstance;

    private TaskDatabaseDao mTaskDao;
    private Context mContext;

    public static UserDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new UserDBRepository(context);

        return sInstance;
    }

    private UserDBRepository(Context context) {
        mContext = context.getApplicationContext();
        TaskDatabase taskDatabase = Room.databaseBuilder(mContext,
                TaskDatabase.class,
                "task.db")
                .allowMainThreadQueries()
                .build();

        mTaskDao = taskDatabase.getTaskDatabaseDAO();
    }

    @Override
    public List<User> getUsers() {
       return mTaskDao.getUsers();
    }

    @Override
    public User getUser(String username ,String password) {
       return mTaskDao.getUser(username ,password);
    }

    @Override
    public void insertUser(User user) {
        mTaskDao.insertUser(user);
    }

    @Override
    public void deleteUser(User user) {
        mTaskDao.deleteUser(user);
    }

    @Override
    public void deleteUserTasks(long userId) {
        mTaskDao.deleteUserTasks(userId);
    }

    @Override
    public List<Task> getUserTasks(long userId) {
        return mTaskDao.getUserTasks(userId);
    }

    @Override
    public int numberOfTask(long userId) {
        return mTaskDao.numberOfTask(userId);
    }
}
