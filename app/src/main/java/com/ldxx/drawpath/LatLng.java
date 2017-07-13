package com.ldxx.drawpath;

import android.os.Parcel;

import java.text.DecimalFormat;

/**
 * 存储经纬度坐标值的类，单位角度。
 */
public final class LatLng implements android.os.Parcelable {

    public double latitude;
    public double longitude;


    private static DecimalFormat df = new DecimalFormat("0.000000");

    /**
     * 使用传入的经纬度构造LatLng 对象，一对经纬度值代表地球上一个地点。
     *
     * @param latitude  地点的纬度，在-90 与90 之间的double 型数值。
     * @param longitude 地点的经度，在-180 与180 之间的double 型数值。
     */
    public LatLng(double latitude, double longitude) {
        if ((-180.0D <= longitude) && (longitude < 180.0D))
            this.longitude = format(longitude);
        else {
            this.longitude = format(((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D);
        }
        this.latitude = format(Math.max(-90.0D, Math.min(90.0D, latitude)));
    }

    /**
     * 格式化方法（SDK封装用的方法，不对外使用）
     * @param paramDouble double值
     * @return 格式话后的值
     */
    private static double format(double paramDouble) {
        return Double.parseDouble(df.format(paramDouble));
    }

    public LatLng clone() {
        return new LatLng(this.latitude, this.longitude);
    }

    public int hashCode() {
        int i = 31;
        int j = 1;
        long l = Double.doubleToLongBits(this.latitude);
        j = i * j + (int) (l ^ l >>> 32);
        l = Double.doubleToLongBits(this.longitude);
        j = i * j + (int) (l ^ l >>> 32);
        return j;
    }

    /**
     * 判断是否与另一个LatLng 对象相等的方法。 如果两个地点的经纬度全部相同，则返回true。否则返回false。
     *
     * @param o latlng对象
     * @return 返回两对象是否相等
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LatLng))
            return false;
        LatLng localLatLng = (LatLng) o;
        return (Double.doubleToLongBits(this.latitude) ==
                Double.doubleToLongBits(localLatLng.latitude)) && (Double.doubleToLongBits(this.longitude)
                == Double.doubleToLongBits(localLatLng.longitude));
    }

    public String toString() {
        return "lng/lat:(" + this.longitude + "," + this.latitude + ")";
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.longitude);
        parcel.writeDouble(this.latitude);
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        public LatLng createFromParcel(Parcel paramParcel) {
            double d1 = paramParcel.readDouble();
            double d2 = paramParcel.readDouble();
            return new LatLng(d2, d1);
        }

        public LatLng[] newArray(int paramInt) {
            return new LatLng[paramInt];
        }
    };
}