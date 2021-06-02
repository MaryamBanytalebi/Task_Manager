package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.model.User;
import org.maktab.taskmanager.repository.IRepository;
import org.maktab.taskmanager.repository.IUserRepository;
import org.maktab.taskmanager.repository.TaskDBRepository;
import org.maktab.taskmanager.repository.UserDBRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class TabsFragment extends Fragment {

    public static final String FRAGMENT_TAG_INSERT_TASK = "InsertTask";
    public static final int REQUEST_CODE_INSERT_TASK = 0;
    public static final String FRAGMENT_TAG_EDIT_TASK = "EditTask";
    public static final int REQUEST_CODE_EDIT_TASK = 1;
    public static final String FRAGMENT_TAG_DELETE_ALL_TASK = "DeleteAllTask";
    public static final int REQUEST_CODE_DELETE_ALL_TASK = 2;
    public static final String KEY_VALUE_STATE = "Key_Value_State";
    public static final String KEY_VALUE_USERNAME = "KEY_VALUE_Username";


    private RecyclerView mRecyclerView;
    private TabsAdapter mAdapter;
    private IRepository mRepository;
    private IUserRepository mIUserRepository;
    private List<Task> mTasks;
    private RelativeLayout mLayoutEmpty;
    private FloatingActionButton mActionButtonInsert;
    private FloatingActionButton mActionButtonDelete;
    private FloatingActionButton mActionButtonLogOut;
    private FloatingActionsMenu mFloatingActionsMenu;
    private boolean isVisible;
    private String mState,mUsername;
    private User mUser;

    public TabsFragment() {
        // Required empty public constructor
    }

    //public Fragment createFragment();

    public static TabsFragment newInstance(String username,String state) {

        Bundle args = new Bundle();
        args.putString(KEY_VALUE_STATE,state);
        args.putString(KEY_VALUE_USERNAME,username);
        TabsFragment fragment = new TabsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUI();
        mFloatingActionsMenu.collapse();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = TaskDBRepository.getInstance(getActivity());
        mIUserRepository = UserDBRepository.getInstance(getActivity());
        mState = getArguments().getString(KEY_VALUE_STATE);
        mUsername = getArguments().getString(KEY_VALUE_USERNAME);
        mUser = mIUserRepository.getUser(Objects.requireNonNull(mUsername));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
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

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && !isVisible) {
            //your code
        }
        isVisible = true;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //your code
                }
            }, 500);

        }
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mLayoutEmpty = view.findViewById(R.id.layout_empty);
        mActionButtonDelete = view.findViewById(R.id.fab_delete);
        mActionButtonLogOut = view.findViewById(R.id.fab_logOut);
        mActionButtonInsert = view.findViewById(R.id.fab_insert);
        mFloatingActionsMenu = view.findViewById(R.id.fam);

    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void listeners() {
        mActionButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertTaskFragment insertTaskFragment = InsertTaskFragment.newInstance();

                insertTaskFragment.setTargetFragment(
                        TabsFragment.this,
                        REQUEST_CODE_INSERT_TASK);

                insertTaskFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_INSERT_TASK);

            }
        });

        mActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAllFragment deleteAllFragment = DeleteAllFragment.newInstance();

                deleteAllFragment.setTargetFragment(
                        TabsFragment.this,
                        REQUEST_CODE_DELETE_ALL_TASK);

                deleteAllFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DELETE_ALL_TASK);

            }
        });
        mActionButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void updateUI() {

        checkEmptyLayout();
        if (mAdapter == null) {
            mAdapter = new TabsAdapter(mTasks);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTasks(mTasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void checkEmptyLayout() {
        if (mState.equalsIgnoreCase("todo"))
            mTasks = mRepository.getTodoTask();
        else if (mState.equalsIgnoreCase("doing"))
            mTasks = mRepository.getDoingTask();
        else if (mState.equalsIgnoreCase("done"))
            mTasks = mRepository.getDoneTask();

        if (mTasks.size()==0)
            mLayoutEmpty.setVisibility(View.VISIBLE);
        else
            mLayoutEmpty.setVisibility(View.GONE);
    }

    private class TabsHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewProfile;
        private Task mTask;

        public TabsHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.txtview_title);
            mTextViewDate = itemView.findViewById(R.id.txtview_date);
            mImageViewProfile = itemView.findViewById(R.id.image_profile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mTask.getId());

                    editTaskFragment.setTargetFragment(
                            TabsFragment.this,
                            REQUEST_CODE_EDIT_TASK);

                    editTaskFragment.show(
                            getActivity().getSupportFragmentManager(),
                            FRAGMENT_TAG_EDIT_TASK);
                }
            });
        }

        public void bindTaskTabs(Task task) {
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            String date = createDateFormat(task);
            mTextViewDate.setText(date);
            String string = task.getTitle().substring(0,1);
            int color = Color.parseColor("#ff80aa");
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(string, color);
            mImageViewProfile.setImageDrawable(drawable);
        }
        private DateFormat getDateFormat() {
            return new SimpleDateFormat("MMM dd,yyyy");
        }

        private DateFormat getTimeFormat() {
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

    private class TabsAdapter extends RecyclerView.Adapter<TabsHolder> {

        private List<Task> mTasks;

        public List<Task> getTasks(){
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TabsAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        @NonNull
        @Override
        public TabsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list,parent,false);
            TabsHolder tabsHolder = new TabsHolder(view);
            return tabsHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TabsHolder holder, int position) {

            Task task = mTasks.get(position);

            holder.bindTaskTabs(task);
        }

    }
}