package com.zyc.doctor.widgets.imagePreview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zyc.doctor.R;

/**
 * @author dundun
 */
public class NavigaterPageIndex extends LinearLayout {
    private Context mContext;
    /**
     * 用户引导页总数
     */
    private int mPageCount;
    /**
     * 点与点之间的间隔
     */
    private int paddingIndex;
    /**
     * 距离父布局底部的距离
     */
    private int paddingBottom;
    private SparseArray<View> mViewArray = new SparseArray<View>();
    private ImageView pageImageView;

    public NavigaterPageIndex(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mContext = paramContext;
        TypedArray a = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NavigaterPageIndex);
        paddingIndex = a.getDimensionPixelSize(R.styleable.NavigaterPageIndex_indexPadding, 0);
        paddingBottom = a.getDimensionPixelSize(R.styleable.NavigaterPageIndex_paddingBottom, 0);
        a.recycle();
    }

    private void addPageIndex(int currentPageIndex) {
        removeAllViews();
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        if (currentPageIndex > mPageCount) {
            return;
        }
        for (int j = 0; j < mPageCount; j++) {
            if (currentPageIndex == j) {
                pageImageView = (ImageView)mViewArray.get(Integer.valueOf(j), null);
                if (pageImageView == null) {
                    pageImageView = new ImageView(mContext);
                    pageImageView.setPadding(paddingIndex, 0, paddingIndex, paddingBottom);
                    mViewArray.put(Integer.valueOf(j), pageImageView);
                }
                else {
                    pageImageView.setPadding(paddingIndex, 0, paddingIndex, paddingBottom);
                }
                pageImageView.setImageResource(R.drawable.circle_blue_splash_small);
            }
            else {
                pageImageView = (ImageView)mViewArray.get(Integer.valueOf(j), null);
                if (pageImageView == null) {
                    pageImageView = new ImageView(mContext);
                    pageImageView.setPadding(paddingIndex, 0, paddingIndex, 0);
                    mViewArray.put(Integer.valueOf(j), pageImageView);
                }
                else {
                    pageImageView.setPadding(paddingIndex, 0, paddingIndex, 0);
                }
                pageImageView.setImageResource(R.drawable.circle_gray_splash_small);
            }
            addView(pageImageView);
        }
    }

    public final void initPageIndex(int initIndex) {
        mPageCount = initIndex;
        addPageIndex(0);
    }

    public final void changePageIndex(int changeIndex) {
        addPageIndex(changeIndex);
    }
}
