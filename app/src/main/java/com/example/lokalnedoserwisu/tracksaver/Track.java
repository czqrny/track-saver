package com.example.lokalnedoserwisu.tracksaver;

import android.graphics.Color;
import android.icu.util.Calendar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class Track {
    private String name;
    private ArrayList<String> time;
    private ArrayList<Double> lon;
    private ArrayList<Double> lat;
    private ArrayList<Double> alt;

    public Track(String name, ArrayList<String> time, ArrayList<Double> lon, ArrayList<Double> lat, ArrayList<Double> alt) {
        this.name = name;
        this.time = time;
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
    }

    public String getName() {
        return this.name;
    }

    public String getTrackText() {
        String text = "Time: " + this.getTime() + "\n";
        text += "Points: " + this.getPointCount() + "\n";
        text += "Location:";
        text += "\n\t" + lon.get(0);
        text += "\n\t" + lat.get(0);
        text += "\n\t" + alt.get(0);
        return text;
    }

    public ArrayList<Object> getPoint(int i) {
        ArrayList<Object> point = new ArrayList<Object>();
        point.add(this.time.get(i));
        point.add(this.lon.get(i));
        point.add(this.lat.get(i));
        point.add(this.alt.get(i));
        return point;
    }

    public int getPointCount() {
        return this.time.size();
    }

    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        Long timeLong = Long.parseLong(this.time.get(0));
        calendar.setTimeInMillis(timeLong);
        String time =  calendar.getTime().toString();
        return time;
    }

    public void showOnMap(GoogleMap googleMap) {
        LatLng start = new LatLng(lat.get(0), lon.get(0));
        LatLng stop = new LatLng(lat.get(lat.size()-1), lon.get(lon.size()-1));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 18));
        googleMap.addMarker(new MarkerOptions().position(start).title("Start point"));
        PolylineOptions polylineTrack = new PolylineOptions()
                .color(Color.GREEN)
                .width(12);
        for (int i = 0; i <= this.getPointCount() - 1; i++) {
            LatLng point = new LatLng(lat.get(i), lon.get(i));
            polylineTrack.add(point);
        }
        googleMap.addPolyline(polylineTrack);
        googleMap.addMarker(new MarkerOptions().position(stop).title("Last point"));
    }
}
