package custom.frame.widgets.imagePreview.cache.memory.impl;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import custom.frame.widgets.imagePreview.cache.memory.IImageMemoryCache;

/**
 * Created by Kyle on 2015/12/14.
 */
public class LruBitmapMemoryCache implements IImageMemoryCache
{

    private LruCache<String, Bitmap> lruCache;

    public LruBitmapMemoryCache(){
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(mCacheSize ){
            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 增
     *
     * @param key
     * @param value
     */
    @Override
    public boolean put(String key, Bitmap value) {
        if( lruCache.put(key, value) == null ){
            return true;
        }
        return false;
    }

    /**
     * 查
     *
     * @param key
     */
    @Override
    public Bitmap get(String key) {
        return lruCache.get(key);
    }

    /**
     * 删
     *
     * @param key
     */
    @Override
    public Bitmap remove(String key) {
        return lruCache.remove(key);
    }

    /**
     * 删所有内存缓存
     */
    @Override
    public void clear() {
        lruCache.evictAll();
    }

}