package com.zyc.doctor.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 文件处理工具类
 *
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
            }
            catch (IOException e) {
                LogUtils.w(TAG, "Exception error!", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        catch (IOException e) {
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
        }
        catch (Exception e) {
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
        }
        else {
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
        }
        else if (filepath.lastIndexOf("/") != -1 && filepath.lastIndexOf(".") != -1) {
            int strat = filepath.lastIndexOf("/") + 1;
            int end = filepath.lastIndexOf(".");
            return strat > end ? "" : filepath.substring(strat, end);
        }
        else if (filepath.lastIndexOf("/") == -1 && filepath.lastIndexOf(".") != -1) {
            int end = filepath.lastIndexOf(".");
            return filepath.substring(0, end);
        }
        else {
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
            fileSizeString = df.format((double)fileLength) + "B";
        }
        else if (fileLength < 1048576) {
            fileSizeString = df.format((double)fileLength / 1024) + "KB";
        }
        else if (fileLength < 1073741824) {
            fileSizeString = df.format((double)fileLength / 1048576) + "MB";
        }
        else {
            fileSizeString = df.format((double)fileLength / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    public static String getFileByUri(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                                                  Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        finally {
            if (cursor != null) { cursor.close(); }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
