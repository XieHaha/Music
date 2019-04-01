package com.zyc.doctor.ui.adapter.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zyc.doctor.ui.base.adapter.HFViewHolder;
import com.zyc.doctor.ui.base.adapter.RecyclerAdapterInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dundun
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>
        implements RecyclerAdapterInterface<T> {
    /**
     * 支撑recyclerview的list对象
     */
    protected List<T> list;
    /**
     * 列表item单击监听
     */
    private OnItemClickListener onItemClickListener = null;
    /**
     * 列表item长按监听
     */
    private OnItemLongClickListener onItemLongClickListener = null;
    /**
     * header列表集合
     */
    private List<View> headers;
    /**
     * header列表集合
     */
    private List<View> footers;
    /**
     * 头部类型标记
     */
    protected static final int HEADER_TYPE = 0x1001, CONTENT_TYPE = 0x1002, FOOTER_TYPE = 0x1003;
    /**
     * 头部尾部引用序列号,在获取viewholer的时候单调递增
     */
    private int headerIndex = 0, footerIndex;

    public BaseRecyclerAdapter(List<T> list) {
        this.list = list;
        headers = new ArrayList<>();
        footers = new ArrayList<>();
    }

    /**
     * 请复写此方法时一定带上super父类此方法
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        /**
         * 如果点击的是header则不做后续处理
         * */
        if (holder.getAdapterPosition() < getHeadersCount()) {
            return;
        }
        /**
         * 如果点击的是footer则不做后续处理
         * */
        if (holder.getAdapterPosition() > list.size() + getHeadersCount() - 1) { return; }
        position -= getHeadersCount();
        onBindViewHolder(holder, position, getItem(holder.getAdapterPosition()));
    }

    /**
     * 自定义带item的绑定viewholder
     */
    public void onBindViewHolder(final BaseViewHolder holder, final int position, T item) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position,
                                                    list.get(holder.getAdapterPosition() - getHeadersCount()));
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(v, position,
                                                            list.get(holder.getAdapterPosition() - getHeadersCount()));
                    return true;
                }
            });
        }
    }

    /**
     * 请复写此方法时一定带上super父类此方法
     */
    @Override
    public int getItemCount() {
        return list == null ? 0 : (list.size() + getHeadersCount() + getFooterCount());
    }

    /**
     * 请复写此方法时一定带上super父类此方法
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) { return new HFViewHolder(headers.get(headerIndex++)); }
        else if (viewType == CONTENT_TYPE) { return onCreateViewHolder(parent); }
        else if (viewType == FOOTER_TYPE) { return new HFViewHolder(footers.get(footerIndex++)); }
        else { return onCreateViewHolder(parent, viewType); }
    }

    /**
     * 得到viewholder的抽象函数
     */
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent);

    @Override
    public int getItemViewType(int position) {
        if (position < getHeadersCount()) { return HEADER_TYPE; }
        if (position > list.size() + getHeadersCount() - 1) { return FOOTER_TYPE; }
        return CONTENT_TYPE;
    }

    /**
     * 当recyclerview 开始使用此adapter时则开始计算每行，栏数。
     */
    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager)manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //返回item占用数
                    return ((getItemViewType(position) == HEADER_TYPE) || (getItemViewType(position) == FOOTER_TYPE))
                           ? gridManager.getSpanCount()
                           : 1;
                }
            });
        }
    }

    //===========================接口通用处理
    @Override
    public List getList() {
        return list;
    }

    @Override
    public void addList(List list) {
        if (this.list != null) { this.list.addAll(list); }
        notifyDataSetChanged();
    }

    @Override
    public void setList(List list) {
        if (this.list != null) { this.list.clear(); }
        if (this.list != null) { this.list.addAll(list); }
        notifyDataSetChanged();
    }

    @Override
    public void clearList() {
        if (this.list != null) { this.list.clear(); }
        notifyDataSetChanged();
    }

    @Override
    public void setItem(T item, int position, boolean isAnim) {
        if (this.list == null) { return; }
        this.list.set(position, item);
        if (isAnim) {
            notifyDataSetChanged();
        }
        else {
            notifyItemChanged(position);
        }
    }

    @Override
    public T getItem(int position) {
        return list == null ? null : list.get(position - getHeadersCount());
    }

    @Override
    public void addItem(T item) {
        if (this.list != null) { this.list.add(item); }
        notifyDataSetChanged();
    }

    @Override
    public void addItemByAnim(T item, int position) {
        if (this.list != null) { this.list.add(item); }
        notifyItemInserted(position);
    }

    @Override
    public void deleteItem(T item) {
        if (this.list != null) { this.list.remove(item); }
        notifyDataSetChanged();
    }

    @Override
    public void deleteItemByAnim(int position) {
        if (position + 1 > list.size()) { return; }
        if (this.list != null) { this.list.remove(position); }
        notifyItemRemoved(position);
    }

    @Override
    public void addHeaderView(View v) {
        headers.add(v);
    }

    @Override
    public void removeHeaderView(View v) {
        if (v != null) { headers.remove(v); }
    }

    @Override
    public void removeHeaderView(int index) {
        if (headers.size() > index) { headers.remove(index); }
    }

    @Override
    public void addFooterView(View v) {
        footers.add(v);
    }

    @Override
    public void removeFooterView(View v) {
        if (v != null) { footers.remove(v); }
    }

    @Override
    public void removeFooterView(int index) {
        if (footers.size() > index) { footers.remove(index); }
    }

    @Override
    public int getHeadersCount() {
        return headers.size();
    }

    @Override
    public int getFooterCount() {
        return footers.size();
    }

    //===========================接口通用处理
    /////////////////////////监听回调
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View v, int position, T item);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View v, int position, T item);
    }
}