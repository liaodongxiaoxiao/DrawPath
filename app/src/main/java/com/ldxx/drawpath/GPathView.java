package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldxx
 * on 2017/7/14.
 */

public class GPathView extends View {
    private static final String TAG = "GPathView";
    private Paint pathPaint;
    private Paint pointPaint;

    private Paint bgPaint;

    private List<Point> points = new ArrayList<>();

    public static final int PADDING = 40;

    int centerX = 0;
    int centerY = 0;

    private LatLngBounds bounds;


    public GPathView(Context context) {
        this(context, null);
    }

    public GPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
    }

    private void initPaint() {
        pathPaint = new Paint();
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(5);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(4);

        bgPaint = new Paint();
        bgPaint.setColor(Color.YELLOW);
        pointPaint.setStyle(Paint.Style.FILL);
    }


    int xV;
    int yV;
    Canvas canvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        int left = getLeft() + PADDING < getRight() ? getLeft() + PADDING : getLeft();
        int bottom = getBottom() - PADDING > getTop() ? getBottom() - PADDING : getBottom();

        int right = getRight() - PADDING > getLeft() ? getRight() - PADDING : getRight();
        int top = getTop() + PADDING < getBottom() ? getTop() + PADDING : getTop();

        canvas.drawRect(left, top, right, bottom, bgPaint);

        int width = getWidth();
        int height = getHeight();
        centerX = width / 2;
        centerY = height / 2;


        if (points.isEmpty()) {
            canvas.drawCircle(centerX, centerY, 10, pointPaint);
        } else {
            Point pointEnd = points.get(points.size() - 1);
            xV = pointEnd.x - centerX;
            yV = pointEnd.y - centerY;

            if (points.size() > 1) {
                Path path = new Path();
                Log.e(TAG, "onDraw: ba la ba la ba la~");
                LatLngBounds pBounds = new LatLngBounds(new Point(centerX - PADDING, 2 * centerY)
                        , new Point(2 * centerX, centerY - PADDING));
                path.moveTo(centerX, centerY);
                Point p;
                for (int i = 0; i < points.size() - 1; i++) {
                    p = getPoint(points.get(i));
                    if (!pBounds.contains(p)) {
                        pBounds.including(p);
                    }
                    path.lineTo(p.x, p.y);
                }

                //Rect newRect = canvas.getClipBounds();
                //newRect.inset(p.x, p.y); //make the rect larger
                //Log.e(TAG, "onDraw: " +newRect );
                canvas.clipRect(pBounds.getRect(), Region.Op.REPLACE);

                Log.e(TAG, "onDraw p bound: " + pBounds);
                if (pBounds.northeast.y < 0) {
                    canvas.translate(0, -pBounds.northeast.y);
                } else if (pBounds.southwest.x < 0) {
                    canvas.translate(-pBounds.southwest.x, 0);
                }
                canvas.drawPath(path, pathPaint);
            }
            Point e = getPoint(pointEnd);
            canvas.drawCircle(e.x, e.y, 10, pointPaint);
        }
    }

    private Point getPoint(Point point) {
        int x;
        int y;
        if (point.x <= xV && point.x <= yV) {
            x = xV - point.x;
            y = yV - point.y;
        } else if (point.x > xV && point.y <= yV) {
            x = point.x - xV;
            y = yV - point.y;
        } else if (point.x > xV && point.y > yV) {
            x = point.x - xV;
            y = point.y - yV;
        } else {
            x = xV - point.x;
            y = point.y - yV;
        }
        //Log.e(TAG, "getPoint: " + x + " " + y);
        return new Point((int) (x * scale), (int) (y * scale));
        //return point;
    }

    public void addPoint(Point point) {
        Log.e(TAG, "addPoint x:" + point.x + " y:" + point.y);
        points.add(point);
        if (bounds == null) {
            initBounds(point);
        }
        if (!bounds.contains(point)) {
            bounds.including(point);
        }
        invalidate();
    }

    private void initBounds(Point point) {
        bounds = new LatLngBounds(new Point(point.x - 1000, point.y + 100),
                new Point(point.x + 1000, point.y - 1000));
        bounds.setOnBoundsChangeListener(new BondChangeListener(canvas));
    }

    private double scale = 1d;

    private class BondChangeListener implements LatLngBounds.OnBoundsChangeListener {

        private Canvas canvas;

        public BondChangeListener(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void onSouthWestXChanged(int oldX, int newX) {
            int change = (oldX - newX);

            /*Rect newRect = canvas.getClipBounds();
            //make the rect larger
            newRect.inset(newX, bounds.southwest.y);
            canvas.clipRect(newRect, Region.Op.REPLACE);*/
            Log.e(TAG, "onSouthWestXChanged: " + change);
            double s = 1 - change * 1d / (change + centerX);
            initScale(s);
        }

        @Override
        public void onSouthWestYChanged(int oldY, int newY) {
            int change = (newY - oldY);
            Log.e(TAG, "onSouthWestYChanged: " + change);
            double s = 1 - change * 1d / (change + centerY);
            initScale(s);
           /* Rect newRect = canvas.getClipBounds();
            //make the rect larger
            newRect.inset(bounds.southwest.x, newY);
            canvas.clipRect(newRect, Region.Op.REPLACE);*/
        }


        @Override
        public void onNorthEastXChanged(int oldX, int newX) {
            /*Rect newRect = canvas.getClipBounds();
            //make the rect larger
            newRect.inset(bounds.northeast.x + (newX - oldX), bounds.northeast.y);
            canvas.clipRect(newRect, Region.Op.REPLACE);*/
            int change = (newX - oldX);
            Log.e(TAG, "onNorthEastXChanged: " + change);
            double s = 1 - change * 1d / (change + centerX);
            initScale(s);
        }

        @Override
        public void onNorthEastYChanged(int oldY, int newY) {
            /*Rect newRect = canvas.getClipBounds();
            //make the rect larger
            newRect.inset(bounds.northeast.x, newY);
            canvas.clipRect(newRect, Region.Op.REPLACE);*/
            int change = (oldY - newY);
            Log.e(TAG, "onNorthEastYChanged: " + change);
            double s = 1 - change * 1d / (change + centerY);
            initScale(s);
        }

    }

    private void initScale(double s) {
        if (s == 0) {
            return;
        }
        if (scale == 1d) {
            scale = s;
        } else {
            scale = Math.min(scale, s);
        }
        //scale = scale ;
        Log.e(TAG, "initScale: " + scale);
    }
}
