package org.maktab.taskmanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.repository.IRepository;
import org.maktab.taskmanager.repository.TaskRepository;

public class DeleteAllFragment extends DialogFragment {


    private IRepository mRepository;

    public DeleteAllFragment() {
        // Required empty public constructor
    }

    public static DeleteAllFragment newInstance() {
        DeleteAllFragment fragment = new DeleteAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = TaskRepository.getInstance();
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_all, container, false);

        return view;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_delete_all, null);

        /*findViews(view);
        initViews();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_all_title)
                .setIcon(R.drawable.ic_high_importance)
                .setView(view)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRepository.deleteAllTasks();

                    }
                })
                .setNegativeButton(R.string.no, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}