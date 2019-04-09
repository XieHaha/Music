package com.zyc.doctor.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by DUNDUN on 17.11.14.
 */
public class FullListView extends ListView
{
    public FullListView(Context context)
    {
        super(context);
    }

    public FullListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FullListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
