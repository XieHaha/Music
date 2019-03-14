package custom.frame.widgets.imagePreview.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.IOException;
import java.util.List;

import custom.frame.utils.ImageLoadUtil;
import custom.frame.widgets.imagePreview.cache.disk.BitmapDiskLruCache;
import custom.frame.widgets.imagePreview.cache.memory.impl.LruBitmapMemoryCache;

/**
 * Created by Kyle on 2015/12/14.
 */
public class CacheUtils {

    private static CacheUtils instance = null;
    private Context context;

    LruBitmapMemoryCache memoryCache;
    BitmapDiskLruCache bitmapDiskLruCache;
    /**
     * -1 不请求； 0 只请求大图，缓存大图； 1 请求大图，缓存大图小图； 2 请求大小图，缓存大小图
     */
    private int LOAD_STATE = -1;

    private CacheUtils(Context context) {
        this.context = context;
        initCache();
    }

    public static CacheUtils getInstance(Context context) {
        if (instance == null) {
            instance = new CacheUtils(context);
        }
        return instance;
    }

    private void initCache() {
        if (bitmapDiskLruCache == null) {
            bitmapDiskLruCache = new BitmapDiskLruCache(context);
        }
        if (memoryCache == null) {
            memoryCache = new LruBitmapMemoryCache();
        }
    }

    public int getCacheState(final String smallUrl, final String bigUrl) {
        Bitmap bigBmp = null;
        Bitmap smallBmp = null;
        bigBmp = getBitmapFromCache(bigUrl);
        smallBmp = getBitmapFromImageLoaderCache(smallUrl);
        //////////
        if (smallUrl.equals(bigUrl)) {
            if (bigBmp != null) {
                LOAD_STATE = -1;
            } else {
                //URL相同，都为空，请求大图，显示默认背景
                LOAD_STATE = 0;
            }
        } else {
            if (bigBmp != null) {
                //URL不同，都不空,不请求
                LOAD_STATE = -1;
            } else {
                if (smallBmp != null) {
                    //URL不同，小图不空大图为空，请求大图，不显示默认背景
                    LOAD_STATE = 1;
                } else {
                    //URL不同，都为空，请求大图小图，显示默认背景
                    LOAD_STATE = 2;
                }
            }
        }
        return LOAD_STATE;
    }

    /**
     * 获取 imageLoader  缓存的图片
     * <p>
     * 使用此加载框架的imageloader加载的图片，设置了缓存后，下次使用，
     * 手工从缓存取出来用，这时特别要注意，不能直接使用：
     * imageLoader.getMemoryCache().getObject(uri)来获取，因为在加载过程中，
     * key是经过运算的，而不单单是uri
     *
     * @param imageUri
     * @return
     */
    public Bitmap getBitmapFromImageLoaderCache(String imageUri) {
        Bitmap bitmap = null;

        MemoryCache memoryCache = ImageLoadUtil.getInstance().getImageLoader().getMemoryCache();
        List<String> memoryKeyList = MemoryCacheUtils.findCacheKeysForImageUri(imageUri, memoryCache);
        if (memoryKeyList != null && memoryKeyList.size() > 0) {
            bitmap = memoryCache.get(memoryKeyList.get(0));
        }
        return bitmap;
    }

    public Bitmap getBitmapFromCache(String imageUri) {
        Bitmap bmp = null;
        //先从缓存获取
        bmp = getFromMemory(imageUri);
        if (bmp != null) {
            return bmp;
        }
        bmp = getFromDisk(imageUri);
        if (bmp != null) {
            addToMemory(imageUri, bmp);
            return bmp;
        }
        return null;
    }

    /**
     * 磁盘缓存管理
     */
    public boolean addToDisk(String imageUri, Bitmap bitmap) throws IOException {
        return bitmapDiskLruCache.cacheImageFileToDisk(imageUri, bitmap);
    }

    public void removeFromDisk(String imageUri) {
        bitmapDiskLruCache.removeBitmapFromDiskCache(imageUri);
    }

    public Bitmap getFromDisk(String imageUri) {
        return bitmapDiskLruCache.getBitmapFromDiskCache(imageUri);
    }

    /**
     * 内存缓存管理
     */
    public boolean addToMemory(String imageUri, Bitmap value) {
        String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
        return memoryCache.put(fileName, value);
    }

    public boolean removeFromMemory(String imageUri) {
        String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
        if (memoryCache.remove(fileName) != null) {
            return true;
        }
        return false;
    }

    public Bitmap getFromMemory(String key) {
        String fileName = key.substring(key.lastIndexOf("/") + 1);
        return memoryCache.get(fileName);
    }

    /**
     * 保存图片到磁盘 1
     */
    public boolean saveImageFileToDisk(String imageUri, Bitmap bitmap) {
        return bitmapDiskLruCache.saveImageFileToDisk(imageUri, bitmap);
    }

    /**
     * 保存图片到磁盘 2
     */
    public boolean saveImageFileToDisk(String imageUri) {
        Bitmap bmp = null;
        //先从内存缓存获取
        bmp = getFromMemory(imageUri);
        if (bmp != null) {
            return bitmapDiskLruCache.saveImageFileToDisk(imageUri, bmp);
        }
        //内存缓存没有再从外存缓存取
        bmp = getFromDisk(imageUri);
        if (bmp != null) {
            addToMemory(imageUri, bmp);
            return bitmapDiskLruCache.saveImageFileToDisk(imageUri, bmp);
        }
        return false;
    }

}