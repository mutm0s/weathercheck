package com.example.WeatherCheck;

public interface Forecast {
    String getDayOfWeek();
    String getWeatherCondition();
    String getMinTemperature();
    String getMaxTemperature();
    String getWindDirection();
    String getWindSpeed();
    String getVisibility();
    String getPressure();
    String getHumidity();
    int getUvRisk();
    String getPollution();
    String getSunrise();
    String getSunset();
}

