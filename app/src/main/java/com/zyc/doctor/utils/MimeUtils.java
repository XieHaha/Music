package com.zyc.doctor.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dundun
 */
public class MimeUtils {
    private static Map<String, String> imageMime = new HashMap<String, String>();

    static {
        imageMime.put("bmp", "image/bmp");
        imageMime.put("gif", "image/gif");
        imageMime.put("jpeg", "image/jpeg");
        imageMime.put("jpg", "image/jpeg");
        imageMime.put("png", "image/png");
    }

    private static Map<String, String> mediaMime = new HashMap<String, String>();

    static {
        mediaMime.put("3gp", "video/3gpp");
        mediaMime.put("avi", "video/x-msvideo");
        mediaMime.put("mp4", "video/mp4");
        mediaMime.put("rmvb", "audio/x-pn-realaudio");
        mediaMime.put("wav", "audio/x-wav");
        mediaMime.put("wma", "audio/x-ms-wma");
        mediaMime.put("wmv", "audio/x-ms-wmv");
    }

    private static Map<String, String> otherMime = new HashMap<String, String>();

    static {
        otherMime.put("3gp", "video/3gpp");
        otherMime.put("avi", "video/x-msvideo");
        otherMime.put("mp3", "audio/x-mpeg");
        otherMime.put("mp4", "video/mp4");
        otherMime.put("rmvb", "audio/x-pn-realaudio");
        otherMime.put("wav", "audio/x-wav");
        otherMime.put("wma", "audio/x-ms-wma");
        otherMime.put("wmv", "audio/x-ms-wmv");
        otherMime.put("apk", "application/vnd.android.package-archive");
        otherMime.put("asf", "video/x-ms-asf");
        otherMime.put("bin", "application/octet-stream");
        otherMime.put("c", "text/plain");
        otherMime.put("class", "application/octet-stream");
        otherMime.put("conf", "text/plain");
        otherMime.put("cpp", "text/plain");
        otherMime.put("doc", "application/msword");
        otherMime.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml" + ".document");
        otherMime.put("xls", "application/vnd.ms-excel");
        otherMime.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        otherMime.put("exe", "application/octet-stream");
        otherMime.put("gtar", "application/x-gtar");
        otherMime.put("gz", "application/x-gzip");
        otherMime.put("h", "text/plain");
        otherMime.put("htm", "text/html");
        otherMime.put("html", "text/html");
        otherMime.put("jar", "application/java-archive");
        otherMime.put("java", "text/plain");
        otherMime.put("js", "application/x-javascript");
        otherMime.put("log", "text/plain");
        otherMime.put("m3u", "audio/x-mpegurl");
        otherMime.put("m4a", "audio/mp4a-latm");
        otherMime.put("m4b", "audio/mp4a-latm");
        otherMime.put("m4p", "audio/mp4a-latm");
        otherMime.put("m4u", "video/vnd.mpegurl");
        otherMime.put("m4v", "video/x-m4v");
        otherMime.put("mov", "video/quicktime");
        otherMime.put("mp2", "audio/x-mpeg");
        otherMime.put("mpc", "application/vnd.mpohun.certificate");
        otherMime.put("mpe", "video/mpeg");
        otherMime.put("mpeg", "video/mpeg");
        otherMime.put("mpg", "video/mpeg");
        otherMime.put("mpg4", "video/mp4");
        otherMime.put("mpga", "audio/mpeg");
        otherMime.put("msg", "application/vnd.ms-outlook");
        otherMime.put("ogg", "audio/ogg");
        otherMime.put("pdf", "application/pdf");
        otherMime.put("pps", "application/vnd.ms-powerpoint");
        otherMime.put("ppt", "application/vnd.ms-powerpoint");
        otherMime.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml" + ".presentation");
        otherMime.put("prop", "text/plain");
        otherMime.put("rc", "text/plain");
        otherMime.put("rtf", "application/rtf");
        otherMime.put("sh", "text/plain");
        otherMime.put("tar", "application/x-tar");
        otherMime.put("tgz", "application/x-compressed");
        otherMime.put("txt", "text/plain");
        otherMime.put("wps", "application/vnd.ms-works");
        otherMime.put("xml", "text/plain");
        otherMime.put("z", "application/x-compress");
        otherMime.put("zip", "application/x-zip-compressed");
    }

    /**
     * 判断是否为图片文件
     *
     * @param ext 文件后缀，不带.
     * @return true：图片类型 false:其他文件类型
     */
    public static boolean isImageType(String ext) {
        if (imageMime.containsKey(ext.toLowerCase())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 判断是否为视频文件
     *
     * @param ext 文件后缀，不带.
     * @return true：图片类型 false:其他文件类型
     */
    public static boolean isMediaType(String ext) {
        if (mediaMime.containsKey(ext.toLowerCase())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 返回文件memi
     *
     * @param ext 文件扩展名
     * @return memi
     */
    public static String getMime(String ext) {
        String memi = "";
        if (imageMime.containsKey(ext.toLowerCase())) {
            memi = imageMime.get(ext);
        }
        else if (otherMime.containsKey(ext.toLowerCase())) {
            memi = otherMime.get(ext);
        }
        return memi;
    }
}
