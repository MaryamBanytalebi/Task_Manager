package org.maktab.taskmanager.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class TodoFragment extends TabsFragment {

    @Override
    public Fragment createFragment() {
        return new TodoFragment();
    }

    public static TodoFragment newInstance() {

        Bundle args = new Bundle();
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}