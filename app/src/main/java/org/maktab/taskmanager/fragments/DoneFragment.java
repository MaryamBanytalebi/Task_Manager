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

public class DoneFragment extends Fragment {

    private RecyclerView mRecyclerViewDone;
    private TaskRepository mRepository;
    private DoneAdapter mDoneAdapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_done, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void findViews(View view) {
        mRecyclerViewDone = view.findViewById(R.id.recycler_done);
    }

    private void initViews(){
        mRecyclerViewDone.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepository = TaskRepository.getInstance();
        List<Task> tasks = mRepository.getTasks();
        mDoneAdapter = new DoneFragment.DoneAdapter(tasks);
        mRecyclerViewDone.setAdapter(mDoneAdapter);
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
            mTextViewDate.setText(task.getDate().toString());
//            mImageViewProfile.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
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