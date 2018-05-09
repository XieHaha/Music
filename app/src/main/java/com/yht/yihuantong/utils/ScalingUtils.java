package com.yht.yihuantong.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 */
public final class ScalingUtils
{
    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 需要旋转图片的Bitmap
     * @return Bitmap  旋转后Bitmap
     */
    public static Bitmap rotaingImage(int angle, Bitmap bitmap)
    {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 保存Bitmap图片
     *
     * @param bitmap
     * @param format
     */
    public static void saveBitmapPic(Bitmap bitmap, String picPath, Bitmap.CompressFormat format)
    {
        if (bitmap == null || picPath == null) { return; }
        File file = new File(picPath);
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            bitmap.compress(format, 50, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.flush();
                    out.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存Bitmap图片: 限制图片最大400kb
     *
     * @param bitmap
     * @param picPath
     */
    public static void saveBitmapPic(Bitmap bitmap, String picPath)
    {
        int maxSize = 400;
        if (bitmap == null || picPath == null) { return; }
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            boolean isCompressed = false;
            if (baos.toByteArray().length / 1024 <= maxSize)
            {
                isCompressed = true;
            }
            else
            {
                while (baos.toByteArray().length / 1024 > maxSize)
                {
                    quality -= 10;
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    isCompressed = true;
                }
            }
            if (isCompressed)
            {
                //                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
                //                recycleBitmap(bitmap);
                File file = new File(picPath);
                FileOutputStream out = new FileOutputStream(file);
                out.write(baos.toByteArray());
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 回收位图对象
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap)
    {
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }

    /**
     * 返回图片的像素宽*高大小
     *
     * @param bitmap
     * @return
     */
    public static long getBitmapSize(Bitmap bitmap)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
        {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @return 修改后的图片
     */
    public static Bitmap toRoundedGrayscale(Bitmap bmpOriginal, float roundPixels)
    {
        return getRoundedCornerBitmap(toGrayscale(bmpOriginal), roundPixels);
    }

    /**
     * 图片处理成圆角
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPixels)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        //        bmpOriginal.recycle();
        return bmpGrayscale;
    }

    /**
     * 根据屏幕大小加载图片，将超过屏幕的原图按比例缩放至屏幕大小
     *
     * @param path         图片路径
     * @param screenWidth  缩放宽度
     * @param screenHeight 缩放高度
     * @return 缩放后Bitmap
     */
    public static Bitmap loadImage(String path, int screenWidth, int screenHeight)
    {
        Bitmap bm = decodeFile(path, screenWidth, screenHeight, ScalingLogic.FIT);
        return bm;
    }

    /**
     * 根据屏幕大小加载图片，将超过屏幕的原图按比例缩放至屏幕大小
     *
     * @param screenWidth  屏幕宽度
     * @param screenHeight 屏幕高度
     * @return 缩放后Bitmap
     */
    public static Bitmap loadImageRes(Resources res, int id, int screenWidth, int screenHeight)
    {
        Bitmap bm = decodeResource(res, id, screenWidth, screenHeight, ScalingLogic.FIT);
        return bm;
    }

    /**
     * 根据屏幕大小加载图片，将超过屏幕的原图按比例缩放至屏幕大小
     *
     * @param res          Resources
     * @param path         图片路径
     * @param screenWidth  屏幕宽度
     * @param screenHeight 屏幕高度
     * @return 缩放后BitmapDrawable
     */
    public static BitmapDrawable loadImage(Resources res, String path, int screenWidth, int screenHeight)
    {
        return new BitmapDrawable(res, loadImage(path, screenWidth, screenHeight));
    }

    /**
     * Utility function for decoding an image resource. The decoded bitmap will
     * be optimized for further scaling to the requested destination dimensions
     * and scaling logic.
     *
     * @param res          The resources object containing the image data
     * @param resId        The resource id of the image data
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Decoded bitmap
     */
    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic)
    {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight,
                                                   scalingLogic);
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Utility function for decoding an image resource. The decoded bitmap will
     * be optimized for further scaling to the requested destination dimensions
     * and scaling logic.
     *
     * @param pathName     The image file path  of the image data
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Decoded bitmap
     */
    public static Bitmap decodeFile(String pathName, int dstWidth, int dstHeight, ScalingLogic scalingLogic)
    {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight,
                                                   scalingLogic);
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * Utility function for creating a scaled version of an existing bitmap
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth       Wanted width of destination bitmap
     * @param dstHeight      Wanted height of destination bitmap
     * @param scalingLogic   Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic)
    {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight,
                                        scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight,
                                        scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic)
    {
        if (scalingLogic == ScalingLogic.FIT)
        {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect)
            {
                return srcWidth / dstWidth;
            }
            else
            {
                return srcHeight / dstHeight;
            }
        }
        else
        {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect)
            {
                return srcHeight / dstHeight;
            }
            else
            {
                return srcWidth / dstWidth;
            }
        }
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     */
    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic)
    {
        if (scalingLogic == ScalingLogic.CROP)
        {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect)
            {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            }
            else
            {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        }
        else
        {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     */
    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
            ScalingLogic scalingLogic)
    {
        if (scalingLogic == ScalingLogic.FIT)
        {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect)
            {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            }
            else
            {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        }
        else
        {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     * <p/>
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     * <p/>
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     */
    public static enum ScalingLogic
    {
        CROP, FIT
    }

    /**
     * 压缩图片，压缩80%
     *
     * @param context  Context
     * @param filepath 图片路径
     */
    public static void resizePic(Context context, String filepath)
    {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        compress(displayMetrics.widthPixels, displayMetrics.heightPixels, 80, filepath);
    }

    /**
     * 缩放图片,并压缩50%
     *
     * @param context  Context
     * @param filepath 图片路径
     */
    public static void resizePic(Context context, String filepath, int width, int height)
    {
        compress(width, height, 50, filepath);
    }

    /**
     * 压缩图片
     *
     * @param width    图片宽度
     * @param height   图片高度
     * @param quality  压缩比例
     * @param filepath 图片路径
     */
    public static void compress(int width, int height, int quality, String filepath)
    {
        try
        {
            Bitmap bm = ScalingUtils.decodeFile(filepath, width, height, ScalingLogic.FIT);
            FileOutputStream fOut = new FileOutputStream(filepath);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            fOut.flush();
            fOut.close();
            bm.recycle();
            bm = null;
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
