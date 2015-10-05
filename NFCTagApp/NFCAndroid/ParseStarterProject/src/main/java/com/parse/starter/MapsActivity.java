package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements  CreateNdefMessageCallback  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList longs;
    ArrayList lats;
    ArrayList cords;
    TextView tagged;
    NfcAdapter mNfcAdapter;
    boolean winner = false;
    int poop = 0;
;

    LocationHandler kareem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tagged = (TextView) findViewById(R.id.tag);
        getIt();
        getUsers();
        Log.d("score", "fefesdsdsdsd" + longs.size() + lats.size());
        Log.d("score", "fefesdsdsdsd" + longs.size() + lats.size());

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            Toast.makeText(this, "NFC is  available", Toast.LENGTH_LONG).show();
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);

    }
    void processIntent(Intent intent) {

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam

        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present

            tagged.setTextColor(Color.RED);
            tagged.setText(new String(msg.getRecords()[0].getPayload()));


    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("you are now It!!");
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/vnd.com.parse.starter", text.getBytes()),

                        NdefRecord.createApplicationRecord("com.parse.starter")
                });

        return msg;
    }

    public void getIt(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("whosit");
        query.whereEqualTo("gameSession",getIntent().getExtras().getString("gamesession"));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < scoreList.toArray().length; i++) {
                        ParseObject object = scoreList.get(i);
                        if (object.getString("playerId") == ParseUser.getCurrentUser().getObjectId()){
                            if (object.getBoolean("isplayerIt") == true){
                                tagged.setText("You are it, Hide!");
                                tagged.setTextColor(Color.BLUE);

                            } else {
                                tagged.setText("You are the Chaser!");
                                tagged.setTextColor(Color.MAGENTA);
                            }
                        }



                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
            winner = true;
        } else {
            setUpMapIfNeeded();
        }
    }

    public void getUsers(){
        String session =  getIntent().getExtras().getString("gamesession");
        lats =  new ArrayList<Double>();
        longs =  new ArrayList<Double>();
        cords = new ArrayList<LatLng>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("playerLocations");
        query.whereEqualTo("gameSession", session);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < scoreList.toArray().length; i++) {
                        ParseObject object = scoreList.get(i);
                        Log.d("reporting", "hey" + scoreList);
                        Log.d("reporting", "hey" + object.getDouble("pLong"));
                        Log.d("reporting", "hey" + object.getDouble("pLat"));
                        LatLng cordNow = new LatLng(object.getDouble("pLat"), object.getDouble("pLong"));
                        cords.add(cordNow);

                    }

                    setUpMapIfNeeded();
                    addPlayers();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addPlayers() {

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


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();
                kareem = new LocationHandler(0.0, 0.0, this);

                kareem.start(this);
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

    public void updateLocation(Location uLocation) {
        if (winner == false ){
            //add marker here
            mMap.clear();
            LatLng currentPosition = new LatLng(uLocation.getLatitude(), uLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentPosition).title("Marker"));
            final double lat = uLocation.getLatitude();
            final double longs = uLocation.getLongitude();

            String id;
            ParseObject currentLocation  = new ParseObject("currentLocation");
            id = currentLocation.getObjectId();


            currentLocation.put("lat", uLocation.getLatitude());
            currentLocation.put("long", uLocation.getLongitude());
            ParseACL acl=new ParseACL();
            acl.setPublicReadAccess(true);
            currentLocation.setACL(acl);
            currentLocation.put("playerID", ParseUser.getCurrentUser().getObjectId());
            currentLocation.put("gameSession", getIntent().getExtras().getString("gamesession"));
            currentLocation.saveInBackground();


            ParseQuery<ParseObject> query = ParseQuery.getQuery("currentLocation");
            query.whereEqualTo("playerID", "qtelMf6bYU");
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        LatLng currentPosition2 = new LatLng( object.getDouble("lat"),  object.getDouble("long"));
                        mMap.addMarker(new MarkerOptions().position(currentPosition2).title("guy"));
                    } else {
                        Log.d("score", "Retrieved the object.");
                    }
                }
            });

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("currentLocation");
            query2.whereEqualTo("playerID", "myWy31kf5u");
            query2.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {

                        LatLng currentPosition2 = new LatLng( object.getDouble("lat"), object.getDouble("long"));
                        mMap.addMarker(new MarkerOptions().position(currentPosition2).title("guy"));

                    } else {
                        Log.d("score", "Retrieved the object.");
                    }
                }
            });
        }

        }





}
