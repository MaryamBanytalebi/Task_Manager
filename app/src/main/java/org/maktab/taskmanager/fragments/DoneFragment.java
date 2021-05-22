package org.maktab.taskmanager.fragments;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class DoneFragment extends TabsFragment {

    @Override
    public Fragment createFragment() {
        return new DoneFragment();
    }

    public static DoneFragment newInstance() {

        Bundle args = new Bundle();
        DoneFragment fragment = new DoneFragment();
        fragment.setArguments(args);
        return fragment;
    }
}