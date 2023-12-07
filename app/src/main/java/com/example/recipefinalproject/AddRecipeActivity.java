package com.example.recipefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import com.example.recipefinalproject.databinding.ActivityAddRecipeBinding;

import android.os.Bundle;

public class AddRecipeActivity extends AppCompatActivity {

    ActivityAddRecipeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}