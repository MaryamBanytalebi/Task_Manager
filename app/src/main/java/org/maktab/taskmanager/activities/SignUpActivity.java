package org.maktab.taskmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.fragments.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivity {

    public static final String EXTRA_USERNAME = "org.maktab.taskmanager.activities.extra_username";
    public static final String EXTRA_PASSWORD = "org.maktab.taskmanager.activities.extra_password";
    private static String mGetExtraUserName,mGetExtraPassword;

    @Override
    public Fragment createFragment() {

        SignUpFragment signUpFragment = SignUpFragment.newInstance(mGetExtraUserName,mGetExtraPassword);
        return signUpFragment;
    }

    public static Intent newIntent(Context context, String userName, String password){

        mGetExtraUserName = userName;
        mGetExtraPassword = password;

        Intent intent = new Intent(context,SignUpActivity.class);
        intent.putExtra(EXTRA_USERNAME, mGetExtraUserName);
        intent.putExtra(EXTRA_PASSWORD, mGetExtraPassword);
        return intent;
    }
}