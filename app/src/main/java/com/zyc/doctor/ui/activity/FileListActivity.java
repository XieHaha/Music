package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.adapter.FileListAdapter;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.MimeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import custom.frame.bean.FileBean;
import custom.frame.bean.NormImage;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * Created by dundun on 18/8/24.
 */
public class FileListActivity extends BaseActivity implements FileListAdapter.OpenFileListener {
    @BindView(R.id.act_file_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    private FileListAdapter fileListAdapter;
    private ArrayList<FileBean> fileBeans = new ArrayList<>();
    private String urls;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_file_list;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("报告列表");
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            urls = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
        }
        if (!TextUtils.isEmpty(urls)) {
            String[] values = urls.split(",");
            for (int i = 0; i < values.length; i++) {
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
        fileListAdapter.setOpenFileListener(this);
        autoLoadRecyclerView.setAdapter(fileListAdapter);
    }

    @Override
    public void onOpen(int position, String path, String fileName) {
        String type = MimeUtils.getMime(FileUtils.getFileExtNoPoint(path));
        if ("image/bmp".equals(type) || "image/gif".equals(type) || "image/jpeg".equals(type) ||
                "image/png".equals(type)) {

            ArrayList<NormImage> imageList = new ArrayList<>();
            NormImage normImage = new NormImage();
            normImage.setImagePath(path);
            imageList.add(normImage);
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, imageList);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.keep);
        } else {
            FileDisplayActivity.show(this, path, fileName);
        }
    }
}
