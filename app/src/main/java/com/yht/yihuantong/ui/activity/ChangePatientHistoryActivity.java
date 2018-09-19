package com.yht.yihuantong.ui.activity;

import android.content.Intent;
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
import com.yht.yihuantong.ui.fragment.ChangePatientFromFragment;
import com.yht.yihuantong.ui.fragment.ChangePatientToFragment;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.view.ViewPrepared;

/**
 * 转诊记录
 *
 * @author DUNDUN
 */
public class ChangePatientHistoryActivity extends BaseActivity
{
    private Button btnBaseInfo, btnHealthRecord;
    private TextView tvTitle;
    private ViewPager viewPager;
    private View viewIndicator;
    private FragmentVpAdapter fragmentVpAdapter;
    private ChangePatientToFragment changePatientToFragment;
    private ChangePatientFromFragment changePatientFromFragment;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.act_change_patient;
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
        viewPager = (ViewPager)findViewById(R.id.act_health_card_viewpager);
        viewIndicator = findViewById(R.id.act_health_card_indicator);
        btnBaseInfo = (Button)findViewById(R.id.act_health_card_base_info);
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        btnHealthRecord = (Button)findViewById(R.id.act_health_card_health_record);
        tvTitle.setText("转诊");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(btnBaseInfo, (w, h) ->
        {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        changePatientFromFragment = new ChangePatientFromFragment();
        changePatientToFragment = new ChangePatientToFragment();
        fragmentList.add(changePatientFromFragment);
        fragmentList.add(changePatientToFragment);
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
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_health_card_base_info:
                viewPager.setCurrentItem(0);
                break;
            case R.id.act_health_card_health_record:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            default:
                break;
        }
    }
}
