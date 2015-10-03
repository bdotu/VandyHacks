package com.parse.starter;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList longs;
    ArrayList lats;
    ArrayList cords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getUsers();
        Log.d("score", "fefesdsdsdsd" + longs.size() + lats.size());
        Log.d("score", "fefesdsdsdsd" + longs.size() + lats.size());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    public void getUsers(){
        lats =  new ArrayList<Double>();
        longs =  new ArrayList<Double>();
        cords = new ArrayList<LatLng>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("playerLocations");
        //query.whereEqualTo("gameSession", "pee");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < scoreList.toArray().length; i++) {
                        ParseObject object = scoreList.get(i);
                        Log.d("reporting", "hey" + scoreList);
                        Log.d("reporting", "hey" + object.getDouble("pLong"));
                        Log.d("reporting", "hey" + object.getDouble("pLat"));
                        LatLng cordNow = new LatLng(object.getDouble("pLat"), object.getDouble("pLong"));
                        cords.add(cordNow );

                    }

                    setUpMapIfNeeded();
                    addPlayers();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void addPlayers(){
        Log.d("LOGGG", "THIS IS IT" + cords.toArray().length);
        for (int i = 0 ; i < cords.toArray().length; i++){

            LatLng currentCord = (LatLng) cords.get(i);
            Log.d("LOGGG", "THIS IS IT" + currentCord);
             mMap.addMarker(new MarkerOptions()
                    .position(currentCord)
                    .title("Player" + i)
                    .snippet("poop"));
        }


    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #mma()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();
                addPlayers();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
