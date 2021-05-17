package org.maktab.taskmanager.fragments;

import android.graphics.Color;
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

import com.amulyakhare.textdrawable.TextDrawable;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.TaskRepository;

import java.util.List;

public class TodoFragment extends Fragment {

    private RecyclerView mRecyclerViewTodo;
    private TaskRepository mRepository;
    private TodoAdapter mTodoAdapter;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance() {
        TodoFragment fragment = new TodoFragment();
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
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void findViews(View view) {
        mRecyclerViewTodo = view.findViewById(R.id.recycler_todo);
    }

    private void initViews(){
        mRecyclerViewTodo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepository = TaskRepository.getInstance();
        List<Task> tasks = mRepository.getTodoTask();
        mTodoAdapter = new TodoFragment.TodoAdapter(tasks);
        mRecyclerViewTodo.setAdapter(mTodoAdapter);
    }

    private class TodoHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewProfile;
        private Task mTask;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.txtview_title);
            mTextViewDate = itemView.findViewById(R.id.txtview_date);
            mImageViewProfile = itemView.findViewById(R.id.image_profile);

        }

        public void bindTaskTodo(Task task) {
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            mTextViewDate.setText(task.getDate().toString());
            String string = task.getTitle().substring(0,1);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(string, Color.RED);
            mImageViewProfile.setImageDrawable(drawable);
        }
    }

    private class TodoAdapter extends RecyclerView.Adapter<TodoHolder>{

        private List<Task> mTasks;

        public List<Task> getTasks(){
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TodoAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list,parent,false);
            TodoHolder todoHolder = new TodoHolder(view);
            return todoHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position) {

            Task task = mTasks.get(position);
            holder.bindTaskTodo(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }
}