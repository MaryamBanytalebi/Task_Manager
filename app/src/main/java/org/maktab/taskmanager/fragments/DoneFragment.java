package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.TaskRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DoneFragment extends Fragment {

    private RecyclerView mRecyclerViewDone;
    private TaskRepository mRepository;
    private DoneAdapter mDoneAdapter;
    private RelativeLayout mLayoutEmptyDone;
    private FloatingActionButton mActionButtonInsert;
    private List<Task> mTasks;

    public static final String FRAGMENT_TAG_INSERT_TASK = "InsertTask";
    public static final int REQUEST_CODE_INSERT_TASK = 0;
    public static final String TAG = "DoneFragment";

    public DoneFragment() {
        // Required empty public constructor
    }


    public static DoneFragment newInstance() {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG,"DoneFragmentOnAttach");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"DoneFragmentOnPause");

        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"DoneFragmentOnResume");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = TaskRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_done, container, false);
        findViews(view);
        checkEmptyLayout();
        initViews();
        setlisteners();
        Log.d(TAG,"DoneFragmentCreateView");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            if (resultCode != Activity.RESULT_OK || data == null)
                return;

            if (requestCode == REQUEST_CODE_INSERT_TASK) {
                updateUI();
        }
    }

    private void setlisteners() {
        mActionButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertTaskFragment insertTaskFragment = InsertTaskFragment.newInstance();

                insertTaskFragment.setTargetFragment(
                        DoneFragment.this,
                        REQUEST_CODE_INSERT_TASK);

                insertTaskFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_INSERT_TASK);
            }
        });
    }

    private void findViews(View view) {
        mRecyclerViewDone = view.findViewById(R.id.recycler_done);
        mLayoutEmptyDone = view.findViewById(R.id.layout_empty_doneTask);
        mActionButtonInsert = view.findViewById(R.id.fab_done);
    }

    private void initViews(){
        mRecyclerViewDone.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void updateUI() {

        checkEmptyLayout();
        if (mDoneAdapter == null) {
            mDoneAdapter = new DoneAdapter(mTasks);
            mRecyclerViewDone.setAdapter(mDoneAdapter);
        }
        else {
            mDoneAdapter.setTasks(mTasks);
            mDoneAdapter.notifyDataSetChanged();
        }
    }

    private void checkEmptyLayout() {
        mTasks = mRepository.getDoneTask();
        if (mTasks.size()==0)
            mLayoutEmptyDone.setVisibility(View.VISIBLE);
        else
            mLayoutEmptyDone.setVisibility(View.GONE);
    }

    private class DoneHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewProfile;
        private Task mTask;

        public DoneHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.txtview_title);
            mTextViewDate = itemView.findViewById(R.id.txtview_date);
            mImageViewProfile = itemView.findViewById(R.id.image_profile);
        }

        public void bindTaskDone(Task task) {
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            String date = createDateFormat(task);
            mTextViewDate.setText(date);
            String string = task.getTitle().substring(0,1);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(string, Color.RED);
            mImageViewProfile.setImageDrawable(drawable);
        }

        private DateFormat getDateFormat() {
            //"yyyy/MM/dd"
            return new SimpleDateFormat("MMM dd,yyyy");
        }

        private DateFormat getTimeFormat() {
            //"HH:mm:ss"
            return new SimpleDateFormat("h:mm a");
        }
        private String createDateFormat (Task task){
            String totalDate = "";
            DateFormat dateFormat = getDateFormat();
            String date = dateFormat.format(task.getDate());

            DateFormat timeFormat = getTimeFormat();
            String time = timeFormat.format(task.getDate());

            totalDate = date + "  " + time;

            return totalDate;
        }

        public ImageView getImageViewProfile(){
            return mImageViewProfile;
        }
    }

    private class DoneAdapter extends RecyclerView.Adapter<DoneHolder>{

        private List<Task> mTasks;

        public List<Task> getTasks(){
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public DoneAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        @NonNull
        @Override
        public DoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list,parent,false);
            DoneHolder doneHolder = new DoneHolder(view);
            return doneHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DoneHolder holder, int position) {

            Task task = mTasks.get(position);

            holder.bindTaskDone(task);
        }

    }
}