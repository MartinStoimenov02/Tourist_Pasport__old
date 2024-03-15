package com.example.touristpasport;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.touristpasport.databinding.ActivityPlaceViewBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlaceView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPlaceViewBinding binding;
    private FirebaseFirestore firestore;
    private TextView textViewPlaceName;
    float zoomvar = 0;
    LatLng SofiaCenter = new LatLng(42.6977082, 23.3218675);
    LatLng TUsofia = new LatLng(42.6570607, 23.3551086);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize TextView to display place name
        textViewPlaceName = findViewById(R.id.textViewPlaceName);

        // Retrieve the documentId passed from MainActivity
        String documentId = getIntent().getStringExtra("documentId");

        // Query Firestore to get the document with the specified documentId
        firestore.collection("places").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                showPlaceInfo(document);
                            } else {
                                // Document does not exist
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Button plusZOOM = findViewById(R.id.zoomIN);
        Button minusZOOM = findViewById(R.id.zoomOUT);
        Button hybrid = findViewById(R.id.hybridBUT);
        Button normal = findViewById(R.id.normalBUT);
        Button satellite = findViewById(R.id.satelliteBUT);
        Button terrain = findViewById(R.id.terrainBUT);
        Button tus = findViewById(R.id.tusBUT);
        Button back = findViewById(R.id.BACK);

        plusZOOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomvar += 1;
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        minusZOOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomvar -= 1;
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        mMap.addMarker(new MarkerOptions().position(TUsofia).title("TU SOFIA"));
    }

    public void showPlaceInfo(DocumentSnapshot document) {
        // Get the place name from the document
        String placeName = document.getString("name");
        // Update the TextView with the retrieved place name
        textViewPlaceName.setText(placeName);
    }
}