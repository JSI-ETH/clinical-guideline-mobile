package com.moh.clinicalguideline.helper.recyclerview;

import android.view.View;

import com.moh.clinicalguideline.core.AlgorithmDescription;

public interface ClickListener {
    void onItemClick(int position, View v);
    void selectNextChildNode(int selectedPosition, int itemPosition, View v);
}
