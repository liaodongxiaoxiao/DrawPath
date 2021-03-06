package com.ldxx.drawpath;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;


/**
 * 代表了经纬度划分的一个矩形区域
 */
public final class LatLngBounds {
    private static final String TAG = "LatLngBounds";

    public final Point southwest;
    public final Point northeast;

    public LatLngBounds(int left, int top, int right, int bottom) {
        southwest = new Point(left, bottom);
        northeast = new Point(right, top);
    }

    public Rect getRect() {
        //left, int top, int right, int bottom
        return new Rect(southwest.x, northeast.y, northeast.x, southwest.y);
    }

    public Rect getRect(int padding) {
        return new Rect(southwest.x - padding, northeast.y - padding,
                northeast.x + padding, southwest.y + padding);

    }

    public LatLngBounds change(int padding) {
        int left = southwest.x;
        int top = northeast.y;
        int right = northeast.x;
        int bottom = southwest.y;
        if (left < 0) {
            left = -left + padding;
            right = right + left;
        }else{
            if(left<padding){
                left = left+(padding-left);
                right = right+left;
            }
        }

        if (top < 0) {
            top = -top + padding;
            bottom = bottom + top;
        }else {
            if(top<padding){
                top = top+(padding-top);
                bottom = bottom+top;
            }
        }
        return new LatLngBounds(left, top, right, bottom);

        /*if(southwest.x<0){
            southwest.x = -southwest.x+padding;
            northeast.x = northeast.x +(-southwest.x);
        }
        if ()*/
    }

    public interface OnBoundsChangeListener {
        void onSouthWestXChanged(int oldX, int newX);

        void onSouthWestYChanged(int oldY, int newY);

        void onNorthEastXChanged(int oldX, int newX);

        void onNorthEastYChanged(int oldY, int newY);
    }

    private OnBoundsChangeListener onBoundsChangeListener;

    public void setOnBoundsChangeListener(OnBoundsChangeListener onBoundsChangeListener) {
        this.onBoundsChangeListener = onBoundsChangeListener;
    }

    /**
     * 使用传入的西南角坐标和东北角坐标创建一个矩形区域
     *
     * @param southwest 西南角坐标
     * @param northeast 东北角坐标
     */
    public LatLngBounds(Point southwest, Point northeast) {
        this.southwest = southwest;
        this.northeast = northeast;
    }


    public boolean contains(Point point) {
        return (point.x >= southwest.x && point.x <= northeast.x && point.y >= northeast.y &&
                point.y <= southwest.y);
    }

    /**
     * 判断矩形区域是否包含传入的经纬度点
     *
     * @param point 经纬度点
     * @return true 矩形包含这个点;false 矩形未包含这个点
     */
    public boolean contains(LatLngBounds point) {
        Log.e(TAG, "contains this: " + this + " child:" + point);
        boolean bool = false;
        if (point == null) {
            return false;
        }
        if (contains(point.southwest) && contains(point.northeast)) {
            bool = true;
        }
        return bool;
    }

    @Override
    public String toString() {
        return "LatLngBounds{" +
                " southwest=" + southwest +
                ", northeast=" + northeast +
                '}';
    }

    /**
     * 加入一个点，重置范围
     *
     * @param point 新加入的点
     */
    public void including(Point point) {
        //若传入的新点在当前范围内，则不变
        if (contains(point)) {
            return;
        }

        if (point.x < southwest.x) {
            southwest.x = point.x;
            if (onBoundsChangeListener != null) {
                onBoundsChangeListener.onSouthWestXChanged(southwest.x, point.x);
            }

        }
        if (point.x > northeast.x) {
            northeast.x = point.x;
            if (onBoundsChangeListener != null) {
                onBoundsChangeListener.onNorthEastXChanged(northeast.x, point.x);
            }

        }

        if (point.y < northeast.y) {
            northeast.y = point.y;
            if (onBoundsChangeListener != null) {
                onBoundsChangeListener.onNorthEastYChanged(northeast.y, point.y);
            }

        }

        if (point.y > southwest.y) {
            southwest.y = point.y;
            if (onBoundsChangeListener != null) {
                onBoundsChangeListener.onSouthWestYChanged(southwest.y, point.y);
            }
        }
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if (!(paramObject instanceof LatLngBounds))
            return false;
        LatLngBounds localLatLngBounds = (LatLngBounds) paramObject;
        return (this.southwest.equals(localLatLngBounds.southwest)) && (this.northeast.equals
                (localLatLngBounds.northeast));
    }


    public int getBoundsWidth() {
        int result = northeast.x - southwest.x;
        return result > 0 ? result : 0;
    }

    public int getBoundsHeight() {
        return (southwest.y - northeast.y) > 0 ? (southwest.y - northeast.y) : 0;
    }

    public int getCenterX() {
        return southwest.x + getBoundsWidth() / 2;
    }

    public int getCenterY() {
        return northeast.y + getBoundsHeight() / 2;
    }

    public LatLngBounds scaleBounds(double scale) {
        if (scale == 0) {
            return this;
        }
        return new LatLngBounds(new Point((int) (southwest.x * scale), (int) (southwest.y * scale)),
                new Point((int) (northeast.x * scale), (int) (northeast.y * scale)));
    }
}