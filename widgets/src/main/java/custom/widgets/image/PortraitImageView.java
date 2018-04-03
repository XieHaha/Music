package custom.widgets.image;

import android.content.Context;
import android.util.AttributeSet;

import custom.base.utils.ImageLoadUtil;

/**
 * Created by luozi on 2016/1/13.
 */
public class PortraitImageView extends CircleImageView {
    public PortraitImageView(Context context) {
        super(context);
    }

    public PortraitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 加载头像
     *
     * @param url
     */
    public void displayImage(String url) {
        if (url == null || url.isEmpty()) return;

        ImageLoadUtil.getInstance().displayPortraitImage(url, this);
    }
}
