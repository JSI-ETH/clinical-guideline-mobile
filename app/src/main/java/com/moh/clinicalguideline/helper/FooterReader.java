package com.moh.clinicalguideline.helper;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FooterReader {
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("db/footer.json");
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

    public HashMap<Integer, Integer> createFooterList(Context context) {
//        ArrayList<HashMap<Integer, Integer>> footersList = new ArrayList<>();
        HashMap<Integer, Integer> footerMap = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            JSONArray footersArray = obj.getJSONArray("footers");
            for (int i = 0; i < footersArray.length(); i++) {
                JSONObject footerJsonObj = footersArray.getJSONObject(i);
                Integer page = footerJsonObj.getInt("page");
                Integer id = footerJsonObj.getInt("id");

                footerMap.put(page, id);
//                footersList.add(footerMap);
            }
                return footerMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return footerMap;
    }
}
