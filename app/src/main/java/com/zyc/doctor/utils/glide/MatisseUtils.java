package com.zyc.doctor.utils.glide;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zyc.doctor.R;

/**
 * @author 顿顿
 * @date 19/4/3 14:38
 * @des
 */
public class MatisseUtils {
    /**
     * 请求修改头像 相册
     */
    private static final int RC_PICK_IMG = 0x0001;

    public static void open(Activity activity) {
        Matisse.from(activity)
               // 选择 mime 的类型
               .choose(MimeType.ofImage())
               // 显示选择的数量
               .countable(true)
               // 黑色背景
               .theme(R.style.Matisse_Zhihu)
               // 图片选择的最多数量
               .maxSelectable(1)
               // 列表中显示的图片大小
               .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.app_picture_size))
               .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
               // 缩略图的比例
               .thumbnailScale(0.85f)
               // 使用的图片加载引擎
               .imageEngine(new GlideEngine())
               // 设置作为标记的请求码，返回图片时使用
               .forResult(RC_PICK_IMG);
    }

    public static void open(Fragment fragment) {
        Matisse.from(fragment)
               // 选择 mime 的类型
               .choose(MimeType.ofImage())
               // 显示选择的数量
               .countable(false)
               // 黑色背景
               .theme(R.style.Matisse_Zhihu)
               // 图片选择的最多数量
               .maxSelectable(1)
               // 列表中显示的图片大小
               .gridExpectedSize(fragment.getResources().getDimensionPixelSize(R.dimen.app_picture_size))
               .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
               // 缩略图的比例
               .thumbnailScale(0.85f)
               // 使用的图片加载引擎
               .imageEngine(new GlideEngine())
               // 设置作为标记的请求码，返回图片时使用
               .forResult(RC_PICK_IMG);
    }
}
