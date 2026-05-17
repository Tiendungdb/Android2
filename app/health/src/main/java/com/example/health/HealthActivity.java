package com.example.health;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HealthActivity extends AppCompatActivity {

    private SensorManager sensorManager;

    private Sensor temperatureSensor, pressureSensor, humidSensor, proximitySensor,
            lightSensor, magneticSensor, accelerometerSensor;

    private TextView temperatureTextView, pressureTextView, humidTextView, proximityTextView,
            lightTextView, magneticTextView, stepTextView;

    private boolean isWalking = false;
    private int stepCount = 0;
    private static final float STEP_THRESHOLD = 10.0f;

    private Button resetButton;
    private ProgressBar progressBar;
    private static final float TARGET = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        createSensors();
        registerSensors();

        stepTextView = findViewById(R.id.stepTextView);
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setProgress((int) ((100 * stepCount) / TARGET));

        resetButton.setOnClickListener(v -> {
            stepCount = 0;
            stepTextView.setText("0");
            progressBar.setProgress(0);
        });


    }

    private void createSensors() {
        // Khởi tạo đối tượng SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        // Khởi tạo các đối tượng cảm biến
        // 1. Cảm biến nhiệt độ
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor != null) {
            temperatureTextView = findViewById(R.id.tempTextView);
            temperatureTextView.setVisibility(View.VISIBLE);
        }


        // 2. Cảm biến áp suất
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressureSensor != null) {
            pressureTextView = findViewById(R.id.pressureTextView);
            pressureTextView.setVisibility(View.VISIBLE);
        }


        // 3. Cảm biến độ ẩm
        humidSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (humidSensor != null) {
            humidTextView = findViewById(R.id.humidTextView);
            humidTextView.setVisibility(View.VISIBLE);
        }


        // 4. Cảm biến tiệm cận
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor != null) {
            proximityTextView = findViewById(R.id.proximityTextView);
            proximityTextView.setVisibility(View.VISIBLE);
        }


        // 5. Cảm biến ánh sáng
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            lightTextView = findViewById(R.id.lightTextView);
            lightTextView.setVisibility(View.VISIBLE);
        }


        // 6. Cảm biến từ trường
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticSensor != null) {
            magneticTextView = findViewById(R.id.magneticTextView);
            magneticTextView.setVisibility(View.VISIBLE);
        }

        // 8. Cảm biến gia tốc
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            stepTextView = findViewById(R.id.stepTextView);
            ((ViewGroup)stepTextView.getParent()).setVisibility(View.VISIBLE);
        }
    }

    private void registerSensors() {
        SensorEventListener sensorListenner = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }


            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    float value = event.values[0];
                    temperatureTextView.setText("🌡️ " + String.format("%.0f", value) + "°C");
                }


                if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                    float value = event.values[0];
                    pressureTextView.setText("🕛 " + String.format("%.0f", value) + "hPA");
                }


                if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    float value = event.values[0];
                    humidTextView.setText("💧 " + String.format("%.0f", value) + "%");
                }


                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float value = event.values[0];
                    proximityTextView.setText("📏 " + String.format("%.0f", value) + "cm");
                }


                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    float value = event.values[0];
                    lightTextView.setText("💡 " + String.format("%.0f", value) + "lux");
                }


                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    float[] values = event.values;
                    float azimuth = (float) Math.toDegrees(Math.atan2(values[0], values[1]));
                    if (azimuth < 0) azimuth += 360;
                    magneticTextView.setText("🧭" + String.format("%.0f", azimuth) + "°");
                }

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    // Tính toán gia tốc
                    float acceleration = (float) Math.sqrt(x * x + y * y + z * z);


                    // Kiểm tra nếu đang đi bộ và có sự thay đổi trong gia tốc độ dọc
                    if (isWalking && acceleration < STEP_THRESHOLD - 0.5f) {
                        isWalking = false;
                    } else if (!isWalking && acceleration > STEP_THRESHOLD) {
                        isWalking = true;
                        stepCount++;
                        updateStep();
                    }
                }
            }
        };


        // Đăng ký sự kiện cho từng loại cảm biến
        sensorManager.registerListener(sensorListenner, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, humidSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void updateStep() {
        stepTextView.setText(String.valueOf(stepCount));
        int progress = (int) ((100 * stepCount) / TARGET);
        progressBar.setProgress(Math.min(progress, 100));
    }

}
