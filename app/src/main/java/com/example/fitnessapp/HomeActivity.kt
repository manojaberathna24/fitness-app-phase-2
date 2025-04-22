package com.example.fitnessapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Buttons
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val btnAdminLogin = findViewById<Button>(R.id.btnAdminLogin)

        // Navigate to Login
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Signup
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Admin Login
        btnAdminLogin.setOnClickListener {
            val intent = Intent(this, owner_activity::class.java)
            startActivity(intent)
        }
    }
}
