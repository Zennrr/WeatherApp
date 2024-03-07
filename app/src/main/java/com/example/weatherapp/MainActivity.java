//MainActivity
package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView weatherTextView;
    private Button fetchWeatherButton;
    private Button openSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weatherTextView);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        openSettingsButton = findViewById(R.id.openSettingsButton);

        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData();
            }
        });

        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void fetchWeatherData() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=[API_KEY_HERE]";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject mainObject = response.getJSONObject("main");
                            double temperatureKelvin = mainObject.getDouble("temp");
                            double temperature = convertTemperature(temperatureKelvin);
                            String unitSymbol = getTemperatureUnitSymbol();

                            weatherTextView.setText("Temperature: " + temperature + " " + unitSymbol);
                        } catch (Exception e) {
                            Log.e("VolleyError", "Error fetching weather data: " + e.getMessage());
                            weatherTextView.setText("Error fetching weather data.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error fetching weather data: " + error.getMessage());
                        weatherTextView.setText("Error fetching weather data.");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private double convertTemperature(double temperatureKelvin) {
        String unitPreference = getTemperatureUnit();
        if (unitPreference.equals("metric")) {
            // Convert to Celsius
            return temperatureKelvin - 273.15;
        } else {
            // Convert to Fahrenheit
            return (temperatureKelvin - 273.15) * 9 / 5 + 32;
        }
    }

    private String getTemperatureUnit() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("unit_preference", "metric");
    }

    private String getTemperatureUnitSymbol() {
        String unitPreference = getTemperatureUnit();
        return (unitPreference.equals("metric")) ? "°C" : "°F";
    }
}
