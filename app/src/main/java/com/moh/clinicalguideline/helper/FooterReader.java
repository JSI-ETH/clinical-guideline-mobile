package com.moh.clinicalguideline.helper;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FooterReader {
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("db/cgFixes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public MutableLiveData<HashMap<Integer, Integer>> createFooterList(Context context) {
       HashMap<Integer, Integer>  footerMap = new HashMap<>();
       MutableLiveData<HashMap<Integer, Integer>>  footerMapLive = new MutableLiveData<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            JSONArray footersArray = obj.getJSONArray("footers");
            for (int i = 0; i < footersArray.length(); i++) {
                JSONObject footerJsonObj = footersArray.getJSONObject(i);
                Integer page = footerJsonObj.getInt("page");
                Integer id = footerJsonObj.getInt("id");

                footerMap.put(page, id);
                footerMapLive.postValue(footerMap);
            }
                return footerMapLive;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return footerMapLive;
    }
}
