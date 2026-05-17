package com.example.weather;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private TextView textView;
    private TextView locationTextView;
    private TextView tempTextView;
    private TextView humidTextView;
    private TextView cloudTextView;
    private TextView rainTextView;
    private TextView windTextView;
    private ProgressBar progressBar;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        textView = findViewById(R.id.textView);
        locationTextView = findViewById(R.id.locationTextView);
        tempTextView = findViewById(R.id.tempTextView);
        humidTextView = findViewById(R.id.humidTextView);
        cloudTextView = findViewById(R.id.cloudTextView);
        rainTextView = findViewById(R.id.rainTextView);
        windTextView = findViewById(R.id.windTextView);
        progressBar = findViewById(R.id.progressBar);

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initLocation();
        }

        googleMap.setOnCameraIdleListener(() -> {
            LatLng center = googleMap.getCameraPosition().target;
            showInfo(center);
        });
    }

    private void initLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));
                    googleMap.addMarker(new MarkerOptions().position(myLocation).title("You are here"));
                    showInfo(myLocation);
                } else {
                    showInfo(googleMap.getCameraPosition().target);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocation();
        }
    }

    public static String convertToDegreeMinutesSeconds(double coordinate) {
        int degree = (int) coordinate;
        coordinate = Math.abs((coordinate - degree) * 60);
        int minute = (int) coordinate;
        double second = (coordinate - minute) * 60;
        return degree + "° " + minute + "' " + (int) second + "\"";
    }

    private String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                if (cityName == null) cityName = addresses.get(0).getAdminArea();
                return cityName != null ? cityName : "-";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "-";
    }

    private void showInfo(LatLng location) {
        textView.setText(convertToDegreeMinutesSeconds(location.latitude) + ", " + convertToDegreeMinutesSeconds(location.longitude));
        locationTextView.setText(getCityName(location.latitude, location.longitude));

        progressBar.setVisibility(View.VISIBLE);
        tempTextView.setVisibility(View.INVISIBLE);
        humidTextView.setVisibility(View.INVISIBLE);
        cloudTextView.setVisibility(View.INVISIBLE);
        rainTextView.setVisibility(View.INVISIBLE);
        windTextView.setVisibility(View.INVISIBLE);

        new Thread(() -> {
            try {
                String API_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + location.latitude + "&longitude=" + location.longitude + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,showers,snowfall,is_day,cloud_cover,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m";
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject baseJsonResponse = new JSONObject(response.toString());
                JSONObject currentJsonObject = baseJsonResponse.getJSONObject("current");
                double temperature_2m = currentJsonObject.getDouble("temperature_2m");
                double relative_humidity_2m = currentJsonObject.getDouble("relative_humidity_2m");
                double rain = currentJsonObject.getDouble("rain");
                double cloud_cover = currentJsonObject.getDouble("cloud_cover");
                double wind_speed_10m = currentJsonObject.getDouble("wind_speed_10m");

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tempTextView.setVisibility(View.VISIBLE);
                    humidTextView.setVisibility(View.VISIBLE);
                    cloudTextView.setVisibility(View.VISIBLE);
                    rainTextView.setVisibility(View.VISIBLE);
                    windTextView.setVisibility(View.VISIBLE);
                    tempTextView.setText("🌡️" + (int) temperature_2m + "°C");
                    humidTextView.setText("💧" + (int) relative_humidity_2m + "%");
                    cloudTextView.setText("☁\n" + (int) cloud_cover + "%");
                    rainTextView.setText("🌧\n" + (int) rain + "mm");
                    windTextView.setText("🌪\n" + (int) wind_speed_10m + "km/h");
                });
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
