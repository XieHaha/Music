package custom.frame.widgets.imagePreview.cache.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import custom.frame.widgets.imagePreview.options.PreviewOptions;

/**
 * Created by Kyle on 2015/12/14.
 */
public class BitmapDiskLruCache {

    private static DiskLruCache mDiskLruCache;
    private Context context;

    public BitmapDiskLruCache(Context context) {
        this.context = context;
        try {
            mDiskLruCache = DiskLruCache.open(context.getCacheDir(), getAppVersion(), 1, PreviewOptions.DiskCacheOptions.CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAppVersion() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    protected int sizeOf(String key, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 外存获取缓存
     */
    public Bitmap getBitmapFromDiskCache(String imageUri) {
        String key = hashKeyForDisk(imageUri);
        try {
            if (mDiskLruCache.get(key) != null) {
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                Bitmap bitmap = null;
                if (snapShot != null) {
                    InputStream is = snapShot.getInputStream(0);
                    bitmap = BitmapFactory.decodeStream(is);
                }
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存写入外存
     */
    public boolean cacheImageFileToDisk(String imageUri, Bitmap bitmap) {
        String key = hashKeyForDisk(imageUri);
        try {
            if (null == mDiskLruCache.get(key)) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.WEBP, PreviewOptions.DiskCacheOptions.IMAGE_QUALITY, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 缓存写入外存图片文件
     */
    public boolean saveImageFileToDisk(String imageUri, Bitmap bitmap) {
        String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
        String path = PreviewOptions.ImageDownloadOptions.Image_SAVE_ABS_DIR;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, PreviewOptions.DiskCacheOptions.IMAGE_QUALITY, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 生成文件名
     */
    public String hashKeyForDisk(String key) {
        //final int RADIX = 10 + 26;
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bi = new BigInteger(hash).abs();
        return bi.toString(36);
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 从缓存中移除图片
     */
    public void removeBitmapFromDiskCache(String imageUri) {
        try {
            String key = hashKeyForDisk(imageUri);
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}