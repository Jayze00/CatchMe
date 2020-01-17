package com.example.linearcatchme;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.linearcatchme.database.AppDatabase;
import com.example.linearcatchme.database.Ort;
import com.example.linearcatchme.database.SaveService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabase db;
    private SaveService service;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private Ort ort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Database creation
        db = db.getAppDatabase(getApplicationContext());
        service = new SaveService(db);

        //Check userinput for tracked position
        EditText hoehengradText = (EditText) findViewById(R.id.hoehengrad);
        EditText breitengradText = (EditText) findViewById(R.id.breitengrad);
        hoehengradText.setOnKeyListener(createListener());
        breitengradText.setOnKeyListener(createListener());
        /*
        / Berechtigungs Abfrage
        */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        Timer timer = new Timer();
        TimerTask tasknew = new TimerTask() {
            @Override
            public void run() {
                locationChanged();
                Log.e("trigger", "now triggered");
            }
        };
        timer.schedule(tasknew, 10, 60000);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in zurich and move the camera
        LatLng zurich = new LatLng(47.368650, 8.539183);
        List<LatLng> trackedPlaces = service.getTrackedPlace();
        for (LatLng place : trackedPlaces){
            mMap.addMarker(new MarkerOptions().position(place));
        }
        mMap.addMarker(new MarkerOptions().position(zurich).title("<3"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zurich, 15));
    }

    private void pointToPosition(LatLng position) {
        //Build camera position
        mMap.addMarker(new MarkerOptions().position(position));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private View.OnKeyListener createListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            EditText breitengradText = (EditText) findViewById(R.id.breitengrad);
                            EditText hoehengradText = (EditText) findViewById(R.id.hoehengrad);
                            double breitengrad = Double.parseDouble(breitengradText.getText().toString());
                            double hoehengrad = Double.parseDouble(hoehengradText.getText().toString());
                            if (hoehengrad != 0 && breitengrad != 0) {
                                LatLng newPosition = new LatLng(hoehengrad, breitengrad);
                                pointToPosition(newPosition);
                                service.savePlace(newPosition);
                                TextView text = (TextView) findViewById(R.id.anzahlBesuche);
                                text.setText("Anzahl Besuche: " + 0);
                                return true;
                            } else {
                                return false;
                            }

                        default:
                            break;
                    }
                }
                return false;
            }
        };
    }

    public void locationChanged() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    double laengengrad;
                    double breitengrad;
                    //längengrad
                    laengengrad = location.getLatitude();
                    //breitengrad
                    breitengrad = location.getLongitude();
                    if(!service.getTrackedOrt().isEmpty()) {
                        ort = service.getTrackedOrt().get(0);
                        if(ort.getBreitengrad() == breitengrad && ort.getLaengengrad() == laengengrad){
                            service.saveAuswertung(ort);
                            TextView text = (TextView) findViewById(R.id.anzahlBesuche);
                            text.setText("Anzahl Besuche: " +service.anzBesuche(ort));
                        }
                    } else{
                        TextView text = (TextView) findViewById(R.id.anzahlBesuche);
                        text.setText("Anzahl Besuche: " + 0);
                    }

                }
            }
        });
    }
}
