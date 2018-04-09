package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.activity.EditInfoActivity;

import custom.frame.ui.fragment.BaseFragment;

public class MyFragment extends BaseFragment
{
    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        view.findViewById(R.id.public_title_bar_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
