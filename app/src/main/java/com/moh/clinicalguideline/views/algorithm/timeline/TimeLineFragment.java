package com.moh.clinicalguideline.views.algorithm.timeline;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.AlgorithmFragmentTimelineBinding;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseFragment;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import javax.inject.Inject;

public class TimeLineFragment extends BaseFragment {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AlgorithmFragmentTimelineBinding binding;
    private TimelineViewModel viewModel;
    private AlgorithmViewModel parentViewModel;
    private TimeLineAdapter<TimeLineNodeViewModel> adapter;

    public static TimeLineFragment newInstance() {
        return new TimeLineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.algorithm_fragment_timeline, container, false);
        return binding.getRoot();
  }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(AlgorithmViewModel.class);
        viewModel = ViewModelProviders.of((FragmentActivity) getActivity(),viewModelFactory).get(TimelineViewModel.class);
        binding.setViewModel(viewModel);
        //load child nodes when new node is selected
        parentViewModel.setOnNodeSelectedListener( node -> {
            viewModel.onNodeSelected(node);
        });
        //when a child node is selected inform parent
        viewModel.getNode().observe((LifecycleOwner) getActivity(), node -> {
            parentViewModel.PreviewNode(node.getNode());
        });
        initAdapters();
    }

    public void initAdapters(){
      adapter = new TimeLineAdapter<>(R.layout.algorithm_fragment_timeline_list, item -> {
          if(item.isActive())
              viewModel.selectNode(item.getPositionId());
          else{
              try {
                  viewModel.selectNode(item.getPositionId() + 1);
              } catch (IndexOutOfBoundsException obe){
                  Log.e("TimeLineFragment", "Unable to navigate to that node", obe);
              }
          }
        });
      binding.setAdapter(adapter);
      viewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
          @Override
          public void onPropertyChanged(Observable sender, int propertyId) {
              adapter.setData(viewModel.getNodes());
          binding.notifyChange();
          }
      });
//      nodes -> {
//          //adapter.setData(nodes);
//          binding.notifyChange();
//      });
    }
}