package com.ldxx.drawpath;

import android.graphics.Point;

/**
 * Created by wangzhuo-neu on 2017/4/11.
 */

public class MapUtils {
    /**
     * 根据用户的起点和终点经纬度计算两点间距离，此距离为相对较短的距离，单位米。
     *
     * @param startLatlng 起点的坐标
     * @param endLatlng   终点的坐标
     * @return 返回两点间相对较短的距离，单位米。
     */
    public static float calculateLineDistance(LatLng startLatlng, LatLng endLatlng) {
        //double d1 = 0.01745329251994329D;
        double d2 = startLatlng.longitude;
        double d3 = startLatlng.latitude;
        double d4 = endLatlng.longitude;
        double d5 = endLatlng.latitude;
        d2 *= 0.01745329251994329D;
        d3 *= 0.01745329251994329D;
        d4 *= 0.01745329251994329D;
        d5 *= 0.01745329251994329D;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0]) + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1]) + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (float) (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }

    /**
     * 计算地图上矩形区域的面积，单位平方米。
     *
     * @param leftTopLatlng     矩形区域左上角点坐标
     * @param rightBottomLatlng 矩形区域右下角点坐标
     * @return 返回地图上矩形区域的面积，单位平方米。
     */
    public static float calculateArea(LatLng leftTopLatlng, LatLng rightBottomLatlng) {
        double d1 = 6378137.0D;
        double d2 = Math.sin(leftTopLatlng.latitude * 3.141592653589793D / 180.0D) - Math.sin(rightBottomLatlng.latitude * 3.141592653589793D / 180.0D);

        double d3 = (rightBottomLatlng.longitude - leftTopLatlng.longitude) / 360.0D;
        if (d3 < 0.0D)
            d3 += 1.0D;
        return (float) (6.283185307179586D * d1 * d1 * d2 * d3);
    }

    /**
     * 将Point对象值转换成LatLng值
     *
     * @param point Point对象
     * @return LatLng对象
     */
    public static LatLng Point2LatLng(Point point) {
        double longitude = point.x / 1E5;
        double latitude = point.y / 1E5;
        return new LatLng(latitude, longitude);
    }

    /**
     * 将LatLng对象转换成Point对象
     *
     * @param latLng LatLng对象
     * @return Point对象
     */
    public static Point LatLng2Point(LatLng latLng) {
        int longitude = (int) (latLng.longitude * 1E5);
        int latitude = (int) (latLng.latitude * 1E5);
        return new Point(longitude, latitude);
    }

}
