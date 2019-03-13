package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.DocAuthStatu;
import com.yht.yihuantong.tools.FileTransferServer;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DirHelper;

/**
 * 启动界面
 *
 * @author DUNDUN
 */
public class SplashActivity extends BaseActivity implements DocAuthStatu {
    @BindView(R.id.iv_start)
    ImageView ivBg;
    @BindView(R.id.act_splash_time_hint)
    TextView tvTimeHint;
    @BindView(R.id.act_splash_btn)
    TextView tvStart;

    private ScheduledExecutorService executorService;
    private final String filePath = DirHelper.getPathImage() + "/splash.png";
    private int time = 0;
    /**
     * 广告页最长等待时间
     */
    private static final int MAX_WAIT_TIME = 5;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            tvTimeHint.setText(String.format(getString(R.string.txt_splash_time_hint), time));
            if (time <= 0) {
                initPage();
            }
            return true;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.act_splash;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        hideBottomUIMenu();
        if (isExist()) {
            initSplashImg();
        } else {
            initScheduledThread();
        }
        getSplash();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvStart.setOnClickListener(this);
        tvTimeHint.setOnClickListener(this);
    }

    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    private void initSplashImg() {
        File file = new File(filePath);
        Glide.with(this).load(file).into(ivBg);
        initScheduledThread();
    }

    /**
     * 页面初始化
     */
    private void initPage() {
        if (loginSuccessBean != null) {
            int checked = loginSuccessBean.getChecked();
            switch (checked) {
                case NONE:
                case VERIFYING:
                case VERIFY_FAILD:
                    startActivity(new Intent(this, AuthDocStatuActivity.class));
                    finish();
                    break;
                case VERIFY_SUCCESS:
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    break;
                default:
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    break;
            }
        } else {
            startLoginPage();
        }
    }

    private void initScheduledThread() {
        time = MAX_WAIT_TIME;
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern(
                        "yht-thread-pool-%d")
                        .daemon(true)
                        .build());
        executorService.scheduleAtFixedRate(() ->
        {
            time--;
            if (time < 0) {
                time = 0;
                executorService.shutdownNow();
            } else {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 广告页
     */
    private void getSplash() {
        try {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mIRequest.getSplash("doctor", "android", name, this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_splash_btn:
                startLoginPage();
                break;
            case R.id.act_splash_time_hint:
                if (executorService != null && !executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
                initPage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_SPLASH:
                String url = response.getData();
                String oldUrl = sharePreferenceUtil.getString(CommonData.KEY_SPLASH_IMG_URL);
                if (!TextUtils.isEmpty(url) && !url.equals(oldUrl) || !isExist()) {
                    sharePreferenceUtil.putString(CommonData.KEY_SPLASH_IMG_URL, url);
                    downloadImg(url);
                }
                break;
        }
    }

    private void downloadImg(String url) {
        FileTransferServer.getInstance(this)
                .downloadFile(0, url, DirHelper.getPathImage(), "splash.png",
                        new DownloadListener() {
                            @Override
                            public void onDownloadError(int what,
                                                        Exception exception) {
                            }

                            @Override
                            public void onStart(int what, boolean isResume,
                                                long rangeSize, Headers responseHeaders,
                                                long allCount) {
                            }

                            @Override
                            public void onProgress(int what, int progress,
                                                   long fileCount, long speed) {
                            }

                            @Override
                            public void onFinish(int what, String filePath) {
                            }

                            @Override
                            public void onCancel(int what) {
                            }
                        });
    }

    /**
     * 判断本地文件是否存在
     *
     * @return
     */
    private boolean isExist() {
        File file = new File(filePath);
        if (file != null && file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
