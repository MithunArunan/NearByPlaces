package com.craftybyte.googleplacesapi;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mithun on 30-Sep-15.
 */
public class FetchGasStation extends AsyncTask<String, Void, ArrayList<HashMap<String,String>>> {

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps){
        super.onPostExecute(hashMaps);
        // Add it to the list...
        if(MainActivity.gasStationList != null)
        MainActivity.gasStationList.clear();
        MainActivity.gasStationList.addAll(hashMaps);
    }


    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String key="AIzaSyDcoalskkoS9L20hYl4nNKCY7zDLg54jhc";
        String format = "json";
        // Will contain the raw JSON response as a string.
        String gasStationJsonStr = null;
        String BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
        final String LOCATION_PARAM = "location";
        final String RADIUS_PARAM = "radius";
        final String TYPE_PARAM = "types";
        final String KEY_PARAM = "key";
        double lan = 9.581762;
        double lon = 77.837698;
        final String latLong = lan+","+lon;
        String BASE_URL = BASE+format+"?";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(LOCATION_PARAM,latLong)
                .appendQueryParameter(RADIUS_PARAM,"50000").appendQueryParameter(TYPE_PARAM,"gas_station")
                .appendQueryParameter(KEY_PARAM,key)
                .build();
        Log.d("FetchGasStation URL: ",builtUri.toString());
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                gasStationJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                gasStationJsonStr = null;
            }
            gasStationJsonStr = buffer.toString();
            Log.v("Data", "Here it is " + gasStationJsonStr);

        }
        catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            gasStationJsonStr = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        GasStationParser gsp = new GasStationParser();
        return gsp.parse(gasStationJsonStr);
    }
}
