package com.easymi.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.easymi.component.R;
import com.easymi.component.utils.Log;
import com.easymi.component.utils.TimeUtil;

import java.util.Arrays;
import java.util.Calendar;

public class TimePickerView extends LinearLayout {

    private Context context;
    private String positiveButtonText;
    private String negativeButtonText;
    private OnClickListener positiveButtonClickListener;
    private OnClickListener negativeButtonClickListener;

    private NumberPicker dayPicker;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private TextView posText;
    private TextView nevText;
    private String[] days;

    private String[] hour = new String[25];
    private String[] minute = new String[6];

    Calendar c = Calendar.getInstance();
    private int testHour = c.get(Calendar.HOUR_OF_DAY);
    private int testMinute = c.get(Calendar.MINUTE);

    private String dayStr;
    private String hourStr;
    private String minStr = "";

    private String shi_str;

    public TimePickerView(Context context) {
        this(context, null);
    }

    public TimePickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public TimePickerView setPositiveButton(String positiveButtonText,
                                            OnClickListener listener) {
        this.positiveButtonText = positiveButtonText;
        this.positiveButtonClickListener = listener;
        if (positiveButtonText != null && positiveButtonClickListener != null) {

            posText.setOnClickListener(v -> {
                dayStr = dayPicker.getDisplayedValues()[dayPicker.getValue()];
                hourStr = hourPicker.getDisplayedValues()[hourPicker.getValue()];
                if (minutePicker.getVisibility() == View.VISIBLE && minutePicker.getDisplayedValues() != null) {
                    minStr = minutePicker.getDisplayedValues()[minutePicker.getValue()];
                } else {
                    minStr = "";
                }
                positiveButtonClickListener.onClick(posText);
            });
        } else {
            posText.findViewById(R.id.positiveButton).setVisibility(
                    View.GONE);
        }
        return this;
    }

    public TimePickerView setNegativeButton(String negativeButtonText,
                                            OnClickListener listener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeButtonClickListener = listener;

        if (negativeButtonText != null && negativeButtonClickListener != null) {

            nevText.setOnClickListener(v -> negativeButtonClickListener.onClick(nevText));
        } else {
            nevText.findViewById(R.id.negativeButton).setVisibility(
                    View.GONE);
        }
        return this;
    }

    public void init() {

        shi_str = context.getString(R.string.shi);
        days = new String[]{context.getString(R.string.today),
                context.getString(R.string.tomorrow),
                context.getString(R.string.houtian)};

        dayStr = context.getString(R.string.today);
        hourStr = context.getString(R.string.now);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_dialog, this);
        dayPicker = view.findViewById(R.id.day_picker);
        hourPicker = view.findViewById(R.id.hour_picker);
        minutePicker = view.findViewById(R.id.minute_picker);
        posText = view.findViewById(R.id.positiveButton);
        nevText = view.findViewById(R.id.negativeButton);

        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        for (int i = 1; i < 25; i++) {
            hour[i] = (i - 1) + shi_str;
        }
        hour[0] = "开始";
        for (int i = 0; i < 6; i++) {
            minute[i] = i + "0";
        }
        initDay();
        initHour(true);
        initMinute();

        // set the confirm button
    }

    private void initMinute() {

        int lastValue = minutePicker.getValue();

        if (hourPicker.getValue() == 0 && dayPicker.getValue() == 0) {
            //现在
            minutePicker.setVisibility(View.INVISIBLE);
            minutePicker.setValue(0);
            return;
        }

        minutePicker.setVisibility(View.VISIBLE);

        if (hourPicker.getValue() == 1 && dayPicker.getValue() == 0) {
            //由非当前小时回到当前小时
            if ((testMinute + 10) / 10 > 5) {
//                minutePicker.setVisibility(View.INVISIBLE);
                minutePicker.setDisplayedValues(null);
                String[] m = new String[]{"00", "10", "20", "30", "40", "50"};
                minutePicker.setDisplayedValues(m);
                minutePicker.setMaxValue(m.length - 1);
                minutePicker.setWrapSelectorWheel(false);
                minutePicker.setMinValue(0);
                return;
            }
            minutePicker.setMaxValue(0);
            String[] strs = Arrays.copyOfRange(minute, (testMinute + 10) / 10, minute.length);
            minutePicker.setDisplayedValues(strs);
            minutePicker.setMaxValue(strs.length - 1);
            minutePicker.setWrapSelectorWheel(false);
            minutePicker.setMinValue(0);
            if (lastValue > (testMinute + 9) / 10) {
                minutePicker.setValue(lastValue - (testMinute + 10) / 10);
            } else {
                minutePicker.setValue(0);
            }
        } else {
            minutePicker.setVisibility(View.VISIBLE);
            minutePicker.setDisplayedValues(minute);
            minutePicker.setMaxValue(minute.length - 1);
            minutePicker.setWrapSelectorWheel(false);
            minutePicker.setMinValue(0);
            minutePicker.setValue(lastValue + (testMinute + 10) / 10);
        }
        minutePicker.scrollBy(0, 0);
    }

    private void initHour(boolean isFrist) {

        int lastValue = hourPicker.getValue();  //上一次的value值，正常选择的时间
        Log.i("TAG", "旧值：" + lastValue);

        if (dayPicker.getValue() == 0) {
            //初始状态，或者从明天，后天回到今天

            hourPicker.setDisplayedValues(null);    //当数据源长度变化时，清空原数据源，防溢出

            String[] strs = null;

            if (testMinute >= 50) {
                strs = Arrays.copyOfRange(hour, testHour + 1, hour.length);
            } else {
                strs = Arrays.copyOfRange(hour, testHour, hour.length);
            }


            strs[0] = context.getString(R.string.now); //添加字头

            hourPicker.setMaxValue(strs.length - 1);
            hourPicker.setWrapSelectorWheel(false);
            hourPicker.setDisplayedValues(strs);
            hourPicker.setMinValue(0);

            if (testHour == 0 && isFrist) {
                hourPicker.setValue(0);
            } else if (lastValue >= testHour) {
                hourPicker.setValue(lastValue - testHour + 1);
            } else {
                hourPicker.setValue(0);
            }

            if (hourPicker.getValue() == 0 && dayPicker.getValue() == 0) {
                minutePicker.setVisibility(View.INVISIBLE);
            } else {
                minutePicker.setVisibility(View.VISIBLE);
            }

        } else {
            //进入明天或者后天
            hourPicker.setDisplayedValues(null);
            String[] strs = Arrays.copyOfRange(hour, 1, hour.length);
            for (String str : strs)
                Log.i("TAG", str);
            hourPicker.setMaxValue(strs.length - 1);
            hourPicker.setWrapSelectorWheel(false);
            hourPicker.setDisplayedValues(strs);
            hourPicker.setMinValue(0);
            if (lastValue == 0 || lastValue == 1) {
                //上一次选中的是当前小时
                hourPicker.setValue(testHour);
            } else {
                hourPicker.setValue(lastValue - 1 + testHour);
            }


        }


        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (dayPicker.getValue() != 0) {
                    return;
                }

                if (oldVal == 0 || (oldVal == 1 && newVal == 2) || (oldVal == 2 && newVal == 1) || (oldVal == 1 && newVal == 0)) {
                    initMinute();
                }

            }
        });

    }

    private void initDay() {
        dayPicker.setDisplayedValues(days);
        dayPicker.setMaxValue(days.length - 1);
        dayPicker.setMinValue(0);
        dayPicker.setValue(0);

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (oldVal == 0 && newVal == 1) {
                    //从今天滑走
                    int temp = hourPicker.getValue();
                    initHour(false);
                    if (temp == 0 || temp == 1) {
                        initMinute();
                    }
                } else if (newVal == 0 && oldVal == 1) {
                    //滑回今天
                    initHour(false);
                    if (hourPicker.getValue() == 0 || hourPicker.getValue() == 1) {
                        initMinute();
                    }

                }

            }
        });

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal == 0 && newVal == 1) {
                    //从今天滑走
                    int temp = hourPicker.getValue();
                    initHour(false);
                    if (temp == 0 || temp == 1) {
                        initMinute();
                    }
                } else if (newVal == 0 && oldVal == 1) {
                    //滑回今天
                    initHour(false);
                    if (hourPicker.getValue() == 0 || hourPicker.getValue() == 1) {
                        initMinute();
                    }

                }

            }
        });

    }

    public String getDayStr() {
        return dayStr;
    }

    public String getHourStr() {
        return hourStr;
    }

    public String getMinStr() {
        return minStr;
    }

    public static long getTime(String day, String hour, String min) {
        if (hour.equals("现在") || hour.equals("現在")) {
            long time = System.currentTimeMillis();
//            Log.e("getTime",""+ time  + DateFormatUtils.format(time,"yyyy-MM--dd HH:mm"));
            return time;
        } else {
            int hourNum = Integer.parseInt(hour.substring(0, hour.length() - 1));
            int minNum = Integer.parseInt(min);
            if (hourNum < 10) {
                hour = "0" + String.valueOf(hourNum);
            } else {
                hour = String.valueOf(hourNum);
            }
            if (minNum < 10) {
                min = "0" + String.valueOf(minNum);
            } else {
                min = String.valueOf(minNum);
            }
            if (day.equals("今天")) {
                day = TimeUtil.getTime(TimeUtil.YMD_2, System.currentTimeMillis());
            } else if (day.equals("明天")) {
                day = TimeUtil.getTime(TimeUtil.YMD_2, System.currentTimeMillis() + 86400000);
            } else if (day.equals("后天") || day.equals("後天")) {
                day = TimeUtil.getTime(TimeUtil.YMD_2, System.currentTimeMillis() + 2 * 86400000);
            }
            String timeStr = day + " " + hour + ":" + min;
            return TimeUtil.parseTime(TimeUtil.YMD_HM, timeStr);
        }
    }

}
