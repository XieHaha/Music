package com.yht.yihuantong.tools;

import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

/**
 * 文件处理工具类
 */
public final class FileUtils
{
    /**
     * 是否有SD Card
     *
     * @return true or false
     */
    public static boolean hasSDCard()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 复制文件
     *
     * @param oldPath 文件原地址
     * @param newPath 文件新地址
     * @return true为复制成功，false为复制失败
     */
    public static boolean copyFile(String oldPath, String newPath)
    {
        try
        {
            java.io.File fileIn = new java.io.File(oldPath);
            java.io.File fileOut = new java.io.File(newPath);
            FileInputStream fileInputStream = new FileInputStream(fileIn);
            FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = fileInputStream.read(bytes)) != -1) { fileOutputStream.write(bytes, 0, c); }
            fileInputStream.close();
            fileOutputStream.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 根据文件路径获取文件后缀名
     *
     * @param filepath 文件路径
     * @return 后缀名, 不带.
     */
    public static String getFileExtNoPoint(String filepath)
    {
        try
        {
            if (filepath != null && filepath.lastIndexOf(".") != -1)
            {
                int start = filepath.lastIndexOf(".");
                int end = filepath.length();
                return filepath.substring(start + 1, end);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据文件路径获取文件后缀名
     *
     * @param filepath 文件路径
     * @return 后缀名
     */
    public static String getFileExt(String filepath)
    {
        if (filepath != null && filepath.lastIndexOf(".") != -1)
        {
            int start = filepath.lastIndexOf(".");
            int end = filepath.length();
            return filepath.substring(start, end);
        }
        else
        {
            return "";
        }
    }

    /**
     * 根据文件路径获取文件名称
     *
     * @param filepath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filepath)
    {
        if (filepath == null)
        {
            return null;
        }
        else if (filepath.lastIndexOf("/") != -1 && filepath.lastIndexOf(".") != -1)
        {
            int strat = filepath.lastIndexOf("/") + 1;
            int end = filepath.lastIndexOf(".");
            return strat > end ? "" : filepath.substring(strat, end);
        }
        else if (filepath.lastIndexOf("/") == -1 && filepath.lastIndexOf(".") != -1)
        {
            int end = filepath.lastIndexOf(".");
            return filepath.substring(0, end);
        }
        else
        {
            return null;
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileLength
     * @return
     */
    public static String formatFileSize(long fileLength)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileLength == 0)
        {
            return wrongSize;
        }
        if (fileLength < 1024)
        {
            fileSizeString = df.format((double)fileLength) + "B";
        }
        else if (fileLength < 1048576)
        {
            fileSizeString = df.format((double)fileLength / 1024) + "KB";
        }
        else if (fileLength < 1073741824)
        {
            fileSizeString = df.format((double)fileLength / 1048576) + "MB";
        }
        else
        {
            fileSizeString = df.format((double)fileLength / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
