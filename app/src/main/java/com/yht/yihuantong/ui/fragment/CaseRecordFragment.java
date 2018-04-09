package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.HealthDetailActivity;

import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * 患者病例
 *
 * @author DUNDUN
 */
public class CaseRecordFragment extends BaseFragment
{
    private LinearLayout llAddNewHealth;
    private AutoLoadRecyclerView autoLoadRecyclerView;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_health_record;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_health_record_recycler);
        llAddNewHealth = view.findViewById(R.id.fragment_health_record_add);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        llAddNewHealth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_health_record_add:
                Intent intent = new Intent(getContext(), HealthDetailActivity.class);
                intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH,true);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
