package com.moh.clinicalguideline.views.algorithm.options;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
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

public class OptionsFragment extends BaseFragment {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmFragmentOptionsBinding binding;
    private OptionsViewModel viewModel;
    private AlgorithmViewModel parentViewModel;
    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;

    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_options, container, false);
        return binding.getRoot();
  }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity(),viewModelFactory).get(OptionsViewModel.class);
        binding.setViewModel(viewModel);
        //load child nodes when new node is selected
        parentViewModel.getNode().observe((LifecycleOwner) getActivity(), node -> {
            viewModel.loadNodes(node.getId());
        });
        //when a child node is selected inform parent
        viewModel.getSelectedNode().observe((LifecycleOwner) getActivity(), node -> {
            parentViewModel.SelectNode(node);
        });
        initAdapters();
    }

    public void initAdapters(){
      adapter = new SimpleLayoutAdapter<>(R.layout.algorithm_fragment_options_list, item -> {
            viewModel.selectNode(item.getNode());
        });
      binding.setAdapter(adapter);
      viewModel.getNodes().observe((LifecycleOwner) getActivity(), algorithmCardViewModels -> {
          adapter.setData(algorithmCardViewModels);
          binding.notifyChange();
      });
    }
}