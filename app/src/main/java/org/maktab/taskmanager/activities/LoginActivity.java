package org.maktab.taskmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.taskmanager.R;
import org.maktab.taskmanager.fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}