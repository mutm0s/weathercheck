package com.example.WeatherCheck.mauritus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class WeatherSensor implements SensorEventListener {

    private static final float HOT_TEMPERATURE_THRESHOLD = 30.0f;
    private static final float WARM_TEMPERATURE_THRESHOLD = 20.0f;

    private static final float HIGH_HUMIDITY_THRESHOLD = 70.0f;
    private static final float COLD_TEMPERATURE_THRESHOLD = -02.0f;


    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mTemperatureSensor;
    private Sensor mHumiditySensor;
    private WeatherNotification mNotification;

    public WeatherSensor(Context context) {
        mContext = context;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mNotification = new WeatherNotification(mContext);
    }

    public void startListening() {
        if (mTemperatureSensor != null && mHumiditySensor != null) {
            mSensorManager.registerListener(this, mTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopListening() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            Log.d("WeatherSensor", "Temperature: " + temperature); // Add this log statement

            if (temperature > HOT_TEMPERATURE_THRESHOLD) {

                mNotification.sendNotification("Today's weather: Hot ");
            } else if (temperature > WARM_TEMPERATURE_THRESHOLD) {
                mNotification.sendNotification("Today's weather: Warm and possibly rainy");
            } else if (temperature < COLD_TEMPERATURE_THRESHOLD) {
                mNotification.sendNotification("Today's weather: Cold and possibly rainy");
            } else {
                mNotification.sendNotification("Today's weather: too hot");
            }
        } else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float humidity = event.values[0];
            Log.d("WeatherSensor", "Humidity: " + humidity);
            if (humidity > HIGH_HUMIDITY_THRESHOLD) {
                mNotification.sendNotification("Today's weather: Humid and possibly rainy");
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
