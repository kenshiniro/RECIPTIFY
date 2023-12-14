package com.example.recipefinalproject

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipefinalproject.databinding.ActivitySignUpBinding
import com.example.recipefinalproject.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects

class SignUpActivity() : AppCompatActivity() {
    var binding: ActivitySignUpBinding? = null
    var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        binding!!.btnSignup.setOnClickListener({ view: View? -> signup() })
        binding!!.tvLogin.setOnClickListener({ view: View? -> finish() })
    }

    private fun signup() {
        val name = Objects.requireNonNull(binding!!.etName.text).toString().trim { it <= ' ' }
        val email = Objects.requireNonNull(binding!!.etEmail.text).toString().trim { it <= ' ' }
        val password =
            Objects.requireNonNull(binding!!.etPassword.text).toString().trim { it <= ' ' }
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your name, email and password", Toast.LENGTH_SHORT)
                .show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                .show()
        } else {
            createNewUser(name, email, password)
        }
    }

    private fun createNewUser(name: String, email: String, password: String) {
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Creating user...")
        dialog!!.setCancelable(false)
        dialog!!.show()
        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener({ task: Task<AuthResult?> ->
                if (task.isSuccessful()) {
                    saveName(name, email)
                } else {
                    dialog!!.dismiss()
                    Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveName(name: String, email: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val user = User(FirebaseAuth.getInstance().uid, name, email, "", "")
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().uid).toString()).setValue(user)
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isComplete) {
                        dialog!!.dismiss()
                        Toast.makeText(
                            this@SignUpActivity,
                            "User created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finishAffinity()
                    } else {
                        dialog!!.dismiss()
                        Toast.makeText(
                            this@SignUpActivity,
                            "Failed to create user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
}