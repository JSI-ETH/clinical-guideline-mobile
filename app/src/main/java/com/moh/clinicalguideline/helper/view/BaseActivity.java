package com.moh.clinicalguideline.helper.view;


import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
}
