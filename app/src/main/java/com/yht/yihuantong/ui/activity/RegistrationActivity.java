package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.RegistrationAdapter;
import com.yht.yihuantong.ui.dialog.HintDialog;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * 患者申请
 *
 * @author DUNDUN
 */
public class RegistrationActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, CommonData
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private TextView tvHintTxt, tvTitle;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsType, tvGoodsInfo;
    private TextView tvNext;
    private LinearLayout llGoodsDetaillayout;
    private View footerView;
    private RegistrationAdapter registrationAdapter;
    private List<String> hospitalList = new ArrayList<>();
    private List<String> goodsList = new ArrayList<>();
    private String curHospital;
    private String curGoods;
    private int type;
    private String typeName;
    /**
     * 显示商品列表
     * 1 医院列表   2 商品列表  3 商品详情
     */
    private int curPage;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_registration;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.act_apply_patient_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(
                R.id.act_apply_patient_recycler_view);
        tvHintTxt = (TextView)findViewById(R.id.act_registration_hint_txt);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        llGoodsDetaillayout = (LinearLayout)findViewById(R.id.act_registration_goods_detail_layout);
        tvNext = (TextView)findViewById(R.id.act_registration_next);
        tvGoodsName = (TextView)findViewById(R.id.act_registration_goods_name);
        tvGoodsType = (TextView)findViewById(R.id.act_registration_goods_type);
        tvGoodsInfo = (TextView)findViewById(R.id.act_registration_goods_info);
        tvGoodsPrice = (TextView)findViewById(R.id.act_registration_goods_price);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            type = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
            typeName = getIntent().getStringExtra(CommonData.KEY_REGISTRATION_TYPE);
        }
        for (int i = 0; i < 15; i++)
        {
            hospitalList.add("华西医院");
        }
        for (int i = 0; i < 15; i++)
        {
            goodsList.add("一般体检");
        }
        curPage = 1;
        ininPageData();
        ArrayList<String> list = new ArrayList<>();
        list.addAll(hospitalList);
        registrationAdapter = new RegistrationAdapter(this, list);
        registrationAdapter.addFooterView(footerView);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(registrationAdapter);
    }

    private void ininPageData()
    {
        tvTitle.setText(String.format(getString(R.string.txt_registration_type), typeName));
        tvHintTxt.setText(String.format(getString(R.string.txt_registration_type_hint), typeName));
        switch (type)
        {
            case TYPE_PRESCRIPTION:
                break;
            case TYPE_CHECK:
                break;
            case TYPE_HEALTH_CHECK:
                break;
            case TYPE_CHEMICAL:
                break;
        }
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvNext.setOnClickListener(this);
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        registrationAdapter.setOnItemClickListener((v, position, item) ->
                                                   {
                                                       if (curPage == 1)
                                                       {
                                                           curHospital = hospitalList.get(position);
                                                           curPage = 2;
                                                           tvHintTxt.setText(curHospital);
                                                           ArrayList<String> list = new ArrayList<>();
                                                           list.addAll(goodsList);
                                                           registrationAdapter.setList(list);
                                                           registrationAdapter.notifyDataSetChanged();
                                                       }
                                                       else if (curPage == 2)
                                                       {
                                                           curPage = 3;
                                                           curGoods = goodsList.get(position);
                                                           tvHintTxt.setText(
                                                                   curHospital + " 》 " + curGoods);
                                                           swipeRefreshLayout.setVisibility(
                                                                   View.GONE);
                                                           llGoodsDetaillayout.setVisibility(
                                                                   View.VISIBLE);
                                                           tvNext.setVisibility(View.VISIBLE);
                                                       }
                                                   });
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                if (onBack())
                {
                    finish();
                }
                break;
            case R.id.act_registration_next:
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.isShowCancelBtn(false);
                hintDialog.setContentString("已发送给患者，请等待患者付款");
                hintDialog.setOnEnterClickListener(() ->
                                                   {
                                                   });
                hintDialog.show();
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        switch (task)
        {
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        super.onResponseError(task, e);
        switch (task)
        {
        }
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (!onBack())
            {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean onBack()
    {
        if (curPage == 3)
        {
            curPage = 2;
            tvHintTxt.setText(curHospital);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            llGoodsDetaillayout.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
            ArrayList<String> list = new ArrayList<>();
            list.addAll(goodsList);
            registrationAdapter.setList(list);
            return false;
        }
        else if (curPage == 2)
        {
            curPage = 1;
            tvHintTxt.setText(
                    String.format(getString(R.string.txt_registration_type_hint), typeName));
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            ArrayList<String> list = new ArrayList<>();
            list.addAll(hospitalList);
            registrationAdapter.setList(list);
            return false;
        }
        return true;
    }
}
