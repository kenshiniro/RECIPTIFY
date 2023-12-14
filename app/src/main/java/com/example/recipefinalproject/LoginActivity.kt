package com.example.recipefinalproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipefinalproject.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.Objects

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        binding!!.btnLogin.setOnClickListener { view: View? -> login() }
        binding!!.tvGuest.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }
        binding!!.tvSignup.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )
        }
    }

    private fun login() {
        val email = Objects.requireNonNull(binding!!.etEmail.text).toString().trim { it <= ' ' }
        val password =
            Objects.requireNonNull(binding!!.etPassword.text).toString().trim { it <= ' ' }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                .show()
        } else {
            FirebaseApp.initializeApp(this)
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Login Failed: " + Objects.requireNonNull(task.exception)!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}