package com.yht.yihuantong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.ui.dialog.ActionSheetDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.FileUtils;
import com.yht.yihuantong.utils.LogUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.permission.Permission;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DirHelper;
import custom.frame.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息编辑  头像、名字
 *
 * @author DUNDUN
 */
public class CompleteInfoActivity extends BaseActivity
{
    private CircleImageView ivHeadImg;
    private EditText etUserName;
    private Uri originUri;
    private Uri cutFileUri;
    private File cameraTempFile;
    private String headImgUrl, userName;
    /**
     * 请求修改头像 相册
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * 请求修改头像 相机
     */
    private static final int RC_PICK_CAMERA_IMG = RC_PICK_IMG + 1;
    /**
     * 图片  裁剪
     */
    public static final int RC_CROP_IMG = RC_PICK_CAMERA_IMG + 1;
    /**
     * 页面处理
     */
    public static final int REQUEST_CODE_SUCCESS = RC_CROP_IMG + 1;

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
        etUserName = (EditText)findViewById(R.id.act_complete_info_name);
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
                if (TextUtils.isEmpty(headImgUrl))
                {
                    ToastUtil.toast(this, R.string.toast_upload_img_hint);
                    return;
                }
                userName = etUserName.getText().toString().trim();
                if (TextUtils.isEmpty(userName))
                {
                    ToastUtil.toast(this, R.string.toast_upload_name_hint);
                    return;
                }
                updateBasicInfo();
                break;
            case R.id.act_complete_info_headimg:
                editHeadImg(this);
                break;
            default:
                break;
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg()
    {
        mIRequest.uploadHeadImg(FileUtils.getFileByUri(cutFileUri, this), "jpg", this);
    }

    /**
     * 上传基本信息
     */
    private void updateBasicInfo()
    {
        mIRequest.updateBasicInfo(loginSuccessBean.getDoctorId(), userName, headImgUrl, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPLOAD_FILE:
                ToastUtil.toast(this, "上传成功!!");
                headImgUrl = response.getData();
                YihtApplication.getInstance().setHeadImgUrl(headImgUrl);
                break;
            case UPDATE_BASIC_INFO:
                loginSuccessBean.setPortraitUrl(headImgUrl);
                Log.i("test", "headImgUrl:" + headImgUrl);
                loginSuccessBean.setName(userName);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                Intent intent = new Intent(this, CompleteInfo2Activity.class);
                startActivityForResult(intent, REQUEST_CODE_SUCCESS);
                break;
            default:
                break;
        }
    }

    private void editHeadImg(final Activity activity)
    {
        new ActionSheetDialog(activity).builder()
                                       .setCancelable(false)
                                       .setCanceledOnTouchOutside(false)
                                       .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                                                     new ActionSheetDialog.OnSheetItemClickListener()
                                                     {
                                                         @Override
                                                         public void onClick(int which)
                                                         {
                                                             //动态申请权限
                                                             permissionHelper.request(new String[] {
                                                                     Permission.STORAGE_WRITE });
                                                         }
                                                     })
                                       .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                                     new ActionSheetDialog.OnSheetItemClickListener()
                                                     {
                                                         @Override
                                                         public void onClick(int which)
                                                         {
                                                             //动态申请权限
                                                             permissionHelper.request(new String[] {
                                                                     Permission.CAMERA,
                                                                     Permission.STORAGE_WRITE });
                                                         }
                                                     })
                                       .show();
    }

    /**
     * 打开图片库
     */
    private void openPhoto()
    {
        Matisse.from(this)
               // 选择 mime 的类型
               .choose(MimeType.allOf())
               // 显示选择的数量
               .countable(true)
               //                //相机
               //                .capture(true)
               //                .captureStrategy(new CaptureStrategy(true, "com.yht.yihuantong.fileprovider"))
               // 黑色背景
               .theme(R.style.Matisse_Dracula)
               // 图片选择的最多数量
               .maxSelectable(1)
               // 列表中显示的图片大小
               .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.app_picture_size))
               .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
               // 缩略图的比例
               .thumbnailScale(0.85f)
               // 使用的图片加载引擎
               .imageEngine(new PicassoEngine())
               // 设置作为标记的请求码，返回图片时使用
               .forResult(RC_PICK_IMG);
    }

    /**
     * 打开相机
     */
    private void openCamera()
    {
        String pathRecv = DirHelper.getPathImage();
        cameraTempFile = new File(pathRecv, System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this,
                                             "com.yht.yihuantong.android7.fileprovider",
                                             cameraTempFile);
        }
        else
        {
            uri = Uri.fromFile(cameraTempFile);
        }
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cameraintent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(cameraintent, RC_PICK_CAMERA_IMG);
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri)
    {
        originUri = uri;
        cutFileUri = cutUri;
        //系统裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                                                                                  PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList)
        {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, originUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                       Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //结束
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (Build.BRAND.toUpperCase().contains("HONOR") ||
            Build.BRAND.toUpperCase().contains("HUAWEI"))
        {
            //华为特殊处理 不然会显示圆
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == RC_PICK_IMG)
            {
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size())
                {
                    String cropPath =
                            DirHelper.getPathImage() + "/" + "crop" + System.currentTimeMillis() +
                            ".jpg";
                    Uri cropUri = Uri.parse("file://" + cropPath);
                    startCutImg(paths.get(0), cropUri);
                }
            }
            else if (requestCode == RC_PICK_CAMERA_IMG)
            {
                if (cameraTempFile.exists())
                {
                    Uri originalUri = Uri.fromFile(cameraTempFile);
                    String cropPath =
                            DirHelper.getPathImage() + "/" + "crop" + System.currentTimeMillis() +
                            ".jpg";
                    Uri cropUri = Uri.parse("file://" + cropPath);
                    startCutImg(originalUri, cropUri);
                }
            }
            else if (requestCode == RC_CROP_IMG)
            {
                //裁剪完成，上传图片
                if (AllUtils.isNetworkAvaliable(this))
                {
                    uploadHeadImg();
                }
                else
                {
                    ToastUtil.toast(this, R.string.toast_public_current_no_network);
                }
                //上传完成，替换本地图片
                Glide.with(this).load(cutFileUri).into(ivHeadImg);
            }
            else if (requestCode == REQUEST_CODE_SUCCESS)
            {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***************************************权限处理**********************/
    private boolean isSamePermission(String o, String n)
    {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n))
        {
            return false;
        }
        if (o.equals(n))
        {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        if (permissions == null)
        {
            return;
        }
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName)
    {
        if (permissionName == null || permissionName.length == 0)
        {
            return;
        }
        if (isSamePermission(Permission.CAMERA, permissionName[0]))
        {
            openCamera();
        }
        else if (isSamePermission(Permission.STORAGE_WRITE, permissionName[0]))
        {
            openPhoto();
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName)
    {
        super.onPermissionDeclined(permissionName);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName)
    {
        LogUtils.d("test", "onPermissionPreGranted:" + permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName)
    {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName)
    {
        new SimpleDialog(this, R.string.dialog_no_camera_permission_tip, false).show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName)
    {
        if (permissionName instanceof String[])
        {
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[])permissionName)[0]))
            {
                openPhoto();
            }
            else if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0]))
            {
                openCamera();
            }
        }
    }
}
