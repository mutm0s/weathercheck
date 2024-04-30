package com.example.WeatherCheck;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private static final int VIEW_TYPE_MODEL_FORECAST = 0;
    private static final int VIEW_TYPE_WEATHER_FORECAST = 1;

    private Context context;
    private List<WeatherForecast> weatherForecastList;
    private List<ModelForecast> modelForecastList;



    public WeatherForecastAdapter() {
    }
    public WeatherForecastAdapter(List<WeatherForecast> weatherForecastList) {
        this.weatherForecastList = weatherForecastList;
    }

    public WeatherForecastAdapter(List<WeatherForecast> weatherForecastList, List<ModelForecast> modelForecastList) {
        this.weatherForecastList = weatherForecastList;
        this.modelForecastList = modelForecastList;
    }

    public WeatherForecastAdapter(Context context, ArrayList<WeatherForecast> weatherData) {
        this.context = context;
        this.weatherForecastList = weatherData;
        this.modelForecastList = new ArrayList<>();
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case VIEW_TYPE_MODEL_FORECAST:
                view = inflater.inflate(R.layout.model_forecast_data, parent, false);
                break;
            case VIEW_TYPE_WEATHER_FORECAST:
                view = inflater.inflate(R.layout.weather_forecast_data, parent, false);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_MODEL_FORECAST:
                holder.bindModelForecast(modelForecastList.get(position));
                break;
            case VIEW_TYPE_WEATHER_FORECAST:
                holder.bindWeatherForecast(weatherForecastList.get(position - modelForecastList.size()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        int weatherItemCount = weatherForecastList != null ? weatherForecastList.size() : 0;
        int modelItemCount = modelForecastList != null ? modelForecastList.size() : 0;
        return weatherItemCount + modelItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position < modelForecastList.size() ? VIEW_TYPE_MODEL_FORECAST : VIEW_TYPE_WEATHER_FORECAST;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeekTextView, weatherConditionTextView, minTemperatureTextView, maxTemperatureTextView,cityname;
        TextView dayOfWeekTextViewFirst, weatherConditionTextViewFirst, maxTemperatureTextViewFirst, windSpeedTextViewFirst, windDirectionTextViewFirst, pressureTextViewFirst, humidityTextViewFirst, uvRiskTextViewFirst, pollutionTextViewFirst, sunRiseTextViewFirst, sunSetTextViewFirst;

        public ViewHolder(View itemView) {
            super(itemView);
            // For the first set of TextViews
            weatherConditionTextViewFirst = itemView.findViewById(R.id.weatherConditionTextViewFirstOne);
            maxTemperatureTextViewFirst = itemView.findViewById(R.id.maxTemperatureTextViewFirstOne);
            pressureTextViewFirst = itemView.findViewById(R.id.pressureTextViewFirstOne);
            humidityTextViewFirst = itemView.findViewById(R.id.humidityTextViewFirstOne);
            sunRiseTextViewFirst = itemView.findViewById(R.id.sunRiseTextViewFirstOne);
            sunSetTextViewFirst = itemView.findViewById(R.id.sunSetTextViewFirstOne);

            // For the second set of TextViews
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            minTemperatureTextView = itemView.findViewById(R.id.minTemperatureTextView);
            maxTemperatureTextView = itemView.findViewById(R.id.maxTemperatureTextView);
        }

        public void bindWeatherForecast(WeatherForecast weatherForecast) {
            dayOfWeekTextView.setText(weatherForecast.getDayOfWeek());
            minTemperatureTextView.setText(weatherForecast.getMinTemperature());
            maxTemperatureTextView.setText(weatherForecast.getMaxTemperature());

        }

        public void bindModelForecast(ModelForecast modelForecast) {
            if (modelForecast != null) {
                // Bind ModelForecast data to ViewHolder views
                weatherConditionTextViewFirst.setText(modelForecast.getWeatherCondition());

                // Check and set max temperature
                if (modelForecast.getMaxTemperature() != null && !modelForecast.getMaxTemperature().isEmpty()) {
                    maxTemperatureTextViewFirst.setText(modelForecast.getMaxTemperature());
                } else {
                    maxTemperatureTextViewFirst.setText("N/A");
                }

                // Check and set max temperature
                if (modelForecast.getMaxTemperature() != null && !modelForecast.getMaxTemperature().isEmpty()) {
                    maxTemperatureTextView.setText(modelForecast.getMaxTemperature());
                } else {
                    maxTemperatureTextView.setText("N/A");
                }
                // Check and set max temperature
                if (modelForecast.getMinTemperature() != null && !modelForecast.getMinTemperature().isEmpty()) {
                    minTemperatureTextView.setText(modelForecast.getMinTemperature());
                } else {
                    minTemperatureTextView.setText("N/A");
                }

                if (modelForecast.getPressure() != null && !modelForecast.getPressure().isEmpty()) {
                    pressureTextViewFirst.setText(modelForecast.getPressure());
                } else {
                    pressureTextViewFirst.setText("N/A");
                }

                // Check and set humidity
                if (modelForecast.getHumidity() != null && !modelForecast.getHumidity().isEmpty()) {
                    humidityTextViewFirst.setText(modelForecast.getHumidity());
                } else {
                    humidityTextViewFirst.setText("N/A");
                }


                if (modelForecast.getSunrise() != null && !modelForecast.getSunrise().isEmpty()) {
                    sunRiseTextViewFirst.setText(modelForecast.getSunrise());
                } else {
                    sunRiseTextViewFirst.setText("N/A");
                }

                // Check and set sunset
                if (modelForecast.getSunset() != null && !modelForecast.getSunset().isEmpty()) {
                    sunSetTextViewFirst.setText(modelForecast.getSunset());
                } else {
                    sunSetTextViewFirst.setText("N/A");
                }

            } else {
                Log.e("WeatherForecastAdapter", "Model Forecast is null");
            }
        }
    }
}








