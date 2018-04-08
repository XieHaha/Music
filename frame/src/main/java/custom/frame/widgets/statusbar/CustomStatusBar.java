package custom.frame.widgets.statusbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import custom.frame.R;

/**
 * Created by luozi on 2015/9/15.
 */
public class CustomStatusBar extends LinearLayout
{
    private View topBarView = null;
    private Context context;
    //status height
    private int statusHeight = 0;
    //status color
    private int statusColor = 0xFF000000;

    public CustomStatusBar(Context context)
    {
        this(context, null);
    }

    public CustomStatusBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
    }

    public CustomStatusBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if (topBarView != null) {return;}
        setOrientation(VERTICAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar);
        statusColor = ta.getColor(R.styleable.CustomActionBar_statusColor, statusColor);
        statusHeight = getStatusHeight(context);
        initTopBar();
    }

    private void initTopBar()
    {
        if (Build.VERSION.SDK_INT >= 19)
        {
            topBarView = new View(context);
            topBarView.setLayoutParams(
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight));
            topBarView.setBackgroundColor(statusColor);
            topBarView.setVisibility(VISIBLE);
            addView(topBarView);
        }
        else
        {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setStatusColor(int color)
    {
        this.statusColor = color;
        topBarView.setBackgroundColor(statusColor);
    }

    /**
     * get status height
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context)
    {
        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(
                    clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
