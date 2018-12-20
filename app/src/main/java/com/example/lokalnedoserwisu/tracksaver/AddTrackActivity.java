package com.example.lokalnedoserwisu.tracksaver;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    private Button start;
    private Button stop;
    private Button save;
    private Button reset;

    private DatabaseTracksHelper databaseTracksHelper;

    private TextView trackTextView;

    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<Double> lonn = new ArrayList<>();
    private ArrayList<Double> latn = new ArrayList<>();
    private ArrayList<Double> altt = new ArrayList<>();
    private Double lon;
    private Double lat;
    private Double alt;
    private String ster;

    private LocationListener listener;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        initViews();
        initListeners();
        initObjects();

    }

    private void initObjects() {
        databaseTracksHelper = new DatabaseTracksHelper(AddTrackActivity.this);
    }

    private void initListeners() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ster = "start";
                initGPSListener();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ster = "stop";
                trackTextView.setText(time.toString());
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ster = "reset";
                time.clear();
                latn.clear();
                lonn.clear();
                altt.clear();
                trackTextView.setText(getApplication().getSharedPreferences("dane", Context.MODE_PRIVATE).getString("email", "noEmail"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ster = "save";
                if (!(time.isEmpty() && latn.isEmpty() && lonn.isEmpty() && altt.isEmpty())) {
                    String tableName = "Track_" + databaseTracksHelper.getTracksCount();
                    trackTextView.setText("Saved track: \"" + tableName + "\" to database");
                    databaseTracksHelper.CreateTrackTable(tableName);
                    for (Integer i = 0; i < time.size(); i++) {
                        String timestamp = time.get(i);
                        Double lon = lonn.get(i);
                        Double lat = latn.get(i);
                        Double alt = altt.get(i);
                        if (timestamp != null && lon != null && lat != null && alt != null) {
                            databaseTracksHelper.AddPointToTrack(tableName, timestamp, lon, lat, alt);
                        }
                    }
                    time.clear();
                    latn.clear();
                    lonn.clear();
                    altt.clear();
                }
            }
        });
    }


    private void initViews() {
        start = findViewById(R.id.start_saving_track);
        stop = findViewById(R.id.stop_saving_track);
        save = findViewById(R.id.save_track_to_database);
        reset = findViewById(R.id.reset_track_button);
        trackTextView = findViewById(R.id.track_textView);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void initGPSListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lon = location.getLongitude();
                lat = location.getLatitude();
                alt = location.getAltitude();
                if (ster.equals("start")) {
                    trackTextView.setText(Calendar.getInstance().getTime().toString());
                    time.add("" + Calendar.getInstance().getTimeInMillis());
                    lonn.add(lon);
                    latn.add(lat);
                    altt.add(alt);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    , 10);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        if (!lm.getAllProviders().isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (ster.equals("start")) {
                        time.add("" + Calendar.getInstance().getTimeInMillis());
                        lonn.add(lon);
                        latn.add(lat);
                        altt.add(alt);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
//        }
        }
    }
}
