package org.maktab.taskmanager.model;

import java.util.UUID;

public class Task {

    private String mTitle;
    private String mDate;
    private String mDescription;
    private String mState;
    private UUID mId;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
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

    public Task(String title, String date, String description, String state) {
        mTitle = title;
        mDate = date;
        mDescription = description;
        mState = state;
        mId = UUID.randomUUID();
    }
}
