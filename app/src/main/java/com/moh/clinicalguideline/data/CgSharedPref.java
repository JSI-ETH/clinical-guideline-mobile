package com.moh.clinicalguideline.data;

import android.content.Context;
import android.preference.PreferenceManager;

public class CgSharedPref {
    private static final String NODE_TITLE = "Node Title";
    private Context context;

    public CgSharedPref(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(NODE_TITLE, title).apply();
    }

    public String getNodeTitle() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NODE_TITLE, "null");
    }
}
