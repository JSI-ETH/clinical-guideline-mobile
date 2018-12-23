package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.AlgorithmActivityMainBinding;
import com.moh.clinicalguideline.helper.view.BaseActivity;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmActivityMainBinding binding;
    private AlgorithmViewModel viewModel;


    public static String Extra_NodeId = "Extra_NodeId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        viewModel.setNavigator(this);
        binding = DataBindingUtil.setContentView(this, R.layout.algorithm_activity_main);
        binding.setLifecycleOwner(this);
        binding.setMenu(viewModel);

        int nodeid = getIntent().getExtras().getInt(Extra_NodeId, 0);
        viewModel.LoadNode(nodeid);
        initViews();
    }

    public void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up,R.anim.nothing);

    }

}
