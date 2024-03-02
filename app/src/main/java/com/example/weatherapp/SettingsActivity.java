//SettingsActivity
package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup unitRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        unitRadioGroup = findViewById(R.id.unitRadioGroup);

        // Load the saved unit preference and set the checked radio button
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String unitPreference = preferences.getString("unit_preference", "metric");

        if (unitPreference.equals("metric")) {
            unitRadioGroup.check(R.id.metricRadioButton);
        } else {
            unitRadioGroup.check(R.id.imperialRadioButton);
        }

        // Add a listener to save the selected unit preference when changed
        unitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selectedUnit = (checkedId == R.id.metricRadioButton) ? "metric" : "imperial";
                saveUnitPreference(selectedUnit);
            }
        });
    }

    private void saveUnitPreference(String unit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("unit_preference", unit);
        editor.apply();
    }
}
