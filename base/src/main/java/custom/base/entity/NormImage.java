package custom.base.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by luozi on 2016/6/2.
 */
public class NormImage implements Serializable {
    private static final long serialVersionUID = -6229093380433256099L;
    String bigImageUrl;//大图图片本地连接地址
    Bitmap bigBitmap;//大图位图对象
    String bigImageName;//大图名称没有后缀

    String middleImageUrl;//中图图片本地连接地址
    Bitmap middleBitmap;//中图位图对象
    String middleImageName;//中图名称没有后缀

    String smallImageUrl;//小图图片本地连接地址
    Bitmap smallBitmap;//小图位图对象
    String smallImageName;//小图名称没有后缀


    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public NormImage setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
        return this;
    }

    public String getMiddleImageUrl() {
        return middleImageUrl;
    }

    public NormImage setMiddleImageUrl(String middleImageUrl) {
        this.middleImageUrl = middleImageUrl;
        return this;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public NormImage setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
        return this;
    }

    public Bitmap getBigBitmap() {
        return bigBitmap;
    }

    public NormImage setBigBitmap(Bitmap bigBitmap) {
        this.bigBitmap = bigBitmap;
        return this;
    }

    public Bitmap getMiddleBitmap() {
        return middleBitmap;
    }

    public NormImage setMiddleBitmap(Bitmap middleBitmap) {
        this.middleBitmap = middleBitmap;
        return this;
    }

    public Bitmap getSmallBitmap() {
        return smallBitmap;
    }

    public NormImage setSmallBitmap(Bitmap smallBitmap) {
        this.smallBitmap = smallBitmap;
        return this;
    }

    public String getBigImageName() {
        return bigImageName;
    }

    public NormImage setBigImageName(String bigImageName) {
        this.bigImageName = bigImageName;
        return this;
    }

    public String getMiddleImageName() {
        return middleImageName;
    }

    public NormImage setMiddleImageName(String middleImageName) {
        this.middleImageName = middleImageName;
        return this;
    }

    public String getSmallImageName() {
        return smallImageName;
    }

    public NormImage setSmallImageName(String smallImageName) {
        this.smallImageName = smallImageName;
        return this;
    }

    public void recyclerBitmaps() {
        if (bigBitmap != null) bigBitmap.recycle();
        if (middleBitmap != null) middleBitmap.recycle();
        if (smallBitmap != null) smallBitmap.recycle();
    }
}
