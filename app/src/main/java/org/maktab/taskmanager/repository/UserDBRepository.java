package org.maktab.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import org.maktab.taskmanager.database.TaskDatabase;
import org.maktab.taskmanager.database.TaskDatabaseDao;
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
    public User getUser(String username) {
       return mTaskDao.getUser(username);
    }

    @Override
    public void insertUser(User user) {
        mTaskDao.insertUser(user);
    }
}
