package com.example.WeatherCheck.london;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.WeatherCheck.Bangladesh.BangladeshSwitch;
import com.example.WeatherCheck.Bangladesh.MapActivityBangladesh;
import com.example.WeatherCheck.ModelForecast;
import com.example.WeatherCheck.R;
import com.example.WeatherCheck.WeatherForecast;
import com.example.WeatherCheck.WeatherForecastAdapter;
import com.example.WeatherCheck.WeatherXmlParser;
import com.example.WeatherCheck.WeatherXmlPullParser;
import com.example.WeatherCheck.glasgow.GlasgowSwitch;
import com.example.WeatherCheck.glasgow.MapActivityGlasgow;
import com.example.WeatherCheck.mauritus.MapActivity;
import com.example.WeatherCheck.mauritus.MauritiusSwitch;
import com.example.WeatherCheck.newyork.MapActivityNewYork;
import com.example.WeatherCheck.newyork.NewyorkSwitch;
import com.example.WeatherCheck.oman.MapActivityOman;
import com.example.WeatherCheck.oman.OmanSwitch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LondonFragment extends Fragment {


    private WeatherForecastAdapter adapter;
    private static final int PERMISSION_REQUEST_CODE = 100;

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
            rootView = inflater.inflate(R.layout.activity_london_land, container, false);
        } else {
            rootView = inflater.inflate(R.layout.activity_london, container, false);
        }

        // Initialize RecyclerView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch weather forecast data
        new LondonFragment.FetchWeatherForecastTask().execute();

        // Initialize bottom navigation view
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId(); // Get the ID of the selected item

                // Check the ID of the selected item
                if (id == R.id.previous) {
                    // Handle previous item click
                    Toast.makeText(requireContext(), "Previous selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.more) {
                    // Show options dialog
                    showWeatherOptionsDialog(R.array.options_array);
                    return true;
                } else if (id == R.id.next) {
                    // Handle next item click
                    showMapsOptionsDialog(R.array.options_array);
                    return true;
                }

                return false;
            }
        });

        return rootView;
    }

    private void showWeatherOptionsDialog(int arrayResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Weather For:");
        builder.setItems(arrayResourceId, (dialog, which) -> {
            // Handle the sub-options selection here
            switch (which) {
                case 0:
                    startActivity(new Intent(requireContext(), MauritiusSwitch.class));
                    break;
                case 1:
                    startActivity(new Intent(requireContext(), LondonSwitch.class));
                    break;
                case 2:
                    startActivity(new Intent(requireContext(), GlasgowSwitch.class));
                    break;
                case 3:
                    startActivity(new Intent(requireContext(), NewyorkSwitch.class));
                    break;
                case 4:
                    startActivity(new Intent(requireContext(), BangladeshSwitch.class));
                    break;
                case 5:
                    startActivity(new Intent(requireContext(), OmanSwitch.class));
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMapsOptionsDialog(int arrayResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Map For:");
        builder.setItems(arrayResourceId, (dialog, which) -> {
            // Handle the sub-options selection here
            switch (which) {
                case 0:
                    startActivity(new Intent(requireContext(), MapActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(requireContext(), MapActivityLondon.class));
                    break;
                case 2:
                    startActivity(new Intent(requireContext(), MapActivityGlasgow.class));
                    break;
                case 3:
                    startActivity(new Intent(requireContext(), MapActivityNewYork.class));
                    break;
                case 4:
                    startActivity(new Intent(requireContext(), MapActivityBangladesh.class));
                    break;
                case 5:
                    startActivity(new Intent(requireContext(), MapActivityOman.class));
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class FetchWeatherForecastTask extends AsyncTask<Void, Void, Pair<List<WeatherForecast>, List<ModelForecast>>> {
        @Override
        protected Pair<List<WeatherForecast>, List<ModelForecast>> doInBackground(Void... voids) {
            List<WeatherForecast> weatherForecastList = new ArrayList<>();
            List<ModelForecast> modelForecastList = new ArrayList<>();

            // Fetch data from WeatherXmlParser
            List<ModelForecast> parser1ForecastList = fetchFromParser1();
            if (parser1ForecastList != null) {
                modelForecastList.addAll(parser1ForecastList);
            }

            // Fetch data from WeatherXmlPullParser
            List<WeatherForecast> parser2ForecastList = fetchFromParser2();
            if (parser2ForecastList != null) {
                weatherForecastList.addAll(parser2ForecastList);
            }

            return new Pair<>(weatherForecastList, modelForecastList);
        }

        @Override
        protected void onPostExecute(Pair<List<WeatherForecast>, List<ModelForecast>> forecasts) {
            super.onPostExecute(forecasts);
            if (forecasts != null && forecasts.first != null && forecasts.second != null) {
                // Initialize and set adapter with combined forecast data
                adapter = new WeatherForecastAdapter(forecasts.first, forecasts.second);
                RecyclerView recyclerView = requireView().findViewById(R.id.recyclerview);
                recyclerView.setAdapter(adapter);
            } else {
                // Handle case where forecastList is null (e.g., network error)
                Toast.makeText(getContext(), "Error fetching forecast data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<ModelForecast> fetchFromParser1() {
        try {
            // Fetch data using WeatherXmlParser
            String xmlData = fetchDataFromUrl("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743");
            ModelForecast modelForecast = WeatherXmlParser.parseXml(xmlData);
            if (modelForecast != null) {
                List<ModelForecast> modelList = new ArrayList<>();
                modelList.add(modelForecast);
                return modelList;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error if fetching or parsing fails
        }
        return null;
    }

    private List<WeatherForecast> fetchFromParser2() {
        try {
            // Fetch data using WeatherXmlPullParser
            URL url = new URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return WeatherXmlPullParser.parse(inputStream);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            // Handle error if fetching or parsing fails
            return null;
        }
    }

    private String fetchDataFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        return convertStreamToString(inputStream);
    }

    private String convertStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, bytesRead));
        }
        return stringBuilder.toString();
    }
}
