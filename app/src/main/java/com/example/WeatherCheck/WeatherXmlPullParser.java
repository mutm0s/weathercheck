package com.example.WeatherCheck;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WeatherXmlPullParser {

    public static List<WeatherForecast> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        List<WeatherForecast> forecasts = null;
        WeatherForecast currentForecast = null;
        XmlPullParser parser = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    forecasts = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("item")) {
                        currentForecast = new WeatherForecast();
                    } else if (currentForecast != null) {
                        switch (name) {
                            case "title":
                                String title = parser.nextText();
                                currentForecast.setDayOfWeek(title.split(":")[0].trim());
                                currentForecast.setWeatherCondition(title.split(":")[1].trim());
                                break;
                            case "description":
                                String description = parser.nextText();
                                currentForecast.setMinTemperature(getValueFromDescription(description, "Minimum Temperature"));
                                currentForecast.setMaxTemperature(getValueFromDescription(description, "Maximum Temperature"));
                                currentForecast.setWindDirection(getValueFromDescription(description, "Wind Direction"));
                                currentForecast.setWindSpeed(getValueFromDescription(description, "Wind Speed"));
                                currentForecast.setVisibility(getValueFromDescription(description, "Visibility"));
                                currentForecast.setPressure(getValueFromDescription(description, "Pressure"));
                                currentForecast.setHumidity(getValueFromDescription(description, "Humidity"));
                                currentForecast.setUvRisk(Integer.parseInt(getValueFromDescription(description, "UV Risk")));
                                currentForecast.setPollution(getValueFromDescription(description, "Pollution"));
                                currentForecast.setSunrise(getValueFromDescription(description, "Sunrise"));
                                currentForecast.setSunset(getValueFromDescription(description, "Sunset"));
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && currentForecast != null) {
                        forecasts.add(currentForecast);
                    }
                    break;
            }
            eventType = parser.next();
        }

        return forecasts;
    }

    private static String getValueFromDescription(String description, String key) {
        String value = "";
        int startIndex = description.indexOf(key + ":");
        if (startIndex != -1) {
            startIndex += key.length() + 1; // Move index to start of value
            int endIndex = description.indexOf(",", startIndex); // Value ends at next comma
            if (endIndex == -1) {
                endIndex = description.length(); // If no comma found, value extends to end of description
            }
            value = description.substring(startIndex, endIndex).trim();
        }
        return value;
    }
}


