package com.moh.clinicalguideline.views.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.ActivityMenuBinding;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.algorithm.AlgorithmActivity;

import javax.inject.Inject;

public class MenuActivity extends BaseActivity implements MenuNavigator{

    @Inject
    public MenuViewModel viewModel;

    private ActivityMenuBinding viewModelBinding;

    private RecyclerView symptomsListView;

    private SearchView searchView;

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
                        viewModel.loadAll();
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
        navigation.setSelectedItemId(R.id.all_symptom);

        searchView = (SearchView) findViewById(R.id.simpleSearchView);
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
        symptomsListView = (RecyclerView) findViewById(R.id.recycler_view);
        if (symptomsListView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) symptomsListView
                    .getLayoutManager();
            final int[] firstVisibleInListview = {linearLayoutManager.findFirstVisibleItemPosition()};

//            symptomsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView,
//                                       int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    int currentFirstVisible = linearLayoutManager.findFirstVisibleItemPosition();
//
//                    if (currentFirstVisible > firstVisibleInListview[0] && firstVisibleInListview[0]> -1 )
//                    {
//                        Log.d("RecyclerView scrolled: ", "scroll down!");
//                        searchView.setVisibility(View.GONE);
//                    }
//                    else  if (currentFirstVisible < firstVisibleInListview[0])
//                    {
//                        Log.d("RecyclerView scrolled: ", "scroll up!");
//                        searchView.setVisibility(View.VISIBLE);
//                    }
//
//                    firstVisibleInListview[0] = currentFirstVisible;
//                }
//            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
    }

    @Override
    public void openSymptom(int nodeId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(AlgorithmActivity.Extra_NodeId, nodeId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }
}
