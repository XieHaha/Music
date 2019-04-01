package com.zyc.doctor.ui.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author luozi
 * @date 2016/1/13
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    /**
     * 基础构造函数
     */
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 展示view
     */
    public abstract void showView(int position, T item);


}
