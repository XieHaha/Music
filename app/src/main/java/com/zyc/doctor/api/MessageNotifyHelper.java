package com.zyc.doctor.api;

import android.text.TextUtils;

import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.utils.SharePreferenceUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 顿顿
 * @date 19/4/8 15:22
 * @des 管理首页 转诊、订单新消息
 */
public class MessageNotifyHelper implements CommonData {
    /**
     * 存
     *
     * @param type
     * @param newsId
     */
    public static void save(SharePreferenceUtil sp, int type, int newsId) {
        switch (type) {
            case JIGUANG_CODE_DOCTOR_TRANS_REFUSE:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS:
                saveMessage(sp, newsId, CommonData.KEY_NEW_TRANSFER_MESSAGE_REMIND);
                break;
            case JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_FINISH:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REPORT:
                saveMessage(sp, newsId, CommonData.KEY_NEW_ORDER_MESSAGE_REMIND);
                break;
            default:
                break;
        }
    }

    /**
     * @param sp
     * @param newsId
     * @param key
     */
    private static void saveMessage(SharePreferenceUtil sp, int newsId, String key) {
        String ids = sp.getString(key);
        if (TextUtils.isEmpty(ids)) {
            ids = String.valueOf(newsId);
        }
        else {
            StringBuilder stringBuilder = new StringBuilder(ids);
            stringBuilder.append(",");
            stringBuilder.append(newsId);
            ids = stringBuilder.toString();
        }
        sp.putString(key, ids);
    }

    /**
     * 移除
     *
     * @param newsId
     */
    public static void remove(SharePreferenceUtil sp, String newsId, String key) {
        String string = sp.getString(key);
        if (!TextUtils.isEmpty(string)) {
            String[] ids = string.split(",");
            ArrayList<String> values = new ArrayList<>(Arrays.asList(ids));
            if (values != null && values.size() > 0) {
                if (values.contains(newsId)) {
                    values.remove(newsId);
                }
                sp.putString(key, StringUtils.join(values.toArray(), ","));
            }
        }
    }

    /**
     * 清除所有
     *
     * @param key
     */
    public static void clear(SharePreferenceUtil sharePreferenceUtil, String key) {
        sharePreferenceUtil.clear(key);
    }
}
