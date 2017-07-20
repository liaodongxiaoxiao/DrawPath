package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;
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
    private Paint boundPaint;

    private Paint bgPaint;
    private PorterDuffXfermode duffXfermode;
    private Paint clearPaint = new Paint();

    private List<Point> points = new ArrayList<>();

    public static final int PADDING = 40;

    int centerX = 0;
    int centerY = 0;

    boolean needScale = false;

    //private LatLngBounds bounds;
    private LatLngBounds pBounds;
    private LatLngBounds defaultBounds;


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

        DashPathEffect pathEffect = new DashPathEffect(new float[]{2, 3}, 2);
        boundPaint = new Paint();
        //boundPaint.reset();
        boundPaint.setStyle(Paint.Style.STROKE);
        boundPaint.setStrokeWidth(10);
        boundPaint.setColor(Color.WHITE);
        boundPaint.setAntiAlias(true);
        boundPaint.setPathEffect(pathEffect);

        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }


    int xV;
    int yV;
    int x0;
    int y0;
    Canvas canvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        int left = getLeft() + PADDING < getRight() ? getLeft() + PADDING : getLeft();
        int bottom = getBottom() - PADDING > getTop() ? getBottom() - PADDING : getBottom();

        int right = getRight() - PADDING > getLeft() ? getRight() - PADDING : getRight();
        int top = getTop() + PADDING < getBottom() ? getTop() + PADDING : getTop();
        if (defaultBounds == null) {
            defaultBounds = new LatLngBounds(new Point(left, bottom), new Point(right, top));
            Log.i(TAG, "onDraw default bounds: " + defaultBounds.toString());
        }

        int width = getWidth();
        int height = getHeight();
        centerX = width / 2;
        centerY = height / 2;
        //清屏
        clearPaint.setXfermode(duffXfermode);
        canvas.drawPaint(clearPaint);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        canvas.drawRect(left, top, right, bottom, bgPaint);
        //canvas.drawColor(Color.YELLOW);
        if (points.isEmpty() || points.size() == 1) {
            canvas.drawCircle(centerX, centerY, 10, pointPaint);
        } else {
            Point pointEnd = points.get(0);
            x0 = pointEnd.x;
            y0 = pointEnd.y;
            xV = pointEnd.x - centerX;
            yV = pointEnd.y - centerY;


            if (points.size() > 1) {
                Path path = new Path();
                //Log.i(TAG, "onDraw: ba la ba la ba la~");
                Point start = getPoint(pointEnd);
                Log.e(TAG, "Point: 0 " + start.x + " " + start.y);
                path.moveTo(start.x, start.y);
                Point p;
                for (int i = 1; i < points.size(); i++) {
                    p = getPoint(points.get(i));
                    Log.e(TAG, "Point: " + i + " " + p.x + " " + p.y);
                    if (!pBounds.contains(p)) {
                        pBounds.including(p);
                    }
                    path.lineTo(p.x, p.y);
                }

                Rect rect = pBounds.getRect();
                canvas.drawRect(rect, boundPaint);


                if (pBounds.northeast.y < 0) {
                    //Log.i(TAG, "onDraw northeast.y: " + (pBounds.northeast.y));
                    canvas.translate(0, -pBounds.northeast.y + PADDING);
                }
                if (pBounds.southwest.x < 0) {
                    //Log.i(TAG, "onDraw southwest.x: " + (pBounds.southwest.x));
                    canvas.translate(-pBounds.southwest.x + PADDING, 0);
                }

                canvas.drawPath(path, pathPaint);
            }
            Point e = getPoint(points.get(points.size() - 1));
            canvas.drawCircle(e.x, e.y, 10, pointPaint);
        }
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Point getLastPoint() {
        if (points != null && !points.isEmpty()) {
            return points.get(points.size() - 1);
        }
        return null;
    }

    private Point getPoint(Point point) {
        /*Log.i(TAG, "getPoint xv: " + xV + " yV:" + yV + " point x:" + point.x + " point y:"
                + point.y + " point0 x" + x0 + " point0 y" + y0);*/
        int x;
        int y;

        /*if (point.x < x0 && point.y < y0) {
            x = point.x - xV;
            y = point.y - yV;
        } else if (point.x >= x0 && point.y < y0) {
            x = point.x - xV;
            y = point.y - yV;
        } else if (point.x >= x0 && point.y >= y0) {
            x = point.x - xV;
            y = point.y - yV;
        } else {
            x = point.x - xV;
            y = point.y - yV;
        }*/
        x = point.x - xV;
        y = point.y - yV;
        Log.e(TAG, "getPoint: " + x + " " + y);
        return new Point((int) (x * scale.doubleValue()), (int) (y * scale.doubleValue()));
        //return new Point(x, y);
        //return point;
    }

    public void addPoint(Point point) {
        //Log.i(TAG, "addPoint x:" + point.x + " y:" + point.y);
        points.add(point);
        if (pBounds == null) {
            initBounds();
        }
        invalidate();
    }

    private void initBounds() {

        pBounds = new LatLngBounds(new Point(centerX, centerY)
                , new Point(centerX, centerY));
        pBounds.setOnBoundsChangeListener(new BondChangeListener());
    }

    private BigDecimal scale = new BigDecimal(1d);

    private class BondChangeListener implements LatLngBounds.OnBoundsChangeListener {

        @Override
        public void onSouthWestXChanged(int oldX, int newX) {
            Log.i(TAG, "onSouthWestXChanged: ");
            calculationScale();
            invalidate();
        }

        @Override
        public void onSouthWestYChanged(int oldY, int newY) {
            Log.i(TAG, "onSouthWestYChanged: ");
            calculationScale();
            invalidate();
        }


        @Override
        public void onNorthEastXChanged(int oldX, int newX) {
            Log.i(TAG, "onNorthEastXChanged: ");
            calculationScale();
            invalidate();
        }

        @Override
        public void onNorthEastYChanged(int oldY, int newY) {
            Log.i(TAG, "onNorthEastYChanged: ");
            calculationScale();
            invalidate();
        }

    }

    private void calculationScale() {
        LatLngBounds temp = pBounds.change(PADDING);
        //|| defaultBounds.contains(pBounds.scaleBounds(scale))
        if (defaultBounds.contains(temp)) {
            return;
        }
        //LatLngBounds temp = pBounds.change(PADDING);
        Log.i(TAG, "calculationScale defaultBounds: " + defaultBounds);
        Log.i(TAG, "calculationScale pBounds: " + pBounds);
        Log.i(TAG, "calculationScale temp: " + temp);
        Log.i(TAG, "calculationScale scale before: " + scale.doubleValue());

        if (defaultBounds.contains(temp.scaleBounds(scale.doubleValue()))) {
            //pBounds = pBounds.scaleBounds(scale.doubleValue());
            //pBounds.setOnBoundsChangeListener(new BondChangeListener());
            return;
        }
        scale = scale.subtract(new BigDecimal(0.005d));
        while (!defaultBounds.contains(temp.change(PADDING).scaleBounds(scale.doubleValue()))) {
            scale = scale.subtract(new BigDecimal(0.005d));
            Log.i(TAG, "calculationScale scale after: " + scale.doubleValue());
            if (scale.doubleValue() <= 0) {
                break;
            }
            needScale = true;
            //Log.i(TAG, " do calculationScale: " + scale);
        }

        //pBounds = pBounds.scaleBounds(scale);
        //Log.i(TAG, "calculationScale: " + scale);
        //float scaleF = scale.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
        /*if (canvas != null && scaleF > 0) {
            canvas.scale(scaleF, scaleF, centerX, centerY);
        }*/
        Log.i(TAG, "calculationScale --: " + scale.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        //pBounds = pBounds.scaleBounds(scale.doubleValue());
        //pBounds.setOnBoundsChangeListener(new BondChangeListener());
    }


}
