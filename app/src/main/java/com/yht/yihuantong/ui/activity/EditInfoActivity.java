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
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.tools.GetImagePath;
import com.yht.yihuantong.tools.GlideHelper;
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
 * 编辑个人信息
 *
 * @author DUNDUN
 */
public class EditInfoActivity extends BaseActivity
{
    private CircleImageView headImg;
    private EditText etName, etHospital, etType, etTitle, etIntroduce;
    private Uri originUri;
    private Uri cutFileUri;
    private File cameraTempFile;
    private String name, hospital, type, title, introduce, headImgUrl;
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

    @Override
    public int getLayoutID()
    {
        return R.layout.act_edit_info;
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
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("编辑信息");
        findViewById(R.id.act_edit_info_save).setOnClickListener(this);
        headImg = (CircleImageView)findViewById(R.id.act_edit_info_headimg);
        etName = (EditText)findViewById(R.id.act_edit_info_name);
        etHospital = (EditText)findViewById(R.id.act_edit_info_hospital);
        etType = (EditText)findViewById(R.id.act_edit_info_type);
        etTitle = (EditText)findViewById(R.id.act_edit_info_title);
        etIntroduce = (EditText)findViewById(R.id.act_edit_info_introduce);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        initPageData();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        headImg.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (loginSuccessBean != null)
        {
            name = loginSuccessBean.getName();
            hospital = loginSuccessBean.getHospital();
            type = loginSuccessBean.getDepartment();
            title = loginSuccessBean.getTitle();
            introduce = loginSuccessBean.getDoctorDescription();
            if (!TextUtils.isEmpty(loginSuccessBean.getPortraitUrl()))
            {
                headImgUrl = loginSuccessBean.getPortraitUrl();
            }
            else if (!TextUtils.isEmpty(YihtApplication.getInstance().getHeadImgUrl()))
            {
                headImgUrl = YihtApplication.getInstance().getHeadImgUrl();
            }
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(headImg);
            }
            etName.setText(name);
            etHospital.setText(hospital);
            etTitle.setText(type);
            etType.setText(title);
            etIntroduce.setText(introduce);
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg()
    {
        mIRequest.uploadHeadImg(FileUtils.getFileByUri(cutFileUri, this), "jpg", this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_edit_info_headimg:
                editHeadImg();
                break;
            case R.id.act_edit_info_save:
                updateUserInfo();
                break;
            default:
                break;
        }
    }

    private void editHeadImg()
    {
        new ActionSheetDialog(this).builder()
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
     * 更新个人信息
     */
    private void updateUserInfo()
    {
        name = etName.getText().toString();
        hospital = etHospital.getText().toString();
        title = etTitle.getText().toString();
        type = etType.getText().toString();
        introduce = etIntroduce.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(hospital) || TextUtils.isEmpty(type) ||
            TextUtils.isEmpty(title) || TextUtils.isEmpty(introduce))
        {
            ToastUtil.toast(this, R.string.toast_upload_job_info_hint);
            return;
        }
        mIRequest.updateUserInfo(loginSuccessBean.getDoctorId(), name, headImgUrl, hospital, type,
                                 title, introduce, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPLOAD_FILE:
                headImgUrl = response.getData();
                break;
            case UPDATE_USER_INFO:
                ToastUtil.toast(this, "保存成功");
                loginSuccessBean.setDepartment(type);
                loginSuccessBean.setHospital(hospital);
                loginSuccessBean.setDoctorDescription(introduce);
                loginSuccessBean.setTitle(title);
                loginSuccessBean.setName(name);
                loginSuccessBean.setPortraitUrl(headImgUrl);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                finish();
                break;
            default:
                break;
        }
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
        Log.e("test", "pathRecv:" + pathRecv);
        cameraTempFile = new File(pathRecv, System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraintent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, "com.yht.yihuantong.android7.fileprovider",
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//        {
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
        ////        在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                                                                                  PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList)
        {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, cutFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

    /**
     * 裁剪图片方法实现
     *
     * @param inputUri
     */
    public void startPhotoZoom(Uri inputUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri outPutUri = Uri.fromFile(cameraTempFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            Uri outPutUri = Uri.fromFile(cameraTempFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(this, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }

        // 设置裁剪
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
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, RC_CROP_IMG);//这里就将裁剪后的图片的Uri返回了
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
                    Uri cropUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        cropUri = FileProvider.getUriForFile(this,
                                                             "com.yht.yihuantong.android7.fileprovider",
                                                             FileUtils.getFileByUri(paths.get(0),
                                                                                    this));
                    }
                    else
                    {
                        cropUri = Uri.parse("file://" + cropPath);
                    }
                    startCutImg(paths.get(0), cropUri);
                }
            }
            else if (requestCode == RC_PICK_CAMERA_IMG)
            {
                if (cameraTempFile.exists())
                {
                    Uri originalUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        originalUri = FileProvider.getUriForFile(this,
                                                                 "com.yht.yihuantong.android7.fileprovider",
                                                                 cameraTempFile);
                    }
                    else
                    {
                        originalUri = Uri.fromFile(cameraTempFile);
                    }
                    String cropPath =
                            DirHelper.getPathImage() + "/" + "crop" + System.currentTimeMillis() +
                            ".jpg";
                    Uri cropUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        cropUri = FileProvider.getUriForFile(this,
                                                             "com.yht.yihuantong.android7.fileprovider",
                                                             new File(cropPath));
                    }
                    else
                    {
                        cropUri = Uri.parse("file://" + cropPath);
                    }
                    startCutImg(originalUri, cropUri);
//                    startPhotoZoom(originalUri,cropUri);
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
                Glide.with(this).load(cutFileUri).into(headImg);
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
