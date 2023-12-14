package com.example.recipefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.recipefinalproject.databinding.ActivityLoginBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate (getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(view -> login());
        binding.tvGuest.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        binding.tvSignup.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));


    }

    private void login() {
        String email = Objects.requireNonNull (binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull (binding.etPassword.getText()).toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText( this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher (email).matches()) {
            Toast.makeText( this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText( this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {

            FirebaseApp.initializeApp(this);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText( this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                Toast.makeText( this, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

            }

        }

    }
