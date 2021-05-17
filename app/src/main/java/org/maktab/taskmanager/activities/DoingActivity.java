package org.maktab.taskmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.taskmanager.fragments.DoingFragment;

public class DoingActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context , DoingActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return DoingFragment.newInstance();
    }
}