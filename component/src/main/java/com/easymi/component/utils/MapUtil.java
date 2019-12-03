package com.easymi.component.utils;

import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.services.core.LatLonPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *@Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class MapUtil {
    public static LatLngBounds getBounds(List<LatLng> latLngs, LatLng center) {

        if (null != latLngs && latLngs.size() > 0) {

            LatLng param = latLngs.get(0);

            double maxLat = param.latitude;
            double minLat = param.latitude;
            double maxLng = param.longitude;
            double minLng = param.longitude;


            for (LatLng driver : latLngs) {
                if (driver.latitude > maxLat) {
                    maxLat = driver.latitude;
                }
                if (driver.latitude < minLat) {
                    minLat = driver.latitude;
                }

                if (driver.longitude > maxLng) {
                    maxLng = driver.longitude;
                }
                if (driver.longitude < minLng) {
                    minLng = driver.longitude;
                }
            }

            double maxDiffLat = Math.max(Math.abs(maxLat - center.latitude), (minLat - center.latitude));
            double maxDiffLng = Math.max(Math.abs(maxLng - center.longitude), (minLng - center.longitude));

            //左上
            LatLng leftTopLatLng = new LatLng(center.latitude - maxDiffLat, center.longitude + maxDiffLng);
            //右下
            LatLng rightBottomLatLng = new LatLng(center.latitude + maxDiffLat, center.longitude - maxDiffLng);

            return new LatLngBounds.Builder().include(leftTopLatLng).include(rightBottomLatLng).build();

        } else {
            return new LatLngBounds.Builder().include(center).build();
        }
    }

    public static LatLngBounds getBounds(List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        return builder.build();
    }

    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

}
