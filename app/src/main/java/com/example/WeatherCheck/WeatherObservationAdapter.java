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

public class WeatherObservationAdapter extends RecyclerView.Adapter<WeatherObservationAdapter.ViewHolder> {

    private Context context;
    private List<WeatherObservation> weatherObservationList;


    public WeatherObservationAdapter(Context context, ArrayList<WeatherObservation> weatherObservationData) {
        this.context = context;
        this.weatherObservationList = weatherObservationData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_observation_data, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherObservation weatherObservation = weatherObservationList.get(position);
        holder.bind(weatherObservation);
    }

    @Override
    public int getItemCount() {
        return weatherObservationList != null ? weatherObservationList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView temperature, windDirection, windSpeed, pressure, humidity;

        ImageView icon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperature = itemView.findViewById(R.id.temperatureView);
            windDirection = itemView.findViewById(R.id.windDirectionView);
            windSpeed = itemView.findViewById(R.id.windSpeedView);
            pressure = itemView.findViewById(R.id.pressureView);
            humidity = itemView.findViewById(R.id.humidityView);
            icon = itemView.findViewById(R.id.mainIcon);
        }

        public void bind(WeatherObservation weatherObservation) {
            temperature.setText(weatherObservation.getTemperature());
            windDirection.setText(weatherObservation.getWindDirection());
            windSpeed.setText(weatherObservation.getWindSpeed());
            pressure.setText(weatherObservation.getPressure());
            humidity.setText(weatherObservation.getHumidity());

            if (weatherObservation.getTemperature() != null) {
                // Remove non-numeric characters from the temperature string
                String temperatureString = weatherObservation.getTemperature().replaceAll("[^\\d.]", "");

                // Parse the modified string to a float
                float maxTemp = Float.parseFloat(temperatureString);

                if (maxTemp > 30.0f) {
                    icon.setImageResource(R.drawable.sun);
                } else if (maxTemp > 20.0f) {
                    icon.setImageResource(R.drawable.day_clear);
                } else if (maxTemp > 10.0f) {
                    icon.setImageResource(R.drawable.day_partial_cloud); // Set warm weather icon
                } else if (maxTemp > 05.0f) {
                    icon.setImageResource(R.drawable.day_sleet);
                } else if (maxTemp > 0.0f) {
                    icon.setImageResource(R.drawable.day_rain);
                } else if (maxTemp > -10.0f) {
                    icon.setImageResource(R.drawable.day_snow);
                } else {
                    icon.setImageResource(R.drawable.day_rain_thunder); // Set cold weather icon
                }
            }

        }
    }
}