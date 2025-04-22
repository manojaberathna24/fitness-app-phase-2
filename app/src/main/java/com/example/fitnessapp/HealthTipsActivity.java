package com.example.fitnessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HealthTipsActivity extends AppCompatActivity {

    private TextView healthTipsText;
    private Spinner tipsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        healthTipsText = findViewById(R.id.healthTipsText);
        tipsSpinner = findViewById(R.id.tipsSpinner);

        // Spinner values
        String[] tipCategories = {"Select a category", "Fitness Tips", "Diet Tips", "Sleep Tips"};

        // Set up Spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipsSpinner.setAdapter(adapter);

        // Handle selection
        tipsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        healthTipsText.setText("1. Exercise daily for at least 30 minutes.\n2. Stretch before workouts.\n3. Maintain proper posture.");
                        break;
                    case 2:
                        healthTipsText.setText("1. Eat more fruits and vegetables.\n2. Reduce sugar intake.\n3. Stay hydrated.");
                        break;
                    case 3:
                        healthTipsText.setText("1. Sleep at least 7-9 hours.\n2. Avoid screens before bed.\n3. Maintain a consistent sleep schedule.");
                        break;
                    default:
                        healthTipsText.setText("Select a category to view tips.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
