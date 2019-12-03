package com.easymi.component.trace;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.easymi.component.Config;
import com.easymi.component.DJOrderStatus;
import com.easymi.component.entity.DymOrder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *@Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */
public class TraceUtil {

    private boolean isFirst = true;//是否首次进入

    private List<TraceLocation> w1TempList = new ArrayList<>();
    private List<TraceLocation> w2TempList = new ArrayList<>();
    private List<TraceLocation> mListPoint = new ArrayList<>();

    private TraceLocation weight1 = new TraceLocation();
    private TraceLocation weight2;

    private final long CAR_MAX_SPEED = 22;

    private int w1Count = 0;


    /**
     * @param aMapLocation
     * @return 是否获得有效点，需要存储和计算距离
     */
    private Boolean filterPos(TraceLocation aMapLocation) {

        try {
            // 获取的第一个定位点不进行过滤
            if (isFirst) {
                isFirst = false;
                weight1.setLat(aMapLocation.getLat());
                weight1.setLng(aMapLocation.getLng());
                weight1.setTime(aMapLocation.getTime());

                // 将得到的第一个点存储入w1的缓存集合
                final TraceLocation traceLocation = new TraceLocation();
                traceLocation.setLat(aMapLocation.getLat());
                traceLocation.setLng(aMapLocation.getLng());
                traceLocation.setTime(aMapLocation.getTime());
                w1TempList.add(traceLocation);
                w1Count++;

                return true;

            } else {
//                // 过滤静止时的偏点，在静止时速度小于1米就算做静止状态
                if (aMapLocation.getSpeed() < 1) {
                    return false;
                }

                if (weight2 == null) {

                    // 计算w1与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils = aMapLocation.getTime() - weight1.getTime();
                    long offsetTimes = (offsetTimeMils / 1000);
                    long MaxDistance = offsetTimes * CAR_MAX_SPEED;
                    float distance = AMapUtils.calculateLineDistance(
                            new LatLng(weight1.getLat(), weight1.getLng()),
                            new LatLng(aMapLocation.getLat(), aMapLocation.getLng()));

                    if (distance > MaxDistance) {
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = new TraceLocation();
                        weight2.setLat(aMapLocation.getLat());
                        weight2.setLng(aMapLocation.getLng());
                        weight2.setTime(aMapLocation.getTime());

                        w2TempList.add(weight2);

                        return false;

                    } else {
                        // 将p1加入到做坐标集合w1TempList
                        TraceLocation traceLocation = new TraceLocation();
                        traceLocation.setLat(aMapLocation.getLat());
                        traceLocation.setLat(aMapLocation.getLng());
                        traceLocation.setTime(aMapLocation.getTime());
                        w1TempList.add(traceLocation);
                        w1Count++;

                        // 更新w1权值点
                        weight1.setLat(weight1.getLat() * 0.2 + aMapLocation.getLat() * 0.8);
                        weight1.setLng(weight1.getLng() * 0.2 + aMapLocation.getLng() * 0.8);
                        weight1.setTime(aMapLocation.getTime());
                        weight1.setSpeed(aMapLocation.getSpeed());

                        if (w1TempList.size() > 3) {
                            // 将w1TempList中的数据放入finalList，并将w1TempList清空
                            mListPoint.addAll(w1TempList);
                            w1TempList.clear();
                            return true;
                        } else {
                            return false;
                        }
                    }

                } else {

                    // 计算w2与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils = aMapLocation.getTime() - weight2.getTime();
                    long offsetTimes = (offsetTimeMils / 1000);
                    long MaxDistance = offsetTimes * 16;
                    float distance = AMapUtils.calculateLineDistance(
                            new LatLng(weight2.getLat(), weight2.getLng()),
                            new LatLng(aMapLocation.getLat(), aMapLocation.getLng()));

                    if (distance > MaxDistance) {
                        w2TempList.clear();
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = new TraceLocation();
                        weight2.setLat(aMapLocation.getLat());
                        weight2.setLng(aMapLocation.getLng());
                        weight2.setTime(aMapLocation.getTime());

                        w2TempList.add(weight2);

                        return false;
                    } else {
                        // 将p1加入到做坐标集合w2TempList
                        TraceLocation traceLocation = new TraceLocation();
                        traceLocation.setLat(aMapLocation.getLat());
                        traceLocation.setLng(aMapLocation.getLng());
                        traceLocation.setTime(aMapLocation.getTime());
                        w2TempList.add(traceLocation);

                        // 更新w2权值点
                        weight2.setLat(weight2.getLat() * 0.2 + aMapLocation.getLat() * 0.8);
                        weight2.setLng(weight2.getLng() * 0.2 + aMapLocation.getLng() * 0.8);
                        weight2.setTime(aMapLocation.getTime());
                        weight2.setSpeed(aMapLocation.getSpeed());

                        if (w2TempList.size() > 4) {
                            // 判断w1所代表的定位点数是否>4,小于说明w1之前的点为从一开始就有偏移的点
                            if (w1Count > 4) {
                                mListPoint.addAll(w1TempList);
                            } else {
                                w1TempList.clear();
                            }

                            // 将w2TempList集合中数据放入finalList中
                            mListPoint.addAll(w2TempList);

                            // 1、清空w2TempList集合 2、更新w1的权值点为w2的值 3、将w2置为null
                            w2TempList.clear();
                            weight1 = weight2;
                            weight2 = null;
                            return true;

                        } else {
                            return false;
                        }
                    }
                }
            }
        } finally {
//            FileWriteUtil.getInstance().save("tutu_driver_filter.txt", filterString);
//            Log.d("hhh","finnaly");
        }


    }

    static String dataSource = "{'waitDistance':0.0,'firstLat':39.376399,'success':true,'runDistance':4.2,'firstLng':117.044386,'orderLocations':[{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.044386,'latitude':39.376399,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514972948000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:08','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.044412,'latitude':39.376398,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514972956000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:16','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.044811,'latitude':39.376534,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514972964000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:24','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.045211,'latitude':39.376727,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514972972000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:32','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.045476,'latitude':39.377002,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514972980000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:40','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.045654,'latitude':39.377338,'radius':7.0,'direction':-1.0,'locType':'gps','locDate':1514972988000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:48','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.045761,'latitude':39.377524,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514972996000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:49:56','locDateStr':'01/03 17:49','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.045868,'latitude':39.377726,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973004000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:04','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046045,'latitude':39.378044,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514973012000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:12','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046191,'latitude':39.378263,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514973020000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:20','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046243,'latitude':39.378379,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973028000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:28','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046274,'latitude':39.378435,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973036000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:36','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046612,'latitude':39.378314,'radius':73.0,'direction':-1.0,'locType':'network','locDate':1514973040000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:40','locDateStr':'01/03 17:50','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.046646,'latitude':39.37836,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973044000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:44','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047005,'latitude':39.37825,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973052000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:50:52','locDateStr':'01/03 17:50','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047223,'latitude':39.378168,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973060000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:00','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047235,'latitude':39.378157,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973068000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:08','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047232,'latitude':39.37815,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973076000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:16','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047455,'latitude':39.378073,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973084000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:24','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.047936,'latitude':39.378109,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973092000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:32','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.04824,'latitude':39.378595,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973100000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:40','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.048584,'latitude':39.379244,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973108000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:48','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.048858,'latitude':39.379741,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973116000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:51:56','locDateStr':'01/03 17:51','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.048443,'latitude':39.378955,'radius':47.0,'direction':-1.0,'locType':'network','locDate':1514973124000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:04','locDateStr':'01/03 17:52','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050039,'latitude':39.381871,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973140000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:20','locDateStr':'01/03 17:52','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050359,'latitude':39.382473,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973148000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:28','locDateStr':'01/03 17:52','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050697,'latitude':39.383079,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973156000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:36','locDateStr':'01/03 17:52','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050708,'latitude':39.383117,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973164000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:44','locDateStr':'01/03 17:52','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050706,'latitude':39.383117,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973172000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:52:52','locDateStr':'01/03 17:52','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050704,'latitude':39.383117,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973180000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:00','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050734,'latitude':39.383161,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973188000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:08','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050947,'latitude':39.38357,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973196000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:16','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05133,'latitude':39.384262,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973204000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:24','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.050908,'latitude':39.383665,'radius':37.0,'direction':-1.0,'locType':'network','locDate':1514973212000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:32','locDateStr':'01/03 17:53','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05261,'latitude':39.386617,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973228000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:48','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05298,'latitude':39.387263,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973236000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:53:56','locDateStr':'01/03 17:53','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053104,'latitude':39.387509,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973244000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:04','locDateStr':'01/03 17:54','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05312,'latitude':39.387496,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973252000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:12','locDateStr':'01/03 17:54','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053121,'latitude':39.387496,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973276000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:36','locDateStr':'01/03 17:54','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05312,'latitude':39.387496,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973284000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:44','locDateStr':'01/03 17:54','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053144,'latitude':39.387555,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973292000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:52','locDateStr':'01/03 17:54','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053575,'latitude':39.388119,'radius':63.0,'direction':-1.0,'locType':'network','locDate':1514973299000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:54:59','locDateStr':'01/03 17:54','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053334,'latitude':39.387892,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973300000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:00','locDateStr':'01/03 17:55','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05364,'latitude':39.388464,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973308000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:08','locDateStr':'01/03 17:55','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.053998,'latitude':39.389143,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973316000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:16','locDateStr':'01/03 17:55','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.054029,'latitude':39.389103,'radius':96.0,'direction':-1.0,'locType':'network','locDate':1514973324000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:24','locDateStr':'01/03 17:55','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.05468,'latitude':39.390348,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973332000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:32','locDateStr':'01/03 17:55','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.054983,'latitude':39.390879,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973340000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:40','locDateStr':'01/03 17:55','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.054356,'latitude':39.389486,'radius':91.0,'direction':-1.0,'locType':'network','locDate':1514973347000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:55:47','locDateStr':'01/03 17:55','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056068,'latitude':39.392782,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973364000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:04','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056151,'latitude':39.3929,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973372000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:12','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056157,'latitude':39.392973,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514973380000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:20','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056333,'latitude':39.393313,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973388000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:28','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056441,'latitude':39.392964,'radius':30.0,'direction':-1.0,'locType':'network','locDate':1514973390000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:30','locDateStr':'01/03 17:56','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056647,'latitude':39.393843,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973396000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:36','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.056785,'latitude':39.393449,'radius':30.0,'direction':-1.0,'locType':'network','locDate':1514973401000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:41','locDateStr':'01/03 17:56','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.057434,'latitude':39.395231,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973412000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:56:52','locDateStr':'01/03 17:56','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.057868,'latitude':39.396014,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973420000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:00','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.058325,'latitude':39.396332,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973428000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:08','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.059102,'latitude':39.396069,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973436000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:16','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.059929,'latitude':39.395807,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973444000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:24','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.060704,'latitude':39.395541,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973452000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:32','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.061443,'latitude':39.395285,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973460000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:40','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.062101,'latitude':39.395093,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973468000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:48','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.062486,'latitude':39.394965,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973476000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:57:56','locDateStr':'01/03 17:57','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.062607,'latitude':39.394928,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973484000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:04','locDateStr':'01/03 17:58','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.062708,'latitude':39.39463,'radius':42.0,'direction':-1.0,'locType':'network','locDate':1514973492000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:12','locDateStr':'01/03 17:58','locTypeStr':'网络定位'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.063517,'latitude':39.394627,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973500000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:20','locDateStr':'01/03 17:58','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.064025,'latitude':39.394465,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973508000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:28','locDateStr':'01/03 17:58','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.064642,'latitude':39.394217,'radius':3.0,'direction':-1.0,'locType':'gps','locDate':1514973516000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:36','locDateStr':'01/03 17:58','locTypeStr':'GPS'},{'id':null,'created':null,'updated':null,'orderId':null,'serviceType':null,'longitude':117.064774,'latitude':39.394169,'radius':4.0,'direction':-1.0,'locType':'gps','locDate':1514973524000,'sortId':0,'run':1,'locDateSecondsStr':'01/03 17:58:44','locDateStr':'01/03 17:58','locTypeStr':'GPS'}]}";

}
