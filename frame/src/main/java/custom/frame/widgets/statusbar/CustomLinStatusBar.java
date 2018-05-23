package custom.frame.widgets.statusbar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Created by luozi on 2015/9/15.
 */
public class CustomLinStatusBar extends LinearLayout {


    private Context context;

    private int statusHeight = 0;//status height

    public CustomLinStatusBar(Context context) {
        this(context, null);
        this.context = context;
    }

    public CustomLinStatusBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        this.context = context;
    }

    public CustomLinStatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        statusHeight = getStatusHeight(context);

        initTopBar();

    }

    private void initTopBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            setPadding(0, statusHeight, 0, 0);
        } else {

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= 19) {
//            setMeasuredDimension(widthMeasureSpec, dip2px(context, actionBarHeight + statusHeight));
//            super.onMeasure(widthMeasureSpec, dip2px(context, actionBarHeight + statusHeight));
        } else {
//            setMeasuredDimension(widthMeasureSpec, dip2px(context, actionBarHeight));
//            super.onMeasure(widthMeasureSpec, dip2px(context, actionBarHeight));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    /**
     * getObject status height
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
