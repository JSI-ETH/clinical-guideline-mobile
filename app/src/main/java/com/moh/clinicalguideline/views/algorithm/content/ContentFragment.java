package com.moh.clinicalguideline.views.algorithm.content;

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
import com.moh.clinicalguideline.databinding.AlgorithmFragmentContentBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseFragment;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

public class ContentFragment extends BaseFragment {

    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmFragmentContentBinding binding;
    private ContentViewModel viewModel;
    private AlgorithmViewModel parentViewModel;
    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_content, container, false);
        return binding.getRoot();
  }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(ContentViewModel.class);
        binding.setViewModel(viewModel);
        //load algorithm content when new node is selected
        parentViewModel.getNode().observe((LifecycleOwner) getActivity(), algorithmDescription -> {
            viewModel.loadNode(algorithmDescription);
        });
        //Open Url Clicked
        viewModel.getSelectedPageId().observe((LifecycleOwner) getActivity(),page->{
//            parentViewModel.LoadPage(page);
        });
    }

}