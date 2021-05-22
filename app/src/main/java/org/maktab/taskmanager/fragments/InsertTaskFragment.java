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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.IRepository;
import org.maktab.taskmanager.repository.TaskDBRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertTaskFragment extends DialogFragment {

    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final String FRAGMENT_TAG_TIME_PICKER = "TimePicker";
    public static final String BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE";
    public static final String BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME";

    private TextInputEditText mTitle, mDescription;
    private TextInputLayout mTitleForm, mDescriptionForm;
    private Button mBtnDate, mBtnTime, mBtnSave, mBtnCancel;
    private RadioButton mRadioBtnTodo, mRadioBtnDoing, mRadioBtnDone;
    private IRepository mRepository;
    private List<Task> mTasks;
    private Task mTask;
    private Calendar mCalendar;
    private String mDate,mTime;
    private boolean mFlag;

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
        if (savedInstanceState != null){
            mDate = savedInstanceState.getString(BUNDLE_KEY_DATE);
            mTime = savedInstanceState.getString(BUNDLE_KEY_TIME);
            mFlag = true;

        }
        mCalendar = Calendar.getInstance();
        mRepository = TaskDBRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_task, container, false);
        findViews(view);
        if (mFlag){
            mBtnDate.setText(mDate);
            mBtnTime.setText(mTime);
        }
        setListeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_DATE, mBtnDate.getText().toString());
        outState.putString(BUNDLE_KEY_TIME, mBtnTime.getText().toString());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Calendar userSelectedDate =
                    (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            updateTaskDate(userSelectedDate.getTime());

        } else if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Calendar userSelectedTime =
                    (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            updateTaskTime(userSelectedTime.getTime());
        }
    }

    private void findViews(View view) {
        mTitle = view.findViewById(R.id.title_insert);
        mDescription = view.findViewById(R.id.description_insert);
        mTitleForm = view.findViewById(R.id.title_form_insert);
        mDescriptionForm = view.findViewById(R.id.description_form_insert);
        mBtnDate = view.findViewById(R.id.btn_date_insert);
        mBtnTime = view.findViewById(R.id.btn_time_insert);
        mBtnSave = view.findViewById(R.id.btn_save_insert);
        mBtnCancel = view.findViewById(R.id.btn_cancel_insert);
        mRadioBtnTodo = view.findViewById(R.id.radioBtn_todo);
        mRadioBtnDoing = view.findViewById(R.id.radioBtn_doing);
        mRadioBtnDone = view.findViewById(R.id.radioBtn_done);
    }
    private void setListeners(){
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    sendResult();
                    dismiss();
                }else {
                    int strId = R.string.toast_insert;
                    Toast toast = Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date taskDate = Calendar.getInstance().getTime();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCalendar.getTime());

                datePickerFragment.setTargetFragment(InsertTaskFragment.this,
                        REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);
            }
        });

        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment =
                        TimePickerFragment.newInstance(mCalendar.getTime());

                timePickerFragment.setTargetFragment(InsertTaskFragment.this,
                        REQUEST_CODE_TIME_PICKER);

                timePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);

            }
        });
    }

    private void sendResult(){
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        createTask();
        updateTasks(mTask);
        //extractTask();
//        intent.putExtra(EXTRA_USER_SELECTED_DATE, userSelectedTask);
        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private boolean validateInput(){
        if (mTitle.getText()!=null && mDescription.getText()!=null && mBtnDate.getText()!=null &&
                mBtnTime.getText()!=null && (mRadioBtnTodo.isChecked() || mRadioBtnDoing.isChecked()
                || mRadioBtnDone.isChecked())) {
            return true;
        }
        else {
            return false;
        }
    }

    private void createTask(){
        String state = "";
        if (mRadioBtnTodo.isChecked())
            state = "Todo";
        else if (mRadioBtnDoing.isChecked())
            state = "Doing";
        else if (mRadioBtnDone.isChecked())
            state = "Done";
        mTask = new Task(mTitle.getText().toString(),mDescription.getText().toString(),mCalendar.getTime(),state);
    }


    private void updateTasks(Task task) {
        mRepository.insertTask(task);
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
        return new SimpleDateFormat("MMM dd,yyyy");
    }

    private DateFormat getTimeFormat() {
        return new SimpleDateFormat("h:mm a");
    }
}