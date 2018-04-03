package com.yht.yihuantong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;

import custom.frame.ui.fragment.BaseFragment;

public class DocFragment extends BaseFragment
{
    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_doc;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("我的医生");
    }
}
