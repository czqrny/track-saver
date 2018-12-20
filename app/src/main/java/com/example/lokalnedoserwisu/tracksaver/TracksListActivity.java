package com.example.lokalnedoserwisu.tracksaver;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TracksListActivity extends AppCompatActivity {

    private TextView tracksTextView;
    private List<String> tracks;
    private int trackId;
    private Track track;

    private DatabaseTracksHelper databaseTracksHelper;
    private Button nextTrackButton;
    private Button prevTrackButton;
    private Button showMapButton;
    private Button deleteTrackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);

        initObjects();
        initViews();
    }

    private void initObjects() {
        databaseTracksHelper=new DatabaseTracksHelper(TracksListActivity.this);
        tracks = databaseTracksHelper.getTracks();
    }

    private void initViews() {
        tracksTextView=findViewById(R.id.tracks_text_view);
        tracksTextView.setText(tracks.toString());
        nextTrackButton=findViewById(R.id.next_track_button);
        prevTrackButton=findViewById(R.id.previous_track_button);
        showMapButton=findViewById(R.id.show_map_button);
        deleteTrackButton=findViewById(R.id.delete_track_button);

        if (tracks.size() > 0) {
            trackId = 0;
            setTrackText(trackId);
            initListeners();
        }
        else {
            tracksTextView.setText("You have no tracks");
        }
    }

    private void initListeners() {
        nextTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trackId >= 0 && trackId <= tracks.size() -2) {
                    trackId ++;
                    setTrackText(trackId);
                }
                else if (trackId == tracks.size() -1) {
                    trackId = 0;
                    setTrackText(trackId);
                }
            }
        });

        prevTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trackId >= 1 && trackId <= tracks.size() -1) {
                    trackId --;
                    setTrackText(trackId);
                }
                else if (trackId == 0) {
                    trackId = tracks.size() -1;
                    setTrackText(trackId);
                }
            }
        });

        deleteTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseTracksHelper.deleteTrack(track);
                tracks = databaseTracksHelper.getTracks();
                if (tracks.size() == 0) {
                    removeListeners();
                    tracksTextView.setText("No tracks left");
                }
                else if (trackId >= 1 && trackId <= tracks.size() -1) {
                    trackId--;
                    Snackbar.make(findViewById(R.id.activity_tracks_list), "Track deleted", Snackbar.LENGTH_LONG).show();
                    setTrackText(trackId);
                }
                else if (trackId == 0) {
                    trackId = tracks.size() -1;
                    Snackbar.make(findViewById(R.id.activity_tracks_list), "Track deleted", Snackbar.LENGTH_LONG).show();
                    setTrackText(trackId);
                }
            }
        });

        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplication().getSharedPreferences("dane", Context.MODE_PRIVATE).edit().putString("trackToShow", track.getName()).commit();
                Intent intent;
                intent= new Intent(getApplicationContext(), ShowTrackMapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTrackText(int trackID){
        String trackName = tracks.get(trackID);
        track = databaseTracksHelper.getTrack(trackName);
        String trackAmountText = "Track " + (trackID+1) + "/" + tracks.size() + "\n";
        tracksTextView.setText(trackAmountText + track.getTrackText());
    }

    private void removeListeners() {
        nextTrackButton.setOnClickListener(null);
        prevTrackButton.setOnClickListener(null);
        showMapButton.setOnClickListener(null);
        deleteTrackButton.setOnClickListener(null);
    }
}
