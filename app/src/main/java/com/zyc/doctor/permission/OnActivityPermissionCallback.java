package com.zyc.doctor.permission;

import android.support.annotation.NonNull;

/**
 * @author dundun
 */
public interface OnActivityPermissionCallback {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onActivityForResult(int requestCode);
}
