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
    private static String mUsername;

    @Override
    public Fragment createFragment() {
        TaskListFragment taskListFragment = TaskListFragment.newInstance(mUsername);
        return taskListFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
    }

    public static Intent newIntent(Context context, String username){
        mUsername = username;
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        return intent;
    }
}