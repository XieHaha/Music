package com.zyc.doctor.version;

/**
 * @author dundun
 * @date 16/4/7
 */
public interface ConstantsVersionMode {
    /**
     * 不更新
     */
    int UPDATE_NONE = -1;
    /**
     * 选择更新
     */
    int UPDATE_CHOICE = 0;
    /**
     * 强制更新
     */
    int UPDATE_MUST = 1;
    /**
     * requestCode
     */
    int UPDATE_VERSION_RESULT = 100;
}
