package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.IRepository;
import org.maktab.taskmanager.repository.TaskDBRepository;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditTaskFragment extends DialogFragment {

    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final String FRAGMENT_TAG_TIME_PICKER = "TimePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 2;
    public static final String TAG = "ETF";
    public static final String AUTHORITY = "org.maktab.taskmanager.fileProvider";
    public static final String BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE";
    public static final String BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME";
    public static final String ARGUMENT_TASK_ID = "Bundle_key_TaskId";

    private Button mButtonSave, mButtonDelete, mButtonEdit, mButtonDate, mButtonTime;
    private RadioButton mTodo, mDoing, mDone;
    private TextInputLayout mTitleForm;
    private TextInputLayout mDescriptionForm;
    private TextInputEditText mTitle;
    private TextInputEditText mDescription;
    private IRepository mRepository;
    private Task mTask;
    private Calendar mCalendar;
    private String mDate, mTime;
    private boolean mFlag;
    private String mState;
    private ImageView mImageViewShare,mImageViewTaskPicture,mImageViewTakePicture;
    private File mPhotoFile;

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(UUID taskId) {

        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_TASK_ID, taskId);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(ARGUMENT_TASK_ID);
        mRepository = TaskDBRepository.getInstance(getActivity());
        mTask = mRepository.getTask(taskId);
        mCalendar = Calendar.getInstance();
        mPhotoFile = mRepository.getPhotoFile(mTask);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        findViews(view);
        if (mFlag) {
            mButtonDate.setText(mDate);
            mButtonTime.setText(mTime);
        }
        setData(mTask);
        listeners();
        updatePhotoView();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_DATE, mButtonDate.getText().toString());
        outState.putString(BUNDLE_KEY_TIME, mButtonTime.getText().toString());
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
        }else if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            Uri photoUri = generateUriForPhotoFile();
            getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void findViews(View view) {
        mTitleForm = view.findViewById(R.id.title_form_edit);
        mTitle = view.findViewById(R.id.title_edit);
        mDescriptionForm = view.findViewById(R.id.description_form_edit);
        mDescription = view.findViewById(R.id.description_edit);
        mButtonDate = view.findViewById(R.id.btn_date_edit);
        mButtonTime = view.findViewById(R.id.btn_time_edit);
        mButtonSave = view.findViewById(R.id.btn_save_edit);
        mButtonDelete = view.findViewById(R.id.btn_delete_edit);
        mButtonEdit = view.findViewById(R.id.btn_edit_edit);
        mTodo = view.findViewById(R.id.radioBtn_todo_edit);
        mDoing = view.findViewById(R.id.radioBtn_doing_edit);
        mDone = view.findViewById(R.id.radioBtn_done_edit);
        mImageViewShare = view.findViewById(R.id.share);
        mImageViewTaskPicture = view.findViewById(R.id.task_picture);
        mImageViewTakePicture = view.findViewById(R.id.btn_picture);

    }

    private void setData(Task task) {
        mTitle.setText(task.getTitle());
        mTitleForm.setEnabled(false);
        mDescription.setText(task.getDescription());
        mDescriptionForm.setEnabled(false);
        DateFormat dateFormat = getDateFormat();
        mButtonDate.setText(dateFormat.format(task.getDate()));
        mButtonDate.setEnabled(false);
        DateFormat timeFormat = getTimeFormat();
        mButtonTime.setText(timeFormat.format(task.getDate()));
        mButtonTime.setEnabled(false);
        if (task.getState().equalsIgnoreCase("Todo")) {
            mTodo.setChecked(true);
            mState = "Todo";
        } else if (task.getState().equalsIgnoreCase("Doing")) {
            mDoing.setChecked(true);
            mState = "Doing";
        } else if (task.getState().equalsIgnoreCase("Done")) {
            mDone.setChecked(true);
            mState = "Done";
        }
        mTodo.setEnabled(false);
        mDoing.setEnabled(false);
        mDone.setEnabled(false);
        mImageViewTakePicture.setEnabled(false);

    }

    private void listeners() {
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleForm.setEnabled(true);
                mDescriptionForm.setEnabled(true);
                mButtonDate.setEnabled(true);
                mButtonTime.setEnabled(true);
                mTodo.setEnabled(true);
                mDoing.setEnabled(true);
                mDone.setEnabled(true);
                mImageViewTakePicture.setEnabled(true);

            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleForm.isEnabled()) {
                    if (validateInput()) {
                        sendResult();
                        dismiss();
                    } else {
                        int strId = R.string.toast_insert;
                        Toast toast = Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    dismiss();
                }
            }
        });
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCalendar.getTime());

                //create parent-child relations between CDF and DPF
                datePickerFragment.setTargetFragment(EditTaskFragment.this,
                        REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);

            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment =
                        TimePickerFragment.newInstance(mCalendar.getTime());

                timePickerFragment.setTargetFragment(EditTaskFragment.this,
                        REQUEST_CODE_TIME_PICKER);

                timePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepository.deleteTask(mTask);
                dismiss();
            }
        });

        mImageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
                Intent intent = intentBuilder
                        .setType("text/plain")
                        .setText(shareWord())
                        .setChooserTitle(getString(R.string.share))
                        .createChooserIntent();

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        mImageViewTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent();
            }
        });
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (mPhotoFile != null && takePictureIntent
                    .resolveActivity(getActivity().getPackageManager()) != null) {

                // file:///data/data/com.example.ci/files/234234234234.jpg
                Uri photoUri = generateUriForPhotoFile();

                grantWriteUriToAllResolvedActivities(takePictureIntent, photoUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void grantWriteUriToAllResolvedActivities(Intent takePictureIntent, Uri photoUri) {
        List<ResolveInfo> activities = getActivity().getPackageManager()
                .queryIntentActivities(
                        takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity: activities) {
            getActivity().grantUriPermission(
                    activity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private Uri generateUriForPhotoFile() {
        return FileProvider.getUriForFile(
                getContext(),
                AUTHORITY,
                mPhotoFile);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists())
            return;


        //this has a better memory management.
        Bitmap bitmap = org.maktab.taskmanager.utils.PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
        mImageViewTaskPicture.setImageBitmap(bitmap);
    }

    private String shareWord() {
        String title = mTask.getTitle();
        String description = mTask.getDescription();
        String date = mTask.getDate().toString();
        String state = mState;

        String shareMassage = getString(
                R.string.shareMassage,
                title,
                description,
                date,
                state);

        return shareMassage;
    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        editTask();
        updateTasks(mTask);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private boolean validateInput() {
        if (mTitle.getText() != null && mDescription.getText() != null && mButtonDate.getText() != null &&
                mButtonTime.getText() != null && (mTodo.isChecked() || mDoing.isChecked()
                || mDone.isChecked())) {
            return true;
        } else
            return false;
    }

    private void editTask() {
        String state = "";
        if (mTodo.isChecked())
            state = "Todo";
        else if (mDoing.isChecked())
            state = "Doing";
        else if (mDone.isChecked())
            state = "Done";
        mTask.setTitle(mTitle.getText().toString());
        mTask.setDescription(mDescription.getText().toString());
        mTask.setDate(mCalendar.getTime());
        mTask.setState(state);
    }

    private void updateTasks(Task task) {
        mRepository.updateTask(task);
    }

    private void updateTaskDate(Date userSelectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userSelectedDate);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        DateFormat dateFormat = getDateFormat();
        mButtonDate.setText(dateFormat.format(userSelectedDate));

    }

    private void updateTaskTime(Date userSelectedTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userSelectedTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        DateFormat timeFormat = getTimeFormat();
        mButtonTime.setText(timeFormat.format(userSelectedTime));
    }


    private DateFormat getDateFormat() {
        //"yyyy/MM/dd"
        return new SimpleDateFormat("MMM dd,yyyy");
    }

    private DateFormat getTimeFormat() {
        //"HH:mm:ss"
        return new SimpleDateFormat("h:mm a");
    }
}