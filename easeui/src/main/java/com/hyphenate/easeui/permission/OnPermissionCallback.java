/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName: .java
 * Description:
 * <author> - <version> - <date> - <desc>
 *      jake - v1.0 - 2016.4.13 -创建类
 */
package com.hyphenate.easeui.permission;

import android.support.annotation.NonNull;

public interface OnPermissionCallback
{
    /**
     * 申请权限确认授予
     * @param permissionName
     */
    void onPermissionGranted(@NonNull String[] permissionName);

    /**
     * 申请权限拒绝
     * @param permissionName
     */
    void onPermissionDeclined(@NonNull String[] permissionName);

    /**
     *  不用申请权限
     * @param permissionsName
     */
    void onPermissionPreGranted(@NonNull String permissionsName);

    /**
     * 申请权限
     * @param permissionName
     */
    void onPermissionNeedExplanation(@NonNull String permissionName);

    /**
     * 被拒绝 不再提示
     * @param permissionName
     */
    void onPermissionReallyDeclined(@NonNull String permissionName);

    /**
     * 已有权限
     * @param permissionName
     */
    void onNoPermissionNeeded(@NonNull Object permissionName);
}
