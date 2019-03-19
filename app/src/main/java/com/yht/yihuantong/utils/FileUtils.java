package com.yht.yihuantong.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 文件处理工具类
 * @author dundun
 */
public final class FileUtils {
    private static final String TAG = "FileUtils";
    /**
     * 是否有SD Card
     *
     * @return true or false
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 复制文件
     *
     * @param oldPath 文件原地址
     * @param newPath 文件新地址
     * @return true为复制成功，false为复制失败
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(oldPath))) {
            byte[] bytes = new byte[1024];
            int c;
            try (FileOutputStream fileOutputStream = new FileOutputStream(new File(newPath))) {
                while ((c = fileInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, c);
                }
                return true;
            } catch (IOException e) {
                LogUtils.w(TAG, "Exception error!", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (IOException e) {
            LogUtils.w(TAG, "Exception error!", e);
            throw new RuntimeException(e.getMessage(), e);
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
            LogUtils.w(TAG, "Exception error!", e);
        }
        return "";
    }

    /**
     * 根据文件路径获取文件后缀名
     *
     * @param filepath 文件路径
     * @return 后缀名
     */
    public static String getFileExt(String filepath) {
        if (filepath != null && filepath.lastIndexOf(".") != -1) {
            int start = filepath.lastIndexOf(".");
            int end = filepath.length();
            return filepath.substring(start, end);
        } else {
            return "";
        }
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
     * 转换文件大小
     *
     * @param fileLength
     * @return
     */
    public static String formatFileSize(long fileLength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileLength == 0) {
            return wrongSize;
        }
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * uri转为file文件
     *
     * @param uri
     * @param context
     * @return
     */
    public static File getFileByUri(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append(
                        "'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID,
                                MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        }
        return null;
    }
}
