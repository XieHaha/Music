package com.zyc.doctor.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.zyc.doctor.R;

/**
 * @author dundun
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_loading);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
