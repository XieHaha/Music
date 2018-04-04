package com.yht.yihuantong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;

import com.yht.yihuantong.R;
import com.yht.yihuantong.YiHTApplication;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.tools.DirHelper;
import com.yht.yihuantong.utils.AllUtils;

import java.io.File;
import java.util.ArrayList;

import custom.frame.ui.activity.BaseActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteInfoActivity extends BaseActivity
{
    private CircleImageView ivHeadImg;
    private Uri originUri;
    private Uri cutFileUri;
    private String tHeadPath;
    /**
     * 请求修改头像
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * 图片  裁剪
     */
    public static final int RC_CROP_IMG = RC_PICK_IMG + 1;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_complete_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.act_complete_info_next).setOnClickListener(this);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_complete_info_headimg);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        ivHeadImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_complete_info_next:
                startActivity(new Intent(this, CompleteInfo2Activity.class));
                break;
            case R.id.act_complete_info_headimg:
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == RC_PICK_IMG)
            {
                ArrayList<String> paths = data.getStringArrayListExtra(CommonData.KEY_CHECKED_PATH);
                if (null != paths && 0 != paths.size())
                {
                    //系统裁剪
                    String path =
                            DirHelper.getPathTemp() + "/" + System.currentTimeMillis() + ".jpg";
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        originUri = FileProvider.getUriForFile(this,
                                                               getApplication().getPackageName() +
                                                               ".fileprovider",
                                                               new File(paths.get(0)));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    else
                    {
                        originUri = Uri.parse("file://" + paths.get(0));
                    }
                    cutFileUri = Uri.parse("file://" + path);
                    intent.setDataAndType(originUri, "image/*");
                    intent.putExtra("crop", "true");
                    // aspectX aspectY 是宽高的比例
                    if (Build.BRAND.toUpperCase().contains("HONOR") ||
                        Build.BRAND.toUpperCase().contains("HUAWEI"))
                    {//华为特殊处理 不然会显示圆
                        intent.putExtra("aspectX", 9998);
                        intent.putExtra("aspectY", 9999);
                    }
                    else
                    {
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                    }
                    intent.putExtra("outputX", 120);
                    intent.putExtra("outputY", 120);
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cutFileUri);
                    intent.putExtra("return-data", false);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true);
                    startActivityForResult(intent, RC_CROP_IMG);
                }
            }
            else if (requestCode == RC_CROP_IMG)
            {
                //裁剪完成，上传图片
                tHeadPath = cutFileUri.getPath();
                if (AllUtils.isNetworkAvaliable(this))
                {
                }
                else
                {
                    YiHTApplication.toast(this, getResources().getString(
                            R.string.toast_public_current_no_network));
                }
                //上传完成，替换本地图片
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
