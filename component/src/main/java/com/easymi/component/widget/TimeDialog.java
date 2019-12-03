package com.easymi.component.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.easymi.component.R;
import com.easymi.component.widget.wheelview.OnWheelChangedListener;
import com.easymi.component.widget.wheelview.WheelView;
import com.easymi.component.widget.wheelview.adapter.AbstractWheelAdapter;
import com.easymi.component.widget.wheelview.adapter.AbstractWheelTextAdapter;
import com.easymi.component.widget.wheelview.adapter.WheelViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xyin on 2017/12/24.
 * 选择时间.
 */

public class TimeDialog extends BottomSheetDialog implements OnWheelChangedListener {

    private static final int PASS_DAY = 3;
    private static final int PRE_MINUTE = 10;  //提前预约的分钟数
    private long startMillis;   //可选的起始毫秒数,毫秒位为0

    private WheelView dayWheelView;
    private WheelView hourWheelView;
    private WheelView minuteWheelView;

    private List<String> dayList = new ArrayList<>();
    private List<String> saveDays = new ArrayList<>();
    private List<String> hourList = new ArrayList<>();
    private List<String> minuteList = new ArrayList<>();

    private OnTimeSelectListener listener;

    public TimeDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.lib_layout_time, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> commit());
        dayWheelView = view.findViewById(R.id.day);
        hourWheelView = view.findViewById(R.id.hour);
        minuteWheelView = view.findViewById(R.id.minute);

        dayWheelView.addChangingListener(this);
        hourWheelView.addChangingListener(this);

        initTime();

        initWheelView(dayWheelView, dayList);
        initWheelView(hourWheelView, hourList);
        initWheelView(minuteWheelView, minuteList);

        setCancelable(false);

        setContentView(view);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);  //抹掉毫秒

        int minute = calendar.get(Calendar.MINUTE); //当前分钟数
        int afterMinute = minute + PRE_MINUTE;  //预约后的分钟数
        int offsetMinute = 0;
        if (afterMinute > 50 && afterMinute < 60) {
            offsetMinute = 60 - afterMinute;  //预约后的时间落在(50,60)区间需要补到60位置
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() + (offsetMinute + PRE_MINUTE) * 60 * 1000);
        startMillis = calendar.getTimeInMillis();

        //初始化天数
        dayList.clear();
        dayList.add("现在");
        saveDays.add("占位");

        int dayOfMonth; //获取日期在本月的第几天

        for (int i = 0; i < PASS_DAY; i++) {
            //修改成今天，明天，后天
//            String week = getWeekDay(calendar);
//            dayList.add(dateFormat(calendar.getTimeInMillis(), "MM月dd日 ") + week);
            saveDays.add(dateFormat(calendar.getTimeInMillis(), "yyyy-MM-dd"));

            //往后加一天
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
        }

        dayList.add("今天");
        dayList.add("明天");
        dayList.add("后天");


        hourList.clear();
        hourList.add("----");

        minuteList.clear();
        minuteList.add("----");

    }


    /**
     * 更新小时数据.
     *
     * @param all 是否不用限制.
     */
    private void updateHour(boolean all) {
        hourList.clear();
        if (all) {
            for (int i = 0; i < 24; i++) {
                hourList.add(i + "点");
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startMillis);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            for (int i = hour; i < 24; i++) {
                hourList.add(i + "点");
            }
        }
    }

    private void updateMinute(boolean all) {
        minuteList.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startMillis);
        int minute = calendar.get(Calendar.MINUTE);

        if (minute == 0 || all) {
            for (int i = 0; i < 6; i++) {
                minuteList.add(i * 10 + "分");
            }
        } else {
            int start = (minute + 9) / 10;
            for (int i = start; i < 6; i++) {
                minuteList.add(i * 10 + "分");
            }
        }
    }

    private void initWheelView(WheelView wheelView, List<String> datas) {
        wheelView.setCyclic(false);
        wheelView.setCurrentItem(0);
        TimeWheelAdapter adapter = new TimeWheelAdapter(wheelView.getContext(), datas);
        adapter.setItemResource(R.layout.lib_item_wheel_content);
        adapter.setItemTextResource(R.id.tvContent);
        wheelView.setViewAdapter(adapter);
    }

    private String dateFormat(long time, String pattern) {
        SimpleDateFormat mFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(time);
        return mFormat.format(date);
    }

    /**
     * 获取一个时间戳是星期几.
     *
     * @param calendar calendar
     * @return 该时间为星期几
     */
    private String getWeekDay(Calendar calendar) {
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

    private void commit() {
        int dayIndex = dayWheelView.getCurrentItem();
        int hourIndex = hourWheelView.getCurrentItem();
        int minuteIndex = minuteWheelView.getCurrentItem();

        long t = -1;
        String hour = "";
        String minute = "";
        if (dayIndex != 0) {
            hour = hourList.get(hourIndex);
            minute = minuteList.get(minuteIndex);
            String time = saveDays.get(dayIndex) + " " + hour + minute;
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH点mm分", Locale.getDefault());
            try {
                Date data = mFormat.parse(time);
                t = data.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (listener != null) {
            listener.onTimeSelect(t, dayList.get(dayIndex) + hour + "" + minute);
        }

        dismiss();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        int id = wheel.getId();
        if (id == R.id.day) {
            setHourView(oldValue, newValue);
            setMinuteView();
        } else if (id == R.id.hour) {
            setMinuteView();
        }
    }

    private void setHourView(int oldValue, int newValue) {
        int hourIndex = hourWheelView.getCurrentItem();
        String lastHour = hourList.get(hourIndex);  //滑动前的值
        if (newValue == 0) {
            hourList.clear();
            hourList.add("----");
        } else if (newValue == 1) {
            updateHour(false);
        } else if (oldValue == 1 && newValue == 2) {
            updateHour(true);
        } else {
            return;
        }
        WheelViewAdapter adapter = hourWheelView.getViewAdapter();
        if (adapter != null && adapter instanceof AbstractWheelAdapter) {
            ((AbstractWheelAdapter) adapter).notifyDataInvalidatedEvent();
            int index = hourList.indexOf(lastHour);
            index = index < 0 ? 0 : index;
            hourWheelView.setCurrentItem(index);
        }
    }

    private void setMinuteView() {
        int dayIndex = dayWheelView.getCurrentItem();
        int hourIndex = hourWheelView.getCurrentItem();
        String lastMinute = minuteList.get(minuteWheelView.getCurrentItem());

        if (dayIndex == 0) {
            minuteList.clear();
            minuteList.add("----");
        } else if (dayIndex == 1 && hourIndex == 0) {
            updateMinute(false);
        } else {
            updateMinute(true);
        }

        WheelViewAdapter adapter = minuteWheelView.getViewAdapter();
        if (adapter != null && adapter instanceof AbstractWheelAdapter) {
            ((AbstractWheelAdapter) adapter).notifyDataInvalidatedEvent();
            int index = minuteList.indexOf(lastMinute);
            index = index < 0 ? 0 : index;
            minuteWheelView.setCurrentItem(index);
        }

    }

    public interface OnTimeSelectListener {
        /**
         * 选中时间的时间戳,当选中现在或者非法值时该值为-1
         *
         * @param time 选中时间的时间戳
         */
        void onTimeSelect(long time, String timeStr);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener listener) {
        this.listener = listener;
    }

    /**
     * 时间文本.
     */
    private class TimeWheelAdapter extends AbstractWheelTextAdapter {

        private List<String> datas;

        TimeWheelAdapter(Context context, List<String> datas) {
            super(context);
            this.datas = datas;
        }

        @Override
        public int getItemsCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return datas == null ? null : datas.get(index);
        }

    }


}
