package com.easymi.component.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TimeUtil {
    public static final String HM = "HH:mm";
    public static final String HMS = "HH:mm:ss";
    public static final String MD_HM = "MM-dd HH:mm";
    public static final String MD_HMS = "MM-dd HH:mm:ss";
    static final int NEXT_CLOCK = 0x7;
    static final int NEXT_MINUTE = 0x0;
    public static final String SERVER_FORMAT = "yyyyMMddHHmmss";
    public static final String YM = "yyyy-MM";
    public static final String YM2 = "yyyyMM";
    public static final String YMD = "yyyyMMdd";
    public static final String YMD_2 = "yyyy-MM-dd";
    public static final String YMD_3 = "yyyy_MM_dd";
    public static final String YMD_4_CN = "yyyy年MM月dd日";
    public static final String YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HM_LOG = "yyyyMMdd_HHmm";

    @SuppressLint("SimpleDateFormat")
    public static long parseTime(String format, String time) {
        Date date = null;
        try {
            if (time != null) {

                date = new SimpleDateFormat(format).parse(time);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? date.getTime() : System.currentTimeMillis();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime(String format, long time) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    public static boolean isExpired(long savedExprieTime) {
        if (savedExprieTime == 0x0) {
            return true;
        }
        long curTime = parseTime("yyyyMMdd", getTime("yyyyMMdd", System.currentTimeMillis()));
        if (savedExprieTime <= curTime) {
            return false;
        }
        return true;
    }

    public static boolean isExpired(String checkTime) {
        if (TextUtils.isEmpty(checkTime)) {
            return true;
        }
        long time = parseTime("yyyyMMdd", checkTime);
        String now = getTime("yyyyMMdd", System.currentTimeMillis());
        long curTime = parseTime("yyyyMMdd", now);
        if (time <= curTime) {
            return false;
        }
        return true;
    }

    public static String getTimeSpan(int seconds) {

        StringBuilder buffer = new StringBuilder();

        int min = (seconds / 0x3c) % 0x3c;
        int hours = ((seconds / 0x3c) / 0x3c) % 0x3c;
        int day = (((seconds / 0x3c) / 0x3c) / 0x18) % 0x18;
        if (day > 0) {
            buffer.append(day + "\u5929");
        }
        if (hours > 0) {
            buffer.append(hours + "\u5c0f\u65f6");
        }
        if (min > 0) {
            buffer.append(min + "\u5206\u949f");
        }
        return buffer.toString();
    }

    public static String getTimeFromMinutes(int minutes) {

        StringBuilder buffer = new StringBuilder();

        int min = minutes % 0x3c;
        int hours = (minutes / 0x3c) % 0x3c;
        if (hours > 0) {
            buffer.append(hours + "\u5c0f\u65f6");
        }
        if (min >= 0) {
            buffer.append(min + "\u5206\u949f");
        }

        return buffer.toString();
    }

    public static String getTimeSpanSeconds(int seconds) {

        StringBuilder buffer = new StringBuilder();

        int sec = seconds % 0x3c;
        int min = (seconds / 0x3c) % 0x3c;
        int hours = ((seconds / 0x3c) / 0x3c) % 0x3c;
        int day = (((seconds / 0x3c) / 0x3c) / 0x18) % 0x18;
        if (day > 0) {
            buffer.append(day + "D");
        }
        if (hours > 0) {
            buffer.append(hours + "h");
        }
        if (min > 0) {
            buffer.append(min + "m");
        }
        if (sec >= 0) {
            buffer.append(sec + "s");
        }
        return buffer.toString();
    }

    public static String getTimeSpanMinutes(int seconds) {

        StringBuilder buffer = new StringBuilder();

        int min = (seconds / 0x3c) % 0x3c;
        int hours = ((seconds / 0x3c) / 0x3c) % 0x3c;
        int day = (((seconds / 0x3c) / 0x3c) / 0x18) % 0x18;
        if (day > 0) {
            buffer.append(day + "D");
        }
        if (hours > 0) {
            buffer.append(hours + "h");
        }
        if (min >= 0) {
            buffer.append(min + "m");
        }
        return buffer.toString();
    }

    public static long getTime(String date, int hour, int minute) {
        return parseTime("yyyy-MM-dd HH:mm", date + " " + String.format("%1$,02d:%2$,02d", hour, minute));
    }

    public static boolean isHourValid(String hour) {
        if (null == hour) {
            return false;
        }
        try {
            Integer h = Integer.parseInt(hour);
            if (h < 0 || h > 23) {
                return false;
            }

            return true;
        } catch (Exception e) {

        }

        return false;
    }

    public static boolean isMinuteValid(String minute) {
        if (null == minute) {
            return false;
        }
        try {
            Integer m = Integer.parseInt(minute);
            if (m < 0 || m > 59) {
                return false;
            }

            return true;
        } catch (Exception e) {

        }

        return false;
    }

    @SuppressLint("DefaultLocale")
    public static float convertMinutesToHours(float minutes) {
        float hours = minutes / 60.0f;
        try {
            return Float.parseFloat(String.format("%.1f", Float.valueOf(hours)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0x0;
    }

    public static Calendar getNextSevenHourCalendar() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(0xb);
        if (hourOfDay >= 0x7) {
            calendar.set(0x6, (calendar.get(0x6) + 0x1));
        }
        calendar.set(0xb, 0x7);
        calendar.set(0xc, 0x0);
        calendar.set(0xd, 0x0);
        return calendar;
    }

    public static Calendar getPreviewSevenHourCalendar() {
        Calendar sevenClockCal = Calendar.getInstance();
        int hourOfDay = sevenClockCal.get(0xb);
        if (hourOfDay < 0x7) {
            sevenClockCal.set(0x6, (sevenClockCal.get(0x6) - 0x1));
        }
        sevenClockCal.set(0xb, 0x7);
        sevenClockCal.set(0xc, 0x0);
        sevenClockCal.set(0xd, 0x0);
        return sevenClockCal;
    }

    public static long getNextSevenHourOfDayDelayMillis() {
        Calendar calendar = getNextSevenHourCalendar();
        return (calendar.getTimeInMillis() - System.currentTimeMillis());
    }

    public static String convertMillisTime(long millisSeconds) {
        return convertSecondsTime((millisSeconds / 0x3e8));
    }

    @SuppressLint("DefaultLocale")
    public static String convertSecondsTime(long seconds) {
        int minute = (int) (seconds / 0x3c);
        int hour = minute / 0x3c;
        int second = (int) (seconds % 0x3c);
        minute = minute % 0x3c;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public static int converSeconds2Minutes(int seconds) {
        return (seconds / 0x3c);
    }

    public static int convertMinutes2Seconds(int minutes) {
        return (minutes * 0x3c);
    }

    public static String getTimeSpanMinuteSeconds(int seconds) {
        int sec = seconds % 0x3c;
        int min = seconds / 0x3c;
        StringBuffer buffer = new StringBuffer();
        buffer.append(min).append("\u5206").append(sec).append("\u79d2");
        return buffer.toString();
    }

    public static int convertSeconds2MinutesRound(int seconds) {
        int min = seconds / 0x3c;
        int sec = seconds % 0x3c;
        if (sec >= 0x1e) {
            min = min + 0x1;
        }
        return min;
    }

    public static String now() {
        return String.valueOf(System.currentTimeMillis());
    }

    //获取当天的开始时间
    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    //获取当天的结束时间
    public static java.util.Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    //获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    //获取昨天的结束时间
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    //获取明天的开始时间
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }
    //获取明天的结束时间
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }
    //获取本周的结束时间
    public static Date getEndDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }
    //获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }
    //获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }
    //获取本年的开始时间
    public static java.util.Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }
    //获取本年的结束时间
    public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }
    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }
    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }
    //两个日期相减得到的天数
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }
    //两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }
    //获取两个日期中的最大日期
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }
    //获取两个日期中的最小日期
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }
    //返回某月该季度的第一个月
    public static Date getFirstSeasonDate(Date date) {
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }
    //返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }
    //返回某个日期前几天的日期
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }
    //获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));

            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }

                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }

    /**
     * 获取一个时间戳是星期几.
     *
     * @param calendar calendar
     * @return 该时间为星期几
     */
    @SuppressLint("SwitchIntDef")
    public static String getWeekDay(Calendar calendar) {
        String dayStr = null;
        int wd = calendar.get(Calendar.DAY_OF_WEEK);
        switch (wd) {
            case Calendar.MONDAY:
                dayStr = "周一";
                break;
            case Calendar.TUESDAY:
                dayStr = "周二";
                break;
            case Calendar.WEDNESDAY:
                dayStr = "周三";
                break;
            case Calendar.THURSDAY:
                dayStr = "周四";
                break;
            case Calendar.FRIDAY:
                dayStr = "周五";
                break;
            case Calendar.SATURDAY:
                dayStr = "周六";
                break;
            case Calendar.SUNDAY:
                dayStr = "周日";
                break;
            default:
                break;
        }
        return dayStr;
    }

    //获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }
}