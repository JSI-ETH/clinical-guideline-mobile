package com.moh.clinicalguideline.helper.view;

import android.app.Activity;
import android.app.Fragment;

import dagger.android.AndroidInjection;

public abstract  class BaseFragment extends Fragment {


    @Override
    public void onAttach(Activity activity) {
        AndroidInjection.inject(this);
        super.onAttach(activity);
        // ...
    }
}