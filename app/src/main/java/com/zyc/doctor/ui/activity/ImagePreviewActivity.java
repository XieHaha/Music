package com.zyc.doctor.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.NormImage;
import com.zyc.doctor.api.DirHelper;
import com.zyc.doctor.api.FileTransferServer;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.imagePreview.options.PreviewOptions;
import com.zyc.doctor.widgets.imagePreview.transformer.CustomTransformer;
import com.zyc.doctor.widgets.imagePreview.utils.CacheUtils;
import com.zyc.doctor.widgets.imagePreview.utils.NavigaterPageIndex;
import com.zyc.doctor.widgets.imagePreview.view.ImageLoadingView;
import com.zyc.doctor.widgets.imagePreview.view.ImagePreviewView;

import java.util.ArrayList;

/**
 * @author dundun
 */
public class ImagePreviewActivity extends Activity implements ViewPager.OnPageChangeListener {
    public static final String INTENT_URLS = "intent_urls";
    public static final String INTENT_POSITION = "intent_position";
    /**
     * 图片对象
     */
    private ArrayList<NormImage> urls;
    private ArrayList<ImagePreviewView> imgPreViews = new ArrayList<>();
    private ViewPager imgViewPager;
    private ImageView btnSaveImage;
    /**
     * 图片加载动画view
     */
    private ImageLoadingView mLoadingView;
    /**
     * 页面游标
     */
    private NavigaterPageIndex indicator;
    /**
     * 当前显示的view
     */
    private ImagePreviewView currentPreviceView;
    /**
     * 当前page 游标
     */
    private int currentIndex;
    /**
     * 图片加载  开始
     */
    private final int LOAD_START = 0;
    /**
     * 图片加载  失败
     */
    private final int LOAD_ERROR = 100;
    /**
     * 图片加载  成功
     */
    private final int LOAD_SUCCESS = 200;
    /**
     * 图片加载  取消
     */
    private final int LOAD_CANCEL = 300;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_ERROR:
                    mLoadingView.setVisibility(View.GONE);
                    ToastUtil.toast(ImagePreviewActivity.this, "图片加载失败");
                    break;
                case LOAD_SUCCESS:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case LOAD_CANCEL:
                    mLoadingView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_image_view);
        Intent intent = getIntent();
        if (intent != null) {
            urls = (ArrayList<NormImage>)intent.getSerializableExtra(INTENT_URLS);
            currentIndex = intent.getIntExtra(INTENT_POSITION, 0);
        }
        mLoadingView = (ImageLoadingView)findViewById(R.id.act_image_view_loading);
        btnSaveImage = (ImageView)findViewById(R.id.act_image_view_save_image);
        //图片游标
        indicator = (NavigaterPageIndex)findViewById(R.id.act_image_view_page_indicator);
        indicator.initPageIndex(urls.size());
        //当只有一张图，不显示图片游标
        if (urls.size() == 1) {
            indicator.setVisibility(View.GONE);
        }
        imgViewPager = (ViewPager)findViewById(R.id.act_image_view_viewpager);
        //滑动效果
        imgViewPager.setPageTransformer(true, new CustomTransformer());
        imgViewPager.addOnPageChangeListener(this);
        imgViewPager.setAdapter(new TouchImageAdapter());
        imgViewPager.setCurrentItem(currentIndex);
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUri = urls.get(currentIndex).getBigImageUrl();
                String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
                if (CacheUtils.getInstance(getBaseContext()).saveImageFileToDisk(imageUri)) {
                    Toast.makeText(ImagePreviewActivity.this,
                                   "图片 " + fileName + " 已保存到 " + PreviewOptions.ImageDownloadOptions.IMAGE_SAVE_REL_DIR,
                                   Toast.LENGTH_SHORT).show();
                }
                else {
                    downloadImg(imageUri, fileName);
                }
            }
        });
        if (imgPreViews.size() > 0) {
            imgPreViews.clear();
        }
        for (int i = 0; i < urls.size(); i++) {
            imgPreViews.add(i, new ImagePreviewView(this, mHandler));
        }
    }

    /**
     * 图片下载
     *
     * @param imageUri
     * @param fileName
     */
    private void downloadImg(String imageUri, String fileName) {
        FileTransferServer.getInstance(ImagePreviewActivity.this)
                          .downloadFile(imageUri, DirHelper.getPathImage(), fileName, new DownloadListener() {
                              @Override
                              public void onDownloadError(int what, Exception exception) {
                                  Toast.makeText(ImagePreviewActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                              }

                              @Override
                              public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders,
                                      long allCount) {
                              }

                              @Override
                              public void onProgress(int what, int progress, long fileCount, long speed) {
                              }

                              @Override
                              public void onFinish(int what, String filePath) {
                                  Toast.makeText(ImagePreviewActivity.this, "图片 " + fileName + " 已保存到 " +
                                                                            PreviewOptions.ImageDownloadOptions.IMAGE_SAVE_REL_DIR,
                                                 Toast.LENGTH_SHORT).show();
                              }

                              @Override
                              public void onCancel(int what) {
                              }
                          });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        indicator.changePageIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class TouchImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            currentPreviceView = imgPreViews.get(position);
            currentPreviceView.loadingImageAsync(urls.get(position).getImagePath(),
                                                 urls.get(position).getMiddleImageUrl(),
                                                 urls.get(position).getBigImageUrl(), position);
            container.addView(currentPreviceView, LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
            return currentPreviceView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.keep, R.anim.fade_out);
    }
}
