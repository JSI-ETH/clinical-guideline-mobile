package com.moh.clinicalguideline.views.symptom;

import android.os.Bundle;
import android.app.Activity;

import com.moh.clinicalguideline.R;

import java.util.ArrayList;
import java.util.List;

public class SymptomActivity extends Activity {

    public static String Extra_NodeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        List<String> strings = new ArrayList<>();
        strings.add("T");
        strings.add("L");

    }
}
