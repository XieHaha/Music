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
import com.yht.yihuantong.ui.fragment.BaseInfoFragment;
import com.yht.yihuantong.ui.fragment.HealthRecordFragment;

import java.util.ArrayList;
import java.util.List;

import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.view.ViewPrepared;

/**
 * 患者健康卡
 *
 * @author DUNDUN
 */
public class HealthCardActivity extends BaseActivity
{
    private Button btnBaseInfo, btnHealthRecord;
    private ViewPager viewPager;
    private View viewIndicator;
    private FragmentVpAdapter fragmentVpAdapter;
    /**
     * 患者基础信息
     */
    private BaseInfoFragment baseInfoFragment;
    /**
     * 患者病例
     */
    private HealthRecordFragment healthRecordFragment;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.act_health_card;
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
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("健康卡");
        viewPager = (ViewPager)findViewById(R.id.act_health_card_viewpager);
        viewIndicator = findViewById(R.id.act_health_card_indicator);
        btnBaseInfo = (Button)findViewById(R.id.act_health_card_base_info);
        btnHealthRecord = (Button)findViewById(R.id.act_health_card_health_record);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(btnBaseInfo, new ViewPrepared.OnPreDrawFinishListener()
        {
            @Override
            public void onPreDrawFinish(int w, int h)
            {
                ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
                params.width = w;
                viewIndicator.setLayoutParams(params);
            }
        });
        baseInfoFragment = new BaseInfoFragment();
        healthRecordFragment = new HealthRecordFragment();
        fragmentList.add(baseInfoFragment);
        fragmentList.add(healthRecordFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        btnBaseInfo.setOnClickListener(this);
        btnHealthRecord.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int tabWidth = btnBaseInfo.getWidth();
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
    public void onClick(View v, int clickID)
    {
        if (clickID == btnBaseInfo.getId())
        {
            viewPager.setCurrentItem(0);
        }
        else if (clickID == btnHealthRecord.getId())
        {
            viewPager.setCurrentItem(1);
        }
    }
}
