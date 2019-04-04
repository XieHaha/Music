package com.zyc.doctor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zyc.doctor.R;

/**
 * @author dundun
 */
public class ImageLoadUtil {
    /**
     * 磁盘最大缓存,50M
     */
    private final int MAX_DISK_CACHE = 1024 * 1024 * 50;
    /**
     * 内存最大缓存,10M
     */
    private final int MAX_MEMORY_CACHE = 1024 * 1024 * 10;
    /**
     * iamgeloader
     */
    private ImageLoader imageLoader;
    /**
     * 单例模式
     */
    private static ImageLoadUtil instance = null;

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            synchronized (ImageLoadUtil.class) {
                imageLoader = ImageLoader.getInstance();
            }
        }
        return imageLoader;
    }

    public static ImageLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImageLoadUtil();
        }
        return instance;
    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.memoryCacheSize(MAX_MEMORY_CACHE);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        getImageLoader().init(config.build());
    }

    /**
     * 自定义加载中肖像图片
     *
     * @param url
     * @param target
     */
    public void displayPortraitImage(String url, ImageView target) {
        imageLoader.displayImage(url, target, getOptionsForPortrait());
    }

    /**
     * 自定义加载中图片
     *
     * @param url
     * @param target
     */
    public void displayImage(String url, ImageView target, @DrawableRes int defaultResourceId) {
        imageLoader.displayImage(url, target, getOptionsForPictureList(defaultResourceId));
    }

    /**
     * 设置图片放缩类型为模式EXACTLY，用于图片详情页的缩放
     *
     * @return
     */
    public DisplayImageOptions getOptionForExactlyType() {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 加载头像专用Options，默认加载中、失败和空url为
     *
     * @return
     */
    public DisplayImageOptions getOptionsForPortrait() {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                //                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.icon_default_img)
                .showImageOnFail(R.mipmap.icon_default_img)
                .showImageOnLoading(R.mipmap.icon_default_img)
                //                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
    }

    /**
     * 加载图片列表专用，加载前会重置View
     * {@link DisplayImageOptions.Builder#resetViewBeforeLoading} = true
     *
     * @param loadingResource
     * @return
     */
    public DisplayImageOptions getOptionsForPictureList(@DrawableRes int loadingResource) {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(loadingResource)
                .considerExifParams(true)
                //                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(loadingResource)
                .showImageOnFail(loadingResource)
                .build();
    }
}
