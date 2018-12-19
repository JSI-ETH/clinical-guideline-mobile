package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.ActivityAlgorithmBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.main.MenuActivity;

import java.util.List;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public static String Extra_NodeId = "Extra_NodeId";
    public static String Extra_ParentNodeId="Extra_ParentNodeId";
    private ActivityAlgorithmBinding viewModelBinding;
    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;
    private SimpleLayoutAdapter<AlgorithmDescription> conditionalAdapter;
    private SimpleLayoutAdapter<AlgorithmDescription> optionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);
        AlgorithmViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        viewModel.setNavigator(this);
        viewModel.loadNode(getIntent().getExtras().getInt(Extra_NodeId, 0));
        viewModelBinding = DataBindingUtil.setContentView(this, R.layout.activity_algorithm);
        viewModelBinding.setMenu(viewModel);
        initViews();
        RecyclerView recyclerView=  findViewById(R.id.rvPostsLis);

        adapter = new SimpleLayoutAdapter<>(R.layout.activity_algorithm_list, item -> {
            if(item.getHasDescription() || item.getChildCount()>1 || viewModel.getAlgorithmNodeDescription().getValue().getFirstChildNodeId() == null)
            {
                this.openAlgorithm(item.getId(),viewModel.getAlgorithmNodeDescription().getValue().getId());
            }
            else {
                this.openAlgorithm(item.getFirstChildNodeId(),viewModel.getAlgorithmNodeDescription().getValue().getId());
            }
        });
        recyclerView.setAdapter(adapter);
        RecyclerView recyclerView1=  findViewById(R.id.rvcPostsLis);
        viewModel.getAdapter().observe(AlgorithmActivity.this, new Observer<List<AlgorithmCardViewModel>>() {
            @Override
            public void onChanged(@Nullable List<AlgorithmCardViewModel> algorithmCardViewModels) {
                adapter.setData(algorithmCardViewModels);
            }
        });

        conditionalAdapter = new SimpleLayoutAdapter<>(R.layout.activity_algorithm_clist, item -> {
            if(item.getHasDescription() || item.getChildCount()>1 || viewModel.getAlgorithmNodeDescription().getValue().getFirstChildNodeId() == null)
            {
                this.openAlgorithm(item.getId(),viewModel.getAlgorithmNodeDescription().getValue().getId());
            }
            else {
                this.openAlgorithm(item.getFirstChildNodeId(),viewModel.getAlgorithmNodeDescription().getValue().getId());
            }
        });
        recyclerView1.setAdapter(conditionalAdapter);


        viewModel.getConditionalAdapter().observe(AlgorithmActivity.this, new Observer<List<AlgorithmDescription>>() {
            @Override
            public void onChanged(@Nullable List<AlgorithmDescription> algorithmDescriptions) {
                conditionalAdapter.setData(algorithmDescriptions);
            }
        });

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
    public void openAlgorithm(int nodeId,int parentId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(Extra_NodeId, nodeId);
        intent.putExtra(Extra_ParentNodeId,parentId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up,R.anim.nothing);
    }
    @Override
    public void returnToPrevious(int parentNodeId) {

        if(parentNodeId != 0) {
            Intent intent = new Intent(this, AlgorithmActivity.class);
            intent.putExtra(Extra_ParentNodeId,parentNodeId);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up,R.anim.nothing);

    }

    @Override
    public void returnHome() {
        finish();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
