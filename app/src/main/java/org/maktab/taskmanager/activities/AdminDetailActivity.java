package org.maktab.taskmanager.activities;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import org.maktab.taskmanager.fragments.AdminDetailFragment;

public class AdminDetailActivity extends SingleFragmentActivity {

    public static final String EXTRA_USER_ID = "userId";
    private static long mUserId;

    public Intent newIntent(Context context, long userId){
        Intent intent = new Intent(context, AdminListActivity.class);
        intent.putExtra(EXTRA_USER_ID,userId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return AdminDetailFragment.newInstance(mUserId);
    }
}