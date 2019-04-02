/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName: Permission.java
 * Description: 定义敏感权限类
 * <author> - <version> - <date> - <desc>
 *      jake - v1.0 - 2016.4.13 -创建类
 */
package com.zyc.doctor.permission;

import android.Manifest;

/**
 * 定义敏感权限类
 *
 * @author dundun
 */
public class Permission {
    /**
     * 相机权限
     */
    public static final String CAMERA = Manifest.permission.CAMERA;
    /**
     * 录音权限
     */
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    /**
     * 位置权限
     */
    public static final String LOCATION[] = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    /**
     * 存储权限
     */
    public static final String STORAGE[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * 写入存储
     */
    public static final String STORAGE_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    /**
     * 手机信息
     */
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
}
