package org.maktab.taskmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


public class DoingFragment extends TabsFragment {

    @Override
    public Fragment createFragment() {
        return new DoingFragment();
    }

    public static DoingFragment newInstance() {

        Bundle args = new Bundle();
        DoingFragment fragment = new DoingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}