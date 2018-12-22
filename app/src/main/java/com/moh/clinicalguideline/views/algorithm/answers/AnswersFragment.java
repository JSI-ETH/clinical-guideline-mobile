package com.moh.clinicalguideline.views.algorithm.answers;

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
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentAnswersBinding;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentOptionsBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseFragment;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import javax.inject.Inject;

public class AnswersFragment extends BaseFragment {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmFragmentAnswersBinding binding;
    private AnswersViewModel viewModel;
    private AlgorithmViewModel parentViewModel;
    private SimpleLayoutAdapter<AlgorithmDescription> adapter;

    public static AnswersFragment newInstance() {
        return new AnswersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_answers, container, false);
        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity(),viewModelFactory).get(AnswersViewModel.class);

        binding.setViewModel(viewModel);

        //load child nodes when new node is selected
        parentViewModel.getSelectedItemId().observe((LifecycleOwner) getActivity(), Id -> {
            viewModel.loadNodes(Id);
        });

        //when a child node is selected inform parent
        viewModel.getSelectedId().observe((LifecycleOwner) getActivity(), Id -> {
            parentViewModel.selectedNewNode(Id);
        });

        initAdapters();
    }

    public void initAdapters(){
      adapter = new SimpleLayoutAdapter<>(R.layout.algorithm_fragment_options_list, item -> {
            viewModel.selectNode(item.getId());
      });
      binding.setAdapter(adapter);
      viewModel.getNodes().observe((LifecycleOwner) getActivity(), nodes -> {
          adapter.setData(nodes);
          binding.notifyChange();
      });
    }
}