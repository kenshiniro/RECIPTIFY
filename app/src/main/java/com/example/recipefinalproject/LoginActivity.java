package com.example.recipefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.recipefinalproject.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate (getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
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
            Toast.makeText( this, "Login successful", Toast.LENGTH_SHORT).show();
        }
    }
}