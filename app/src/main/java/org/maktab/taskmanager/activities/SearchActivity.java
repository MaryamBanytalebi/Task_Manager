package org.maktab.taskmanager.activities;


import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.maktab.taskmanager.fragments.SearchFragment;

public class SearchActivity extends SingleFragmentActivity {

    public static final String EXTRA_SEARCH_VALUE = "extra_search_value";
    public static final String EXTRA_USER_ID = "Extra_User_Id";
    private static long mUserId;

    public static Intent newIntent(Context context, String search, long userId){
        mUserId = userId;
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_VALUE, search);
        intent.putExtra(EXTRA_USER_ID,userId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return SearchFragment.newInstance(mUserId);
    }
}