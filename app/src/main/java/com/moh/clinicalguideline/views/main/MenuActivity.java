package com.moh.clinicalguideline.views.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.ActivityMenuBinding;
import com.moh.clinicalguideline.helper.BaseActivity;
import com.moh.clinicalguideline.views.algorithm.AlgorithmActivity;

import javax.inject.Inject;

public class MenuActivity extends BaseActivity implements MenuNavigator{

    @Inject
    public MenuViewModel viewModel;

    private ActivityMenuBinding viewModelBinding;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.adult_symptom:
                        viewModel.loadAdultSymptom();
                         return true;
                    case R.id.child_symptom:
                        viewModel.loadChildSymptom();
                         return true;
                    case R.id.chronic_care:
                        viewModel.loadChronic();
                         return true;
                    case R.id.all_symptom:
                        viewModel.loadChronic();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel.setNavigator(this);
        viewModelBinding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        viewModelBinding.setMenu(viewModel);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.adult_symptom);
    }

    @Override
    public void openSymptom(int nodeId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(AlgorithmActivity.Extra_NodeId, nodeId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        SearchView searchView = (SearchView) viewModelBinding.simpleSearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                viewModel.getAdapter().getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}
