package com.example.icamobilegame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

public class AccelerometerHandler implements SensorEventListener {

    float accelX;
    float accelY;
    float accelZ;
    final float alpha = 0.8f;

    private Sensor accelerometer;
    private SensorManager manager;

    private float gravity[] = new float[3];
    private float linear_acceleration[] = new float[3];

    public AccelerometerHandler(Context context) {
        manager = (SensorManager)
                context.getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            accelerometer =
                    manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public float getAccelX() {
        return accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public float getAccelZ() { return accelZ; }

    public void RegisterListener() {
        if (accelerometer != null) {
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void UnregisterListener() {
        manager.unregisterListener(this);
    }


        @Override
        public void onSensorChanged(SensorEvent event) {


            accelX = event.values[0];
            accelY = event.values[1];
            accelZ = event.values[2];

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }




}
