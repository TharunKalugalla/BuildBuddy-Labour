package com.s23010350nuwan.buildbuddylabour;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner statusSpinner, categorySpinner;
    EditText nameInput, phoneInput, districtInput, cityInput;
    TextView locationText;
    Button selectLocationButton, saveButton;

    String selectedLocation = "No location selected"; // Replace with real location later
    String[] statusOptions = {"Available", "Not Available"};
    String[] categoryOptions = {"Electrician", "Plumber", "Carpenter", "Welder", "Mason", "Mechanic", "Painter", "Roofer", "Gardener"};

    DatabaseReference laborRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase setup
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        laborRef = FirebaseDatabase.getInstance().getReference("laborers").child(userId);

        // UI binding
        statusSpinner = findViewById(R.id.status_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
        nameInput = findViewById(R.id.input_name);
        phoneInput = findViewById(R.id.input_phone);
        districtInput = findViewById(R.id.input_district);
        cityInput = findViewById(R.id.input_city);
        locationText = findViewById(R.id.text_location);
        selectLocationButton = findViewById(R.id.btn_select_location);
        saveButton = findViewById(R.id.btn_save);

        // Spinners
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        statusSpinner.setAdapter(statusAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryOptions);
        categorySpinner.setAdapter(categoryAdapter);

        // Fake location logic
        selectLocationButton.setOnClickListener(view -> {
            // You can integrate Google Maps later here
            selectedLocation = "123 Main St, Colombo"; // Dummy data
            locationText.setText(selectedLocation);
        });

        // Save button
        saveButton.setOnClickListener(v -> saveToFirebase());
    }

    private void saveToFirebase() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String district = districtInput.getText().toString().trim();
        String city = cityInput.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || district.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("phone", phone);
        data.put("status", status);
        data.put("category", category);
        data.put("location", selectedLocation);
        data.put("district", district);
        data.put("city", city);

        laborRef.setValue(data)
                .addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}