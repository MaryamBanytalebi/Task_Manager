package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.TaskRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class EditTaskFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "taskId";
    public static final int REQUEST_CODE_TIME_PICKER = 0;
    public static final int REQUEST_CODE_DATE_PICKER = 1;
    public static final String FRAGMENT_TAG_TIME_PICKER = "time picker";
    public static final String FRAGMENT_TAG_DATE_PICKER = "date picker";
    public static final String BUNDLE_KEY_DATE = "date picker";
    public static final String BUNDLE_KEY_TIME = "time picker";

    private Button mBtnSave, mBtnDelete, mBtnEdit, mBtnDate, mBtnTime;
    private CheckBox mCheckBoxTodo, mCheckBoxDoing, mCheckBoxDone;
    private TextInputLayout mTitleForm;
    private TextInputLayout mDescriptionForm;
    private TextInputEditText mTitle;
    private TextInputEditText mDescription;

    private UUID mTaskId;
    private TaskRepository mRepository;
    private Task mTask;
    private Calendar mCalendar;
    private boolean mFlag;
    private String mDate,mTime;

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(UUID taskId) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTaskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        findViews(view);
        if (mFlag) {
            mBtnDate.setText(mDate);
            mBtnTime.setText(mTime);
        }
        setData(mTask);
        setListeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_KEY_DATE, mBtnDate.getText().toString());
        outState.putString(BUNDLE_KEY_TIME, mBtnTime.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null ){
            return;
        }
        if (requestCode == REQUEST_CODE_DATE_PICKER){
            Calendar userSelectedDate = (Calendar) data.getSerializableExtra(
                    DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            updateTaskDate(userSelectedDate.getTime());

        }else if (requestCode == REQUEST_CODE_TIME_PICKER){
            Calendar userSelectedTime = (Calendar) data.getSerializableExtra(
                    TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            updateTaskTime(userSelectedTime.getTime());
        }
    }

    private void setData(Task task){
        mTitle.setText(task.getTitle());
        mDescription.setText(task.getDescription());
        DateFormat dateFormat = getDateFormat();
        mBtnDate.setText(dateFormat.format(task.getDate()));
        DateFormat timeFormat = getTimeFormat();
        mBtnTime.setText(timeFormat.format(task.getDate()));
        if (task.getState().equalsIgnoreCase("Todo"))
            mCheckBoxTodo.setChecked(true);
        else if (task.getState().equalsIgnoreCase("Doing"))
            mCheckBoxDoing.setChecked(true);
        else if (task.getState().equalsIgnoreCase("Done"))
            mCheckBoxDone.setChecked(true);
    }

    private void setListeners(){
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleForm.isEnabled()) {
                    if (validInput()) {
                        sendResult();
                        dismiss();
                    } else {
                        int strId = R.string.toast_insert;
                        Toast toast = Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    dismiss();
                }            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRepository.deleteTask(mTask);
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validInput()){
                    sendResult();
                }
            }
        });

        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment =
                        TimePickerFragment.newInstance(mCalendar.getTime());
                timePickerFragment.setTargetFragment(EditTaskFragment.this,
                        REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);
            }
        });

        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCalendar.getTime());
                datePickerFragment.setTargetFragment(EditTaskFragment.this,
                        REQUEST_CODE_TIME_PICKER);
                datePickerFragment.show(getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);
            }
        });
    }

    private boolean validInput() {
        if (mTitle.getText() != null && mDescription.getText() != null &&
                mBtnDate.getText() != null && mBtnTime != null &&
                (mCheckBoxDoing.isChecked()
                        || mCheckBoxDone.isChecked()
                        || mCheckBoxTodo.isChecked())){
            return true;
        }else
            return false;
    }

    private void sendResult(){
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        editTask();
        updateTasks(mTask);
        /* extractTask();*/
//        intent.putExtra(EXTRA_USER_SELECTED_DATE, userSelectedTask);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void editTask(){
        String state = "";
        if (mCheckBoxDoing.isChecked())
            state = "DOING";
        if (mCheckBoxTodo.isChecked())
            state = "TODO";
        if (mCheckBoxDone.isChecked())
            state = "DONE";
        mTask.setTitle(mTitle.getText().toString());
        mTask.setDescription(mDescription.getText().toString());
        mTask.setDate(mCalendar.getTime());
        mTask.setState(state);
    }

    private void updateTasks(Task task){
        mRepository.updateTask(task);
    }

    private void updateTaskDate(Date userSelectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userSelectedDate);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(Calendar.YEAR,year);
        mCalendar.set(Calendar.MONTH,monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat dateFormat = getDateFormat();
        mBtnDate.setText(dateFormat.format(userSelectedDate));

    }

    private void updateTaskTime(Date userSelectedTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userSelectedTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mCalendar.set(Calendar.HOUR_OF_DAY,hour);
        mCalendar.set(Calendar.MINUTE,minute);
        DateFormat timeFormat = getTimeFormat();
        mBtnTime.setText(timeFormat.format(userSelectedTime));
    }


    private DateFormat getDateFormat() {
        //"yyyy/MM/dd"
        return new SimpleDateFormat("MMM dd,yyyy");
    }

    private DateFormat getTimeFormat() {
        //"HH:mm:ss"
        return new SimpleDateFormat("h:mm a");
    }

    private void findViews(View view) {
        mTitleForm = view.findViewById(R.id.title_form_edit);
        mTitle = view.findViewById(R.id.title_edit);
        mDescriptionForm = view.findViewById(R.id.description_form_edit);
        mDescription = view.findViewById(R.id.description_edit);
        mBtnDate = view.findViewById(R.id.btn_date_edit);
        mBtnTime = view.findViewById(R.id.btn_time_edit);
        mBtnSave = view.findViewById(R.id.btn_save_edit);
        mBtnDelete = view.findViewById(R.id.btn_delete_edit);
        mBtnEdit = view.findViewById(R.id.btn_edit_edit);
        mCheckBoxTodo = view.findViewById(R.id.checkBox_todo_edit);
        mCheckBoxDoing = view.findViewById(R.id.checkBox_doing_edit);
        mCheckBoxDone = view.findViewById(R.id.checkBox_done_edit);
    }
}