package org.maktab.taskmanager.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.model.User;
import org.maktab.taskmanager.repository.UserDBRepository;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private static final String ARG_USERNAME = "extra_username";
    private static final String ARG_PASSWORD = "extra_password";
    public static final String EXTRA_USERNAME_SIGN_UP = "extraUsername";
    public static final String EXTRA_PASSWORD_SIGN_UP = "EXTRA_password";

    private String mUser;
    private String mPass;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private TextInputLayout mUsernameForm;
    private TextInputLayout mPasswordForm;
    private Button mBtnSignUP;
    private UserDBRepository mUserRepository;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String username, String passWord) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, passWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getString(ARG_USERNAME);
            mPass = getArguments().getString(ARG_PASSWORD);
            mUserRepository = UserDBRepository.getInstance(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        findViews(view);
        mUsername.setText(mUser);
        mPassword.setText(mPass);
        setListeners();
        return view;
    }

    private void setListeners() {
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameForm.setErrorEnabled(false);
                mPasswordForm.setErrorEnabled(false);
                if (validateInput()){
                    setUserPassResult();
                    getActivity().finish();

                }
            }
        });
    }

    private void setUserPassResult() {
        String username = Objects.requireNonNull(mUsername.getText()).toString();
        String password = Objects.requireNonNull(mPassword.getText()).toString();
        User user = new User(username,password);
        mUserRepository.insertUser(user);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USERNAME_SIGN_UP,mUsername.getText().toString());
        intent.putExtra(EXTRA_PASSWORD_SIGN_UP,mPassword.getText().toString());
        getActivity().setResult(getActivity().RESULT_OK,intent);
    }

    private void findViews(View view) {
        mUsername = view.findViewById(R.id.username_signUp);
        mPassword = view.findViewById(R.id.password_signUp);
        mUsernameForm = view.findViewById(R.id.username_form_signUp);
        mPasswordForm = view.findViewById(R.id.password_form_signUp);
        mBtnSignUP = view.findViewById(R.id.btnSignUp_SignUP);
    }

    private boolean validateInput() {
        if (mUsername.getText().toString().trim().isEmpty() && mPassword.getText().toString().trim().isEmpty()) {
            mUsernameForm.setErrorEnabled(true);
            mUsernameForm.setError("Field cannot be empty!");
            mPasswordForm.setErrorEnabled(true);
            mPasswordForm.setError("Field cannot be empty!");
            return false;
        } else if (mUsername.getText().toString().trim().isEmpty()) {
            mUsernameForm.setErrorEnabled(true);
            mUsernameForm.setError("Field cannot be empty!");
            return false;
        } else if (mPassword.getText().toString().trim().isEmpty()) {
            mPasswordForm.setErrorEnabled(true);
            mPasswordForm.setError("Field cannot be empty!");
            return false;
        }
        mUsernameForm.setErrorEnabled(false);
        mPasswordForm.setErrorEnabled(false);
        return true;
    }
}