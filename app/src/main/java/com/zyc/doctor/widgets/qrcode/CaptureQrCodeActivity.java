package com.zyc.doctor.widgets.qrcode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.base.activity.BaseActivity;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/3/29 18:21
 * @des 二维码扫描界面
 */
public class CaptureQrCodeActivity extends BaseActivity {
    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView zxingBarcodeScanner;
    private CaptureManager capture;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_caputure_qrcode;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("扫一扫");
        capture = new CaptureManager(this, zxingBarcodeScanner);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
