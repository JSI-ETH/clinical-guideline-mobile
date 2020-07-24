package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.ViewModelProvider;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.recyclerview.ClickListener;
import com.moh.clinicalguideline.helper.recyclerview.MainNodeAdapter;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.main.MenuViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class AlgorithmActivity extends BaseActivity implements AlgorithmNavigator {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Inject
    public MenuViewModel menuViewModel;

    private AlgorithmViewModel viewModel;
    private List<AlgorithmDescription> algorithmDescriptions;
    public static String Extra_NodeId = "Extra_NodeId";
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    TextView textViewSymptomTitle, textViewFooter;
    private MainNodeAdapter mainNodeAdapter;
    private ProgressBar loadingProgressBar;
    public static HashMap<Integer, Integer> footersList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_activity_main);

        RecyclerView recyclerView = findViewById(R.id.node_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        textViewSymptomTitle = findViewById(R.id.symptom_title);
        textViewFooter = findViewById(R.id.footerTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        textViewFooter.setMovementMethod(new ScrollingMovementMethod());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        footersList = menuViewModel.getFooterList();

        viewModel.setNavigator(this);
        int nodeId = getIntent().getExtras().getInt(Extra_NodeId, 0);
        mainNodeAdapter = new MainNodeAdapter(this, viewModel.getNodeList(), viewModel.getRecyclerMap(), viewModel);

        viewModel.getLiveNodeList().observe(this, nodes -> {
            assert nodes != null;
            mainNodeAdapter.setKeyNodesList(
                    nodes,
                    viewModel.getRecyclerMap(),
                    viewModel.getRecyclerUpdate().getTypeOfUpdate(),
                    viewModel.getRecyclerUpdate().getUpdateIndex());

           viewModel.setRecyclerDefault();
            Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(mainNodeAdapter.getItemCount() - 1);
        });

        viewModel.setAdapterToViewModel(mainNodeAdapter);
        viewModel.LoadNode(nodeId);
        viewModel.setTitle(nodeId, textViewSymptomTitle);
        viewModel.getFooterText().observe(this,this::setFooter);
        viewModel.getNode().observe(this, algorithmDescription -> {
            loadingProgressBar.setVisibility(View.GONE);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainNodeAdapter);

        //Open Url Clicked
        viewModel.getSelectedPageId().observe(this, page -> {
            viewModel.LoadPage(page, textViewSymptomTitle);
        });

        mainNodeAdapter.setOnItemClickHandler(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }

            @Override
            public void selectNextChildNode(int selectedPosition, int itemPosition, View v) {
                viewModel.feedMap(mainNodeAdapter.getList().get(selectedPosition), null);
            }
        });
        initViews();
    }

    public void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setFooter(String footerText) {
        if(textViewFooter.getText().toString().equals("Notes") && footerText.length() > 0) textViewFooter.setText("");
        textViewFooter.setText(Html.fromHtml(String.format("%s%s", textViewFooter.getText().toString() + "\n", footerText)));
    }

    public static HashMap<Integer, Integer> getFooterList() {
        return footersList;
    }
}
