/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) JustSafe Information Technology Co., Ltd. All Rights Reserved
 *
 * Description:
 *   <author> - <version> - <date> - <desc>
 */
package com.yht.yihuantong.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * 工具类
 */
public class AllUtils {
    private static final String TAG = AllUtils.class.getSimpleName();
    private static String REGEX_PHONE = "^((13[0-9])|(14[5|7|9])|(15([0-3]|[5-9]))|(16[0-9])|(17([0-3]|[5-8]))|(18[0-9])|(19[0-9]))\\d{8}$";

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static String DATE_FORMAT_NO_HOUR = "yyyy-MM-dd";

    /**
     * trim方法，去掉字符串首、尾的空格，制表符，回车符。字符串null将被替换为空字符串
     *
     * @param s 待处理字符串
     * @return 处理结果
     */
    public static String trim(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        s = s.replaceAll("^\\s*|\t|\r|\n", "").replaceAll("\\s*|\t|\r|\n$", "");
        return "null".equals(s) ? "" : s;
    }

    public static String ipIntToString(int ip) {
        return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

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
     * 将字符串转为int
     *
     * @param value        待转字符串
     * @param defaultValue 失败默认值
     * @return 转后int
     */
    public static int tryParse(String value, int defaultValue) {
        if (isEmptyOrNull(value)) {
            return defaultValue;
        }
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 将字符串转为float
     *
     * @param value        待转字符串
     * @param defaultValue 失败默认值
     * @return 转后float
     */
    public static float tryParse(String value, float defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        float result;
        try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 将字符串转为long
     *
     * @param value        待转字符串
     * @param defaultValue 失败默认值
     * @return 转后long
     */
    public static long tryParse(String value, long defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        long result;
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 将字符串转为boolean
     *
     * @param value        待转字符串
     * @param defaultValue 失败默认值
     * @return 转后boolean
     */
    public static boolean tryParse(String value, boolean defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        boolean result;
        try {
            result = Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 判断字符串是否为空或为“null”字符
     *
     * @param str
     * @return
     */
    public static boolean isEmptyOrNull(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if (str.equals("null")) {
            return true;
        }
        return false;
    }

    public static String dealString(String str) {
        if (isEmptyOrNull(str)) {
            return "";
        } else {
            return str;
        }
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

    public static float dipToPx(Context mContext, String size) {
        Resources r;
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        float f;
        try {
            if (size.endsWith("dp")) {
                size = size.substring(0, size.length() - 2);
            } else if (size.endsWith("dip")) {
                size = size.substring(0, size.length() - 3);
            }
            f = Float.valueOf(size);
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, r.getDisplayMetrics());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
        return 0f;
    }

    /**
     * 将sp值转换为像素值
     *
     * @param mContext Context对象
     * @param size     sp值
     * @return 像素值
     */
    public static float spToPx(Context mContext, float size) {
        Resources r;
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
    }

    /**
     * 根据给定字符串生成id，值为该字符串hash的绝对值
     *
     * @param id String id
     * @return int id
     */
    public static int generateId(String id) {
        return Math.abs(id.hashCode());
    }

    /**
     * 删除目录或文件，包括目录下的文件
     *
     * @param dir 待删除目录或文件
     */
    public static void deleteDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                deleteDir(f);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    /**
     * Delete the file with path
     *
     * @param strFileName
     */
    public static void deleteFile(String strFileName) {
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    /**
     * 是否合法的字符串
     *
     * @param target
     * @param regex
     * @return
     */
    public static boolean regex(String target, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(target);
        return m.find();
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
     * 使用MD5加密
     *
     * @param data 加密前字符串
     * @return
     */
    public static byte[] encryptMD5(String data) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
        return bytes;
    }

    /**
     * 把二进制数据转化为十六进制
     *
     * @param bytes
     * @param isToUpper 是否大写
     * @return
     */
    public static String byte2hex(byte[] bytes, boolean isToUpper) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            if (isToUpper) {
                sign.append(hex.toUpperCase());
            } else {
                sign.append(hex);
            }
        }
        return sign.toString();
    }

    /**
     * 复制文件
     *
     * @param oldPath 文件原地址
     * @param newPath 文件新地址
     * @return true为复制成功，false为复制失败
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            java.io.File fileIn = new java.io.File(oldPath);
            java.io.File fileOut = new java.io.File(newPath);
            FileInputStream fileInputStream = new FileInputStream(fileIn);
            FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = fileInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, c);
            }
            fileInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
        return "127.0.0.1";
    }

    /**
     * 判断应用是否是在后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(
                KEYGUARD_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
                boolean isBackground = (appProcess.importance !=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        appProcess.importance !=
                                ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
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
}
