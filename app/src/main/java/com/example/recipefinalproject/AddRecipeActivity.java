package com.example.recipefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import com.example.recipefinalproject.databinding.ActivityAddRecipeBinding;

import android.os.Bundle;
import android.view.View;

public class AddRecipeActivity extends AppCompatActivity {

    ActivityAddRecipeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        String recipeName = binding.etRecipeName.getText().toString();
        String recipeDescription = binding.etDescription.getText().toString();
    }
}