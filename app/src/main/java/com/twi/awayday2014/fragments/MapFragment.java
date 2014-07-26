package com.twi.awayday2014.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twi.awayday2014.R;
import android.app.Fragment;

public class MapFragment extends com.google.android.gms.maps.MapFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final LatLng HOTEL_MARRIOTT = new LatLng(17.385044, 78.486671);

    private GoogleMap map;

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

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map != null) {
            return;
        }
        map = this.getMap();
        if (map == null) {
            return;
        }

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);

        map.setMyLocationEnabled(true);

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(HOTEL_MARRIOTT, 10));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(HOTEL_MARRIOTT, 18));

        map.addMarker(new MarkerOptions()
                .title("Hotel Marriott")
                .snippet("TWI Away Day 2014 is happening right here.")
                .position(HOTEL_MARRIOTT));
    }
}
