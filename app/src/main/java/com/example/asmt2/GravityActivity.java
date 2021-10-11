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
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GravityActivity extends AppCompatActivity implements SensorEventListener {
    static int GRAV_MULTI = 10000;
    private SensorManager sm;
    private Sensor gravity_sensor;
    private List<Sensor> sensor_list;
    private GravityView chart;
    private double max = 0;
    private double min = 10 * GRAV_MULTI;
    long lastPrinted = 0;
    /* TODO fix std dev math and why it starts below 0
        * labels
        * take out gravity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        chart = (GravityView) findViewById(R.id.chart);
        chart.setMax(9.806652878216305 * GravityActivity.GRAV_MULTI);
        chart.setMin(9.806647432344281 * GravityActivity.GRAV_MULTI);
        chart.setStdDevMax(.008);
        chart.setStdDevMin(0.0);
//        chart.setConvertedMax(52);
//        chart.setConvertedMax(48);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_list = sm.getSensorList(Sensor.TYPE_ALL);
        for(int i = 0; i < sensor_list.size(); i++) {
            Log.v("-------->>", i + ": " + sensor_list.get(i).getName() + "");
        }
        //get gravity and light sensor
        gravity_sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);

        //register sensors
        sm.registerListener(this, gravity_sensor, 10200000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.timestamp - lastPrinted >= 1e8
                &&event.sensor.getName().contains(gravity_sensor.getName())) {
            lastPrinted = event.timestamp;
            double gravity = (double) Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]);
            gravity *= GRAV_MULTI;
            if (gravity > max) {
                max = gravity;
            }
            if (gravity < min) {
                min = gravity;
            }
            chart.addPoint(gravity);
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
