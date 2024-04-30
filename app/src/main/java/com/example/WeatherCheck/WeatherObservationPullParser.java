package com.example.WeatherCheck;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

public class WeatherObservationPullParser {

    public static WeatherObservation parseXml(InputStream inputStream) throws XmlPullParserException, IOException {

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        WeatherObservation observation = null;
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                observation = new WeatherObservation();
            } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("description")) {
                String description = parser.nextText();
                parseDescription(observation, description);
            }
            eventType = parser.next();
        }
        return observation;
    }

    private static void parseDescription(WeatherObservation observation, String description) {
        if (observation == null || description == null) {
            return;
        }

        String[] parts = description.split(",");
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) {
                if (trimmedPart.contains("Temperature")) {
                    String temperature = extractTemperature(trimmedPart);
                    observation.setTemperature(temperature);
                } else if (trimmedPart.contains("Wind Direction")) {
                    String windDirection = trimmedPart.substring(trimmedPart.indexOf(":") + 1).trim();
                    observation.setWindDirection(windDirection);
                } else if (trimmedPart.contains("Wind Speed")) {
                    String windSpeed = trimmedPart.substring(trimmedPart.indexOf(":") + 1).trim();
                    observation.setWindSpeed(windSpeed);
                } else if (trimmedPart.contains("Humidity")) {
                    String humidity = trimmedPart.substring(trimmedPart.indexOf(":") + 1).trim();
                    observation.setHumidity(humidity);
                } else if (trimmedPart.contains("Pressure")) {
                    String pressure = trimmedPart.substring(trimmedPart.indexOf(":") + 1).trim();
                    observation.setPressure(pressure);
                }
            }
        }
    }

    private static String extractTemperature(String description) {
        String[] parts = description.split(":");
        if (parts.length > 1) {
            String temperaturePart = parts[1].trim();
            if (temperaturePart.contains("°C")) {
                return temperaturePart.split("°C")[0].trim() + "°C";
            }
        }
        return "";
    }
}






/*

public class WeatherObservationPullParser {

    public static WeatherObservation parseXml(InputStream inputStream) throws XmlPullParserException, IOException {

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        WeatherObservation observation = null;
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                observation = new WeatherObservation();
            } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("description")) {
                String description = parser.nextText();
                parseDescription(observation, description);
            }
            eventType = parser.next();
        }
        return observation;
    }

    private static void parseDescription(WeatherObservation observation, String description) {
        String[] parts = description.split(",");
        for (String part : parts) {
            if (part.contains("Temperature")) {
                observation.setTemperature(part.trim());
            } else if (part.contains("Wind Direction")) {
                observation.setWindDirection(part.trim());
            } else if (part.contains("Wind Speed")) {
                observation.setWindSpeed(part.trim());
            } else if (part.contains("Humidity")) {
                observation.setHumidity(part.trim());
            } else if (part.contains("Pressure")) {
                observation.setPressure(part.trim());
            }
        }
    }
}
*/
