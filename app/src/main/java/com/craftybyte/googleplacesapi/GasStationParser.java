package com.craftybyte.googleplacesapi;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mithun on 04-Oct-15.
 */
public class GasStationParser {

    private ArrayList<HashMap<String,String>> tmpList = null;
    public static final String TAG_STATUS = "status";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_ICON = "icon";
    public static final String TAG_VICINITY = "vicinity";
    public static final String TAG_GEOMETRY = "geometry";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG = "lng";
    public static final String TAG_NAME = "name";

    public ArrayList<HashMap<String,String>> parse(String jsonStr)
    {
        tmpList = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String s = jsonObj.getString(TAG_STATUS);
            if(s.equalsIgnoreCase("OK"))
            {  // If the JSON response is OK
                Log.d(String.valueOf(getClass()),"Status is OKAY");
                JSONArray results = jsonObj.getJSONArray(TAG_RESULTS);
                HashMap<String,String> haspMap = null;
                for(int i=0; i < results.length(); i++)
                {  // For each result!
                    JSONObject resultObj = results.getJSONObject(i);
                    haspMap = new HashMap<String,String>();
                    JSONObject geoObject = resultObj.getJSONObject(TAG_GEOMETRY);
                    JSONObject locObject = geoObject.getJSONObject(TAG_LOCATION);
                    haspMap.put(TAG_NAME, resultObj.getString(TAG_NAME));
                    haspMap.put(TAG_LAT,String.valueOf(locObject.getDouble(TAG_LAT)));
                    haspMap.put(TAG_LNG,String.valueOf(locObject.getDouble(TAG_LNG)));
                    haspMap.put(TAG_ICON, resultObj.getString(TAG_ICON));
                    haspMap.put(TAG_VICINITY, resultObj.getString(TAG_VICINITY));
                    tmpList.add(haspMap);
                }
            }
            else
            { // Do some notifications to avoid trouble
                Log.d(String.valueOf(getClass()),"JSON: Status Failed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmpList;
    }
}
