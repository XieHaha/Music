package com.yht.yihuantong.tools;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.yht.yihuantong.R;

public final class GlideHelper {
    public static RequestOptions options;

    public static RequestOptions getOptions() {
        if (options == null) {
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.icon_default_imgs)
                    .error(R.mipmap.icon_default_imgs)
                    .priority(Priority.NORMAL);
        }
        return options;
    }

    /**
     * 矩形头像
     * @return
     */
    public static RequestOptions getOptionsRect() {
        if (options == null) {
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.icon_default_imgs_rect)
                    .error(R.mipmap.icon_default_imgs_rect)
                    .priority(Priority.NORMAL);
        }
        return options;
    }
}
