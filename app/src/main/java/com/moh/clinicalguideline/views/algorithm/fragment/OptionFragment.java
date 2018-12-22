package com.moh.clinicalguideline.views.algorithm.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentOptionsBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseFragment;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import javax.inject.Inject;

public class OptionFragment extends BaseFragment {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmFragmentOptionsBinding viewModelBinding;
    private OptionsViewModel viewModel;
    private AlgorithmViewModel parentViewModel;
    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;

    public static OptionFragment newInstance() {
        return new OptionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModelBinding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_options, container, false);
        View view = viewModelBinding.getRoot();
       return view;
  }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity(),viewModelFactory).get(OptionsViewModel.class);
        parentViewModel.getSelectedItemId().observe((LifecycleOwner) getActivity(), Id -> {
            viewModel.loadNodes(Id);
        });
        initAdapters();
    }
    public void initAdapters(){
        adapter = new SimpleLayoutAdapter<>(R.layout.algorithm_fragment_options_list, item -> {
            if(item.getHasDescription() || item.getChildCount()>1 || item.getFirstChildNodeId() == null)
            {
                parentViewModel.getNavigator().openAlgorithm(item.getId());
            }
            else {
                parentViewModel.getNavigator().openAlgorithm(item.getFirstChildNodeId());
            }
        });
      viewModelBinding.setOptionsAdapter(adapter);
      viewModel.getNodes().observe((LifecycleOwner) getActivity(), algorithmCardViewModels -> {
          adapter.setData(algorithmCardViewModels);
          viewModelBinding.notifyChange();
      });
    }
}