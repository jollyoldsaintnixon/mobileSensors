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

    ArrayList<Double> points;
    ArrayList<Double> meanList;
    ArrayList<Double> sdList;
    int divisions = 10;
    Paint paint = new Paint();
    Paint greenPaint = new Paint();
    Paint redPaint = new Paint();
    Paint yellowPaint = new Paint();
    int width, height;
    double max = 9.806652878216305 * GravityActivity.GRAV_MULTI;
    double min = 9.806647432344281 * GravityActivity.GRAV_MULTI;
    double stdDevMax = 0.008;
    double stdDevMin = 0.001;
    double span = max - min;
    float radius = 5;
    double[][] coords = new double[divisions+1][2] ;
    int count = 0;
    double total = 0.0;

//    public GravityView(Context context) {
//        super(context);
//    }

    public GravityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.v("start", "im here");
        points = new ArrayList<Double>();
        meanList = new ArrayList<Double>();
        sdList = new ArrayList<Double>();
        Log.v("start", "width: " + width);
        Log.v("start", "height: " + height);
        paint.setColor(Color.BLACK);
        greenPaint.setColor(Color.GREEN);
        yellowPaint.setColor(Color.YELLOW);
        redPaint.setColor(Color.RED);
    }

    public void clear() {
        points.clear();
        meanList.clear();
        sdList.clear();
    }

    public void addPoint(Double f) {
        while (points.size() > divisions) {
            points.remove(0);
        }
        points.add(f);
        computeMean(f);
        computeStdDev(f);
    }

    private void computeMean(Double f) {
        total += f;
        double mean = (double) total/++count;
        Log.v("MEAN", String.valueOf(mean));
        Log.v("SUM", "" + total);
        while (meanList.size() > divisions) {
            meanList.remove(0);
        }
        meanList.add(mean);
    }


    private void computeStdDev(Double f) {
        double stdDev = 0;
//        Log.v("STD_DEV", "inside");
        for (int i=0; i<points.size(); i++) {
//            Log.v("STD_DEV", "point: " + points.get(i));
//            Log.v("STD_DEV", "mean: " + meanList.get(meanList.size()-1));
            stdDev += Math.pow((points.get(i) - meanList.get(meanList.size()-1)), 2);
//            Log.v("STD_DEV", "stdDev: " + stdDev);
        }
        int sample = count < 10 ? count : 10;
        stdDev /= sample;
        stdDev = Math.sqrt(stdDev);
        while (sdList.size() > divisions) {
            sdList.remove(0);
        }
        if (stdDev > stdDevMax) {
            this.stdDevMax = stdDev;
        }
        if (stdDev < stdDevMin && stdDev > 0) {
            this.stdDevMin = stdDev;
        }
        sdList.add(stdDev);
        Log.v("STD_DEV", "max: " + stdDevMax);
        Log.v("STD_DEV", "min: " + stdDevMin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
        int box_left = 60;
        int box_top = 100;
        int box_right = width - 60;
        int box_bottom = height / 2;
        int box_width = box_right - box_left;
        int box_height = box_bottom - box_top;
        int box_dx = box_width / divisions;
        int box_dy = box_height / divisions;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(box_left, box_top, box_right, box_bottom, paint);
        paint.setColor(Color.BLACK);
        for (int i=0; i<divisions; i++) {
            canvas.drawLine(box_right, box_top + (i * box_dy), box_left, box_top + (i * box_dy), paint); // top to bottom
            canvas.drawLine(box_left + (i * box_dx), box_top, box_left + (i * box_dx), box_bottom, paint); // side to side
        }
        plotLines(points, canvas, box_dx, box_dy, box_left, box_top, box_height, greenPaint);
        plotLines(meanList, canvas, box_dx, box_dy, box_left, box_top, box_height, redPaint);
        plotLines(sdList, canvas, box_dx, box_dy, box_left, box_top, box_height, yellowPaint, (stdDevMax - stdDevMin), stdDevMax);
        invalidate();
        requestLayout();
    }

    private void plotLines(ArrayList<Double> list, Canvas canvas, int dx, int dy, int left, int top, int height, Paint paint) {
        plotLines(list, canvas, dx, dy, left, top, height, paint, span, max);
    }

    private void plotLines(ArrayList<Double> list, Canvas canvas, int dx, int dy, int left, int top, int height, Paint paint, double local_span, double local_max) {
        for (int i=0; i<list.size(); i++) {
            double val = list.get(i);
            double x = (i * dx) + left;
            double raw_y = (local_max - val) / local_span;
            double y = (height * raw_y) + top;
            coords[i] = new double[] {x, y};
            if (i > 0) {
                canvas.drawLine((float) coords[i-1][0],(float) coords[i-1][1],
                        (float)coords[i][0],(float) coords[i][1], paint);
                canvas.drawCircle((float) x, (float) y, radius, paint);
            }
        }
    }
}
