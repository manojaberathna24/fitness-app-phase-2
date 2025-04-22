package com.example.fitnessapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private EditText timeInput;
    private TextView timerText;
    private Button startButton, stopButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 0; // Dynamic start time in milliseconds
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timeInput = findViewById(R.id.timeInput);
        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    Toast.makeText(TimerActivity.this, "Timer is already running!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });
    }

    private void startTimer() {
        String input = timeInput.getText().toString();
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a time in minutes", Toast.LENGTH_SHORT).show();
            return;
        }

        int minutes = Integer.parseInt(input);
        if (minutes <= 0) {
            Toast.makeText(this, "Please enter a valid time greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        timeLeftInMillis = minutes * 60 * 1000; // Convert minutes to milliseconds
        updateTimer();

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerText.setText("Time's up!");
                isTimerRunning = false;
            }
        }.start();

        isTimerRunning = true;
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
            timerText.setText("00:00");
        }
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeft = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeLeft);
    }
}
