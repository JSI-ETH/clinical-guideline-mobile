package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.recyclerview.MainNodeAdapter;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.algorithm.content.ContentViewModel;

import java.util.List;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmViewModel viewModel;
    private ContentViewModel contentViewModel;
    private List<AlgorithmDescription> algorithmDescriptions;
    public static String Extra_NodeId = "Extra_NodeId";
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    TextView textViewSymptomTitle;
    private MainNodeAdapter mainNodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_activity_main);
        RecyclerView recyclerView = findViewById(R.id.node_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        textViewSymptomTitle = findViewById(R.id.symptom_title);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel.class);
        viewModel.setNavigator(this);

        int nodeid = getIntent().getExtras().getInt(Extra_NodeId, 0);
        viewModel.LoadNode(nodeid);
        textViewSymptomTitle.setText(viewModel.getSymptomTitle());
        viewModel.getNode().observe(this, algorithmDescription -> {
            algorithmDescriptions.add(algorithmDescription);
            mainNodeAdapter = new MainNodeAdapter(this, algorithmDescriptions);
        });
//        textViewSymptomTitle.setText(algorithmDescriptions.get(0).getTitle());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mainNodeAdapter = new MainNodeAdapter(this, viewModel.getNodeList());
        recyclerView.setAdapter(mainNodeAdapter);
        initViews();
    }

    public void initViews(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        sheetBehavior = BottomSheetBehavior.from(binding.foregroundContainer);
//        sheetBehavior.setFitToContents(false);
//        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
//        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
//        viewModel.getNode().observe(this, new Observer<AlgorithmDescription>() {
//            @Override
//            public void onChanged(@Nullable AlgorithmDescription algorithmDescription) {
//                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                   // binding.toolbar.setNavigationIcon();
//                }
//
//            }
//        });
    }
//
//
//    @Override
//    public void onBackPressed() {
//        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//        else {
//           super.onBackPressed();}
//    }
}
