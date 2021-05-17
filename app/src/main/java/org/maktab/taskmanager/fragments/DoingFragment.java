package org.maktab.taskmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.TaskRepository;

import java.util.List;

public class DoingFragment extends Fragment {

    private RecyclerView mRecyclerViewDoing;
    private TaskRepository mRepository;
    private DoingAdapter mDoingAdapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doing, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerViewDoing.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepository = TaskRepository.getInstance();
        List<Task> tasks = mRepository.getTasks();
        mDoingAdapter = new DoingAdapter(tasks);
        mRecyclerViewDoing.setAdapter(mDoingAdapter);
    }

    private void findViews(View view) {
        mRecyclerViewDoing = view.findViewById(R.id.recycler_doing);
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
        }

        public void bindTaskDoing(Task task){
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            mTextViewDate.setText(task.getDate().toString());

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