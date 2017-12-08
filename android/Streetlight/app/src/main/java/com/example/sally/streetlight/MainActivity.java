package com.example.sally.streetlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");

                map = googleMap;

                LatLng sookmyung = new LatLng(37.546637, 126.964692);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(sookmyung).zoom(17.0f).build();
                map.addMarker(new MarkerOptions().position(sookmyung).title("Marker in Sydney"));
                map.moveCamera(CameraUpdateFactory.newLatLng(sookmyung));
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(37.546432, 126.963887))
                        .title("진리관")
                        .snippet("진리관입니다")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mountain_flag));
                map.addMarker(markerOptions);
            }

        });

        try {
            MapsInitializer.initialize(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
