package com.moh.clinicalguideline.views.algorithm.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.AlgorithmActivityMainBinding;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentOptionsBinding;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentOptionsListBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseFragment;
import com.moh.clinicalguideline.views.algorithm.AlgorithmActivity;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import java.util.List;

import javax.inject.Inject;

public class Options extends BaseFragment {

    private AlgorithmFragmentOptionsBinding viewModelBinding;
    private OptionsViewModel mViewModel;
    private AlgorithmViewModel viewModel;
    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;
    public static Options newInstance() {
        return new Options();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModelBinding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_options, container, false);
        View view = viewModelBinding.getRoot();
       return view;
 // return inflater.inflate(R.layout.algorithm_fragment_options,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelBinding.setMenu(viewModel);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
       // mViewModel = ViewModelProviders.of(this).get(OptionsViewModel.class);
        initAdapters();
        // TODO: Use the ViewModel

    }
    public void initAdapters(){
        adapter = new SimpleLayoutAdapter<>(R.layout.algorithm_fragment_options_list, item -> {
            if(item.getHasDescription() || item.getChildCount()>1 || item.getFirstChildNodeId() == null)
            {
                viewModel.getNavigator().openAlgorithm(item.getId());
            }
            else {
                viewModel.getNavigator().openAlgorithm(item.getFirstChildNodeId());
            }
        });
      viewModelBinding.setOptionsAdapter(adapter);

        viewModel.getChildNodes().observe((LifecycleOwner) getActivity(), new Observer<List<AlgorithmCardViewModel>>() {
            @Override
            public void onChanged(@Nullable List<AlgorithmCardViewModel> algorithmCardViewModels) {
                adapter.setData(algorithmCardViewModels);
                viewModelBinding.notifyChange();
            }
        });
    }
}
