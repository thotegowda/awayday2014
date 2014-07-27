package com.twi.awayday2014;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;

public class Locator
        implements ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    public static final String SHARED_PREFERENCES = "com.example.android.location.SHARED_PREFERENCES";
    public static final String KEY_UPDATES_REQUESTED ="com.example.android.location.KEY_UPDATES_REQUESTED";

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    public static final int FAST_CEILING_IN_SECONDS = 1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    public static final List<Address> UNKNOWN_ADDRESS = new ArrayList<Address>();
    public static final String TAG = "Maps";

    private final LocationRequest mLocationRequest;
    private final Context context;
    private final LocationListener locationListener;
    private final ConnectionCallbacks callbacks;
    private boolean mUpdatesRequested;
    private final SharedPreferences mPrefs;
    private final SharedPreferences.Editor mEditor;
    private final LocationClient mLocationClient;

    public Locator(Context context, LocationListener locationListener, ConnectionCallbacks callbacks) {
        this.context = context;
        this.locationListener = locationListener;
        this.callbacks = callbacks;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        mUpdatesRequested = false;

        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mLocationClient = new LocationClient(context, this, this);
    }

    public void onStop() {
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        Log.e(TAG, "calling disconnect");
        mLocationClient.disconnect();
    }

    public void onPause() {
        mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
    }

    public void onStart() {
        Log.e(TAG, "calling connect");
        mLocationClient.connect();
    }

    public void onResume() {
        if (mPrefs.contains(KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(KEY_UPDATES_REQUESTED, false);
        } else {
            mEditor.putBoolean(KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }

    private boolean servicesConnected() {
        if (ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)) {
            return true;
        } else {
            return false;
        }
    }

    public Location getLocation() {
        if (servicesConnected()) {
            return mLocationClient.getLastLocation();
        }
        return null;
    }

    public List<Address> getCurrentAddress() {
        return getAddress(mLocationClient.getLastLocation());
    }

    @SuppressLint("NewApi")
    public List<Address> getAddress(Location location) {
        List<Address> addresses = UNKNOWN_ADDRESS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            Toast.makeText(context, "Geocoder is not available for your OS version", Toast.LENGTH_LONG).show();
            return UNKNOWN_ADDRESS;
        }

        if (servicesConnected()) {
            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                Log.e(TAG, "Failed to retrieve address : " + e.getMessage());
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Failed to retrieve address : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return addresses;
    }

    public void startUpdates(View v) {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "onConnected");
        callbacks.onConnected(bundle);
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    @Override
    public void onDisconnected() {
        callbacks.onDisconnected();
        Log.e(TAG, "onDisconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        (Activity) context,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    private void startPeriodicUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, locationListener);
    }


    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(locationListener);
    }
}
