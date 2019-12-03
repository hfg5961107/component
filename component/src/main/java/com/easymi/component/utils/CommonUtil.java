package com.easymi.component.utils;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/23.
 * 常用一般工具类.
 */

@SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
public class CommonUtil {
    /**
     * 将double类型的数字转化保留1位小数后以String类型返回.
     *
     * @param d       double类型的数字
     * @param pattern 需要格式化的格式
     * @return 转化后的结果
     */
    public static String d2s(double d, String pattern) {
        DecimalFormat df;
        if (TextUtils.isEmpty(pattern)) {
            df = new DecimalFormat("0.0");    //舍弃一位小数后面的
        } else {
            df = new DecimalFormat(pattern);    //舍弃一位小数后面的
        }
        return df.format(d);
    }

    /**
     * 将double类型的数字转化保留1位小数后以String类型返回.
     *
     * @param d double类型的数字
     * @return 转化后的结果
     */
    public static String d2s(double d) {
        return d2s(d, null);
    }

    /**
     * 四舍五入double数字.
     * (后台数据最好格式化,经常传回13.0000000001这种数字)
     *
     * @param d        需要格式的double数字
     * @param newScale 向下保留到小数的位数
     * @return 返回格式化后的数字
     */
    public static double df(double d, int newScale) {
        BigDecimal bg = new BigDecimal(d).setScale(newScale, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();
    }

    /**
     * 按照yyyy-MM-dd HH:mm的格式格式化一个日期.
     *
     * @param time    需要格式化日期的毫秒值
     * @param pattern 日期格式
     * @return 返回格式化后的数据
     */
    public static String dateFormat(long time, String pattern) {
        SimpleDateFormat mFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(time);
        return mFormat.format(date);
    }

    /**
     * 将字符串转化成double数字.
     *
     * @param s 输入的字符串
     * @return 返回解析后的支付串
     */
    public static double s2d(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        double number = 0;
        try {
            number = Double.parseDouble(s.toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return number;
    }


    private static Pattern NUMBER_PATTERN = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud800\udc00-\udbff\udfff\ud800-\udfff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 判断一个字符串是否含有emoji表情.
     *
     * @param string 需要检测的字符串
     * @return 如果包含emoji表情则返回true, 否则返回false
     */
    public static boolean isEmoji(String string) {
        // TODO: 2017/1/3 当emoji表情增加时,需要扩展范围
//        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud800\udc00-\udbff\udfff\ud800-\udfff]",
//                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = NUMBER_PATTERN.matcher(string);
        return m.find();
    }

    /**
     * 验证手机号码的正确性.
     *
     * @param mobiles 需要验证的手机号码
     * @return 正确则返回true, 否则返回false
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    新增：17开头的电话号码，如170、177
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
//        // TODO: 2017/1/3 随着号码增加,验证也需要跟进
////        String telRegex = "[1][35789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
////        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
        return (mobiles.trim().length() == 11);
    }

    /**
     * 获取有高亮提醒的字符串,(只匹配第一次出现的).
     *
     * @param wholeStr 完整的字符串
     * @param lightStr 需要高亮显示的字符串
     * @param color    需要设置的颜色
     * @return 设置完后的字符串
     */
    public static SpannableStringBuilder getSpannableString(String wholeStr, String lightStr, int color) {
        if (TextUtils.isEmpty(wholeStr) || TextUtils.isEmpty(lightStr)) {
            return null;
        }
        if (!wholeStr.contains(lightStr)) {
            return null;
        }
        int start = wholeStr.indexOf(lightStr);
        int end = start + lightStr.length();

        SpannableStringBuilder spBuilder = new SpannableStringBuilder(wholeStr);
        CharacterStyle charaStyle = new ForegroundColorSpan(color);
        spBuilder.setSpan(charaStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spBuilder;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * 获取一个唯一的id.
     *
     * @return 返回一个id
     */
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId(); //用系统的
        }
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * @param json
     * @param clazz
     * @return
     * @author I321533
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * md5值编码.
     */
    public static String md5(byte[] byteStr) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(byteStr);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String getPassengerDesc(int type, int sort) {
        if (type == 1) {
            if (sort == 1) {
                return "前右";
            } else if (sort == 2) {
                return "后左";
            } else if (sort == 3) {
                return "后中";
            } else if (sort == 4) {
                return "后右";
            }
        } else if (type == 2) {
            if (sort == 1) {
                return "前右";
            } else if (sort == 2) {
                return "中左";
            } else if (sort == 3) {
                return "中右";
            } else if (sort == 4) {
                return "后左";
            } else if (sort == 5) {
                return "后中";
            } else if (sort == 6) {
                return "后右";
            }
        }
        return "";
    }

    public static String getPassengerDescAndType(int type, String sorts, String sortsType) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(sorts) && !TextUtils.isEmpty(sortsType)) {
            String[] sortsData = sorts.split("\\,");
            String[] typeData = sortsType.split("\\,");
            if (sortsData.length == typeData.length) {
                for (int i = 0; i < sortsData.length; i++) {
                    int sort = Integer.parseInt(sortsData[i]);
                    int sortType = Integer.parseInt(typeData[i]);
                    if (type == 1) {
                        if (sort == 1) {
                            stringBuilder.append("前右");
                        } else if (sort == 2) {
                            stringBuilder.append("后左");
                        } else if (sort == 3) {
                            stringBuilder.append("后中");
                        } else if (sort == 4) {
                            stringBuilder.append("后右");
                        }
                    } else if (type == 2) {
                        if (sort == 1) {
                            stringBuilder.append("前右");
                        } else if (sort == 2) {
                            stringBuilder.append("中左");
                        } else if (sort == 3) {
                            stringBuilder.append("中右");
                        } else if (sort == 4) {
                            stringBuilder.append("后左");
                        } else if (sort == 5) {
                            stringBuilder.append("后中");
                        } else if (sort == 6) {
                            stringBuilder.append("后右");
                        }
                    }

                    if (sortType == 1) {
                        stringBuilder.append("(儿童)");
                    } else if (sortType == 2) {
                        stringBuilder.append("(成人)");
                    }

                    if (i != sortsData.length - 1) {
                        stringBuilder.append(" ");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}
