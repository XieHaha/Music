/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName: .java
 * Description:
 * <author> - <version> - <date> - <desc>
 *      jake - v1.0 - 2016.4.13 -创建类
 */
package custom.frame.permission;

import android.support.annotation.NonNull;

public interface OnPermissionCallback
{
    void onPermissionGranted(@NonNull String[] permissionName);

    void onPermissionDeclined(@NonNull String[] permissionName);

    void onPermissionPreGranted(@NonNull String permissionsName);

    void onPermissionNeedExplanation(@NonNull String permissionName);

    void onPermissionReallyDeclined(@NonNull String permissionName);

    void onNoPermissionNeeded(@NonNull Object permissionName);
}
