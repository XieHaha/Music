/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName:Log.java
 * Description:log日志打印类
 * <author> - <version> - <date> - <desc>
 *     Lucioli － v1.0 - 2015.11.12 - 创建类
 */
package com.zyc.doctor.utils;

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

/**
 * log日志打印类
 *
 * @author dundun
 */
public class LogUtils {
    private static boolean isEnableLog = false;
    private static final char V = 'v';
    private static final char D = 'd';
    private static final char W = 'w';
    private static final char I = 'i';
    private static final char E = 'e';

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
     * 本类输出的日志文件名称
     */
    private static String logFileName = "Log.txt";
    /**
     * 日志的输出格式
     */
    private static SimpleDateFormat mLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * 日志文件格式
     */
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");

    public static void w(String tag, String text) {
        log(tag, text, W, null);
    }

    public static void e(String tag, String text) {
        log(tag, text, E, null);
    }

    public static void d(String tag, String text) {
        log(tag, text, D, null);
    }

    public static void i(String tag, String text) {
        log(tag, text, I, null);
    }

    public static void v(String tag, String text) {
        log(tag, text, V, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg, W, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg, E, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(tag, msg, D, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(tag, msg, I, tr);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(tag, msg, V, tr);
    }

    private static void log(String tag, String msg, char level, Throwable tr) {
        if (!canLog()) { return; }
        switch (level) {
            case E:
                if (tr == null) {
                    Log.e(tag, msg);
                }
                else {
                    Log.e(tag, msg, tr);
                }
                break;
            case W:
                if (tr == null) {
                    Log.w(tag, msg);
                }
                else {
                    Log.w(tag, msg, tr);
                }
                break;
            case D:
                if (tr == null) {
                    Log.d(tag, msg);
                }
                else {
                    Log.d(tag, msg, tr);
                }
                break;
            case I:
                if (tr == null) {
                    Log.i(tag, msg);
                }
                else {
                    Log.i(tag, msg, tr);
                }
                break;
            default:
                if (tr == null) {
                    Log.v(tag, msg);
                }
                else {
                    Log.v(tag, msg, tr);
                }
                break;
        }
        if (E == level) {
            if (tr == null) {
                writeLogToFile(String.valueOf(level), tag, msg);
            }
            else {
                writeLogToFile(String.valueOf(level), tag + " " + msg, tr);
            }
        }
    }

    /**
     * 新建或打开日志文件
     *
     * @param logType type
     * @param tag     tag
     * @param text    错误内容
     */
    private static void writeLogToFile(String logType, String tag, String text) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { return; }
        Date nowTime = new Date();
        String needWriteFile = logfile.format(nowTime);
        String needWriteMessage = mLogSdf.format(nowTime) + "    " + logType + "    " + tag + "    " + text;
        String pathLog = DirHelper.getPathLog();
        File file = new File(pathLog);
        if (!file.exists() && !file.mkdirs()) { return; }
        file = new File(pathLog, needWriteFile + "-" + logFileName);
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufWriter != null) { bufWriter.close(); }
                if (filerWriter != null) { filerWriter.close(); }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 记录日志
     *
     * @param level l
     * @param tag   t
     * @param ex    t
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
