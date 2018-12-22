package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.AlgorithmActivityMainBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.main.MenuActivity;

import java.util.List;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmActivityMainBinding binding;
    private AlgorithmViewModel viewModel;

    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;
    private SimpleLayoutAdapter<AlgorithmDescription> conditionalAdapter;

    public static String Extra_NodeId = "Extra_NodeId";
    public static String Extra_ParentNodeId="Extra_ParentNodeId";
    public static String Extra_PageId ="Extra_PageId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        viewModel.setNavigator(this);
        binding = DataBindingUtil.setContentView(this, R.layout.algorithm_activity_main);
        binding.setLifecycleOwner(this);
        binding.setMenu(viewModel);

        int nodeid = getIntent().getExtras().getInt(Extra_NodeId, 0);
        //int page = getIntent().getExtras().getInt(Extra_PageId, 0);
        viewModel.loadNode(nodeid);
        initViews();
        initAdapters();
    }

    public void initAdapters(){
        conditionalAdapter = new SimpleLayoutAdapter<>(R.layout.algorithm_fragment_answers_list, item -> {
            if(item.getHasDescription() || item.getChildCount()>1 || item.getFirstChildNodeId() == null)
            {
                this.openAlgorithm(item.getId());
            }
            else {
                this.openAlgorithm(item.getFirstChildNodeId());
            }
        });
        binding.setAnswersAdapter(conditionalAdapter);
        binding.getMenu().getAnswerNodes().observe(AlgorithmActivity.this, new Observer<List<AlgorithmDescription>>() {
            @Override
            public void onChanged(@Nullable List<AlgorithmDescription> algorithmDescriptions) {
                conditionalAdapter.setData(algorithmDescriptions);
                binding.notifyChange();
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
    public void openAlgorithm(int nodeId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(Extra_NodeId, nodeId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up,R.anim.nothing);
    }

    @Override
    public void openAlgorithmByPage(int pageId,int nodeId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(Extra_PageId, pageId);
        intent.putExtra(Extra_ParentNodeId,nodeId);
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
