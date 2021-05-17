package org.maktab.taskmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.activities.SignUpActivity;
import org.maktab.taskmanager.activities.TaskListActivity;

public class LoginFragment extends Fragment {

    public static final int REQUEST_CODE_SIGN_UP = 0;
    public static final String BUNDLE_KEY_USERNAME = "UserBundle";
    public static final String BUNDLE_KEY_PASSWORD = "passBundle";

    private TextInputLayout mUsernameForm;
    private TextInputLayout mPasswordForm;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private Button mBtnSignUp,mBtnLogin;

    private String user, pass;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_SIGN_UP){
            user = data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SIGN_UP);
            pass = data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SIGN_UP);
            mUsername.setText(user);
            mPassword.setText(pass);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_PASSWORD,pass);
        outState.putString(BUNDLE_KEY_USERNAME,user);
    }

    private void setListeners() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUsernameForm.setErrorEnabled(false);
                mPasswordForm.setErrorEnabled(false);
                if (validateInput()){
                    Intent intent = TaskListActivity.newIntent(getActivity(), mUsername.getText().toString());
                    startActivity(intent);
                }

            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.newIntent(getActivity(), mUsername.getText().toString(),
                        mPassword.getText().toString());
                startActivityForResult(intent,REQUEST_CODE_SIGN_UP);

            }
        });
    }

    private void findViews(View view) {
        mUsername = view.findViewById(R.id.username_login);
        mPassword = view.findViewById(R.id.username_login);
        mUsernameForm = view.findViewById(R.id.username_form_login);
        mPasswordForm = view.findViewById(R.id.username_form_login);
        mBtnLogin = view.findViewById(R.id.btnLogin_Login);
        mBtnLogin = view.findViewById(R.id.btnSignUp_Login);
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
        } else if (!mUsername.getText().toString().equals(user) ||
                !mPassword.getText().toString().equals(pass)) {
            Toast toast = Toast.makeText(getActivity(), R.string.toast_login, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        mUsernameForm.setErrorEnabled(false);
        mPasswordForm.setErrorEnabled(false);
        return true;
    }
}