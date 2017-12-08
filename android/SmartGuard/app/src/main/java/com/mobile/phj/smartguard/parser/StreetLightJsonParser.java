package com.mobile.phj.smartguard.parser;

import android.util.Log;

import com.mobile.phj.smartguard.vo.StreetLight;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 2017-12-08.
 */

public class StreetLightJsonParser {
    public static ArrayList<StreetLight> parse(String json) {
        ArrayList<StreetLight> list = new ArrayList<StreetLight>();
        try {

            JSONArray array = new JSONArray(json);
            StreetLight streetLight;
            for (int i = 0; i < array.length(); i++) {
                streetLight = new StreetLight();
                streetLight.id = array.getJSONObject(i).getString("id");
                streetLight.code = array.getJSONObject(i).getInt("code");
                streetLight.lat = array.getJSONObject(i).getDouble("lat");
                streetLight.lon = array.getJSONObject(i).getDouble("lon");
                streetLight.info = array.getJSONObject(i).getString("info");
                streetLight.alram = array.getJSONObject(i).getInt("alarm");
                list.add(streetLight);
                Log.v("in Parser..........", streetLight.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
