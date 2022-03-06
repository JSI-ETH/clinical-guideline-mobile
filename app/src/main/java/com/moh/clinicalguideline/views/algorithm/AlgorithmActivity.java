package com.moh.clinicalguideline.views.algorithm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import androidx.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
    public static MutableLiveData<HashMap<Integer, Integer>>  footersList = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_activity_main);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlgorithmViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.node_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        textViewSymptomTitle = findViewById(R.id.symptom_title);
        textViewFooter = findViewById(R.id.footerTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        textViewFooter.setMovementMethod(new ScrollingMovementMethod());
         menuViewModel.getFooterList().observe(this, footers -> {
             footersList.postValue(footers);
         } );

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
                if (selectedPosition < mainNodeAdapter.getList().size() && selectedPosition != -1 && itemPosition + 1 > mainNodeAdapter.lastActivatedNode) {
                    viewModel.isFirstChildDuplicate = false;
                    mainNodeAdapter.lastActivatedNode = selectedPosition;
                viewModel.feedMap(mainNodeAdapter.getList().get(selectedPosition), null);
                }
            }
        });
        initViews();
    }

    public void initViews() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    public static MutableLiveData<HashMap<Integer, Integer>> getFooterList() {
        return footersList;
    }
}
