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

public class DoingFragment extends Fragment {

    private RecyclerView mRecyclerViewDoing;
    private TaskRepository mRepository;
    private DoingAdapter mDoingAdapter;
    private RelativeLayout mLayoutEmptyDoing;
    private FloatingActionButton mActionButtonInsert;
    private List<Task> mTasks;

    public static final String FRAGMENT_TAG_INSERT_TASK = "InsertTask";
    public static final String FRAGMENT_TAG_EDIT_TASK = "EditTask";
    public static final int REQUEST_CODE_INSERT_TASK = 0;
    public static final int REQUEST_CODE_EDIT_TASK = 1;

    public DoingFragment() {
        // Required empty public constructor
    }

    public static DoingFragment newInstance() {
        DoingFragment fragment = new DoingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onPause() {
        super.onPause();
        updateUI();
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
        View view = inflater.inflate(R.layout.fragment_doing, container, false);
        findViews(view);
        checkEmptyLayout();
        initViews();
        listeners();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_INSERT_TASK || requestCode == REQUEST_CODE_EDIT_TASK) {
            updateUI();


        }
    }

    private void listeners() {
        mActionButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertTaskFragment insertTaskFragment = InsertTaskFragment.newInstance();

                insertTaskFragment.setTargetFragment(
                        DoingFragment.this,
                        REQUEST_CODE_INSERT_TASK);

                insertTaskFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_INSERT_TASK);

            }
        });
    }

    private void initViews() {
        mRecyclerViewDoing.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void updateUI() {

        checkEmptyLayout();
        if (mDoingAdapter == null) {
            mDoingAdapter = new DoingAdapter(mTasks);
            mRecyclerViewDoing.setAdapter(mDoingAdapter);
        }
        else {
            mDoingAdapter.setTasks(mTasks);
            mDoingAdapter.notifyDataSetChanged();
        }
    }

    private void checkEmptyLayout() {
        mTasks = mRepository.getDoingTask();
        if (mTasks.size()==0)
            mLayoutEmptyDoing.setVisibility(View.VISIBLE);
        else
            mLayoutEmptyDoing.setVisibility(View.GONE);
    }

    private void findViews(View view) {
        mRecyclerViewDoing = view.findViewById(R.id.recycler_doing);
        mLayoutEmptyDoing = view.findViewById(R.id.layout_empty_doingTask);
        mActionButtonInsert = view.findViewById(R.id.fab_doing);
    }

    private class DoingHolder extends RecyclerView.ViewHolder{

        private Task mTask;

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewProfile;

        public DoingHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.txtview_title);
            mTextViewDate = itemView.findViewById(R.id.txtview_date);
            mImageViewProfile = itemView.findViewById(R.id.image_profile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mTask.getId());
                    editTaskFragment.setTargetFragment(DoingFragment.this,
                            REQUEST_CODE_EDIT_TASK);
                    editTaskFragment.show(getActivity().getSupportFragmentManager(),
                            FRAGMENT_TAG_EDIT_TASK);
                }
            });
        }

        public void bindTaskDoing(Task task){
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
    }

    private class DoingAdapter extends RecyclerView.Adapter<DoingHolder>{

        private List<Task> mTasks;

        @NonNull
        @Override
        public DoingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list,parent,false);
            DoingHolder doingHolder = new DoingHolder(view);
            return doingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DoingHolder holder, int position) {
            Task task = mTasks.get(position);

            holder.bindTaskDoing(task);

        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        public List<Task> getTasks(){
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public DoingAdapter(List<Task> tasks) {
            mTasks = tasks;
        }
    }
}