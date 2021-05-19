package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import org.maktab.taskmanager.R;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {


    public static final String ARGS_TASK_TIME = "taskTime";
    public static final String EXTRA_USER_SELECTED_TIME = "org.maktab.taskmanager.userSelectedTime";
    private Date mTaskDate;
    private int mSecond;

    private TimePicker mTimePicker;
    private Calendar mCalendar;

    public TimePickerFragment() {
        // Required empty public constructor
    }
    public static TimePickerFragment newInstance(Date taskDate) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TASK_TIME, taskDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskDate = (Date) getArguments().getSerializable(ARGS_TASK_TIME);
        mCalendar = Calendar.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null);

        findViews(view);
        initViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setIcon(R.drawable.ic_clock)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            extractTimeFromTimePicker();
                        }
                        sendResult(mCalendar);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void findViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker_task);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        initTimePicker();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimePicker() {
        // i have a date and i want to set it in date picker.

        mCalendar.setTime(mTaskDate);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void extractTimeFromTimePicker() {
        LocalDateTime now = LocalDateTime.now();
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        int second = now.getSecond();

        mCalendar.set(Calendar.HOUR_OF_DAY,hour);
        mCalendar.set(Calendar.MINUTE,minute);
        mCalendar.set(Calendar.SECOND,second);
    }

    private void sendResult(Calendar userSelectedDate) {
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_TIME, userSelectedDate);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }

}