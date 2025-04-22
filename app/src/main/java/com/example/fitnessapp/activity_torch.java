package com.example.fitnessapp;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity_torch extends AppCompatActivity {

    private TextView torchStatus;
    private Button emergencyButton;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isEmergencyMode = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);

        torchStatus = findViewById(R.id.torchStatus);
        emergencyButton = findViewById(R.id.emergencyButton);

        // Check if the device has a flashlight
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            torchStatus.setText("No Flashlight Available");
            return;
        }

        // Get CameraManager and Camera ID
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Emergency Mode Button
        emergencyButton.setOnClickListener(v -> {
            isEmergencyMode = !isEmergencyMode;
            if (isEmergencyMode) {
                startEmergencyLight();
                emergencyButton.setText("Stop Emergency");
            } else {
                stopEmergencyLight();
                emergencyButton.setText("Emergency Light");
            }
        });
    }

    private void startEmergencyLight() {
        handler.postDelayed(new Runnable() {
            private boolean isLightOn = false;

            @Override
            public void run() {
                if (!isEmergencyMode) return;

                try {
                    cameraManager.setTorchMode(cameraId, isLightOn);
                    torchStatus.setText(isLightOn ? "Emergency Light ON" : "Emergency Light OFF");
                    isLightOn = !isLightOn;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                handler.postDelayed(this, 500); // Adjust blink speed
            }
        }, 0);
    }

    private void stopEmergencyLight() {
        handler.removeCallbacksAndMessages(null);
        try {
            cameraManager.setTorchMode(cameraId, false);
            torchStatus.setText("Torch is OFF");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
