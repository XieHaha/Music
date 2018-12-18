package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.FileListAdapter;
import com.yht.yihuantong.utils.AllUtils;

import java.util.ArrayList;

import custom.frame.bean.FileBean;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * Created by dundun on 18/8/24.
 */
public class FileListActivity extends BaseActivity
{
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private FileListAdapter fileListAdapter;
    private ArrayList<FileBean> fileBeans = new ArrayList<>();
    private String urls;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_file_list;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("报告列表");
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(R.id.act_file_recycler_view);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            urls = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
        }
        if (!TextUtils.isEmpty(urls))
        {
            String[] values = urls.split(",");
            for (int i = 0; i < values.length; i++)
            {
                String fileUrl = values[i];
                FileBean fileBean = new FileBean();
                fileBean.setFileUrl(fileUrl);
                fileBean.setFileName(
                        AllUtils.getFileName(fileUrl) + "." + AllUtils.getFileExtNoPoint(fileUrl));
                fileBean.setFileNameNoSuffix(AllUtils.getFileName(fileUrl));
                fileBean.setFileType(AllUtils.getFileExtNoPoint(fileUrl));
                fileBeans.add(fileBean);
            }
        }
        fileListAdapter = new FileListAdapter(this, fileBeans);
        autoLoadRecyclerView.setAdapter(fileListAdapter);
    }
}
