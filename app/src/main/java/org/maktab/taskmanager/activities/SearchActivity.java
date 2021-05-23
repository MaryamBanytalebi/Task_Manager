package org.maktab.taskmanager.activities;


import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.maktab.taskmanager.fragments.SearchFragment;

public class SearchActivity extends SingleFragmentActivity {

    public static final String EXTRA_SEARCH_VALUE = "extra_search_value";

    public static Intent newIntent(Context context, String search){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_VALUE, search);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return SearchFragment.newInstance();
    }
}