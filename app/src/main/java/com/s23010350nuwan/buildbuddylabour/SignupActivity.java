package com.s23010350nuwan.buildbuddylabour;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText signupEmail, signupPassword, confirmPassword;
    private Button signupButton;
    private TextView signInText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirm_password);
        signupButton = findViewById(R.id.button2);
        signInText = findViewById(R.id.textView5); // "Sign In" text

        // Sign Up button click
        signupButton.setOnClickListener(view -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(email)) {
                signupEmail.setError("Email is required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                signupPassword.setError("Password is required.");
                return;
            }
            if (password.length() < 6) {
                signupPassword.setError("Password must be at least 6 characters.");
                return;
            }
            if (!password.equals(confirm)) {
                confirmPassword.setError("Passwords do not match.");
                return;
            }

            // Create user with Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            // Redirect to login or main activity
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // "Sign In" text click → Navigate to Login screen
        signInText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}
