package com.zyc.doctor.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;

import com.zyc.doctor.tools.ThreadPoolHelper;

/**
 * @author zhangrj
 * @date 2017/4/21
 */
public class BarCodeImageView extends AppCompatImageView {
    /**
     * 生成二维码的字符串
     */
    private String content;
    /**
     * 二维码中间添加的图案
     */
    private int resId;

    public BarCodeImageView(Context context) {
        super(context);
    }

    public BarCodeImageView(Context context, String content) {
        super(context);
        this.content = content;
    }

    public BarCodeImageView(Context context, String content, int resId) {
        super(context);
        this.content = content;
        this.resId = resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && !TextUtils.isEmpty(content)) {
            ThreadPoolHelper.getInstance().execInCached(new Runnable() {
                @Override
                public void run() {
                    Bitmap logoBm = null;
                    if (resId > 0) {
                        logoBm = BitmapFactory.decodeResource(getResources(), resId);
                    }
                    final Bitmap bitmap = QrCodeHelper.createQRImage(content, getWidth(), getHeight(), logoBm);
                    if (bitmap != null) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            });
        }
    }
}
