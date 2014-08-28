package com.twi.awayday2014.components.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.twi.awayday2014.models.Locator;
import com.twi.awayday2014.R;

import static com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;

public class MapFragment extends com.google.android.gms.maps.MapFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final LatLng HOTEL_MARRIOTT = new LatLng(17.385044, 78.486671);
    private static final LatLng THOUGHTWORKS_BANGALORE = new LatLng(12.9316556, 77.6226959);
    private static final LatLng MAJESTIC_BANGALORE = new LatLng(12.9779381, 77.5683899);
    private static final String TAG = "Maps";

    private GoogleMap map;
    private Locator locator;

    public static MapFragment newInstance(int sectionNumber) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpMapIfNeeded(THOUGHTWORKS_BANGALORE);
    }

    private void setUpMapIfNeeded(LatLng place) {
        if (map != null) {
            return;
        }

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Play service is not available");
        } else {
            map = this.getMap();
        }


        map = this.getMap();

        if (map == null) {
            return;
        }

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setMyLocationEnabled(true);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 18));

        map.addMarker(new MarkerOptions()
                .title("Hotel Marriott")
                .snippet("TWI Away Day 2014 is happening right here.")
                .position(place));

        locator = new Locator(getActivity(),
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, " location : " + location.toString());
                    }
                },
                new ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        showCurrentLocationOnMap();
                        //directTo(MAJESTIC_BANGALORE);
                        //overlayImage();
                        //overlayOnLocation(location());
                    }

                    @Override
                    public void onDisconnected() {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (locator != null)
            locator.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (locator != null)
            locator.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locator != null)
            locator.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (locator != null)
            locator.onPause();
    }

    private LatLng location() {
        Location location = locator.getLocation();
        if (location != null) {
            Log.d(TAG, " current Location: " + location.toString());
            Log.d(TAG, " current address: " + locator.getCurrentAddress().size());
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        return THOUGHTWORKS_BANGALORE;
    }

    private void showCurrentLocationOnMap() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location(), 18));
        Marker marker = map.addMarker(new MarkerOptions()
                .title("You are here")
                .position(location()));
        marker.showInfoWindow();
    }

    private void directTo(LatLng place) {
        map.addPolyline(new PolylineOptions().geodesic(true)
                .add(location())
                .add(THOUGHTWORKS_BANGALORE)
                .add(MAJESTIC_BANGALORE));
    }

    public void overlayOnLocation(LatLng loc) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18));

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_drawer))
                .anchor(0, 1)
                .position(loc, 8600f, 6500f);
        map.addGroundOverlay(newarkMap);
    }

    private void overlayImage() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.712216, -74.22655), 18));

        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(40.712216, -74.22655),
                new LatLng(40.773941, -74.12544));
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .positionFromBounds(newarkBounds);
        map.addGroundOverlay(newarkMap);
    }

}