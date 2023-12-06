package com.example.recipefinalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.recipefinalproject.databinding.ActivityAddRecipeBinding;

public class AddRecipeActivity extends AppCompatActivity {
    ActivityAddRecipeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
