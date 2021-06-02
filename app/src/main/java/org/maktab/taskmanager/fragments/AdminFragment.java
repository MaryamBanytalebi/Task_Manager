package org.maktab.taskmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.User;
import org.maktab.taskmanager.repository.IUserRepository;
import org.maktab.taskmanager.repository.UserDBRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminFragment extends Fragment {

    private RecyclerView mRecyclerViewAdmin;
    private IUserRepository mIUserRepository;
    List<User> mUsers;
    private UserAdapter mUserAdapter;

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIUserRepository = UserDBRepository.getInstance(getActivity());
        mUsers = mIUserRepository.getUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerViewAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void findViews(View view) {
        mRecyclerViewAdmin = view.findViewById(R.id.recycler_admin);
    }

    private void updateUI(){
        if (mUserAdapter == null){
            mUserAdapter = new UserAdapter(mUsers);
            mRecyclerViewAdmin.setAdapter(mUserAdapter);
        }
        else {
            mUserAdapter.setUsers(mUsers);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    private class UserHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewRegistryDate;
        private TextView mTextViewNumberOfTask;
        private TextView mTextViewUsername;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewRegistryDate = itemView.findViewById(R.id.registry_date);
            mTextViewNumberOfTask = itemView.findViewById(R.id.number_of_tasks);
            mTextViewUsername = itemView.findViewById(R.id.user_name);
        }

        private DateFormat getDateFormat(){
            return new SimpleDateFormat("MMM dd,yyyy,h:mm a");
        }

        public void bindUserDetail(User user){
            DateFormat dateFormat = getDateFormat();
            String date = dateFormat.format(user.getDate());
            mTextViewUsername.setText(user.getUsername());
            mTextViewRegistryDate.setText(date);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder>{

        private List<User> mUsers;

        public List<User> getUsers() {
            return mUsers;
        }

        public void setUsers(List<User> users) {
            mUsers = users;
        }

        public UserAdapter(List<User> users) {
            mUsers = users;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.user_admin_row_list,parent,false);
            UserHolder userHolder = new UserHolder(view);
            return userHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bindUserDetail(user);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }
}