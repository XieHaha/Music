package com.zyc.doctor.utils.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * @author 顿顿
 * @date 19/4/3 14:45
 * @des
 */
public class GlideEngine implements ImageEngine {
    public static RequestOptions options, options2;

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context).asBitmap().load(uri).apply(getOptions(placeholder, resize)).into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context).asBitmap().load(uri).apply(getOptions(placeholder, resize)).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context).load(uri).apply(getOptions2(resizeX, resizeY)).into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context).asGif().load(uri).apply(getOptions2(resizeX, resizeY)).into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }

    private RequestOptions getOptions(Drawable placeholder, int resize) {
        if (options == null) {
            options = new RequestOptions().override(resize, resize).placeholder(placeholder).centerCrop();
        }
        return options;
    }

    private RequestOptions getOptions2(int resizeX, int resizeY) {
        if (options2 == null) {
            options2 = new RequestOptions().override(resizeX, resizeY).priority(Priority.HIGH).fitCenter();
        }
        return options2;
    }
}
