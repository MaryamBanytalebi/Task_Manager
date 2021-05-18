package org.maktab.taskmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab.taskmanager.R;

public class InsertTaskFragment extends Fragment {

    public InsertTaskFragment() {
        // Required empty public constructor
    }

    public static InsertTaskFragment newInstance() {
        InsertTaskFragment fragment = new InsertTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_task, container, false);
        return view;
    }
}