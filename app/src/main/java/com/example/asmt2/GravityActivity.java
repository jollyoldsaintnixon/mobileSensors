package com.example.asmt2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GravityActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor gravity_sensor;
    private List<Sensor> sensor_list;
    private GravityView chart;
    private float max = 0;
    private float min = 10;
    long lastPrinted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        Log.v("gravity_activity", "gravity_activity created");
        chart = (GravityView) findViewById(R.id.chart);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_list = sm.getSensorList(Sensor.TYPE_ALL);
        for(int i = 0; i < sensor_list.size(); i++) {
            Log.v("-------->>", i + ": " + sensor_list.get(i).getName() + "");
        }
        //get gravity and light sensor
        gravity_sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);

        //register sensors
        sm.registerListener(this, gravity_sensor, 1000000);
//        chart.addPoint((float) 9.806650);
//        chart.addPoint((float) 9.806651322253177);
//        chart.addPoint((float) 9.806650544271521);
//        chart.addPoint((float) 9.806651322253177);
//        chart.addPoint((float) 9.806650155280671);
//        chart.addPoint((float) 9.806650155280671);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("gravity_activity", "gravity_activity sensor check");
        Log.v("gravity_activity", "sensor name: " + event.sensor.getName());
        Log.v("gravity_activity", "event.sensor.getName().contains(gravity_sensor.getName()): " + event.sensor.getName().contains(gravity_sensor.getName()));
        if (event.timestamp - lastPrinted >= 1e9
                &&event.sensor.getName().contains(gravity_sensor.getName())) {
            lastPrinted = event.timestamp;
            float gravity = (float) Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]);
            if (gravity > max) {
                max = gravity;
            }
            if (gravity < min) {
                min = gravity;
            }
            Log.v("gravity_activity", "gravity: " + gravity);
            Log.v("gravity_activity", "max: " + max);
            Log.v("gravity_activity", "min: " + min);
            chart.addPoint(gravity);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
