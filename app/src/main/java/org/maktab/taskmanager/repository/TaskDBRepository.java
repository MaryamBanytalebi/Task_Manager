package org.maktab.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab.taskmanager.database.TaskDBHelper;
import org.maktab.taskmanager.database.TaskDBSchema;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.database.TaskDBSchema.TaskTable.Cols;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskDBRepository implements IRepository {

    private static TaskDBRepository sInstance;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private List<Task> mTasks;

    public static TaskDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskDBRepository(context);

        return sInstance;
    }

    private TaskDBRepository(Context context) {
        mContext = context.getApplicationContext();
        TaskDBHelper taskDBHelper = new TaskDBHelper(mContext);

        //all 4 checks happens on getDataBase
        mDatabase = taskDBHelper.getWritableDatabase();
    }

    @Override
    public List<Task> getTasks() {
        mTasks = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mTasks;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Task task = extractTaskFromCursor(cursor);
                mTasks.add(task);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mTasks;
    }

    @Override
    public List<Task> searchTasks(String searchValue) {
        mTasks = new ArrayList<>();
        String where = Cols.TITLE + " = ? or description = ? or date = ?";
        String whereArgs[] = new String[]{searchValue};

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mTasks;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Task task = extractTaskFromCursor(cursor);
                mTasks.add(task);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mTasks;
    }

    @Override
    public Task getTask(UUID taskId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{taskId.toString()};

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            Task task = extractTaskFromCursor(cursor);

            return task;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void insertTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskDBSchema.TaskTable.NAME,null,values);
    }

    @Override
    public void updateTask(Task task) {
        ContentValues values = getContentValues(task);
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getId().toString()};
        mDatabase.update(TaskDBSchema.TaskTable.NAME,values,whereClause,whereArgs);
    }

    @Override
    public void deleteTask(Task task) {
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getId().toString()};
        mDatabase.delete(TaskDBSchema.TaskTable.NAME, whereClause, whereArgs);
    }

    @Override
    public void deleteAllTasks() {
        mDatabase.delete(TaskDBSchema.TaskTable.NAME,null,null);
    }

    @Override
    public List<Task> getTodoTask() {
        mTasks = new ArrayList<>();
        String where = Cols.STATE + " = ?";
        String[] whereArgs = new String[]{"Todo"};

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mTasks;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Task task = extractTaskFromCursor(cursor);
                mTasks.add(task);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mTasks;
    }

    @Override
    public List<Task> getDoingTask() {
        mTasks = new ArrayList<>();
        String where = Cols.STATE + " = ?";
        String[] whereArgs = new String[]{"Doing"};

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mTasks;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Task task = extractTaskFromCursor(cursor);
                mTasks.add(task);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mTasks;
    }

    @Override
    public List<Task> getDoneTask() {
        mTasks = new ArrayList<>();
        String where = Cols.STATE + " = ?";
        String[] whereArgs = new String[]{"Done"};

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mTasks;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Task task = extractTaskFromCursor(cursor);
                mTasks.add(task);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mTasks;
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, task.getId().toString());
        values.put(Cols.TITLE, task.getTitle());
        values.put(Cols.DESCRIPTION, task.getDescription());
        values.put(Cols.DATE, task.getDate().getTime());
        values.put(Cols.STATE, task.getState());

        return values;
    }

    private Task extractTaskFromCursor(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.UUID)));
        String title = cursor.getString(cursor.getColumnIndex(Cols.TITLE));
        String description = cursor.getString(cursor.getColumnIndex(Cols.DESCRIPTION));
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(Cols.DATE)));
        String state = cursor.getString(cursor.getColumnIndex(Cols.STATE));

        return new Task(uuid, title,description, date, state);
    }
}
