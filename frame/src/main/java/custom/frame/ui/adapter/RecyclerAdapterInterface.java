package custom.frame.ui.adapter;

import android.view.View;

import java.util.List;

/**
 * Created by luozi on 2016/1/12.
 */
public interface RecyclerAdapterInterface<T> {
    /**
     * 得到列表
     */
    List<T> getList();

    /**
     * 添加到列表
     *
     * @param list
     */
    void addList(List<T> list);

    /**
     * 设置列表
     *
     * @param list
     */
    void setList(List<T> list);

    /**
     * 清空列表
     */
    void clearList();

    /**
     * 得到单个item
     *
     * @param position
     */
    T getItem(int position);


    /**
     * 改变单个item，帶动画
     *
     * @param item
     * @param position
     * @param isAnim   是否动画改变
     */
    void setItem(T item, int position, boolean isAnim);

    /**
     * 添加单个item
     *
     * @param item
     */
    void addItem(T item);

    /**
     * 添加单个itme，带添加动画
     *
     * @param item
     * @param position 添加动画的位置
     */
    void addItemByAnim(T item, int position);

    /**
     * 删除单个item
     */
    void deleteItem(T item);

    /**
     * 删除单个item，带动画
     *
     * @param position 删除的item位置
     */
    void deleteItemByAnim(int position);

    //=============header or footer deal

    /**
     * 添加header
     */
    void addHeaderView(View v);

    /**
     * 移除header
     */
    void removeHeaderView(View v);

    /**
     * 移除header
     */
    void removeHeaderView(int index);

    /**
     * 得到headers  count
     */
    int getHeadersCount();

    /**
     * 添加footer
     */
    void addFooterView(View v);

    /**
     * 移除footer
     */
    void removeFooterView(View v);

    /**
     * 移除footer
     */
    void removeFooterView(int index);

    /**
     * 得到footer  count
     */
    int getFooterCount();
}
