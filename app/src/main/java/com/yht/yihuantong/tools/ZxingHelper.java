package com.yht.yihuantong.tools;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * 扫码帮助类
 */
public class ZxingHelper {

    private Activity activity;

    public ZxingHelper() {
    }

    public void init(Object object) {
        if(object instanceof Fragment)
        {
            activity = ((Fragment)object).getActivity();
        }else {
            activity = (Activity) object;
        }
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("将二维码放入框内，即可自动识别");
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setTimeout(8000);
        integrator.initiateScan();
    }
}
