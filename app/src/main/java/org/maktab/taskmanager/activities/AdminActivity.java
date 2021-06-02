package org.maktab.taskmanager.activities;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.maktab.taskmanager.fragments.AdminFragment;

public class AdminActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,AdminActivity.class);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        return AdminFragment.newInstance();
    }
}