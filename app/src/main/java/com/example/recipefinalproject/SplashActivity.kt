package com.example.recipefinalproject;

import android.content.Intent
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recipefinalproject.databinding.ActivitySplashBinding;
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {

    private var splashScreenTime = 3000
    private var timeInterval = 100
    private var progress = 0
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.max = splashScreenTime
        binding.progressBar.progress = progress

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {


            override fun run() {
                if (progress < splashScreenTime) {
                    progress += timeInterval
                    binding.progressBar.progress = progress
                    handler.postDelayed(runnable, timeInterval.toLong())
                } else {
                    FirebaseApp.initializeApp(this@SplashActivity)
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    if (user !=null )
                        if (user != null) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        } else {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        }
                    finish();
                }
            }
        }

        handler.postDelayed(runnable, timeInterval.toLong())
    }
}