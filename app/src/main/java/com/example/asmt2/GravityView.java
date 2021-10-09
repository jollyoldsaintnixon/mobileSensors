package com.example.asmt2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GravityView extends View {

    ArrayList<Float> points;
    int divisions = 10;
    Paint paint = new Paint();
    Paint greenPaint = new Paint();
    int width, height;
//    float max = 9.806652878216305;
//    float min = 9.806647432344281;
    float max = 9.806652f;
    float min = 9.806648f;
    float span = max - min;
    float radius = 5;
    float[][] coords = new float[divisions][2] ;
    ArrayList<Float> meanList;
    int count = 0;
    double total = 0.0;

//    public GravityView(Context context) {
//        super(context);
//    }

    public GravityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.v("start", "im here");
        points = new ArrayList<Float>();
        Log.v("start", "width: " + width);
        Log.v("start", "height: " + height);
        paint.setColor(Color.BLACK);
        greenPaint.setColor(Color.GREEN);
    }

//    public GravityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public GravityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void clear() { points.clear(); }

    public void addPoint(Float f) {
        while (points.size() > divisions) {
            points.remove(0);
        }
        points.add(f);
        computeMean(f);
    }

    private void computeMean(Float f) {
        total += f;
        float mean = (float) total/++count;
        Log.v("MEAN", String.valueOf(mean));
        while (meanList.size() > divisions) {
            points.remove(0);
        }
        meanList.add(mean);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
        Log.v("update", "width: " + width);
        Log.v("update", "height: " + height);
        int box_left = 60;
        int box_top = 100;
        int box_right = width - 60;
        int box_bottom = height / 2;
        int box_width = box_right - box_left;
        int box_height = box_bottom - box_top;
        int box_dx = box_width / divisions;
        int box_dy = box_height / divisions;
        float cx;
        float cy;
        Log.v("hey", "box_left: " + box_left);
        Log.v("hey", "box_bottom: " + box_bottom);
        Log.v("hey", "box_right: " + box_right);
        Log.v("hey", "box_top: " + box_top);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(box_left, box_top, box_right, box_bottom, paint);
        paint.setColor(Color.BLACK);
        for (int i=0; i<divisions; i++) {
            canvas.drawLine(box_right, box_top + (i * box_dy), box_left, box_top + (i * box_dy), paint); // top to bottom
            canvas.drawLine(box_left + (i * box_dx), box_top, box_left + (i * box_dx), box_bottom, paint); // side to side
            if (i<points.size()) {
                double grav = points.get(i);
                double raw_y = (max - grav) / span;
                cx = (i * box_dx) + box_left;
                cy = (float) (box_height * raw_y) + box_top;
                coords[i] = new float[] {cx, cy};
                canvas.drawCircle(cx, cy, radius, greenPaint);
            }
        }
        for (int i=0; i<coords.length-1; i++) {
            if (coords[i+1][1] > 9.8f) {
                canvas.drawLine(coords[i][0], coords[i][1], coords[i+1][0], coords[i+1][1], greenPaint);
            }
        }
        plotLines(meanList, canvas, box_dx, box_dy, box_left, box_top, box_height);
        invalidate();
        requestLayout();
    }

    private void plotLines(ArrayList<Float> list, Canvas canvas, int dx, int dy, int left, int top, int height) {
        for (int i=0; i<list.size(); i++) {
            float val = (float) list.get(i);
            float x = (i * dx) + left;
            float raw_y = (max - val) / span;
            float y = (height * raw_y) + top;
            coords[i] = new float[] {x, y};
            if (i > 0) {
                canvas.drawLine(coords[i-1][0], coords[i-1][1], coords[i][0], coords[i][1], greenPaint);
            }
        }
    }
}
