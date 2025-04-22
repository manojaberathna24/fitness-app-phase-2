package com.example.fitnessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BmiCalculatorActivity extends AppCompatActivity {

    private EditText heightInput, weightInput;
    private Button calculateButton;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        double height = Double.parseDouble(heightInput.getText().toString()) / 100; // Convert cm to meters
        double weight = Double.parseDouble(weightInput.getText().toString());

        // BMI formula
        double bmi = weight / (height * height);

        String result;
        if (bmi < 18.5) {
            result = "Underweight";
        } else if (bmi < 24.9) {
            result = "Normal weight";
        } else if (bmi < 29.9) {
            result = "Overweight";
        } else {
            result = "Obese";
        }

        resultText.setText("BMI: " + bmi + "\nCategory: " + result);
    }
}
