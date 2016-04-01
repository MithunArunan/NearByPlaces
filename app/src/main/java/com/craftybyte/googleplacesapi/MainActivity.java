package com.craftybyte.googleplacesapi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.craftybyte.googleplacesapi.R.layout.list_gasstation_details;

public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private ListView gasStationListView = null;
    public static ArrayList<HashMap<String,String>> gasStationList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check for Google Play services..
     /*   int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }

        // Set up to use the Google maps API!
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
     */

        gasStationListView = (ListView) findViewById(R.id.list_gasStation_id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
        FetchGasStation fetchStation = new FetchGasStation();
        fetchStation.execute("");
        if(gasStationList != null && gasStationList.size() > 0) {

            ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(),
                    gasStationList,
                    list_gasstation_details,
                    new String[]{GasStationParser.TAG_NAME, GasStationParser.TAG_VICINITY},
                    new int[]{R.id.list_gas_name, R.id.list_gas_description});
            gasStationListView.setAdapter(listAdapter);

        }
    }

    protected void onStop(){
        //mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
