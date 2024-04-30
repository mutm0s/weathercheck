package com.example.WeatherCheck.mauritus;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.WeatherCheck.R;
import com.example.WeatherCheck.WeatherObservation;
import com.example.WeatherCheck.WeatherObservationAdapter;
import com.example.WeatherCheck.WeatherObservationPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

//public class SecFragmentForMauritius extends Fragment {

//    private ArrayList<WeatherObservation> weatherData;
//    private WeatherObservationAdapter adapter;
//    private RecyclerView recyclerView;
//    private RecyclerView mapRecyclerView; // Second RecyclerView for map
//
//    private Context context;
//    MapAdapter mapAdapter;
//    private static final String MAURITIUS_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123";
//
//    @SuppressLint("MissingInflatedId")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.secfragmentformauritius, container, false);
//        context = getContext();
//
//        // Initialize the first RecyclerView with WeatherObservationAdapter
//        recyclerView = view.findViewById(R.id.secmauritiusrecyclerview);
//        weatherData = new ArrayList<>();
//        adapter = new WeatherObservationAdapter(context, weatherData);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(adapter);
//
//        // Initialize the second RecyclerView with MapAdapter
//        mapRecyclerView = view.findViewById(R.id.secondRecyclerView);
//        mapAdapter = new MapAdapter(context);
//        mapRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mapRecyclerView.setAdapter(mapAdapter);
//
//        fetchWeatherData();
//
//        return view;
//    }
//
//    private void fetchWeatherData() {
//        new AsyncTask<Void, Void, WeatherObservation>() {
//            @Override
//            protected WeatherObservation doInBackground(Void... voids) {
//                try {
//                    // Fetch data using WeatherXmlPullParser
//                    InputStream inputStream = new URL(MAURITIUS_URL).openStream();
//                    return WeatherObservationPullParser.parseXml(inputStream);
//                } catch (IOException | XmlPullParserException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(WeatherObservation observation) {
//                Log.d("SecFragmentForMauritius", "Observation received: " + observation);
//
//                if (observation != null) {
//                    weatherData.clear();
//                    weatherData.add(observation);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Log.e("SecFragmentForMauritius", "Observation is null");
//                }
//            }
//        }.execute();
//    }
//}


public class SecFragmentForMauritius extends Fragment {
    private WeatherSensor weatherSensor;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ArrayList<WeatherObservation> weatherData;
    private WeatherObservationAdapter adapter;
    private RecyclerView recyclerView;

    private Context context;

    private static final String MAURITIUS_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get the current device configuration
        int orientation = getResources().getConfiguration().orientation;

        View view;

        // Load the appropriate layout based on the orientation
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            view = inflater.inflate(R.layout.secfragmentformauritius_land, container, false);
        } else {
            view = inflater.inflate(R.layout.secfragmentformauritius, container, false);
        }


    context = getContext();



        recyclerView = view.findViewById(R.id.secmauritiusrecyclerview);
        weatherData = new ArrayList<>();
        adapter = new WeatherObservationAdapter(context, weatherData);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);



        fetchWeatherData();

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

        return view;

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
        new AsyncTask<Void, Void, WeatherObservation>() {
            @Override
            protected WeatherObservation doInBackground(Void... voids) {
                try {
                    // Fetch data using WeatherXmlPullParser
                    InputStream inputStream = new URL(MAURITIUS_URL).openStream();
                    return WeatherObservationPullParser.parseXml(inputStream);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(WeatherObservation observation) {
                Log.d("SecFragmentForMauritius", "Observation received: " + observation);

                if (observation != null) {
                    weatherData.clear();
                    weatherData.add(observation);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("SecFragmentForMauritius", "Observation is null");
                }
            }
        }.execute();
    }


}



