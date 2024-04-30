//Amos Kasumba s2110980
package com.example.WeatherCheck.mauritus;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.WeatherCheck.Bangladesh.BangladeshSwitch;
import com.example.WeatherCheck.Bangladesh.MapActivityBangladesh;
import com.example.WeatherCheck.MainActivity;
import com.example.WeatherCheck.R;
import com.example.WeatherCheck.glasgow.GlasgowSwitch;
import com.example.WeatherCheck.glasgow.MapActivityGlasgow;
import com.example.WeatherCheck.london.LondonSwitch;
import com.example.WeatherCheck.london.MapActivityLondon;
import com.example.WeatherCheck.mauritus.MapActivity;
import com.example.WeatherCheck.mauritus.MauritiusSwitch;
import com.example.WeatherCheck.newyork.MapActivityNewYork;
import com.example.WeatherCheck.newyork.NewyorkSwitch;
import com.example.WeatherCheck.oman.MapActivityOman;
import com.example.WeatherCheck.oman.OmanSwitch;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {

    private RecyclerView mapRecyclerView;
    private MapAdapter mapAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the current device configuration
        Configuration config = getResources().getConfiguration();

        // Check the current orientation
        int orientation = config.orientation;

        // Load the appropriate layout based on the orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.map_land);
        } else {
            setContentView(R.layout.map);
        }

        mapRecyclerView = findViewById(R.id.secondRecyclerView);
        mapAdapter = new MapAdapter(this);
        mapRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mapRecyclerView.setAdapter(mapAdapter);

        // Initialize bottom navigation view

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navigation);
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId(); // Get the ID of the selected item

                // Check the ID of the selected item
                if (id == R.id.previous) {
                    startActivity(new Intent( MapActivity.this, MainActivity.class));
                    return true;
                } else if (id == R.id.more) {
                    showWeatherOptionsDialog(R.array.options_array);

                    return true;
                } else if (id == R.id.next) {
                    showMapsOptionsDialog(R.array.options_array);

                    return true;
                }

                return false;
            }

        });
        loadMapFragment();
    }


    private void loadMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.map, mapFragment);
            fragmentTransaction.commit();
        }

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // Customize your map here if needed
        });
    }
    private void showWeatherOptionsDialog(int arrayResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Weather For:");
        builder.setItems(arrayResourceId, (dialog, which) -> {
            // Handle the sub-options selection here
            switch (which) {
                case 0:
                    startActivity(new Intent(MapActivity.this, MauritiusSwitch.class));
                    break;
                case 1:
                    startActivity(new Intent(MapActivity.this, LondonSwitch.class));

                    break;
                case 2:
                    startActivity(new Intent(MapActivity.this, GlasgowSwitch.class));
                    break;
                case 3:
                    startActivity(new Intent(MapActivity.this, NewyorkSwitch.class));

                    break;
                case 4:
                    startActivity(new Intent(MapActivity.this, BangladeshSwitch.class));

                    break;

                case 5:
                    startActivity(new Intent(MapActivity.this, OmanSwitch.class));
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMapsOptionsDialog(int arrayResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Map For:");
        builder.setItems(arrayResourceId, (dialog, which) -> {
            // Handle the sub-options selection here
            switch (which) {
                case 0:
                    startActivity(new Intent(MapActivity.this, MapActivity.class));
                    break;
                case 1:

                    startActivity(new Intent(MapActivity.this, MapActivityLondon.class));
                    break;
                case 2:
                    startActivity(new Intent(MapActivity.this, MapActivityGlasgow.class));;
                    break;
                case 3:
                    startActivity(new Intent(MapActivity.this, MapActivityNewYork.class));
                    break;
                case 4:
                    startActivity(new Intent(MapActivity.this, MapActivityBangladesh.class));
                    break;

                case 5:
                    startActivity(new Intent(MapActivity.this, MapActivityOman.class));
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
