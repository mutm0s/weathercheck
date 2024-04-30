package com.example.WeatherCheck.oman;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.WeatherCheck.ForecastAdapter;
import com.example.WeatherCheck.R;
import com.example.WeatherCheck.WeatherForecast;
import com.example.WeatherCheck.WeatherXmlPullParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentForOman extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private ArrayList<WeatherForecast> weatherData;
    private ForecastAdapter adapter;
    private RecyclerView recyclerViewofmauritius;


    private static final String omanUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286";

    // WeatherSensor instance
    private WeatherSensor weatherSensor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get the current device configuration
        Configuration config = getResources().getConfiguration();

        // Check the current orientation
        int orientation = config.orientation;

        // Inflate the layout based on the orientation
        View rootView;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView = inflater.inflate(R.layout.fragmentformauritius_land, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragmentformauritius, container, false);
        }

        recyclerViewofmauritius = rootView.findViewById(R.id.mauritiusrecyclerview);
        weatherData = new ArrayList<>();
        adapter = new ForecastAdapter(getContext(), weatherData);
        recyclerViewofmauritius.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewofmauritius.setAdapter(adapter);

        fetchWeatherData();

        // Check if the temperature and humidity sensors are available

        // Initialize WeatherSensor
        weatherSensor = new WeatherSensor(getContext());


        // Start listening for weather changes
        weatherSensor.startListening();

        // Request permission for POST_NOTIFICATIONS if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, start listening for weather changes
            weatherSensor.startListening();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start listening for weather changes when the Fragment is resumed
        if (weatherSensor != null) {
            weatherSensor.startListening();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop listening for weather changes when the Fragment is paused
        if (weatherSensor != null) {
            weatherSensor.stopListening();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop listening for weather changes to avoid memory leaks
        weatherSensor.stopListening();
    }

    private void fetchWeatherData() {
        new AsyncTask<Void, Void, List<WeatherForecast>>() {
            @Override
            protected List<WeatherForecast> doInBackground(Void... voids) {
                try {
                    // Fetch data using WeatherXmlPullParser
                    InputStream inputStream = new URL(omanUrl).openStream();
                    return WeatherXmlPullParser.parse(inputStream);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<WeatherForecast> forecasts) {
                if (forecasts != null) {
                    weatherData.clear(); // Clear existing data
                    weatherData.addAll(forecasts); // Add new data
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle case where forecasts are null
                    Toast.makeText(getContext(), "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, start listening for weather changes
            weatherSensor.startListening();
        } else {
            // Permission denied, handle accordingly (e.g., show a message or disable functionality)
            Toast.makeText(getContext(), "Permission denied for POST_NOTIFICATIONS", Toast.LENGTH_SHORT).show();
        }
    }
}