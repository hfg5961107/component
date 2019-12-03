package com.easymi.component.entity;

import android.location.Location;

import com.amap.api.location.AMapLocation;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午5:00
 * Description:
 * History:
 */
public class EmLoc {

    /**
     * 示例：
     * {
     * "altitude": 0,
     * "speed": 0,
     * "bearing": 0,
     * "citycode": "028",
     * "adcode": "510115",
     * "country": "中国",
     * "province": "四川省",
     * "city": "成都市",
     * "district": "温江区",
     * "road": "锦绣大道南段",
     * "street": "锦绣大道南段",
     * "number": "345号",
     * "poiname": "四川青年(大学生)创业示范园",
     * "errorCode": 0,
     * "errorInfo": "success",
     * "locationType": 2,
     * "locationDetail": "#csid:59466c348b714c6da8394002aad120d9",
     * "aoiname": "成都SBI创业街",
     * "address": "四川省成都市温江区锦绣大道南段345号靠近四川青年(大学生)创业示范园",
     * "poiid": "",
     * "floor": "",
     * "description": "在四川青年(大学生)创业示范园附近",
     * "time": 1510981974041,
     * "provider": "lbs",
     * "lon": 103.813832,
     * "lat": 30.668303,
     * "accuracy": 25,
     * "isOffset": true,
     * "isFixLastLocation": false
     * }
     */

    public double latitude;

    public double longitude;

    public double accuracy;

    public String province;
    public String city;
    public String district;

    public long locTime;

    public String cityCode;

    public String adCode;

    public String address;
    public String country;
    public String road;

    public String poiName;
    public String street;

    public String streetNum;

    public String aoiName;

    public String description;//

    public double altitude;
    public float speed;
    public float bearing;

    public String provider;

    public int locationType;//定位类型
    //    LOCATION_TYPE_GPS = 1;
//     LOCATION_TYPE_SAME_REQ = 2;
//    /** @deprecated */
//     LOCATION_TYPE_FAST = 3;
//    LOCATION_TYPE_FIX_CACHE = 4;
//    LOCATION_TYPE_WIFI = 5;
//    LOCATION_TYPE_CELL = 6;
//    LOCATION_TYPE_AMAP = 7;
//    LOCATION_TYPE_OFFLINE = 8;
//     LOCATION_TYPE_LAST_LOCATION_CACHE = 9;

    public boolean isOffset;//补偿
    public boolean isFixLastLocation;

    public int satellites;//卫星颗数

    public long sysTime;

    public boolean isOffline;

    public static EmLoc ALocToLoc(AMapLocation aLoc) {
        if (null == aLoc) {
            return null;
        }
        EmLoc locationInfo = new EmLoc();
        locationInfo.latitude = aLoc.getLatitude();
        locationInfo.longitude = aLoc.getLongitude();
        locationInfo.accuracy = aLoc.getAccuracy();
        locationInfo.province = aLoc.getProvince();
        locationInfo.city = aLoc.getCity();
        locationInfo.district = aLoc.getDistrict();
        locationInfo.locTime = aLoc.getTime();
        locationInfo.cityCode = aLoc.getCityCode();
        locationInfo.adCode = aLoc.getAdCode();
        locationInfo.address = aLoc.getAddress();
        locationInfo.country = aLoc.getCountry();
        locationInfo.road = aLoc.getRoad();
        locationInfo.poiName = aLoc.getPoiName();
        locationInfo.street = aLoc.getStreet();
        locationInfo.streetNum = aLoc.getStreetNum();
        locationInfo.aoiName = aLoc.getAoiName();
        locationInfo.description = aLoc.getDescription();
        locationInfo.altitude = aLoc.getAltitude();
        locationInfo.speed = aLoc.getSpeed();
        locationInfo.bearing = aLoc.getBearing();
        locationInfo.provider = aLoc.getProvider();
        locationInfo.locationType = aLoc.getLocationType();
        locationInfo.isOffset = aLoc.isOffset();
        locationInfo.isFixLastLocation = aLoc.isFixLastLocation();
        locationInfo.satellites = aLoc.getSatellites();
        locationInfo.sysTime = System.currentTimeMillis();
        locationInfo.isOffline = false;
        return locationInfo;
    }

    public static Location emLoc2Loc(EmLoc loc) {
        if (loc == null) {
            return null;
        }
        Location location = new Location("em");
        location.setLatitude(loc.latitude);
        location.setLongitude(loc.longitude);
        location.setAccuracy((float) loc.accuracy);
        location.setAltitude(loc.altitude);
        location.setBearing(loc.bearing);
        location.setTime(loc.locTime);
        location.setSpeed(loc.speed);
        return location;
    }

    @Override
    public String toString() {
        return "EmLoc{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", locTime=" + locTime +
                ", cityCode='" + cityCode + '\'' +
                ", adCode='" + adCode + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", road='" + road + '\'' +
                ", poiName='" + poiName + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", aoiName='" + aoiName + '\'' +
                ", description='" + description + '\'' +
                ", altitude=" + altitude +
                ", speed=" + speed +
                ", bearing=" + bearing +
                ", provider='" + provider + '\'' +
                ", locationType=" + locationType +
                ", isOffset=" + isOffset +
                ", isFixLastLocation=" + isFixLastLocation +
                ", satellites=" + satellites +
                ", isOffline=" + isOffline +
                '}';
    }
}
