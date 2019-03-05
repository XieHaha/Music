package com.yht.yihuantong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.fanneng.android.web.file.FileReaderView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.ui.activity.BaseActivity;

/**
 * describe：文件阅读类
 *
 * @author dundun 2019年3月5日17:33:04
 */
public class FileDisplayActivity extends BaseActivity
{
    private TextView tvTitle;
    private FileReaderView mDocumentReaderView;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_file_display;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.public_title_bar_title);
        mDocumentReaderView = findViewById(R.id.documentReaderView);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            tvTitle.setText(getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING));
            String url = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            mDocumentReaderView.show(url);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mDocumentReaderView != null)
        {
            mDocumentReaderView.stop();
        }
    }

    public static void show(Context context, String url, String fileName)
    {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, url);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, fileName);
        context.startActivity(intent);
    }
}
