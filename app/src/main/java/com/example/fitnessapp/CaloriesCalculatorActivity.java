package com.example.fitnessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CaloriesCalculatorActivity extends AppCompatActivity {

    private EditText weightInput, durationInput;
    private Button calculateButton;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_calculator);

        weightInput = findViewById(R.id.weightInput);
        durationInput = findViewById(R.id.durationInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    private void calculateCalories() {
        double weight = Double.parseDouble(weightInput.getText().toString());
        double duration = Double.parseDouble(durationInput.getText().toString());

        // Calories burned formula (approximation)
        double caloriesBurned = (weight * 0.035) + ((Math.pow(weight, 2) / 10000) * 0.029) * duration;
        resultText.setText("Calories Burned: " + caloriesBurned);
    }
}
