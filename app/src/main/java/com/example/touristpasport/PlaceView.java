package com.example.touristpasport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlaceView extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private TextView textViewPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

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

    public void showPlaceInfo(DocumentSnapshot document){
        // Get the place name from the document
        String placeName = document.getString("name");
        // Update the TextView with the retrieved place name
        textViewPlaceName.setText(placeName);
    }
}