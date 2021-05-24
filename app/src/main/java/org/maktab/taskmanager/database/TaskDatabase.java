package org.maktab.taskmanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.model.User;

@Database(entities = {Task.class, User.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDatabaseDao getTaskDatabaseDAO();
}
