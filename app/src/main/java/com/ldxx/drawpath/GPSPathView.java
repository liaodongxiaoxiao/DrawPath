package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhuo-neu
 * on 2017/4/10.
 */

public class GPSPathView extends View {
    private static final String TAG = "GSPPathView";
    private List<Point> ps = new ArrayList<>();
    private Paint paint;

    private Paint pointPaint;

    //private int lng = 116405994;
    //private int lat = 39914935;

    LatLngBounds bounds;
    LatLngBounds viewBounds;

    private Point myLocation;

    private double scaleX = 1d;
    private double scaleY = 1d;

    private int xV;
    private int yV;

    public GPSPathView(Context context) {
        super(context);
        initPaint();
    }

    public GPSPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public GPSPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        //bounds = LatLngBounds.builder().build();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(4);
    }

    public void clear() {
        ps.clear();
        invalidate();
    }

    private class GVOnBoundsChangeListener implements LatLngBounds.OnBoundsChangeListener {
        @Override
        public void onSouthWestXChanged(int oldX, int newX) {
            Log.e(TAG, "onSouthWestXChanged oldX:" + oldX + " newX:" + newX);
            Log.e(TAG, "onSouthWestXChanged: " + (oldX - newX));
            Log.e(TAG, "onSouthWestXChanged: "+((oldX-newX)*1f/((oldX-newX)+centerX)));
            //xV = (int) (xV*((oldX-newX)*1f/((oldX-newX)+centerX)));
            scaleX = 1- ((xV-newX)*1f/((xV-newX)+centerX));
            //xV = xV - (oldX - newX);
            //Log.e(TAG, "xV: " + xV + " yV:" + yV);
        }

        @Override
        public void onSouthWestYChanged(int oldY, int newY) {
            Log.e(TAG, "onSouthWestYChanged  oldY:" + oldY + " newY:" + newY);
            Log.e(TAG, "onSouthWestYChanged: " + (newY - oldY));
            //yV = (int) (yV*((newY-oldY)*1f/((newY-oldY)+getHeight())));
            Log.e(TAG, "onSouthWestYChanged: "+ (newY-oldY)*1f/((newY-oldY)+getHeight()));
            //yV = yV + (newY - oldY);
            scaleY = 1-(newY-yV)*1f/((newY-yV)+centerY);
            //Log.e(TAG, "xV: " + xV + " yV:" + yV);
        }

        @Override
        public void onNorthEastXChanged(int oldX, int newX) {
            Log.e(TAG, "onNorthEastXChanged  oldX:" + oldX + " newX:" + newX);
            Log.e(TAG, "onNorthEastXChanged: " + (newX - oldX));
            Log.e(TAG, "onNorthEastXChanged: "+(newX-oldX)*1f/(newX-oldX+getWidth()));
            //xV = (int) (xV*((newX-oldX)*1f/(newX-oldX+getWidth())));
            //xV = xV + (newX - oldX);
            //Log.e(TAG, "xV: " + xV + " yV:" + yV);
            scaleX = 1-(newX-xV)*1f/(newX-xV+centerX);
        }

        @Override
        public void onNorthEastYChanged(int oldY, int newY) {
            Log.e(TAG, "onNorthEastYChanged  oldY:" + oldY + " newY:" + newY);
            Log.e(TAG, "onNorthEastYChanged: " + (oldY - newY));
            Log.e(TAG, "onNorthEastYChanged: "+(oldY-newY)*1f/((oldY-newY)+centerY));
            //yV = (int) (yV*((oldY-newY)*1f/((oldY-newY)+centerY)));
            //yV = yV + (oldY - newY);
            //Log.e(TAG, "xV: " + xV + " yV:" + yV);
            scaleY = (yV-newY)*1f/((yV-newY)+centerY);
        }
    }

    int centerX = 0;
    int centerY = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        canvas.drawCircle(0,0,20,pointPaint);
        int width = getWidth();
        int height = getHeight();
        centerX = width / 2;
        centerY = height / 2;

        if (ps.isEmpty()) {
            //Log.e(TAG, "onDraw centerX: " + centerX + " centerY:" + centerY);
            canvas.drawCircle(centerX, centerY, 10, pointPaint);
            return;
        }


        Path path = new Path();

        //Path对象
        path.moveTo(centerX, centerY);

        Point p;
        for (int i = 0; i < ps.size(); i++) {
            Log.e(TAG, "center(" + centerX + "," + centerY + ") point( " + ps.get(i).x + "," + ps
                    .get(i).y + ")");
            p = new Point((int) ((ps.get(i).x - xV + centerX)*scaleX), (int) ((ps.get(i).y - yV + centerY)*scaleY));
            Log.e(TAG, "onDraw p.x" + p.x + " p.y" + p.y);
            path.lineTo(p.x, p.y);
        }

        //Log.e(TAG, "onDraw: " + scaleX + " " + scaleY);

        canvas.drawPath(path, paint);
        drawPoint(canvas, ps.get(ps.size() - 1));

    }

    private void initBounds() {

        if (viewBounds == null) {
            Log.e(TAG, "initBounds: ");
            int left = getLeft() + 20 < getRight() ? getLeft() + 20 : getLeft();
            //int bottom = getBottom() - 20 > getTop() ? getBottom() - 20 : getBottom();

            //int right = getRight() - 20 > getLeft() ? getRight() - 20 : getRight();
            int top = getTop() + 20 < getBottom() ? getTop() + 20 : getTop();

            viewBounds = new LatLngBounds(new Point(left, myLocation.y + centerY), new Point
                    (myLocation.x + centerX,
                            top));
            bounds = new LatLngBounds(new Point(myLocation.x - 100, myLocation.y + 100), new
                    Point(myLocation.x + 100, myLocation.y - 100));
            bounds.setOnBoundsChangeListener(new GVOnBoundsChangeListener());
            Log.e(TAG, "initBounds: " + viewBounds.toString());
            Log.e(TAG, "initBounds: " + bounds.toString());
        }
    }


    public void addPoint(Point latLng) {
        Log.e(TAG, "addPoint: " + latLng.x + " " + latLng.y);
        ps.add(latLng);
        if (!bounds.contains(latLng)) {
            bounds.including(latLng);
        }
        invalidate();
    }

    private void drawPoint(Canvas canvas, Point point) {
        canvas.drawCircle((int) ((point.x - xV + centerX)*scaleX), (int) ((point.y - yV + centerY)*scaleY), 10, pointPaint);
        int x = (int) ((point.x-myLocation.x)*scaleX);
        int y = (int) ((point.y-myLocation.y)*scaleY);
        if(x<centerX&&y<centerY){
            canvas.translate(centerX-x,centerY-y);
            Log.e(TAG, "drawPoint: "+(centerX-x)+" "+(centerY-y) );
        }
        //canvas.translate((int)(centerX-((point.x-myLocation.x)*scaleX)),(int)(centerY-((point.y-myLocation.y)*scaleY)));
        //Log.e(TAG, "drawPoint: "++" "+(point.y-myLocation.y)*scaleY );
    }

    public void setMyLocation(Point myLocation) {
        this.myLocation = myLocation;
        xV = myLocation.x - centerX;
        yV = myLocation.y - centerY;
        Log.e(TAG, "setMyLocation: " + xV + " " + yV);
        initBounds();
    }
}
