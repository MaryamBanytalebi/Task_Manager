package org.maktab.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "userTable")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long primaryId;

    @ColumnInfo(name = "username")
    private String mUsername;

    @ColumnInfo(name = "password")
    private String mPassword;

    @ColumnInfo(name = "date")
    private Date mDate;

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
        mDate = new Date();
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(long primaryId) {
        this.primaryId = primaryId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
