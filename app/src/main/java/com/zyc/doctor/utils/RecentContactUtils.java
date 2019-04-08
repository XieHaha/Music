package com.zyc.doctor.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zyc.doctor.data.bean.PatientBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dundun
 * @date 16/4/14
 */
public class RecentContactUtils {
    private Context context;
    private static RecentContactUtils recentContactUtils;
    private static SharePreferenceUtil sharePreferenceUtil;
    public static final String KEY = "recent_contact";

    private RecentContactUtils(Context context) {
        this.context = context;
    }

    /**
     * 初始化
     */
    public static void init(Context context) {
        if (recentContactUtils == null) {
            recentContactUtils = new RecentContactUtils(context);
        }
        sharePreferenceUtil = new SharePreferenceUtil(context);
    }

    /**
     * 存
     *
     * @param id
     */
    public static void save(String id) {
        List<String> value = getList(id);
        if (value != null) {
            if (value.contains(id)) {
                value.remove(id);
            }
            Collections.reverse(value);
            value.add(id);
            Collections.sort(value);
            saveString(value);
        }
    }

    /**
     * 删
     *
     * @param id
     */
    public static void delete(String id) {
        String value = sharePreferenceUtil.getString(KEY);
        if (!TextUtils.isEmpty(value)) {
            List<String> values = new ArrayList(Arrays.asList(value.split(",")));
            if (values != null) {
                if (values.contains(id)) {
                    values.remove(id);
                }
                saveString(values);
            }
        }
    }

    private static void saveString(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sharePreferenceUtil.putString(KEY, sb.toString());
    }

    private static List<String> getList(String id) {
        if (sharePreferenceUtil != null) {
            String value = sharePreferenceUtil.getString(KEY);
            if (!TextUtils.isEmpty(value)) {
                return new ArrayList(Arrays.asList(value.split(",")));
            }
            else {
                sharePreferenceUtil.putString(KEY, id);
                return null;
            }
        }
        return null;
    }

    public static List<PatientBean> getRecentContactList() {
        List<PatientBean> patientBeans = new ArrayList<>();
        String value = sharePreferenceUtil.getString(KEY);
        if (!TextUtils.isEmpty(value)) {
            List<String> ids = Arrays.asList(value.split(","));
            for (String id : ids) {
                List<PatientBean> list = DataSupport.where("patientId = ?", id).find(PatientBean.class);
                if (list != null && list.size() > 0) {
                    patientBeans.add(list.get(0));
                }
            }
        }
        return patientBeans;
    }
}
