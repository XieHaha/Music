/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName:Log.java
 * Description:log日志打印类
 * <author> - <version> - <date> - <desc>
 *     Lucioli － v1.0 - 2015.11.12 - 创建类
 */
package com.yht.yihuantong.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import custom.frame.utils.DirHelper;

/**
 * log日志打印类
 * @author dundun
 */
public class LogUtils {
    private static final String TAG = "LogUtils";
    private static boolean isEnableLog = false;

    public static void setIsEnableLog(boolean isEnableLog) {
        LogUtils.isEnableLog = isEnableLog;
    }

    /**
     * @return True if the settings manager is set and debug logs are enabled, False otherwise
     */
    private static boolean canLog() {
        return isEnableLog;
    }

    /**
     * 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
     */
    private static char LOG_TYPE = 'v';
    /**
     * 本类输出的日志文件名称
     */
    private static String LOG_FILENAME = "Log.txt";

    public static void w(String tag, String text) {
        log(tag, text, 'w', null);
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e', null);
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd', null);
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i', null);
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v', null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg, 'w', tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg, 'e', tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(tag, msg, 'd', tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(tag, msg, 'i', tr);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(tag, msg, 'v', tr);
    }

    private static void log(String tag, String msg, char level, Throwable tr) {
        if (!canLog()) {
            return;
        }
        if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) {
            //输出错误信息
            if (tr == null) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, msg, tr);
            }
        } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
            if (tr == null) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, msg, tr);
            }
        } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
            if (tr == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, tr);
            }
        } else if ('i' == level && ('i' == LOG_TYPE || 'v' == LOG_TYPE)) {
            if (tr == null) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, msg, tr);
            }
        } else {
            if (tr == null) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, msg, tr);
            }
        }
        if (('d' == level) || 'e' == level) {
            if (tr == null) {
                writeLogToFile(String.valueOf(level), tag, msg);
            } else {
                writeLogToFile(String.valueOf(level), tag + " " + msg, tr);
            }
        }
    }

    /**
     * 新建或打开日志文件
     *
     * @param mylogtype
     * @param tag
     * @param text
     */
    private static void writeLogToFile(String mylogtype, String tag, String text) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return;
        }
        Date nowtime = new Date();
        String needWriteFile = new SimpleDateFormat("yyyy-MM-dd").format(nowtime);
        String needWriteMessage =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
        String pathLog = DirHelper.getPathLog();
        File file = new File(pathLog);
        if (!file.exists() && !file.mkdirs()) {
            return;
        }
        file = new File(pathLog, needWriteFile + "-" + LOG_FILENAME);
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
        } catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        } finally {
            try {
                if (bufWriter != null) {
                    bufWriter.close();
                }
                if (filerWriter != null) {
                    filerWriter.close();
                }
            } catch (IOException ioe) {
                LogUtils.w(TAG, "Exception error!", ioe);
            }
        }
    }

    /**
     * 记录日志
     *
     * @param level
     * @param tag
     * @param ex
     */
    private static void writeLogToFile(String level, String tag, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        writeLogToFile(level, tag + ":\n", sb.toString() + "\n");
    }
}
