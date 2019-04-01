package com.zyc.doctor.http.data;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by luozi on 2016/6/2.
 */
public class NormImage implements Serializable
{
    private static final long serialVersionUID = -6229093380433256099L;
    /**
     * 大图图片本地连接地址
     */
    String bigImageUrl;
    /**
     * 大图位图对象
     */
    Bitmap bigBitmap;
    /**
     * 大图名称没有后缀
     */
    String bigImageName;
    /**
     * 中图图片本地连接地址
     */
    String middleImageUrl;
    /**
     * 中图位图对象
     */
    Bitmap middleBitmap;
    /**
     * 中图名称没有后缀
     */
    String middleImageName;
    /**
     * 小图图片本地连接地址
     */
    String smallImageUrl;
    /**
     * 小图位图对象
     */
    Bitmap smallBitmap;
    /**
     * 小图名称没有后缀
     */
    String smallImageName;
    /**
     * 本地地址
     */
    String imagePath;

    public String getBigImageUrl()
    {
        return bigImageUrl;
    }

    public NormImage setBigImageUrl(String bigImageUrl)
    {
        this.bigImageUrl = bigImageUrl;
        return this;
    }

    public String getMiddleImageUrl()
    {
        return middleImageUrl;
    }

    public NormImage setMiddleImageUrl(String middleImageUrl)
    {
        this.middleImageUrl = middleImageUrl;
        return this;
    }

    public String getSmallImageUrl()
    {
        return smallImageUrl;
    }

    public NormImage setSmallImageUrl(String smallImageUrl)
    {
        this.smallImageUrl = smallImageUrl;
        return this;
    }

    public Bitmap getBigBitmap()
    {
        return bigBitmap;
    }

    public NormImage setBigBitmap(Bitmap bigBitmap)
    {
        this.bigBitmap = bigBitmap;
        return this;
    }

    public Bitmap getMiddleBitmap()
    {
        return middleBitmap;
    }

    public NormImage setMiddleBitmap(Bitmap middleBitmap)
    {
        this.middleBitmap = middleBitmap;
        return this;
    }

    public Bitmap getSmallBitmap()
    {
        return smallBitmap;
    }

    public NormImage setSmallBitmap(Bitmap smallBitmap)
    {
        this.smallBitmap = smallBitmap;
        return this;
    }

    public String getBigImageName()
    {
        return bigImageName;
    }

    public NormImage setBigImageName(String bigImageName)
    {
        this.bigImageName = bigImageName;
        return this;
    }

    public String getMiddleImageName()
    {
        return middleImageName;
    }

    public NormImage setMiddleImageName(String middleImageName)
    {
        this.middleImageName = middleImageName;
        return this;
    }

    public String getSmallImageName()
    {
        return smallImageName;
    }

    public NormImage setSmallImageName(String smallImageName)
    {
        this.smallImageName = smallImageName;
        return this;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public void recyclerBitmaps()
    {
        if (bigBitmap != null) { bigBitmap.recycle(); }
        if (middleBitmap != null) { middleBitmap.recycle(); }
        if (smallBitmap != null) { smallBitmap.recycle(); }
    }
}
