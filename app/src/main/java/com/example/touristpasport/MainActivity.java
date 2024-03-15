package com.example.touristpasport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button redirect = (Button) findViewById(R.id.button);

        firestore = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("username", "martinstoimenov02");
        user.put("password", "1234567");
        user.put("email", "martinstoimenov02@gmail.com");
        user.put("name", "Marty");
        user.put("phoneNumber", "0884462531");
        user.put("rememberMe", true);
        user.put("FirstLogin", true);
        user.put("notifications", true);

        final Object[] retreivedUser = {null};
        final Object[] retreivedPlace = {null};

        firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                retreivedUser[0] = documentReference;
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Map<String, Object> nto100 = new HashMap<>();
        nto100.put("urlMap", "https://maps.app.goo.gl/rH6N9rinyzmwrCGT6");
        nto100.put("name", "Черни връх");

        firestore.collection("nto100").add(nto100).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                retreivedPlace[0] = documentReference;
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        Map<String, Object> place = new HashMap<>();
        place.put("isNTO", retreivedPlace[0]);
        place.put("userId", retreivedUser[0]);
        place.put("isFavourite", true);
        place.put("isVisited", false);
        place.put("urlMap", "https://maps.app.goo.gl/rH6N9rinyzmwrCGT6");
        place.put("name", "Черни връх");

        firestore.collection("places").add(place).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Create a reference to the cities collection
        CollectionReference placesRef = firestore.collection("places");
        String documentId;
        placesRef.limit(1) // Limit the query to 1 result
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the ID of the document
                                String documentId = document.getId();
                                // Handle the ID (you can print it or use it as needed)
                                Toast.makeText(getApplicationContext(), "Document ID: " + documentId, Toast.LENGTH_LONG).show();

                                // Set click listener for redirect button
                                redirect.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Create intent to start PlaceView activity
                                        Intent intent = new Intent(MainActivity.this, PlaceView.class);
                                        // Pass documentId as an extra
                                        intent.putExtra("documentId", documentId);
                                        // Start the activity
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            // Handle errors
                            Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}