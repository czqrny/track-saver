package com.example.lokalnedoserwisu.tracksaver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TracksActivity extends AppCompatActivity {

    private Button addTrack;
    private Button showTracks;
    private TextView welcomeText;

    private DatabaseTracksHelper databaseTracksHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        initObjects();
        initViews();
        initListeners();
    }

    private void initObjects() {
    databaseTracksHelper=new DatabaseTracksHelper(TracksActivity.this);
    }

    private void initListeners() {
        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplicationContext(), AddTrackActivity.class);
                startActivity(intent);
            }
        });

        showTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent= new Intent(getApplicationContext(), TracksListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        addTrack=findViewById(R.id.add_track_button);
        showTracks=findViewById(R.id.show_tracks_button);
        welcomeText=findViewById(R.id.welcome_textView);

        String username = getApplication().getSharedPreferences("dane", Context.MODE_PRIVATE).getString("username", null);

        welcomeText.setText("Welcome "+username);
    }
}
