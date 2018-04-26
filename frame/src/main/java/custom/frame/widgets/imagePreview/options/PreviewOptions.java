package custom.frame.widgets.imagePreview.options;

import android.graphics.Color;
import android.os.Environment;

/**
 * Created by Kyle on 2015/12/11.
 * 配置ImagePreview工具相关类参数
 */
public class PreviewOptions {

    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getPath();
    public static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();

    public static final class AsyncLoadBmpServiceOptions {
        public static final int MAX_CONCURRENT_THREAD_COUNT = 7;
    }

    public static final class PreviewViewOptions {
        public static int CIRCLE_COLOR = Color.parseColor("#AAFFFFFF");
        public static float RADIUS = 50.0f;
        public static int BUFFER_MAX_LENGTH = 1024;
        public static final int CONNECT_TIME_OUT = 5000;
        public static String REQUEST_METHOD = "GET";
    }

    public static final class ImageDownloadOptions {
        private static final String APP_FILE_DIR = EXTERNAL_STORAGE;
        public static final String Image_SAVE_ABS_DIR = APP_FILE_DIR + "/com.dz.tt/image/";
        public static final String IMAGE_SAVE_REL_DIR = "com.dz.tt/image";
    }

    public static final class DiskCacheOptions {
        //最大图片缓存空间（单位：字节）
        public static final int CACHE_SIZE = 12 * 1024 * 1024;
        //图片质量（0~100）
        public static int IMAGE_QUALITY = 100;
        //缓存文件后缀
        public static final String TEMP_IMAGE_POSTFIX = ".b_tmp";
    }

}