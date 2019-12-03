package com.easymi.component.trace;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */

public class DistanceHelper {

    private static final int MAX_ACCURACY = 200;

    private TraceLocation lastBean;

    public DistanceHelper() {

    }

    public List<TraceLocation> legalTraces = new ArrayList<>();

    public void filterPos(TraceLocation posBean) {
        if (legalTraces.size() != 0) {
            lastBean = legalTraces.get(legalTraces.size() - 1);
        }
        if (lastBean == null) {
            trigonometricAdjust(posBean);
        } else {
            if (filter(lastBean, posBean)) {
                trigonometricAdjust(posBean);
            }
        }
    }

    private boolean isNetLoc(TraceLocation location) {
        return location.getLocType().equals("network");
    }

    private TraceLocation awaitingTrialLoc;//等待审判的点

    /**
     * 三角函数判断是否通过
     *
     * @return
     */
    private void trigonometricAdjust(TraceLocation currentLocation) {
        boolean awaitingTrialLocBeNull = true;
        if (null != lastBean) {
            if (lastBean.getLocType().equals("gps") && isNetLoc(currentLocation)) {//如果上个点是gps并且这个点是network，这个点将被审判
                awaitingTrialLoc = currentLocation;
                awaitingTrialLocBeNull = false;
            } else if (awaitingTrialLoc != null && !isNetLoc(currentLocation)) { //如果待审判不为空，并且这个点是gps，开始审判
                if (isJustice(lastBean, awaitingTrialLoc, currentLocation)) {
                    legalTraces.add(awaitingTrialLoc);
                    legalTraces.add(currentLocation);
                } else {
                    legalTraces.add(currentLocation);
                }
            } else if (awaitingTrialLoc != null && isNetLoc(currentLocation)) { //如果待审判不为空，并且这个点不是gps，那么还是都要插入到数据库区
                legalTraces.add(awaitingTrialLoc);
                legalTraces.add(currentLocation);
            } else {
                legalTraces.add(currentLocation);
            }
        } else {
            legalTraces.add(currentLocation);
        }
        if (awaitingTrialLocBeNull) {
            awaitingTrialLoc = null;
        }
    }

    private boolean isJustice(TraceLocation locA, TraceLocation locB, TraceLocation locC) {

        LatLng a = new LatLng(locA.getLat(), locA.getLng());
        LatLng b = new LatLng(locB.getLat(), locB.getLng());
        LatLng c = new LatLng(locC.getLat(), locC.getLng());

        double ab = getDistance(a, b);
        double bc = getDistance(b, c);
        double ac = getDistance(a, c);

        double cosABC = (Math.pow(ab, 2) + Math.pow(bc, 2) - Math.pow(ac, 2)) / (2 * ab * bc);//角ABC的cos值

        double angle = Math.acos(cosABC) * 180 / Math.PI;

        if (angle >= 90 && angle <= 180) {
            return true;
        } else {
            return false;
        }
    }

    private boolean filter(TraceLocation lastBean, TraceLocation posBean) {
        if (isLocationValid(posBean)) {
            if (isAccuracyValid(posBean)) {
                if (null == lastBean) {
                    return true;
                } else {
                    if (isSameLoc(lastBean, posBean)) {
                        return false;
                    } else if (posBean.getLocType().equals("gps")) {
                        if (isTooFar(lastBean, posBean)) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (isTooFar(lastBean, posBean)) {
                            return false;
                        } else {
                            if (isTimeValid(lastBean, posBean)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    private boolean isLocationValid(TraceLocation posBean) {
        if (posBean.getLat() <= 90 && posBean.getLng() <= 180) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAccuracyValid(TraceLocation posBean) {
        return posBean.getBearing() < MAX_ACCURACY;
    }

    private boolean isSameLoc(TraceLocation lastLoc, TraceLocation posBean) {
        double mileage = getDistance(new LatLng(lastLoc.getLat(), lastLoc.getLng()), new LatLng(posBean.getLat(), posBean.getLng()));
        if (mileage > 5) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isTooFar(TraceLocation lastLoc, TraceLocation posBean) {
        long time = (posBean.getTime() - lastLoc.getTime()) / 1000;//秒
        double mileage = getDistance(new LatLng(lastLoc.getLat(), lastLoc.getLng()), new LatLng(posBean.getLat(), posBean.getLng()));//米
        double max = 120 * 1000 / (60 * 60);
        double current = mileage / time;
        if (current > max) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTimeValid(TraceLocation lastLoc, TraceLocation posBean) {
        if (posBean.getTime() < lastLoc.getTime()) {
            return false;
        } else {
            return true;
        }
    }

    private double getDistance(LatLng last, LatLng current) {
        return AMapUtils.calculateLineDistance(last, current);
    }
}
