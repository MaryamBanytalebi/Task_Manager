package org.maktab.taskmanager.activities;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.maktab.taskmanager.fragments.AdminListFragment;

public class AdminListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, AdminListActivity.class);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        return AdminListFragment.newInstance();
    }
}