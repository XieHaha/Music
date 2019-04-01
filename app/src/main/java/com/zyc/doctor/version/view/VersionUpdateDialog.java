package com.zyc.doctor.version.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.ui.base.activity.AppManager;
import com.zyc.doctor.utils.DirHelper;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.version.ConstantsVersionMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * @author dundun
 */
public class VersionUpdateDialog extends Dialog implements ConstantsVersionMode, View.OnClickListener {
    private static final String TAG = "VersionUpdateDialog";
    private TextView tvTitle, tvCancel, tvUpdate, tvPercent, tvContent;
    private LinearLayout llUpdateContentLayout;
    private RelativeLayout rlDownloadLayout;
    private Context context;
    /**
     * 更新模式   （强制更新还是选择更新）
     */
    private int upDateMode;
    /**
     * 是否需要下载apk (检查本地是否有已下载好的最新的apk)
     */
    private boolean isDownNewAPK = true;
    /**
     * 更新内容概要
     */
    private List<String> list;
    private String[] contentArray;

    public VersionUpdateDialog(Context context) {
        this(context, R.style.normal_dialog);
    }

    public VersionUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_version);
        initView();
        initUpdateData();
    }

    private void initView() {
        tvTitle = (TextView)findViewById(R.id.act_update_version_title);
        tvCancel = (TextView)findViewById(R.id.act_update_version_content_cancel);
        tvUpdate = (TextView)findViewById(R.id.act_update_version_content_update);
        tvPercent = (TextView)findViewById(R.id.act_update_version_content_percent);
        tvContent = (TextView)findViewById(R.id.act_update_version_content);
        tvCancel.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        llUpdateContentLayout = (LinearLayout)findViewById(R.id.act_update_version_content_layout);
        rlDownloadLayout = (RelativeLayout)findViewById(R.id.act_update_version_content_down_layout);
    }

    /**
     * 初始化更新数据
     */
    private void initUpdateData() {
        switch (upDateMode) {
            case UPDATE_NONE:
            case UPDATE_CHOICE:
                tvCancel.setText("忽略");
                break;
            case UPDATE_MUST:
                tvCancel.setText("退出");
                break;
            default:
                break;
        }
        if (list != null && list.size() > 0) {
            llUpdateContentLayout.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                TextView textView = new TextView(context);
                textView.setText(list.get(i));
                textView.setTextColor(context.getResources().getColor(R.color.app_minor_color));
                textView.setTextSize(12);
                textView.setPadding(0, 0, 0, 10);
                llUpdateContentLayout.addView(textView);
            }
        }
        else {
            llUpdateContentLayout.setVisibility(View.GONE);
            tvContent.setVisibility(View.GONE);
        }
        if (isDownNewAPK) {
            tvUpdate.setText("马上更新");
        }
        else {
            tvUpdate.setText("立即安装");
        }
    }

    /**
     * 更新模式    强制更新  选择更新
     *
     * @param upDateMode
     * @return
     */
    public VersionUpdateDialog setUpdateMode(int upDateMode) {
        this.upDateMode = upDateMode;
        return this;
    }

    /**
     * 是否需要下载apk
     *
     * @param isDownNewAPK
     * @return
     */
    public VersionUpdateDialog setIsDownNewAPK(boolean isDownNewAPK) {
        this.isDownNewAPK = isDownNewAPK;
        return this;
    }

    /**
     * 版本更新内容概要
     *
     * @return
     */
    public VersionUpdateDialog setContent(String content) {
        list = new ArrayList<>();
        if (!TextUtils.isEmpty(content)) {
            try {
                contentArray = content.split("##");
            }
            catch (PatternSyntaxException e) {
                LogUtils.w(TAG, "Exception error!", e);
                return this;
            }
        }
        if (contentArray != null && contentArray.length > 0) {
            for (int i = 0; i < contentArray.length; i++) {
                list.add(contentArray[i]);
            }
        }
        return this;
    }

    /**
     * 下载的进度值
     *
     * @return
     */
    public void setProgressValue(long total, long current) {
        if (total == current) {
            isDownNewAPK = false;
            tvCancel.setEnabled(true);
            tvUpdate.setVisibility(View.VISIBLE);
            tvUpdate.setText("立即安装");
            rlDownloadLayout.setVisibility(View.GONE);
        }
        else {
            tvPercent.setText((int)(current / (float)total * 100) + "%");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_update_version_content_cancel:
                switch (upDateMode) {
                    case UPDATE_NONE:
                    case UPDATE_CHOICE:
                        dismiss();
                        break;
                    case UPDATE_MUST:
                        dismiss();
                        AppManager.getInstance().finishAllActivity();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.act_update_version_content_update:
                if (isDownNewAPK && onEnterClickListener != null) {
                    switch (upDateMode) {
                        //选择更新
                        case UPDATE_CHOICE:
                            onEnterClickListener.onEnter(false);
                            dismiss();
                            break;
                        //强制更新 不让用户操作
                        case UPDATE_MUST:
                            tvCancel.setEnabled(false);
                            tvUpdate.setVisibility(View.GONE);
                            rlDownloadLayout.setVisibility(View.VISIBLE);
                            onEnterClickListener.onEnter(true);
                            break;
                        default:
                            break;
                    }
                }
                else {
                    File file = new File(DirHelper.getPathFile(), "YHT.apk");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, YihtApplication.getInstance().getPackageName() +
                                                                  ".fileprovider", file);
                    }
                    else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private OnEnterClickListener onEnterClickListener;

    public interface OnEnterClickListener {
        void onEnter(boolean isMustUpdate);
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}

