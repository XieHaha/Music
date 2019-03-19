package com.zyc.doctor.version;

/**
 * Created by dundun on 16/4/7.
 */
public interface ConstantsVersionMode
{
    int UPDATE_NONE = -1;//不更新
    int UPDATE_CHOICE = 0;//选择更新
    int UPDATE_MUST = 1;//强制更新

    int UPDATE_VERSION_RESULT = 100;//  requestCode

    String PROGRESS_TOTAL_VALUE_KEY = "total_value";
    String PROGRESS_CURRENT_VALUE_KEY = "current_value";
}
