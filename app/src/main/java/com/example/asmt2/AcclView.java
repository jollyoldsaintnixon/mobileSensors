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

public class AcclView extends View {

    ArrayList<Double> points;
    ArrayList<Double> meanList;
    ArrayList<Double> sdList;
    int divisions = 10;
    Paint paint = new Paint();
    Paint greenPaint = new Paint();
    Paint redPaint = new Paint();
    Paint yellowPaint = new Paint();
    int width, height;
    double max = 20;
    double min = -20;
    double stdDevMax = 4.0;
    double stdDevMin = 0.0;
    float radius = 5;
    double[][] coords = new double[divisions+1][2] ;
    int count = 0;
    double total = 0.0;
    double mean;
    // box variables
    int box_left = 100;
    int box_top = 35;

//    public GravityView(Context context) {
//        super(context);
//    }

    public AcclView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        points = new ArrayList<Double>();
        meanList = new ArrayList<Double>();
        sdList = new ArrayList<Double>();
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        greenPaint.setColor(Color.GREEN);
        yellowPaint.setColor(Color.BLUE);
        redPaint.setColor(Color.RED);
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setStdDevMax(double stdDevMax) {
        this.stdDevMax = stdDevMax;
    }

    public void setStdDevMin(double stdDevMin) {
        this.stdDevMin = stdDevMin;
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
//        Log.v("ACCL", "point: " + f);
//        Log.v("ACCL", "max: " + max);
//        Log.v("ACCL", "min: " + min);
    }

    private void computeMean(Double f) {
        total += f;
        double mean = (double) total/++count;
//        Log.v("MEAN", String.valueOf(mean));
//        Log.v("SUM", "" + total);
        while (meanList.size() > divisions) {
            meanList.remove(0);
        }
        this.mean = mean;
        meanList.add(mean);
    }


    private void computeStdDev(Double f) {
        if (count <= 1) {
            sdList.add(0.0);
            return;
        }
        double stdDev = 0;
        double variance_sum = 0;
//        Log.v("STD_DEV", "inside");
        for (int i=0; i<points.size(); i++) {
            double variance;
            variance = points.get(i) - mean;
            variance *= variance;
            variance_sum += variance;
//            Log.v("STD_DEV", "stdDev: " + stdDev);
        }
        variance_sum /= (count - 1);
        stdDev = Math.sqrt(variance_sum);
        while (sdList.size() > divisions) {
            sdList.remove(0);
        }
        if (stdDev > stdDevMax) {
            this.stdDevMax = stdDev;
        }
        if (stdDev < stdDevMin && stdDev > 0) {
            this.stdDevMin = stdDev;
        }
//        Log.v("STD", "std dev:" + stdDev);
        sdList.add(stdDev);
//        Log.v("STD_DEV", "max: " + stdDevMax);
//        Log.v("STD_DEV", "min: " + stdDevMin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
        int box_right = width - box_left;
        int box_bottom = height - 10;
        int box_width = box_right - box_left;
        int box_height = box_bottom - box_top;
        int box_dx = box_width / divisions;
        float box_dy = box_height / divisions;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(box_left, box_top, box_right, box_bottom, paint);
        paint.setColor(Color.BLACK);
        double span = max - min;
        float spanIncrement = (float) span / divisions;
//        double stdDevSpan = stdDevMax - stdDevMin;
//        double stdDevSpanIncrement = stdDevSpan / box_dy;
//        Log.v("NOTE", "std dev span: " + stdDevSpan);
//        Log.v("NOTE", "std dev increment: " + stdDevSpanIncrement);
        canvas.drawText(String.valueOf(stdDevMax),(float) box_right +3,(float) box_top, paint);
        canvas.drawLine(box_left+100, 10, box_left + 120, 10, greenPaint);
        canvas.drawText("value", box_left+125, 18, paint);
        canvas.drawLine(box_left + 190, 10, box_left + 210, 10,redPaint);
        canvas.drawText("mean", box_left+220, 18, paint);
        canvas.drawLine(box_left+300, 10, box_left + 320, 10, yellowPaint);
        canvas.drawText("std. dev.", box_left+325, 18, paint);
        canvas.drawText(String.valueOf(stdDevMax),(float) box_right +3,(float) box_top, paint);
        for (int i=0; i<divisions; i++) {
            canvas.drawLine(box_right, box_top + (i * box_dy), box_left, box_top + (i * box_dy), paint); // top to bottom
            canvas.drawLine(box_left + (i * box_dx), box_top, box_left + (i * box_dx), box_bottom, paint); // side to side
            if (i%2==0) {
                float lineMarker = (float) (max - ((float) i * spanIncrement));
                canvas.drawText(String.valueOf((int) lineMarker),(float) box_left - 30,(float) box_top + (i * box_dy), paint);

//                float stdDevLineMarker = (float) (stdDevMax - ((double) i * stdDevSpanIncrement));
//                Log.v("NOTE", "std dev maker at" + i + " : " + stdDevLineMarker);
//                canvas.drawText(String.format("%.3f %n", stdDevLineMarker),(float) box_right +3,(float) box_top + (i * box_dy), paint);
            }
        }
        canvas.drawText(String.valueOf((int) min),(float) box_left - 30,(float) box_top + (divisions * box_dy), paint);
        canvas.drawText(String.valueOf(stdDevMin),(float) box_right +3,(float) box_top + (divisions * box_dy), paint);

        plotLines(points, canvas, box_dx, box_dy, box_left, box_top, box_height, greenPaint);
        plotLines(meanList, canvas, box_dx, box_dy, box_left, box_top, box_height, redPaint);
        plotLines(sdList, canvas, box_dx, box_dy, box_left, box_top, box_height, yellowPaint, (stdDevMax - stdDevMin), stdDevMax);
        invalidate();
        requestLayout();
    }

    private void plotLines(ArrayList<Double> list, Canvas canvas, int dx, float dy, int left, int top, int height, Paint paint) {
        plotLines(list, canvas, dx, dy, left, top, height, paint, (max-min), max);
    }

    private void plotLines(ArrayList<Double> list, Canvas canvas, int dx, float dy, int left, int top, int height, Paint paint, double local_span, double local_max) {
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
