package com.example.asmt2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    static double DEFAULT_GRAVITY = 9.809989073394384;

    private TextView tv;
    private TextView acclText;
    private TextView gravText;
    private SensorManager sm;
    private Sensor gravity_sensor;
    private Sensor accl_sensor;
    private List<Sensor> sensor_list;
//    private List<Sensor> active_sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("hi");
        acclText = (TextView) findViewById(R.id.acclText);
        gravText = (TextView) findViewById(R.id.gravText);
        sensor_list = sm.getSensorList(Sensor.TYPE_ALL);
        for(int i = 0; i < sensor_list.size(); i++) {
            Log.v("-------->>", i + ": " + sensor_list.get(i).getName() + "");
        }
        //get gravity and light sensor
        gravity_sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        accl_sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // add sensors to active_sensors TODO what is wrong here?
//        active_sensors.add(gravity_sensor);
//        active_sensors.add(accl_sensor);
        //register sensors
        sm.registerListener(this, gravity_sensor, 1000000);
        sm.registerListener(this, accl_sensor, 1000000);
//        updatedSensorStatus(); TODO what is wrong here?
        makeText(gravity_sensor, gravText);
        makeText(accl_sensor, acclText);
    }

    private void makeText(Sensor sensor, TextView tv) {
        String text = "Status: " + sensor.getName() + " is Present\n"
                + "Range: " + sensor.getMaximumRange()
                + " Resolution: " + sensor.getResolution()
                + " Delay: " + sensor.getMaxDelay();
//        tv.setText(text);
    }

//    private void updatedSensorStatus() {
//        Log.v("MYTEST", "61");
//        LinearLayout rowMaster = (LinearLayout) findViewById(R.id.rowMaster);
//        for (int i=0; i<active_sensors.size(); i++) {
//            Sensor s = active_sensors.get(i);
//            Log.v("MYTEST", "65");
//            if (sensor_list.contains(s)) {
//                // dynamically create button and textviews
//                String name = s.getName();
//                Log.v("MYTEST", "NAME: " + name);
//                //row
//                LinearLayout row = new LinearLayout(this);
//                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
//                row.setOrientation(LinearLayout.HORIZONTAL);
//                row.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
//                row.setWeightSum(1);
//                // button
//                Button button = new Button(this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//                button.setLayoutParams(params);
//                button.setText(name);
//                button.setId(View.generateViewId());
//                button.setOnClickListener(activate);
//                // textview
//                TextView tv = new TextView(this);
//                tv.setLayoutParams(params); // same params i think
//                tv.setId(View.generateViewId());
//                tv.setPadding(3, 3, 3, 3);
//                tv.setGravity(Gravity.CENTER);
//                // add together
//                row.addView(button);
//                row.addView(tv);
//                rowMaster.addView(row);
//            }
//        }
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getName().contains(accl_sensor.getName())) {
            String tmp = "" + (Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]) - DEFAULT_GRAVITY);
//            Log.v("GRAVITY", tmp);
            tv.setText(tmp);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void viewGravity(View view) {
        Log.v("BUTTON", "attempting new activity");
        Intent gravityIntent = new Intent(this, GravityActivity.class);
        startActivity(gravityIntent);
    }

    public void viewAccl(View view) {
        Log.v("BUTTON", "attempting new activity");
        Intent acclIntent = new Intent(this, AcclActivity.class);
        startActivity(acclIntent);
    }

//    View.OnClickListener activate = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int id = v.getId();
//            Log.v("button", "button was clciked: " + id);
//        }
//    };

}