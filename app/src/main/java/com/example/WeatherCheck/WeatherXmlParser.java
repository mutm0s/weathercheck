package com.example.WeatherCheck;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class WeatherXmlParser {
    private static final String XML_TAG_ITEM = "item";
    private static final String XML_TAG_TITLE = "title";
    private static final String XML_TAG_DESCRIPTION = "description";

    public static ModelForecast parseXml(String xmlData) {
        ModelForecast modelForecast = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xmlData));

            int eventType = parser.getEventType();
            boolean itemFound = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if (tagName.equals(XML_TAG_ITEM)) {
                            itemFound = true;
                        } else if (itemFound && tagName.equals(XML_TAG_TITLE)) {
                            String title = parser.nextText();
                            modelForecast = parseTitle(title);
                        } else if (itemFound && tagName.equals(XML_TAG_DESCRIPTION)) {
                            String description = parser.nextText();
                            if (modelForecast != null) {
                                parseDescription(modelForecast, description);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(XML_TAG_ITEM)) {
                            // We've reached the end of the <item> element, stop parsing
                            return modelForecast;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return modelForecast;
    }

    private static ModelForecast parseTitle(String title) {
        // Split the title by colon and take the first part as the day
        String[] parts = title.split(":");
        String dayOfWeek = parts[0].trim();
        // Get the weather condition by trimming the title
        String weatherCondition = parts[1].trim();
        // Trim the weather condition to remove additional information
        int endIndex = weatherCondition.indexOf(",");
        if (endIndex != -1) {
            weatherCondition = weatherCondition.substring(0, endIndex);
        }
        return new ModelForecast(dayOfWeek, weatherCondition);
    }
    private static void parseDescription(ModelForecast modelForecast, String description) {
        String[] parts = description.split(", ");

        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "Minimum Temperature":
                        modelForecast.setMinTemperature(parseTemperature(value));
                        break;
                    case "Maximum Temperature":
                        modelForecast.setMaxTemperature(parseTemperature(value));
                        break;
                    case "Wind Direction":
                        modelForecast.setWindDirection(value);
                        break;
                    case "Wind Speed":
                        modelForecast.setWindSpeed(value);
                        break;
                    case "Visibility":
                        modelForecast.setVisibility(value);
                        break;
                    case "Pressure":
                        modelForecast.setPressure(value);
                        break;
                    case "Humidity":
                        modelForecast.setHumidity(value);
                        break;
                    case "UV Risk":
                        modelForecast.setUvRisk(parseUvRisk(value));
                        break;
                    case "Pollution":
                        modelForecast.setPollution(value);
                        break;
                    case "Sunrise":
                        modelForecast.setSunrise(value);
                        break;
                    case "Sunset":
                        modelForecast.setSunset(value);
                        break;
                    default:
                        // Handle unknown key or ignore it
                        break;
                }
            }
        }
    }

    private static String parseTemperature(String temperature) {
        // Extract the Celsius value without the Fahrenheit value and symbol
        String[] parts = temperature.split(" ");
        if (parts.length > 0) {
            return parts[0] ;
        } else {
            return temperature; // Return as it is if unable to parse
        }
    }

    private static int parseUvRisk(String uvRisk) {
        try {
            return Integer.parseInt(uvRisk);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

/*
public class WeatherXmlParser {
    private static final String XML_TAG_ITEM = "item";
    private static final String XML_TAG_TITLE = "title";
    private static final String XML_TAG_DESCRIPTION = "description";

    public static ModelForecast parseXml(String xmlData) {
        ModelForecast modelForecast = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xmlData));

            int eventType = parser.getEventType();
            boolean itemFound = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if (tagName.equals(XML_TAG_ITEM)) {
                            itemFound = true;
                        } else if (itemFound && tagName.equals(XML_TAG_TITLE)) {
                            String title = parser.nextText();
                            modelForecast = parseTitle(title);
                        } else if (itemFound && tagName.equals(XML_TAG_DESCRIPTION)) {
                            String description = parser.nextText();
                            if (modelForecast != null) {
                                parseDescription(modelForecast, description);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(XML_TAG_ITEM)) {
                            // We've reached the end of the <item> element, stop parsing
                            return modelForecast;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return modelForecast;
    }

    private static ModelForecast parseTitle(String title) {
        String[] parts = title.split(":");
        String dayOfWeek = parts[0].trim();
        String weatherCondition = parts[1].trim();
        return new ModelForecast(dayOfWeek, weatherCondition);
    }




    private static void parseDescription(ModelForecast modelForecast, String description) {
        String[] parts = description.split(", ");

        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "Minimum Temperature":
                        modelForecast.setMinTemperature(value);
                        break;
                    case "Maximum Temperature":
                        modelForecast.setMaxTemperature(value);
                        break;
                    case "Wind Direction":
                        modelForecast.setWindDirection(value);
                        break;
                    case "Wind Speed":
                        modelForecast.setWindSpeed(value);
                        break;
                    case "Visibility":
                        modelForecast.setVisibility(value);
                        break;
                    case "Pressure":
                        modelForecast.setPressure(value);
                        break;
                    case "Humidity":
                        modelForecast.setHumidity(value);
                        break;
                    case "UV Risk":
                        modelForecast.setUvRisk(parseUvRisk(value));
                        break;
                    case "Pollution":
                        modelForecast.setPollution(value);
                        break;
                    case "Sunrise":
                        modelForecast.setSunrise(value);
                        break;
                    case "Sunset":
                        modelForecast.setSunset(value);
                        break;
                    default:
                        // Handle unknown key or ignore it
                        break;
                }
            }
        }
    }

    private static int parseUvRisk(String uvRisk) {
        try {
            return Integer.parseInt(uvRisk);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
*/
