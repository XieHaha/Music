package com.zyc.doctor.utils.permission;

import android.support.annotation.NonNull;

/**
 * @author dundun
 */
public interface OnActivityPermissionCallback {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onActivityForResult(int requestCode);
}
