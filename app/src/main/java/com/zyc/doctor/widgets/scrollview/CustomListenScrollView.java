package com.zyc.doctor.widgets.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author dundun
 * @date 18/5/9
 * 监听ScroView的滑动情况，比如滑动了多少距离，
 */
public class CustomListenScrollView extends ScrollView {
    private OnScrollChangeListener mOnScrollChangeListener;

    /**
     * 设置滚动接口
     *
     * @param
     */
    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    public CustomListenScrollView(Context context) {
        super(context);
    }

    public CustomListenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 定义一个滚动接口
     */
    public interface OnScrollChangeListener {
        /**
         * 监听
         *
         * @param scrollView
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        void onScrollChanged(CustomListenScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    /**
     * 当scrollView滑动时系统会调用该方法,并将该回调放过中的参数传递到自定义接口的回调方法中,
     * 达到scrollView滑动监听的效果
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
