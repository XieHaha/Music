package com.yht.yihuantong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.MsgListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 消息
 */
public class MsgFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View footerView;
    private TextView tvFooterHintTxt;
    private MsgListAdapter msgListAdapter;
    private List<String> msgList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 20;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("消息列表");
        swipeRefreshLayout = view.findViewById(R.id.fragment_msg_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_msg_recycler_view);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        msgListAdapter = new MsgListAdapter(this, msgList);
        msgListAdapter.addFooterView(footerView);
        mIRequest.getEaseAppKey(this);
    }

    @Override
    public void initListener()
    {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(msgListAdapter);
        msgListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<String>()
        {
            @Override
            public void onItemClick(View v, int position, String item)
            {
            }
        });
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENTS_LIST:
                if (response.getData() != null)
                {
                    msgList = response.getData();
                    if (page == 0)
                    {
                        msgListAdapter.setList(msgList);
                    }
                    else
                    {
                        msgListAdapter.addList(msgList);
                    }
                    msgListAdapter.notifyDataSetChanged();
                    if (msgList.size() < PAGE_SIZE)
                    {
                        tvFooterHintTxt.setText("暂无更多数据");
                        autoLoadRecyclerView.loadFinish(false);
                    }
                    else
                    {
                        tvFooterHintTxt.setText("上拉加载更多");
                        autoLoadRecyclerView.loadFinish(true);
                    }
                }
                break;
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(getContext(), "处理成功");
                break;
            case GET_EASE_APPKEY:
                Log.e("test", "key:" + response.getData());
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        if (page > 0)
        {
            page--;
        }
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        super.onResponseError(task, e);
        if (page > 0)
        {
            page--;
        }
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh()
    {
        page = 0;
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
    }
}
