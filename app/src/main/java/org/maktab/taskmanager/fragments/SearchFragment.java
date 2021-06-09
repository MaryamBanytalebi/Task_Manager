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
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.activities.SingleFragmentActivity;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.repository.IRepository;
import org.maktab.taskmanager.repository.TaskDBRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class SearchFragment extends Fragment{

    public static final int REQUEST_CODE_EDIT_TASK = 0;
    public static final String FRAGMENT_TAG_EDIT_TASK = "EditTask";
    public static final String ARG_USER_ID = "Arg_userId";

    private String mSearch;
    private List<Task> mTasks;
    private Task mTask;
    private SearchAdapter mAdapter;
    private long mUserId;

    private RecyclerView mRecyclerView;
    private TextInputEditText mEditTextSearch;
    private ImageView mImageViewSearch;
    private IRepository mRepository;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(long userId) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID,userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRepository = TaskDBRepository.getInstance(getActivity());
            mUserId = getArguments().getLong(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    private void setListeners(){
        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
                initViews();
            }
        });
    }

    private void search() {
        String search = "%" + mEditTextSearch.getText() + "%";
        mTasks = mRepository.searchTasks(search,mUserId);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_search);
        mEditTextSearch = view.findViewById(R.id.search);
        mImageViewSearch = view.findViewById(R.id.search_img);
    }

    private void updateUI(){
        if (mAdapter == null) {
            mAdapter = new SearchAdapter(mTasks);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTasks(mTasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class SearchHolder extends RecyclerView.ViewHolder{

        private ImageView mImageViewProfile;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewProfile = itemView.findViewById(R.id.image_profile);
            mTextViewTitle = itemView.findViewById(R.id.txtview_title);
            mTextViewDate = itemView.findViewById(R.id.txtview_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mTask.getId(),true);
                    editTaskFragment.setTargetFragment(
                            SearchFragment.this, REQUEST_CODE_EDIT_TASK);
                    editTaskFragment.show(
                            getActivity().getSupportFragmentManager(), FRAGMENT_TAG_EDIT_TASK);
                }
            });
        }

        public void bindTaskSearch(Task task) {
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            String date = createDateFormat(task);
            mTextViewDate.setText(date);
            int color = Color.parseColor("#ff80aa");
            String string = task.getTitle().substring(0,1);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(string,color);
            mImageViewProfile.setImageDrawable(drawable);
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

        private DateFormat getDateFormat() {
            return new SimpleDateFormat("MMM dd,yyyy");
        }

        private DateFormat getTimeFormat() {
            return new SimpleDateFormat("h:mm a");
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder>{

        private List<Task> mTasks;

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public SearchAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list,parent,false);
            SearchHolder searchHolder = new SearchHolder(view);
            return searchHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder holder, int position) {

            Task task = mTasks.get(position);
            holder.bindTaskSearch(task);

        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }
}