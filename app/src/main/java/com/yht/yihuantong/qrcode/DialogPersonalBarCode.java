package com.yht.yihuantong.qrcode;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;

import custom.frame.bean.LoginSuccessBean;
import custom.frame.utils.GlideHelper;

/**
 * Created by zhangrj on 2017/4/10.
 */
public class DialogPersonalBarCode extends Dialog {
    private Activity mActivity;
    private ImageView close, imgHead;
    private TextView tvName, tvDeptname;
    private RelativeLayout llBarCode;

    private BarCodeImageView barCodeImageView;

    public DialogPersonalBarCode(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.mActivity = activity;
        init();
    }

    private void init() {
        //得到对话框
        Window window = getWindow();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_person_barcode_card, null);
        close = (ImageView) view.findViewById(R.id.close);
        imgHead = (ImageView) view.findViewById(R.id.imgHead);
        tvName = (TextView) view.findViewById(R.id.name);
        tvDeptname = (TextView) view.findViewById(R.id.deptname);
        tvDeptname.setVisibility(View.VISIBLE);
        llBarCode = (RelativeLayout) view.findViewById(R.id.llBarCode);
        fillData();
        // addLogo();
        setContentView(view);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        //设置窗口显示
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void fillData() {
        //addLogo();
        getUserInfo();
    }

    public void setQRImageViewSrc(BarCodeImageView barCodeImageView) {
        this.barCodeImageView = barCodeImageView;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        llBarCode.addView(barCodeImageView, params);
    }

    private void getUserInfo() {
        LoginSuccessBean loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        Glide.with(mActivity).load(loginSuccessBean.getPortraitUrl()).apply(GlideHelper.getOptions()).into(imgHead);
        tvName.setText(loginSuccessBean.getName());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (llBarCode != null) {
            llBarCode.removeAllViews();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (llBarCode != null) {
            llBarCode.removeAllViews();
        }
    }
}
