package com.example.WeatherCheck.mauritus;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.WeatherCheck.MainActivity;
import com.example.WeatherCheck.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MauritiusSwitch extends AppCompatActivity {

    private Fragment observerFragment;
    private Fragment forecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the current device configuration
        Configuration config = getResources().getConfiguration();

        // Check the current orientation
        int orientation = config.orientation;

        // Load the appropriate layout based on the orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.mauritius_switch_layout_land);
        } else {
            setContentView(R.layout.mauritius_switch_layout);
        }

        // Initialize fragments
        observerFragment = new SecFragmentForMauritius();
        forecastFragment = new FragmentForMauritius();

        // Set default fragment
        setFragment(observerFragment);

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_observer) {
                    setFragment(observerFragment);
                    return true;
                } else if (id == R.id.menu_forecast) {
                    setFragment(forecastFragment);
                    return true;
                }  else if (id == R.id.home) {
                    startActivity(new Intent(MauritiusSwitch.this, MainActivity.class));
                    return true;
                }
                else {
                    return false;
                }
            }

        }
        );
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}


/*

public class MauritiusSwitch extends AppCompatActivity {

    private Fragment forecast;
    private Fragment observer;
    private boolean isForecastVisible = false; // Initial state: Observer fragment is visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mauritius_switch_layout);

        // Initialize fragments
        forecast = new FragmentForMauritius();
        observer = new SecFragmentForMauritius();

        // Set the initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, observer)
                .commit();

        // Find custom view elements
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnObserver = findViewById(R.id.btnObserver);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnForecast = findViewById(R.id.btnForecast);

        // Set click listeners for custom view elements
        btnObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showObserverFragment();
            }
        });

        btnForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForecastFragment();
            }
        });
    }

    private void showObserverFragment() {
        if (!isForecastVisible) return; // Already showing Observer fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, observer)
                .commit();
        isForecastVisible = false;
    }

    private void showForecastFragment() {
        if (isForecastVisible) return; // Already showing Forecast fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, forecast)
                .commit();
        isForecastVisible = true;
    }
}

*/
