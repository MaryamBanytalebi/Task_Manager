package org.maktab.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long primaryId;

    @ColumnInfo(name = "uuid")
    private UUID mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "date")
    private Date mDate;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "state")
    private String mState;

    @ColumnInfo(name = "user_id_fk")
    private long userIdFk;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(long primaryId) {
        this.primaryId = primaryId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public long getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(long userIdFk) {
        this.userIdFk = userIdFk;
    }

    /* public Task(UUID id, String title, String description, Date date, String state) {
        mTitle = title;
        mDate = date;
        mDescription = description;
        mState = state;
        mId = id;
    }

    public Task(String title, String description, Date date, String state) {
        this(UUID.randomUUID(),title,description,date,state);
    }*/

    public Task(String title, String description, Date date, String state) {
        mId = UUID.randomUUID();
        mTitle = title;
        mDescription = description;
        mDate = date;
        mState = state;
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
