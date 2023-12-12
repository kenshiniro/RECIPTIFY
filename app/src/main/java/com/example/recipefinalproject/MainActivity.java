package com.example.recipefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.recipefinalproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.recipefinalproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.floatingActionButton.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                Toast.makeText(this, "Please login to add recipe", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
        });
    }
}