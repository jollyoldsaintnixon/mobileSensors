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

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sm;
    private Sensor gravity_sensor;
    private Sensor acl_sensor;
    private List<Sensor> sensor_list;
    double currentGravity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // why no new?
        sensor_list = sm.getSensorList(Sensor.TYPE_ALL);
        gravity_sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        acl_sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, gravity_sensor, 500000); // in micro seconds
        sm.registerListener(this, acl_sensor, 500000); // in micro seconds


        for (Sensor s : sensor_list) {
            System.out.println(s.getName());
            Log.v("s_tag", "name: " + s.getName());
            Log.v("s_tag", "vendor: " + s.getVendor());
//            tv.setText(tv.getText() + "\n" + s.getName());
        }
        setContentView(R.layout.activity_main);
    }

    public double getCurrentGravity() {
        return this.currentGravity;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getName().contains(gravity_sensor.getName())) {
            float gravity_x = event.values[0];
            float gravity_y = event.values[1];
            float gravity_z = event.values[2];
            double total_gravity = Math.sqrt(gravity_y*gravity_y+gravity_z*gravity_z+gravity_x*gravity_x);
            currentGravity = total_gravity;
            Log.v("GRAVITY", "gravity; " + total_gravity);
            Log.v("GRAVITY", "grav sensor");
            try {  // TODO Why doesn't this work?
            } catch (Exception e) {
                Log.v("MY_ERROR", e.getMessage());
            }
        } else if (event.sensor.getName().contains(acl_sensor.getName())) {
            float acl_x = event.values[0];
            float acl_y = event.values[1];
            float acl_z = event.values[2];
            double total_acl = Math.sqrt(acl_y*acl_y+acl_z+acl_z+acl_x+acl_x);
            Log.v("ACL", "Acl x:" + acl_x);
            Log.v("ACL", "acl sensor");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}