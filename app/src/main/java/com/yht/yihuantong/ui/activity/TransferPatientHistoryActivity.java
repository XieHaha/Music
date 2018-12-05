package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.FragmentVpAdapter;
import com.yht.yihuantong.ui.fragment.TransferPatientFromFragment;
import com.yht.yihuantong.ui.fragment.TransferPatientToFragment;

import java.util.ArrayList;
import java.util.List;

import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.view.ViewPrepared;

/**
 * Created by dundun on 18/10/11.
 * 转诊历史
 */
public class TransferPatientHistoryActivity extends BaseActivity
{
    private Button to, from;
    private ViewPager viewPager;
    private View viewIndicator;
    private FragmentVpAdapter fragmentVpAdapter;
    /**
     * 患者基础信息
     */
    private TransferPatientFromFragment transferPatientFromFragment;
    /**
     * 患者病例
     */
    private TransferPatientToFragment transferPatientToFragment;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_transfer_patient_history;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("转诊记录");
        viewPager = (ViewPager)findViewById(R.id.act_health_card_viewpager);
        viewIndicator = findViewById(R.id.act_health_card_indicator);
        to = (Button)findViewById(R.id.act_health_card_base_info);
        from = (Button)findViewById(R.id.act_health_card_health_record);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(to, (w, h) ->
        {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        transferPatientFromFragment = new TransferPatientFromFragment();
        transferPatientToFragment = new TransferPatientToFragment();
        fragmentList.add(transferPatientToFragment);
        fragmentList.add(transferPatientFromFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        to.setOnClickListener(this);
        from.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int tabWidth = to.getWidth();
                viewIndicator.setTranslationX((position * tabWidth) + (positionOffset * tabWidth));
            }

            @Override
            public void onPageSelected(int position)
            {
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_health_card_base_info:
                viewPager.setCurrentItem(0);
                break;
            case R.id.act_health_card_health_record:
                viewPager.setCurrentItem(1);
                break;
            case R.id.fragment_cooperate_apply_layout:
                break;
        }
    }
}
