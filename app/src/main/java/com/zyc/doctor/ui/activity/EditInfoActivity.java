package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.permission.Permission;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.ActionSheetDialog;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.DirHelper;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.GlideHelper;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.MatisseUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.FilterEmojiEditText;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 编辑个人信息
 *
 * @author DUNDUN
 */
public class EditInfoActivity extends BaseActivity {
    @BindView(R.id.act_edit_info_headimg)
    CircleImageView headImg;
    @BindView(R.id.act_edit_info_name)
    FilterEmojiEditText etName;
    @BindView(R.id.act_edit_info_title)
    FilterEmojiEditText etTitle;
    @BindView(R.id.act_edit_info_type)
    FilterEmojiEditText etType;
    @BindView(R.id.act_edit_info_hospital)
    FilterEmojiEditText etHospital;
    @BindView(R.id.act_edit_info_introduce)
    FilterEmojiEditText etIntroduce;
    private Uri originUri, cutFileUri;
    private File cameraTempFile;
    private String name, hospital, type, title, introduce, headImgUrl;
    /**
     * 名字最长字符
     */
    private int maxCount;
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
    public int getLayoutID() {
        return R.layout.act_edit_info;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("编辑信息");
        findViewById(R.id.act_edit_info_save).setOnClickListener(this);
        maxCount = 10;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initPageData();
    }

    @Override
    public void initListener() {
        super.initListener();
        headImg.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = etName.getText();
                int len = editable.length();
                if (len > maxCount) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxCount);
                    etName.setText(newStr);
                    editable = etName.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etName.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        etHospital.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        etType.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        etTitle.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        etIntroduce.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        if (loginSuccessBean != null) {
            name = loginSuccessBean.getName();
            hospital = loginSuccessBean.getHospital();
            type = loginSuccessBean.getDepartment();
            title = loginSuccessBean.getTitle();
            introduce = loginSuccessBean.getDoctorDescription();
            if (!TextUtils.isEmpty(loginSuccessBean.getPortraitUrl())) {
                headImgUrl = loginSuccessBean.getPortraitUrl();
            }
            else if (!TextUtils.isEmpty(YihtApplication.getInstance().getHeadImgUrl())) {
                headImgUrl = YihtApplication.getInstance().getHeadImgUrl();
            }
            if (!TextUtils.isEmpty(headImgUrl)) {
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
    private void uploadHeadImg(Uri uri) {
        File file = FileUtils.getFileByUri(uri, this);
        RequestUtils.uploadImg(this, file, "jpg", this);
    }

    /**
     * 更新个人信息
     */
    private void updateUserInfo() {
        name = etName.getText().toString();
        hospital = etHospital.getText().toString();
        title = etTitle.getText().toString();
        type = etType.getText().toString();
        introduce = etIntroduce.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(hospital) || TextUtils.isEmpty(type) ||
            TextUtils.isEmpty(title) || TextUtils.isEmpty(introduce)) {
            ToastUtil.toast(this, R.string.toast_upload_job_info_hint);
            return;
        }
        if (headImgUrl == null) {
            headImgUrl = "";
        }
        JsonObject json = new JsonObject();
        json.addProperty("portraitUrl", headImgUrl);
        json.addProperty("privateName", name);
        json.addProperty("portraitUrl", headImgUrl);
        json.addProperty("privateDepartment", type);
        json.addProperty("privateDoctorDescription", introduce);
        json.addProperty("privateHospital", hospital);
        json.addProperty("privateTitle", title);
        RequestUtils.updateUserInfo(this, loginSuccessBean.getDoctorId(), loginSuccessBean.getFieldId(), json, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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

    private void editHeadImg() {
        new ActionSheetDialog(this).builder()
                                   .setCancelable(true)
                                   .setCanceledOnTouchOutside(true)
                                   .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                       //动态申请权限
                                       permissionHelper.request(new String[] {
                                               Permission.STORAGE_WRITE });
                                   })
                                   .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                       //动态申请权限
                                       permissionHelper.request(new String[] {
                                               Permission.CAMERA, Permission.STORAGE_WRITE });
                                   })
                                   .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                headImgUrl = (String)response.getData();
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
    private void openPhoto() {
        MatisseUtils.open(this);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        cameraTempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                             cameraTempFile);
        }
        else {
            uri = Uri.fromFile(cameraTempFile);
        }
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                                                                                  PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri,
                               Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA_IMG);
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        originUri = uri;
        cutFileUri = cutUri;
        //系统裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (Build.BRAND.toUpperCase().contains("HONOR") || Build.BRAND.toUpperCase().contains("HUAWEI")) {
            //华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        }
        else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutFileUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RC_CROP_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size()) {
                    cameraTempFile = FileUtils.getFileByUri(paths.get(0), this);
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    startCutImg(paths.get(0), Uri.fromFile(file));
                }
                break;
            case RC_PICK_CAMERA_IMG:
                if (cameraTempFile.exists()) {
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    Uri imageUri;
                    Uri cropUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(this, YihtApplication.getInstance().getPackageName() +
                                                                    ".fileprovider", cameraTempFile);
                    }
                    else {
                        imageUri = Uri.fromFile(cameraTempFile);
                    }
                    cropUri = Uri.fromFile(file);
                    startCutImg(imageUri, cropUri);
                }
                break;
            case RC_CROP_IMG:
                //裁剪完成，上传图片
                if (AllUtils.isNetworkAvaliable(this)) {
                    uploadHeadImg(cutFileUri);
                }
                else {
                    ToastUtil.toast(this, R.string.toast_public_current_no_network);
                }
                //上传完成，替换本地图片
                Glide.with(this).load(cutFileUri).into(headImg);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***************************************权限处理**********************/
    private boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        if (o.equals(n)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (permissions == null) {
            return;
        }
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        if (permissionName == null || permissionName.length == 0) {
            return;
        }
        if (isSamePermission(Permission.CAMERA, permissionName[0])) {
            openCamera();
        }
        else if (isSamePermission(Permission.STORAGE_WRITE, permissionName[0])) {
            openPhoto();
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        super.onPermissionDeclined(permissionName);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        LogUtils.d("test", "onPermissionPreGranted:" + permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        new SimpleDialog(this, R.string.dialog_no_camera_permission_tip, false).show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[])permissionName)[0])) {
                openPhoto();
            }
            else if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openCamera();
            }
        }
    }
}
