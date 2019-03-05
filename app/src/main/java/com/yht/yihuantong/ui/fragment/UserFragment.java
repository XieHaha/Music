package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.qrcode.BarCodeImageView;
import com.yht.yihuantong.qrcode.DialogPersonalBarCode;
import com.yht.yihuantong.ui.activity.AuthDocActivity;
import com.yht.yihuantong.ui.activity.AuthDocStatuActivity;
import com.yht.yihuantong.ui.activity.EditInfoActivity;
import com.yht.yihuantong.ui.activity.LoginActivity;
import com.yht.yihuantong.ui.activity.SettingActivity;
import com.yht.yihuantong.ui.activity.TransferPatientFromActivity;
import com.yht.yihuantong.ui.activity.TransferPatientToActivity;
import com.yht.yihuantong.ui.dialog.ActionSheetDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.FileUtils;
import com.yht.yihuantong.utils.LogUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.HttpConstants;
import custom.frame.permission.OnPermissionCallback;
import custom.frame.permission.Permission;
import custom.frame.permission.PermissionHelper;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.DirHelper;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.scrollview.CustomListenScrollView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment
        implements CustomListenScrollView.OnScrollChangeListener, CommonData
{
    private CircleImageView headImg, authImg;
    private LinearLayout llTitleLayout;
    private RelativeLayout rlAuthLayout, rlTransferMsg;
    private CustomListenScrollView scrollView;
    private TextView tvName, tvHospital, tvType, tvTitle, tvIntroduce;
    private TextView tvAuth, tvAuthStatus;
    private TextView tvDocNum, tvPatientNum;
    private TextView tvRemind;
    private ImageView ivEditInfo;
    private LoginSuccessBean loginSuccessBean;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private Uri originUri, cutFileUri;
    private File cameraTempFile;
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 权限管理类
     */
    protected PermissionHelper permissionHelper;
    /**
     * 二维码
     */
    private BarCodeImageView barCodeImageView;
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
     * 图片  裁剪
     */
    public static final int REQUEST_CODE_DOC_AUTH = RC_CROP_IMG + 1;
    private String headImgUrl;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                    //认证失败  更新本地数据
                    loginSuccessBean.setChecked(2);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initAuthStatus(2);
                    break;
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
                    //认证成功  更新本地数据
                    loginSuccessBean.setChecked(6);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initAuthStatus(6);
                    break;
                case 10:
                    //TODO 统计未查看转诊申请数量
                    sharePreferenceUtil.putBoolean(CommonData.KEY_CHANGE_PATIENT_NUM, true);
                    if (onTransferCallbackListener != null)
                    {
                        onTransferCallbackListener.onTransferCallback();
                    }
                    rlTransferMsg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    /**
     * 推送回调监听  转诊申请
     */
    private IChange<String> doctorTransferPatientListener = data ->
    {
        //        if ("from".equals(data))
        //        {
        //            handler.sendEmptyMessage(10);
        //        }
    };
    /**
     * 医生认证状态
     */
    private IChange<Integer> doctorAuthStatusChangeListener = data ->
    {
        handler.sendEmptyMessage(data);
    };

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_my;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        initPageData();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                              getStateBarHeight(getActivity())));//填充状态栏
        ivEditInfo = view.findViewById(R.id.public_title_bar_back);
        ivEditInfo.setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_setting_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_train_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_service_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_transfer_to_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_transfer_from_layout).setOnClickListener(this);
        rlAuthLayout = view.findViewById(R.id.fragment_my_auth_layout);
        rlTransferMsg = view.findViewById(R.id.message_red_point);
        scrollView = view.findViewById(R.id.fragment_my_scrollview);
        llTitleLayout = view.findViewById(R.id.fragment_my_title_layout);
        headImg = view.findViewById(R.id.fragmrnt_user_info_headimg);
        authImg = view.findViewById(R.id.fragmrnt_user_info_auth);
        tvName = view.findViewById(R.id.fragmrnt_user_info_name);
        tvHospital = view.findViewById(R.id.fragmrnt_user_info_hospital);
        tvType = view.findViewById(R.id.fragmrnt_user_info_type);
        tvTitle = view.findViewById(R.id.fragmrnt_user_info_title);
        tvIntroduce = view.findViewById(R.id.fragmrnt_user_info_introduce);
        tvAuth = view.findViewById(R.id.fragment_my_auth);
        tvAuthStatus = view.findViewById(R.id.fragment_my_auth_status);
        tvDocNum = view.findViewById(R.id.fragmrnt_user_doctors_num);
        tvPatientNum = view.findViewById(R.id.fragmrnt_user_patients_num);
        tvRemind = view.findViewById(R.id.fragment_user_remind);
        view.findViewById(R.id.fragmrnt_user_info_qrcode_layout).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        /**
         * 权限管理类
         */
        permissionHelper = PermissionHelper.getInstance(getActivity(), onPermissionCallback);
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        headImg.setOnClickListener(this);
        scrollView.setOnScrollChangeListener(this);
        rlAuthLayout.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.REGISTER);
        //注册转诊申请监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.REGISTER);
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg(Uri uri)
    {
        File file = FileUtils.getFileByUri(uri, getContext());
        mIRequest.uploadHeadImg(file, "jpg", this);
    }

    /**
     * 上传基本信息
     */
    private void updateBasicInfo()
    {
        if (TextUtils.isEmpty(headImgUrl))
        {
            return;
        }
        JSONObject merchant = new JSONObject();
        //        Map<String, Object> merchant = new HashMap<>();
        merchant.put("portraitUrl", headImgUrl);
        mIRequest.updateUserInfo(loginSuccessBean.getDoctorId(), loginSuccessBean.getFieldId(),
                                 merchant, this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        tvPatientNum.setText(String.format(getString(R.string.txt_user_info_num),
                                           sharePreferenceUtil.getString(
                                                   CommonData.KEY_PATIENT_NUM)));
        tvDocNum.setText(String.format(getString(R.string.txt_user_info_num),
                                       sharePreferenceUtil.getString(CommonData.KEY_DOCTOR_NUM)));
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        if (loginSuccessBean != null)
        {
            barCodeImageView = new BarCodeImageView(getContext(),
                                                    HttpConstants.BASE_BASIC_DOWNLOAD_URL +
                                                    loginSuccessBean.getDoctorId());
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
            //状态处理
            initAuthStatus(loginSuccessBean.getChecked());
            tvName.setText(loginSuccessBean.getName());
            tvHospital.setText(loginSuccessBean.getHospital());
            tvTitle.setText(loginSuccessBean.getTitle());
            tvType.setText(loginSuccessBean.getDepartment());
            tvIntroduce.setText(loginSuccessBean.getDoctorDescription());
        }
        if (YihtApplication.getInstance().isVersionRemind())
        {
            tvRemind.setVisibility(View.VISIBLE);
        }
        else
        {
            tvRemind.setVisibility(View.GONE);
        }
    }

    /**
     * 医生认证状态处理
     *
     * @param status
     */
    private void initAuthStatus(int status)
    {
        switch (status)
        {
            case 0://未认证
                tvAuth.setText("去认证");
                tvAuthStatus.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                ivEditInfo.setVisibility(View.VISIBLE);
                break;
            case 1://审核中
                tvAuthStatus.setText("审核中");
                tvAuth.setText("查看");
                tvAuthStatus.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                //                ivEditInfo.setVisibility(View.GONE);
                break;
            case 2://审核未通过
                tvAuthStatus.setText("审核未通过");
                tvAuth.setText("查看");
                tvAuthStatus.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                ivEditInfo.setVisibility(View.VISIBLE);
                break;
            case 6://审核已通过
                tvAuthStatus.setText("已认证");
                tvAuth.setText("查看");
                tvAuthStatus.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.app_auth_success));
                Glide.with(this).load(R.mipmap.icon_certified).into(authImg);
                ivEditInfo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                intent = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_qrcode_layout:
                DialogPersonalBarCode dialogPersonalBarCode = new DialogPersonalBarCode(
                        getActivity());
                dialogPersonalBarCode.setQRImageViewSrc(barCodeImageView);
                dialogPersonalBarCode.show();
                break;
            case R.id.fragment_my_auth_layout:
                intent = new Intent(getContext(), AuthDocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DOC_AUTH);
                break;
            case R.id.fragmrnt_user_info_headimg:
                editHeadImg();
                break;
            case R.id.fragmrnt_user_info_setting_layout:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fragmrnt_user_transfer_to_layout:
                intent = new Intent(getContext(), TransferPatientToActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_transfer_from_layout:
                rlTransferMsg.setVisibility(View.GONE);
                sharePreferenceUtil.putBoolean(CommonData.KEY_CHANGE_PATIENT_NUM, false);
                if (onTransferCallbackListener != null)
                {
                    onTransferCallbackListener.onTransferCallback();
                }
                intent = new Intent(getContext(), TransferPatientFromActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_train_layout:
            case R.id.fragmrnt_user_info_service_layout:
                ToastUtil.toast(getContext(), "敬请期待");
                break;
        }
    }

    /**
     * 更换头像
     */
    private void editHeadImg()
    {
        new ActionSheetDialog(getContext()).builder()
                                           .setCancelable(true)
                                           .setCanceledOnTouchOutside(true)
                                           .addSheetItem("相册",
                                                         ActionSheetDialog.SheetItemColor.Blue,
                                                         which ->
                                                         {
                                                             //动态申请权限
                                                             permissionHelper.request(new String[] {
                                                                     Permission.STORAGE_WRITE });
                                                         })
                                           .addSheetItem("拍照",
                                                         ActionSheetDialog.SheetItemColor.Blue,
                                                         which ->
                                                         {
                                                             //动态申请权限
                                                             permissionHelper.request(new String[] {
                                                                     Permission.CAMERA,
                                                                     Permission.STORAGE_WRITE });
                                                         })
                                           .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPLOAD_FILE:
                headImgUrl = response.getData();
                Log.i("test", "headImgUrl:" + headImgUrl);
                updateBasicInfo();
                break;
            case UPDATE_USER_INFO:
                ToastUtil.toast(getContext(), "上传成功");
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(headImg);
                loginSuccessBean.setPortraitUrl(headImgUrl);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                break;
        }
    }

    /**
     * 监听scrollview滑动情况
     *
     * @param scrollView
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onScrollChanged(CustomListenScrollView scrollView, int l, int t, int oldl, int oldt)
    {
        //        int scrollHeight = (scrollView.getChildAt(0).getHeight() - scrollView.getMeasuredHeight());
        //        int alpha;
        //        if (t < scrollHeight && t > 0)
        //        {
        //            alpha = t * 255 / scrollHeight;
        //            llTitleLayout.setBackgroundColor(Color.argb(alpha, 231, 50, 120));
        //        }
        //        if (t >= scrollHeight)
        //        {
        //            llTitleLayout.setBackgroundColor(Color.argb(255, 231, 50, 120));
        //        }
        //        if (t <= 0)
        //        {
        //            llTitleLayout.setBackgroundColor(Color.argb(0, 231, 50, 120));
        //        }
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
               //               .captureStrategy(new CaptureStrategy(true, "${applicationId}.fileprovider"))
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
        cameraTempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(getContext(), "${applicationId}.fileprovider",
                                             cameraTempFile);
        }
        else
        {
            uri = Uri.fromFile(cameraTempFile);
        }
        List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                                                     .queryIntentActivities(intent,
                                                                            PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList)
        {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, uri,
                                             Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                             Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA_IMG);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != getActivity().RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size())
                {
                    cameraTempFile = FileUtils.getFileByUri(paths.get(0), getContext());
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    startCutImg(paths.get(0), Uri.fromFile(file));
                }
                break;
            case RC_PICK_CAMERA_IMG:
                if (cameraTempFile.exists())
                {
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    Uri imageUri;
                    Uri cropUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        imageUri = FileProvider.getUriForFile(getContext(),
                                                              "${applicationId}.fileprovider",
                                                              cameraTempFile);
                    }
                    else
                    {
                        imageUri = Uri.fromFile(cameraTempFile);
                    }
                    cropUri = Uri.fromFile(file);
                    startCutImg(imageUri, cropUri);
                }
                break;
            case RC_CROP_IMG:
                //裁剪完成，上传图片
                if (AllUtils.isNetworkAvaliable(getContext()))
                {
                    uploadHeadImg(cutFileUri);
                }
                else
                {
                    ToastUtil.toast(getContext(), R.string.toast_public_current_no_network);
                }
                //上传完成，替换本地图片
                Glide.with(this).load(cutFileUri).into(headImg);
                break;
            case REQUEST_CODE_DOC_AUTH:
                //清除数据库数据
                DataSupport.deleteAll(PatientBean.class);
                DataSupport.deleteAll(CooperateDocBean.class);
                startActivity(new Intent(getContext(), AuthDocStatuActivity.class));
                getActivity().finish();
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

    private OnPermissionCallback onPermissionCallback = new OnPermissionCallback()
    {
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
            if (permissionName == null)
            {
                return;
            }
            for (String permission : permissionName)
            {
                if (Permission.STORAGE_WRITE.equals(permission))
                {
                    ToastUtil.toast(getContext(),
                                    custom.frame.R.string.dialog_no_storage_permission_tip);
                    break;
                }
                if (Permission.CAMERA.equals(permission))
                {
                    ToastUtil.toast(getContext(),
                                    custom.frame.R.string.dialog_no_camera_permission_tip);
                    break;
                }
            }
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
            new SimpleDialog(getActivity(), R.string.dialog_no_camera_permission_tip, false).show();
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
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.UNREGISTER);
        //注销转诊申请监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.UNREGISTER);
    }

    public OnTransferCallbackListener onTransferCallbackListener;

    public void setOnTransferCallbackListener(OnTransferCallbackListener onTransferCallbackListener)
    {
        this.onTransferCallbackListener = onTransferCallbackListener;
    }

    public interface OnTransferCallbackListener
    {
        void onTransferCallback();
    }
}
