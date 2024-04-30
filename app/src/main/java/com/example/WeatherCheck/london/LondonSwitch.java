package com.example.WeatherCheck.london;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.WeatherCheck.MainActivity;
import com.example.WeatherCheck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LondonSwitch extends AppCompatActivity {

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
        observerFragment = new SecFragmentForLondon();
        forecastFragment = new FragmentForLondon();

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
                                                                             startActivity(new Intent(LondonSwitch.this, MainActivity.class));
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