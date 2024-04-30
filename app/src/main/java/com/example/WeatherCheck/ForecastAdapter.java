package com.example.WeatherCheck;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private Context context;
    private List<WeatherForecast> forecasts;


    public ForecastAdapter(Context context, ArrayList<WeatherForecast> weatherData) {
        this.context = context;
        this.forecasts = weatherData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_data, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherForecast weatherForecast = forecasts.get(position);
        holder.bind(weatherForecast);
    }

    @Override
    public int getItemCount() {
        return forecasts != null ? forecasts.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView maxtemperature, mintemperature, windDirection, windSpeed, pressure, humidity, sunrise, sunset;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maxtemperature = itemView.findViewById(R.id.maxtemperatureView);
            mintemperature = itemView.findViewById(R.id.mintemperatureView);
            windDirection = itemView.findViewById(R.id.windDirectionView);
            windSpeed = itemView.findViewById(R.id.windSpeedView);
            pressure = itemView.findViewById(R.id.pressureView);
            humidity = itemView.findViewById(R.id.humidityView);
            sunrise = itemView.findViewById(R.id.sunriseView);
            sunset = itemView.findViewById(R.id.sunsetView);
            icon = itemView.findViewById(R.id.forecastIcon);
        }

        public void bind(WeatherForecast weatherForecast) {
            maxtemperature.setText(weatherForecast.getMaxTemperature());
            mintemperature.setText(weatherForecast.getMinTemperature());
            windDirection.setText(weatherForecast.getWindDirection());
            windSpeed.setText(weatherForecast.getWindSpeed());
            pressure.setText(weatherForecast.getPressure());
            humidity.setText(weatherForecast.getHumidity());
            sunrise.setText(weatherForecast.getSunrise());
            sunset.setText(weatherForecast.getSunset());

            if (weatherForecast.getMaxTemperature() != null && !weatherForecast.getMaxTemperature().isEmpty()) {
                // Extract only the numeric part of the temperature string
                String temperatureString = weatherForecast.getMaxTemperature().replaceAll("[^\\d.]", "");

                // Check if temperatureString is not empty after removing non-numeric characters
                if (!temperatureString.isEmpty()) {
                    // Parse the modified string to a float
                    float maxTemp = Float.parseFloat(temperatureString);

                    if (maxTemp > 30.0f) {
                        icon.setImageResource(R.drawable.sun);
                    } else if (maxTemp > 20.0f) {
                        icon.setImageResource(R.drawable.day_clear);
                    } else if (maxTemp > 10.0f) {
                        icon.setImageResource(R.drawable.day_partial_cloud);
                    } else if (maxTemp > 05.0f) {
                        icon.setImageResource(R.drawable.day_sleet);
                    } else if (maxTemp > 0.0f) {
                        icon.setImageResource(R.drawable.day_rain);
                    } else if (maxTemp > -10.0f) {
                        icon.setImageResource(R.drawable.day_snow);
                    } else {
                        icon.setImageResource(R.drawable.day_rain_thunder);
                    }
                }


            }
        }
    }
}
