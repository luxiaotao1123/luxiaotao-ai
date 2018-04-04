package com.wuai.company.util;

import java.text.DecimalFormat;

/**
 * 距离计算工具
 * Created by Ness on 2017/6/5.
 */
public class DistanceTools {


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /*
     1. Lat1 Lung1 表示A点经纬度，Lat2 Lung2 表示B点经纬度；
     2. a=Lat1 – Lat2 为两点纬度之差  b=Lung1 -Lung2 为两点经度之差；
     3. 6378.137为地球半径，单位为千米；
     */
    public static double getKmByPoint(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        return s;

    }


    public static double getMByPoint(double lat1, double lng1, double lat2, double lng2) {

        return getKmByPoint(lat1, lng1, lat2, lng2) * 1000;

    }

    public static void main(String[] args) {

        DecimalFormat df = new DecimalFormat("######0.00");
        Double distanceKm = DistanceTools.getKmByPoint(30.182027,120.195437,30.228522,119.992334);
        String distance = df.format(distanceKm)+"km";
//        getKmByPoint(35.166,133.401,35.142,133.396);
//        System.out.println(getMByPoint(35.166,133.401,35.142,133.396));
//        System.out.println(getKmByPoint(30.182027,120.195437,30.228522,119.992334));
        System.out.println(distance);
    }

}
