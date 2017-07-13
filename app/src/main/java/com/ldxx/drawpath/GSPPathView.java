/*
package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by wangzhuo-neu
 * on 2017/4/10.
 *//*


public class GSPPathView extends View {
    private static final String TAG = "GSPPathView";
    private List<Point> ps = new ArrayList<>();
    private Paint paint;
    LatLngBounds bounds;
    LatLngBounds viewBounds;
    float scaleX = 1f;
    float scaleY = 1f;

    public GSPPathView(Context context) {
        super(context);
        initPaint();
    }

    public GSPPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public GSPPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        //bounds = LatLngBounds.builder().build();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    private int sX;
    private int sY;

    private int nX;
    private int nY;

    private class GVOnBoundsChangeListener implements LatLngBounds.OnBoundsChangeListener {

        private Canvas canvas;

        public GVOnBoundsChangeListener(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void onSouthWestXChanged(int newX) {
            sX = newX - viewBounds.southwest.x - 20;
            Log.e(TAG, "onSouthWestXChanged: " + newX + " " + sX);
        }

        @Override
        public void onSouthWestYChanged(int newY) {
            sY = newY - viewBounds.southwest.y - 20;
            Log.e(TAG, "onSouthWestYChanged: " + newY + "" + sY);
        }

        @Override
        public void onNorthEastXChanged(int newX) {
            nX =   newX-viewBounds.northeast.x - 20;
            Log.e(TAG, "onNorthEastXChanged: " + newX + " " + nX);
        }

        @Override
        public void onNorthEastYChanged(int newY) {
            nY = viewBounds.northeast.y - newY - 20;
            Log.e(TAG, "onNorthEastYChanged: " + newY + " " + nY);
        }
    }

    int centerX;
    int centerY;

    Bitmap bitmap;
    Canvas bitmapCanvas;

    @Override
    protected void onDraw(Canvas canvas) {

        */
/*if (bitmap == null) {
            bitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }

        bitmapCanvas.save();
        //bitmapCanvas.translate(offsetX, offsetY);
        bitmapCanvas.drawColor(0xfff9f9f9);
        //bitmapCanvas.drawPath(path, outerPaint);
        if (getPath() != null) {
            bitmapCanvas.drawPath(getPath(), paint);
        }

        bitmapCanvas.restore();

        canvas.drawBitmap(bitmap, 0, 0, paint);*//*

        int width = getWidth();
        int height = getHeight();
        centerX = width / 2;
        centerY = height / 2;

        //Log.e(TAG, "onDraw  x:" + centerX + " y:" + centerY);

        if (viewBounds == null) {
            viewBounds = new LatLngBounds(new Point(getLeft(), getBottom()), new Point(getRight(),
                    getTop()));
            bounds = new LatLngBounds(new Point(getLeft(), getBottom()), new Point(getRight(),
                    getTop()));
            bounds.setOnBoundsChangeListener(new GVOnBoundsChangeListener(canvas));
            Log.e(TAG, "onDraw: " + viewBounds.toString());
        }

        // canvas.drawColor(Color.YELLOW);
        if (ps.isEmpty()) {
            canvas.drawPoint(width / 2, height / 2, paint);
            //canvas.drawCircle(width / 2, height / 2, 10, paint);
            Log.e(TAG, "onDraw  center:" + width / 2 + " " + height / 2);
        } else {
            Path path = new Path();                     //Path对象
            path.moveTo(centerX, centerY);
            for (Point p : ps) {
                Log.e(TAG, "p x:" + p.x+" y:"+p.y);
                Log.e(TAG, "onDraw: " + bounds);
                if (!bounds.contains(p)) {
                    bounds.including(p);
                    Log.e(TAG, "onDraw: " + viewBounds);
                    Log.e(TAG, "onDraw: " + bounds);
                }
                */
/*if (bounds.contains(viewBounds) && bounds != viewBounds) {
                    scaleX = getScale(viewBounds.getBoundsWidth(), bounds.getBoundsWidth());
                    scaleY = getScale(viewBounds.getBoundsHeight(), bounds.getBoundsHeight());
                }*//*

                Log.e(TAG, "onDraw: x"+(p.x -Math.max(sX,nX)) );
                Log.e(TAG, "onDraw: y"+(p.y -Math.max(sY,nY)) );
                path.lineTo(p.x -Math.max(sX,nX), p.y -Math.max(sY,nY));
            }
            //canvas.scale(scaleX, scaleY,centerX,centerY);
            //Log.e(TAG, "onDraw: " + scaleX + " " + scaleY);

            canvas.drawPath(path, paint);
        }

    }

    */
/*private Path getPath() {
        Path path = new Path();
        if (ps.size() < 1) {
            return null;
        }
        //Path对象
        path.moveTo(ps.get(0).x, ps.get(0).x);
        for (int i = 1; i < ps.size(); i++) {
            path.lineTo(ps.get(i).x, ps.get(i).y);
        }

        return path;
    }*//*



    public void addPoint(Point latLng) {
        ps.add(latLng);
        invalidate();
    }

    private float getScale(int w, int ww) {
        BigDecimal d1 = new BigDecimal(w);
        BigDecimal d2 = new BigDecimal(ww);
        float result = d1.divide(d2, 2, BigDecimal.ROUND_DOWN).floatValue();
        return result == 0d ? 1 : result;
    }
}
*/
