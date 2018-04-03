package custom.widgets.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import custom.base.utils.ImageLoadUtil;

/**
 * Created by thl on 2016-03-25.
 */
public class SyncImageView extends ImageView {

    public SyncImageView(Context context) {
        super(context);
    }

    public SyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SyncImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param defaultResourceId
     */
    public void displayImage(String url, @DrawableRes int defaultResourceId) {
        if (url == null || url.isEmpty()) return;
        ImageLoadUtil.getInstance().displayImage(url, this, defaultResourceId);
    }

}
