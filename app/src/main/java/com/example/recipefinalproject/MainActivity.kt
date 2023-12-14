package com.example.recipefinalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.recipefinalproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding: ActivityMainBinding
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        val navController = findNavController(this, R.id.nav_host_fragment_activity_main2)
        setupWithNavController(binding.navView, navController)
        binding.floatingActionButton.setOnClickListener { view: View? ->
            if (FirebaseAuth.getInstance().currentUser == null) Toast.makeText(
                this,
                "Please login to add recipe",
                Toast.LENGTH_SHORT
            ).show() else startActivity(Intent(this@MainActivity, AddRecipeActivity::class.java))
        }
    }
}