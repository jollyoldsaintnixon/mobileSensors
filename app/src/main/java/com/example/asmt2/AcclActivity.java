package com.example.asmt2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AcclActivity extends AppCompatActivity implements SensorEventListener {
//    static int GRAV_MULTI = 10000;
    private SensorManager sm;
    private Sensor acclSensor;
    private GravityView chart;
//    private double max = 0;
//    private double min = 10 * GRAV_MULTI;
    long lastPrinted = 0;
    /* TODO

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accl);
        chart = (GravityView) findViewById(R.id.chart);
        chart.setMax(20);
        chart.setMin(-20);
        chart.setStdDevMax(4.0);
        chart.setStdDevMin(0.0);
        chart.setConvertedMax(20);
        chart.setConvertedMax(-20);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get gravity and light sensor
        acclSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //register sensors
        sm.registerListener(this, acclSensor, 10200000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.timestamp - lastPrinted >= 1e8 /// every 100 millisecs
                &&event.sensor.getName().contains(acclSensor.getName())) {
            lastPrinted = event.timestamp;
            double accl = Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]);
            accl -= MainActivity.DEFAULT_GRAVITY; // subtract out default gravity
            chart.addPoint(accl);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void backClick(View view) {
        Intent activityIntent = new Intent(this, MainActivity.class);
        chart.clear();
        startActivity(activityIntent);
    }
}
