package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.ui.dialog.ActionSheetDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.FileUtils;
import com.yht.yihuantong.utils.LogUtils;
import com.yht.yihuantong.utils.ScalingUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CheckUrl;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.permission.Permission;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DirHelper;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/5/7.
 * 医生认证
 */
public class AuthDocActivity extends BaseActivity
{
    private TextView tvTitleMore;
    private EditText etName, etCardNumber, etHospital, etTitle, etDepart;
    private File tempFile, idCardFrontTempFile, idCardBackTempFile, docCardFrontTempFile, docCardBackTempFile;
    private ImageView ivIdCardFront, ivIdCardBack, ivDocCardFront, ivDocCardBack;
    private TextView tvIdCardFrontHint, tvIdCardBackHint, tvDocCardFrontHint, tvDocCardBackHint;
    private RelativeLayout rlApplyLayout;
    private String txtName, txtCardNum, txtHospital, txtTitle, txtDepart;
    private CooperateDocBean cooperateDocBean;
    /**
     * 请求修改头像 相册
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * 请求修改头像 相机
     */
    private static final int RC_PICK_CAMERA_IMG = RC_PICK_IMG + 1;
    private int type = -1;
    /**
     * 身份证正面
     */
    private final int ID_CARD_FRONT = 1;
    /**
     * 身份证背面
     */
    private final int ID_CARD_BACK = 2;
    /**
     * 资质证明正面
     */
    private final int DOC_CARD_FRONT = 3;
    /**
     * 资质证明背面
     */
    private final int DOC_CARD_BACK = 4;
    /**
     * 医生认证状态
     */
    private int authStatus = -1;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_auth_doc;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("认证");
        tvTitleMore = (TextView)findViewById(R.id.public_title_bar_more_txt);
        tvTitleMore.setText("重新认证");
        ivIdCardFront = (ImageView)findViewById(R.id.act_auth_doc_idcard_front);
        ivIdCardBack = (ImageView)findViewById(R.id.act_auth_doc_idcard_back);
        ivDocCardFront = (ImageView)findViewById(R.id.act_auth_doc_doccard_front);
        ivDocCardBack = (ImageView)findViewById(R.id.act_auth_doc_doccard_back);
        tvIdCardFrontHint = (TextView)findViewById(R.id.act_auth_doc_idcard_front_hint);
        tvIdCardBackHint = (TextView)findViewById(R.id.act_auth_doc_idcard_back_hint);
        tvDocCardFrontHint = (TextView)findViewById(R.id.act_auth_doc_doccard_front_hint);
        tvDocCardBackHint = (TextView)findViewById(R.id.act_auth_doc_doccard_back_hint);
        etName = (EditText)findViewById(R.id.act_auth_doc_name);
        etCardNumber = (EditText)findViewById(R.id.act_auth_doc_card_num);
        etHospital = (EditText)findViewById(R.id.act_auth_doc_hospital);
        etTitle = (EditText)findViewById(R.id.act_auth_doc_title);
        etDepart = (EditText)findViewById(R.id.act_auth_doc_depart);
        rlApplyLayout = (RelativeLayout)findViewById(R.id.act_auth_doc_apply_layout);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        authStatus = loginSuccessBean.getChecked();
        if (authStatus != 0)
        {
            tvTitleMore.setVisibility(View.VISIBLE);
        }
        else
        {
            tvTitleMore.setVisibility(View.GONE);
        }
        if (authStatus == 0)
        {
            updateMode(true);
        }
        else
        {
            updateMode(false);
        }
        getDocInfo();
    }

    @Override
    public void initListener()
    {
        tvIdCardFrontHint.setOnClickListener(this);
        tvIdCardBackHint.setOnClickListener(this);
        tvDocCardFrontHint.setOnClickListener(this);
        tvDocCardBackHint.setOnClickListener(this);
        //0未认证
        if (authStatus == 0)
        {
            findViewById(R.id.act_auth_doc_idcard_front_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_idcard_back_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_doccard_front_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_doccard_back_layout).setOnClickListener(this);
        }
        findViewById(R.id.act_auth_doc_apply).setOnClickListener(this);
        etDepart.setOnEditorActionListener((v, actionId, event) ->
                                           {
                                               if (actionId == EditorInfo.IME_ACTION_DONE)
                                               {
                                                   qualifiyDoc();
                                               }
                                               return false;
                                           });
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (cooperateDocBean != null)
        {
            etName.setText(cooperateDocBean.getName());
            etCardNumber.setText(cooperateDocBean.getIdentityNumber());
            etHospital.setText(cooperateDocBean.getHospital());
            etTitle.setText(cooperateDocBean.getTitle());
            etDepart.setText(cooperateDocBean.getDepartment());
            CheckUrl checkUrl = JSON.parseObject(cooperateDocBean.getCheckUrl(), CheckUrl.class);
            if (checkUrl != null)
            {
                Glide.with(this).load(checkUrl.getIdFront()).into(ivIdCardFront);
                Glide.with(this).load(checkUrl.getIdEnd()).into(ivIdCardBack);
                Glide.with(this).load(checkUrl.getQualifiedFront()).into(ivDocCardFront);
                Glide.with(this).load(checkUrl.getQualifiedEnd()).into(ivDocCardBack);
            }
            new Thread(() ->
                       {
                           if (checkUrl != null)
                           {
                               FutureTarget<File> target = Glide.with(AuthDocActivity.this)
                                                                .asFile()
                                                                .load(checkUrl.getIdFront())
                                                                .submit();
                               FutureTarget<File> target1 = Glide.with(AuthDocActivity.this)
                                                                 .asFile()
                                                                 .load(checkUrl.getIdEnd())
                                                                 .submit();
                               FutureTarget<File> target2 = Glide.with(AuthDocActivity.this)
                                                                 .asFile()
                                                                 .load(checkUrl.getQualifiedFront())
                                                                 .submit();
                               FutureTarget<File> target3 = Glide.with(AuthDocActivity.this)
                                                                 .asFile()
                                                                 .load(checkUrl.getQualifiedEnd())
                                                                 .submit();
                               try
                               {
                                   idCardFrontTempFile = target.get();
                                   idCardBackTempFile = target1.get();
                                   docCardFrontTempFile = target2.get();
                                   docCardBackTempFile = target3.get();
                               }
                               catch (InterruptedException e)
                               {
                                   e.printStackTrace();
                               }
                               catch (ExecutionException e)
                               {
                                   e.printStackTrace();
                               }
                           }
                       }).start();
        }
    }

    /**
     * 编辑模式
     *
     * @param isEdit
     */
    private void updateMode(boolean isEdit)
    {
        etName.setFocusable(isEdit);
        etName.setFocusableInTouchMode(isEdit);
        etCardNumber.setFocusable(isEdit);
        etCardNumber.setFocusableInTouchMode(isEdit);
        etHospital.setFocusable(isEdit);
        etHospital.setFocusableInTouchMode(isEdit);
        etTitle.setFocusable(isEdit);
        etTitle.setFocusableInTouchMode(isEdit);
        etDepart.setFocusable(isEdit);
        etDepart.setFocusableInTouchMode(isEdit);
        if (!isEdit)
        {
            rlApplyLayout.setVisibility(View.GONE);
        }
        else
        {
            rlApplyLayout.setVisibility(View.VISIBLE);
        }
        if (authStatus != 0)
        {
            if (!isEdit)
            {
                tvIdCardFrontHint.setVisibility(View.GONE);
                tvIdCardBackHint.setVisibility(View.GONE);
                tvDocCardFrontHint.setVisibility(View.GONE);
                tvDocCardBackHint.setVisibility(View.GONE);
            }
            else
            {
                tvIdCardFrontHint.setVisibility(View.VISIBLE);
                tvIdCardBackHint.setVisibility(View.VISIBLE);
                tvDocCardFrontHint.setVisibility(View.VISIBLE);
                tvDocCardBackHint.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo()
    {
        showProgressDialog("");
        mIRequest.getDocInfo(loginSuccessBean.getDoctorId(), this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_DOC_INFO:
                closeProgressDialog();
                cooperateDocBean = response.getData();
                initPageData();
                break;
            case QUALIFIY_DOC:
                closeProgressDialog();
                ToastUtil.toast(this, response.getMsg());
                //改变认证状态，当前为审核中
                loginSuccessBean.setChecked(1);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                finish();
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        switch (task)
        {
            case GET_DOC_INFO:
                break;
            case QUALIFIY_DOC:
                closeProgressDialog();
                ToastUtil.toast(this, response.getMsg());
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_auth_doc_idcard_front_hint:
            case R.id.act_auth_doc_idcard_front_layout:
                uploadImg(ID_CARD_FRONT);
                break;
            case R.id.act_auth_doc_idcard_back_hint:
            case R.id.act_auth_doc_idcard_back_layout:
                uploadImg(ID_CARD_BACK);
                break;
            case R.id.act_auth_doc_doccard_front_hint:
            case R.id.act_auth_doc_doccard_front_layout:
                uploadImg(DOC_CARD_FRONT);
                break;
            case R.id.act_auth_doc_doccard_back_hint:
            case R.id.act_auth_doc_doccard_back_layout:
                uploadImg(DOC_CARD_BACK);
                break;
            case R.id.act_auth_doc_apply:
                qualifiyDoc();
                break;
        }
    }

    /**
     * @param view
     */
    public void onTitleMoreClick(View view)
    {
        tvTitleMore.setVisibility(View.GONE);
        updateMode(true);
    }
    /**
     * 提交审核 医生资质认证
     */
    private void qualifiyDoc()
    {
        txtName = etName.getText().toString().trim();
        txtCardNum = etCardNumber.getText().toString().trim();
        txtHospital = etHospital.getText().toString().trim();
        txtTitle = etTitle.getText().toString().trim();
        txtDepart = etDepart.getText().toString().trim();
        if (!AllUtils.isCardNum(txtCardNum))
        {
            ToastUtil.toast(this, R.string.toast_upload_card_hint);
            return;
        }
        if (TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtCardNum) ||
            idCardFrontTempFile == null || idCardBackTempFile == null)
        {
            ToastUtil.toast(this, "请完善您的身份信息");
            return;
        }
        if (TextUtils.isEmpty(txtHospital) || TextUtils.isEmpty(txtTitle) ||
            TextUtils.isEmpty(txtDepart) || docCardFrontTempFile == null ||
            docCardBackTempFile == null)
        {
            ToastUtil.toast(this, "请完善您的资质信息");
            return;
        }
        showProgressDialog("信息上传中");
        mIRequest.qualifiyDoc(loginSuccessBean.getDoctorId(), txtName, txtCardNum, txtTitle,
                              txtDepart, txtHospital, idCardFrontTempFile, idCardBackTempFile,
                              docCardFrontTempFile, docCardBackTempFile, this);
    }

    private void uploadImg(int type)
    {
        this.type = type;
        new ActionSheetDialog(this).builder()
                                   .setCancelable(true)
                                   .setCanceledOnTouchOutside(true)
                                   .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                                                 which ->
                                                 {
                                                     //动态申请权限
                                                     permissionHelper.request(new String[] {
                                                             Permission.STORAGE_WRITE });
                                                 })
                                   .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                                 which ->
                                                 {
                                                     //动态申请权限
                                                     permissionHelper.request(new String[] {
                                                             Permission.CAMERA,
                                                             Permission.STORAGE_WRITE });
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
               //               .capture(true)
               //               .captureStrategy(new CaptureStrategy(true, "com.yht.yihuantong.fileprovider"))
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
        tempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, "com.yht.yihuantong.fileprovider", tempFile);
        }
        else
        {
            uri = Uri.fromFile(tempFile);
        }
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                                                                                  PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList)
        {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                 Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size())
                {
                    Uri imgUri = paths.get(0);
                    switch (type)
                    {
                        case ID_CARD_FRONT:
                            tvIdCardFrontHint.setVisibility(View.GONE);
                            idCardFrontTempFile = FileUtils.getFileByUri(imgUri, this);
                            ScalingUtils.resizePic(this, idCardFrontTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivIdCardFront);
                            break;
                        case ID_CARD_BACK:
                            tvIdCardBackHint.setVisibility(View.GONE);
                            idCardBackTempFile = FileUtils.getFileByUri(imgUri, this);
                            ScalingUtils.resizePic(this, idCardBackTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivIdCardBack);
                            break;
                        case DOC_CARD_FRONT:
                            tvDocCardFrontHint.setVisibility(View.GONE);
                            docCardFrontTempFile = FileUtils.getFileByUri(imgUri, this);
                            ScalingUtils.resizePic(this, docCardFrontTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivDocCardFront);
                            break;
                        case DOC_CARD_BACK:
                            tvDocCardBackHint.setVisibility(View.GONE);
                            docCardBackTempFile = FileUtils.getFileByUri(imgUri, this);
                            ScalingUtils.resizePic(this, docCardBackTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivDocCardBack);
                            break;
                    }
                }
                break;
            case RC_PICK_CAMERA_IMG:
                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    imageUri = FileProvider.getUriForFile(this, "com.yht.yihuantong.fileprovider",
                                                          tempFile);
                }
                else
                {
                    imageUri = Uri.fromFile(tempFile);
                }
                ScalingUtils.resizePic(this, tempFile.getAbsolutePath());
                switch (type)
                {
                    case ID_CARD_FRONT:
                        tvIdCardFrontHint.setVisibility(View.GONE);
                        idCardFrontTempFile = tempFile;
                        Glide.with(this).load(imageUri).into(ivIdCardFront);
                        break;
                    case ID_CARD_BACK:
                        tvIdCardBackHint.setVisibility(View.GONE);
                        idCardBackTempFile = tempFile;
                        Glide.with(this).load(imageUri).into(ivIdCardBack);
                        break;
                    case DOC_CARD_FRONT:
                        tvDocCardFrontHint.setVisibility(View.GONE);
                        docCardFrontTempFile = tempFile;
                        Glide.with(this).load(imageUri).into(ivDocCardFront);
                        break;
                    case DOC_CARD_BACK:
                        tvDocCardBackHint.setVisibility(View.GONE);
                        docCardBackTempFile = tempFile;
                        Glide.with(this).load(imageUri).into(ivDocCardBack);
                        break;
                }
                break;
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
