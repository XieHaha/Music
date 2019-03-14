/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) JustSafe Information Technology Co., Ltd. All Rights Reserved
 *
 * Description:
 *   <author> - <version> - <date> - <desc>
 */
package com.yht.yihuantong.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.TypedValue;

import com.yht.yihuantong.YihtApplication;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import custom.frame.utils.ToastUtil;

/**
 * 工具类
 */
public class AllUtils {
    private static final String TAG = AllUtils.class.getSimpleName();
    private static final String REGEX_PHONE = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    private static final String REGEX_CARD_NUM = "(^[1-8][0-7]{2}\\d{3}([12]\\d{3})(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9Xx])$)";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY = "yyyy";


    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvaliable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
    }

    /**
     * 将dp值转换为像素值
     *
     * @param mContext Context对象
     * @param size     dp值
     * @return 像素值
     */
    public static float dipToPx(Context mContext, float size) {
        Resources r;
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());
    }

    /**
     * 字符串转换为java.util.Date<br>
     * 支持格式为yyyy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'<br>
     * yyyy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'<br>
     * yyyy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00' <br>
     * yyyy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am' <br>
     *
     * @param time String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date formatDate(String time) {
        time = time.trim();
        String formatter = "yyyy-MM-dd HH:mm:ss";
        if ((time.indexOf("/") > -1) && (time.indexOf(" ") > -1)) {
            formatter = "yyyy/MM/dd HH:mm:ss";
        } else if ((time.indexOf("/") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = "yyyy/MM/dd KK:mm:ss a";
        } else if ((time.indexOf("-") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = "yyyy-MM-dd KK:mm:ss a";
        }
        return formatDate(time, formatter);
    }

    /**
     * 字符串转换为java.util.Date<br>
     *
     * @param time   String 字符串<br>
     * @param format String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date formatDate(String time, String format) {
        SimpleDateFormat formatter;
        time = time.trim();
        formatter = new SimpleDateFormat(format, Locale.getDefault());
        java.util.Date ctime = formatter.parse(time, new ParsePosition(0));
        return ctime;
    }

    /**
     * 时间戳转为北京时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(long time, String format) {
        if (format != null) {
            return new SimpleDateFormat(format).format(new Date(time));
        }
        return "";
    }

    public static String getAge(long time) {
        try {
            Date birthDay = new Date(time);
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDay)) {
                throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
            }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(birthDay);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    age--;
                }
            }
            return String.valueOf(age);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 根据文件路径获取文件后缀名
     *
     * @param filepath 文件路径
     * @return 后缀名, 不带.
     */
    public static String getFileExtNoPoint(String filepath) {
        try {
            if (filepath != null && filepath.lastIndexOf(".") != -1) {
                int start = filepath.lastIndexOf(".");
                int end = filepath.length();
                return filepath.substring(start + 1, end);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
        return "";
    }

    /**
     * 根据文件路径获取文件名称
     *
     * @param filepath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filepath) {
        if (filepath == null) {
            return null;
        } else if (filepath.lastIndexOf("/") != -1 && filepath.lastIndexOf(".") != -1) {
            int strat = filepath.lastIndexOf("/") + 1;
            int end = filepath.lastIndexOf(".");
            return strat > end ? "" : filepath.substring(strat, end);
        } else if (filepath.lastIndexOf("/") == -1 && filepath.lastIndexOf(".") != -1) {
            int end = filepath.lastIndexOf(".");
            return filepath.substring(0, end);
        } else {
            return null;
        }
    }


    /**
     * 判断是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobileNumber(String str) {
        Matcher phoneMatcher = Pattern.compile(REGEX_PHONE).matcher(str);
        if (phoneMatcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否身份证号
     *
     * @param str
     * @return
     */
    public static boolean isCardNum(String str) {
        Matcher phoneMatcher = Pattern.compile(REGEX_CARD_NUM).matcher(str);
        if (phoneMatcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 打开文件
     *
     * @param context
     * @param filePath 文件路径
     */
    public static void openFile(Context context, String filePath) {
        try {
            String type = "*/*";
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            type = MimeUtils.getMime(FileUtils.getFileExtNoPoint(filePath));
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(context, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                        new File(filePath));
            } else {
                uri = Uri.fromFile(new File(filePath));
            }
            //设置intent的data和Type属性。
            intent.setDataAndType(uri, type);
            //跳转
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtil.toast(context, "无法打开文件");
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtil.toast(context, "无法打开文件");
        }
    }
}
