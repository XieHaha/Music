package com.zyc.doctor.widgets.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zyc.doctor.utils.ImageLoadUtil;
import com.zyc.doctor.widgets.recyclerview.callback.LoadFinishCallBack;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

/**
 *
 * @author zhaokaiqiang
 * @date 15/4/9
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack
{
    private LoadMoreListener loadMoreListener;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadingMore;
    /**
     * 是否可以加载更多
     */
    private boolean canLoadMore = true;

    public AutoLoadRecyclerView(Context context)
    {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        isLoadingMore = false;
        addOnScrollListener(
                new AutoLoadScrollListener(ImageLoadUtil.getInstance().getImageLoader(), true,
                                           true));
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener)
    {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void loadFinish()
    {
        loadFinish(true);
    }

    public void loadFinish(boolean canLoadMore)
    {
        isLoadingMore = false;
        this.canLoadMore = canLoadMore;
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener
    {
        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll,
                boolean pauseOnFling)
        {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            super.onScrolled(recyclerView, dx, dy);
            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager)
            {
                int lastVisibleItem = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();
                //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
                if (canLoadMore && loadMoreListener != null && !isLoadingMore &&
                    lastVisibleItem >= totalItemCount - 2 && dy > 0)
                {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {
//            if (imageLoader != null)
//            {
//                switch (newState)
//                {
//                    case SCROLL_STATE_IDLE:
//                        imageLoader.resume();
//                        break;
//                    case SCROLL_STATE_DRAGGING:
//                        if (pauseOnScroll)
//                        {
//                            imageLoader.pause();
//                        }
//                        else
//                        {
//                            imageLoader.resume();
//                        }
//                        break;
//                    case SCROLL_STATE_SETTLING:
//                        if (pauseOnFling)
//                        {
//                            imageLoader.pause();
//                        }
//                        else
//                        {
//                            imageLoader.resume();
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
        }
    }
}
