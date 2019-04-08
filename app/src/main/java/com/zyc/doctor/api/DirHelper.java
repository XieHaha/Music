/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName: DirHelper.java
 * Description: 目录管理帮助类
 * <author> - <version> - <date> - <desc>
 *
 */
package com.zyc.doctor.api;

import android.os.Environment;

import java.io.File;

/**
 * 目录管理帮助类
 *
 * @author dundun
 */
public final class DirHelper {
    /**
     * 程序根目录
     */
    private static final String PATH_APP_ROOT = "YHT";
    /**
     * 头像、图片缓存目录 universalimageloader中使用
     */
    private static final String PATH_CACHE = "cache";
    /**
     * 日志文件目录
     */
    private static final String PATH_LOG = "log";
    /**
     * 临时文件目录
     */
    private static final String PATH_TEMP = "tmp";
    /**
     * 文件目录
     */
    private static final String PATH_FILE = "file";
    /**
     * 图片文件目录
     */
    private static final String PATH_IMAGE = "image";
    /**
     * 语音文件目录
     */
    private static final String PATH_VOICE = "voice";
    /**
     * 视频文件目录
     */
    private static final String PATH_VIDEO = "video";
    /**
     * 缩略图文件目录
     */
    private static final String PATH_THUMB = "thumb";

    /**
     * 获取应用缓存根目录
     *
     * @return
     */
    public static String getRootPath() {
        return getPath(PATH_APP_ROOT);
    }

    /**
     * 获取缓存目录
     *
     * @return
     */
    public static String getPathCache() {
        return getPath(PATH_CACHE);
    }

    /**
     * 获取文件缓存目录
     *
     * @return
     */
    public static String getPathFile() {
        return getPath(PATH_FILE);
    }

    /**
     * 获取图片缓存目录
     *
     * @return
     */
    public static String getPathImage() {
        return getPath(PATH_IMAGE);
    }

    /**
     * 获取临时文件缓存目录
     *
     * @return
     */
    public static String getPathTemp() {
        return getPath(PATH_TEMP);
    }

    /**
     * 获取缩略图缓存目录
     *
     * @return
     */
    public static String getPathThumb() {
        return getPath(PATH_THUMB);
    }

    /**
     * 获取日志缓存目录
     *
     * @return
     */
    public static String getPathLog() {
        return getPath(PATH_LOG);
    }

    /**
     * 获取视频文件缓存目录
     *
     * @return
     */
    public static String getPathVideo() {
        return getPath(PATH_VIDEO);
    }

    /**
     * 获取语音缓存目录
     *
     * @return
     */
    public static String getPathVoice() {
        return getPath(PATH_VOICE);
    }

    /**
     * 根据目录名称返回路径
     *
     * @param dir 目录名
     * @return 目录路径
     */
    private static String getPath(String dir) {
        String root = addPath(Environment.getExternalStorageDirectory().getPath(), PATH_APP_ROOT);
        if (PATH_APP_ROOT.equals(dir)) {
            return checkDir(root);
        }
        else if (PATH_TEMP.equals(dir)) {
            return checkDir(addPath(root, PATH_TEMP));
        }
        else if (PATH_LOG.equals(dir)) {
            return checkDir(addPath(root, PATH_LOG));
        }
        else if (PATH_IMAGE.equals(dir)) {
            return checkDir(addPath(root, PATH_IMAGE));
        }
        else if (PATH_VOICE.equals(dir)) {
            return checkDir(addPath(root, PATH_VOICE));
        }
        else if (PATH_FILE.equals(dir)) {
            return checkDir(addPath(root, PATH_FILE));
        }
        else if (PATH_VIDEO.equals(dir)) {
            return checkDir(addPath(root, PATH_VIDEO));
        }
        else if (PATH_THUMB.equals(dir)) {
            return checkDir(addPath(root, PATH_THUMB));
        }
        else if (PATH_CACHE.equals(dir)) {
            return checkDir(addPath(root, PATH_CACHE));
        }
        return checkDir(root);
    }

    //检查目录是否存在，如果不存在则创建，并返回目录路径
    private static String checkDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    //组装路径
    private static String addPath(String parent, String child) {
        return String.format("%s/%s", parent, child);
    }
}
