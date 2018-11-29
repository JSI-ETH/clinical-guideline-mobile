package com.moh.clinicalguideline.views.algorithm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.ActivityAlgorithmBinding;
import com.moh.clinicalguideline.databinding.ActivityMenuBinding;
import com.moh.clinicalguideline.helper.BaseActivity;
import com.moh.clinicalguideline.views.main.MenuActivity;
import com.moh.clinicalguideline.views.symptom.SymptomActivity;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {

    public static String Extra_NodeId = "Extra_NodeId";
    public static String Extra_ParentNodeId="Extra_ParentNodeId";
    @Inject
    public AlgorithmViewModel viewModel;

    private ActivityAlgorithmBinding viewModelBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel.setNavigator(this);
        viewModelBinding = DataBindingUtil.setContentView(this, R.layout.activity_algorithm);
        viewModelBinding.setMenu(viewModel);
        int id = getIntent().getExtras().getInt(Extra_NodeId,0);
        int parentId = getIntent().getExtras().getInt(Extra_ParentNodeId,0);
        viewModel.loadNode(id,parentId);
    }

    @Override
    public void openAlgorithm(int nodeId,int parentId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(SymptomActivity.Extra_NodeId, nodeId);
        intent.putExtra(Extra_ParentNodeId,parentId);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        returnToPrevious(viewModel.getParentId());
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
    public void returnHome() {
        finish();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
