//package com.example.lokalnedoserwisu.tracksaver;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//
//public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {
//    private GoogleMap showTrackMap;
//
//    private DatabaseHelper databaseHelper;
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_map);
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.track_map_view);
//        supportMapFragment.getMapAsync(this);
//        initObjects();
//    }
//
//    public void initObjects() {
//        databaseHelper = new DatabaseHelper(ShowMapActivity.this);
//    }
//
//    public void initViews() {
//
//    }
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        showTrackMap = googleMap;
//        showTrackMap.getUiSettings().setZoomControlsEnabled(true);
//        showTrackMap.setMyLocationEnabled(true);
//    }
//}
