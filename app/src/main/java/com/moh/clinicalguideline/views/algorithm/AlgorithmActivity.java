package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.AlgorithmActivityMainBinding;
import com.moh.clinicalguideline.helper.view.BaseActivity;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmActivityMainBinding binding;
    private AlgorithmViewModel viewModel;


    public static String Extra_NodeId = "Extra_NodeId";
    private BottomSheetBehavior<LinearLayout> sheetBehavior;

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

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.appBarLayout.setOutlineProvider(null);
        }
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sheetBehavior = BottomSheetBehavior.from(binding.foregroundContainer);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
        viewModel.getNode().observe(this, new Observer<AlgorithmDescription>() {
            @Override
            public void onChanged(@Nullable AlgorithmDescription algorithmDescription) {
                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                   // binding.toolbar.setNavigationIcon();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
           super.onBackPressed();}
    }
}
