package com.example.fitnessapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var dateTimeTextView: TextView
    private lateinit var timerButton: Button
    private lateinit var stepCounterButton: Button
    private lateinit var healthTipsButton: Button
    private lateinit var caloriesCalculatorButton: Button
    private lateinit var bmiCalculatorButton: Button
    private lateinit var torchButton: Button
    private lateinit var stopwatchButton: Button
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var btnLogout: Button

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        dateTimeTextView = findViewById(R.id.dateTimeTextView)
        timerButton = findViewById(R.id.timerButton)
        stepCounterButton = findViewById(R.id.stepCounterButton)
        healthTipsButton = findViewById(R.id.healthTipsButton)
        caloriesCalculatorButton = findViewById(R.id.caloriesCalculatorButton)
        bmiCalculatorButton = findViewById(R.id.bmiCalculatorButton)
        torchButton = findViewById(R.id.torchButton)
        stopwatchButton = findViewById(R.id.stopwatchButton)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)
        btnLogout = findViewById(R.id.btnLogout)

        // Start updating date and time
        updateDateTime()
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateDateTime()
                handler.postDelayed(this, 1000)
            }
        }, 1000)

        // Set click listeners for each button
        timerButton.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
        }

        stepCounterButton.setOnClickListener {
            startActivity(Intent(this, StepCounterActivity::class.java))
        }

        healthTipsButton.setOnClickListener {
            startActivity(Intent(this, HealthTipsActivity::class.java))
        }

        caloriesCalculatorButton.setOnClickListener {
            startActivity(Intent(this, CaloriesCalculatorActivity::class.java))
        }

        bmiCalculatorButton.setOnClickListener {
            startActivity(Intent(this, BmiCalculatorActivity::class.java))
        }

        torchButton.setOnClickListener {
            startActivity(Intent(this, activity_torch::class.java))
        }

        stopwatchButton.setOnClickListener {
            startActivity(Intent(this, activity_stopwatch::class.java))
        }

        // Login Button Click Event
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Signup Button Click Event
        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Logout Button Click Event
        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // Close MainActivity after logout
        }

        // Check if user is logged in
        checkUserLogin()
    }

    private fun updateDateTime() {
        val currentDateTime = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        dateTimeTextView.text = currentDateTime
    }

    private fun checkUserLogin() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User is not logged in
            // Show Login and Signup buttons, hide Logout button
            btnLogin.visibility = View.VISIBLE
            btnSignup.visibility = View.VISIBLE
            btnLogout.visibility = View.GONE
        } else {
            // User is logged in
            // Hide Login and Signup buttons, show Logout button
            btnLogin.visibility = View.GONE
            btnSignup.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE
        }
    }
}
