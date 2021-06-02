package org.maktab.taskmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.fragments.TaskListFragment;

public class TaskListActivity extends SingleFragmentActivity {

    public static final String EXTRA_USERNAME = "org.maktab.taskmanager.activities.extra_username";
    public static final String EXTRA_PASSWORD = "org.maktab.taskmanager.activities.extra_password";
    private static String mUsername;
    private static String mPassword;

    @Override
    public Fragment createFragment() {
        TaskListFragment taskListFragment = TaskListFragment.newInstance(mUsername,mPassword);
        return taskListFragment;
    }

    public static Intent newIntent(Context context, String username, String password){
        mUsername = username;
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        intent.putExtra(EXTRA_PASSWORD,password);
        return intent;
    }
}