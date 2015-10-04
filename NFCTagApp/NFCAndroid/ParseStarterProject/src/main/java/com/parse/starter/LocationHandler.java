package com.parse.starter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

class LocationHandler implements LocationListener {
    private static final String TAG = "LocationHandler";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private MapsActivity mOwner;


    public LocationHandler(double latitude, double longitude, MapsActivity owner) {
        mLastLocation = new Location("DEFAULT");
        mLastLocation.setLatitude(latitude);
        mLastLocation.setLongitude(longitude);
        mOwner = owner;
    }


    public void start(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);

                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(1000);
                        mLocationRequest.setFastestInterval(1000);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient,
                                mLocationRequest,
                                LocationHandler.this);
                        Log.d(TAG, "onConnected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "onConnectionSuspended - " + i);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed");
                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mOwner.updateLocation(location);
    }

    //returns last location in the format of 'latitude,longitude'
    public String format() {
        Log.d(TAG, "lat: " + mLastLocation.getLatitude() + " | long: " + mLastLocation.getLongitude());
        return mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
    }

    //no longer need to listen for updates.
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mGoogleApiClient.disconnect();

    }
}


