package com.zyc.doctor.chat;

import android.content.Context;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.UserInfoCallback;
import com.hyphenate.easeui.domain.EaseUser;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.http.listener.AbstractResponseAdapter;
import com.zyc.doctor.http.retrofit.RequestUtils;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

/**
 * @author dundun
 */
public class HxHelper {
    /**
     * 扩展消息-昵称
     */
    public static final String MSG_EXT_NICKNAME = "hx_nickname";
    /**
     * 扩展消息-头像
     */
    public static final String MSG_EXT_AVATAR = "hx_avatar";
    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public static class Resource {
        private Context context;
        /**
         * 所有的会话集合
         */
        private Map<String, EMConversation> mConvMap;

        private Resource() {
            if (null != resource) {
                throw new IllegalStateException("Can not instantiate singleton class.");
            }
        }

        /**
         * 初始化
         *
         * @param context context
         */
        public void init(Context context) {
            this.context = context;
        }

        public EaseUser getUser(String username, UserInfoCallback callback) {
            EaseUser user = new EaseUser(username);
            if (username.contains("p")) {
                List<PatientBean> list = DataSupport.where("patientId = ?", username).find(PatientBean.class);
                if (list != null && list.size() > 0) {
                    PatientBean bean = list.get(0);
                    if (!TextUtils.isEmpty(bean.getNickName()) && bean.getNickName().length() < BaseData.BASE_NICK_NAME_LENGTH) {
                        user.setNickname(bean.getNickName());
                    }
                    else {
                        user.setNickname(bean.getName());
                    }
                    user.setAvatar(bean.getPatientImgUrl());
                    callback.onSuccess(user);
                    return user;
                }
                RequestUtils.getPatientInfo(context, username, new AbstractResponseAdapter<BaseResponse>() {
                    @Override
                    public void onResponseSuccess(Tasks task, BaseResponse response) {
                        PatientBean patientBean = (PatientBean)response.getData();
                        if (patientBean != null) {
                            if (!TextUtils.isEmpty(patientBean.getNickName()) &&
                                patientBean.getNickName().length() < BaseData.BASE_NICK_NAME_LENGTH) {
                                user.setNickname(patientBean.getNickName());
                            }
                            else {
                                user.setNickname(patientBean.getName());
                            }
                            user.setAvatar(patientBean.getPatientImgUrl());
                        }
                        callback.onSuccess(user);
                    }
                });
            }
            else {
                List<CooperateDocBean> list = DataSupport.where("doctorId = ?", username).find(CooperateDocBean.class);
                if (list != null && list.size() > 0) {
                    CooperateDocBean bean = list.get(0);
                    if (!TextUtils.isEmpty(bean.getNickname()) && bean.getNickname().length() < BaseData.BASE_NICK_NAME_LENGTH) {
                        user.setNickname(bean.getNickname());
                    }
                    else {
                        user.setNickname(bean.getName());
                    }
                    user.setAvatar(bean.getPortraitUrl());
                    callback.onSuccess(user);
                    return user;
                }
                RequestUtils.getDocInfo(context, username, new AbstractResponseAdapter<BaseResponse>() {
                    @Override
                    public void onResponseSuccess(Tasks task, BaseResponse response) {
                        CooperateDocBean bean = (CooperateDocBean)response.getData();
                        if (bean != null) {
                            if (!TextUtils.isEmpty(bean.getNickname()) && bean.getNickname().length() < BaseData.BASE_NICK_NAME_LENGTH) {
                                user.setNickname(bean.getNickname());
                            }
                            else {
                                user.setNickname(bean.getName());
                            }
                            user.setAvatar(bean.getPortraitUrl());
                        }
                        callback.onSuccess(user);
                    }
                });
            }
            return user;
        }
    }

    /**
     * 配置项
     */
    public static class Opts {
        private boolean showChatTitle;

        public boolean isShowChatTitle() {
            return showChatTitle;
        }

        public void setShowChatTitle(boolean showChatTitle) {
            this.showChatTitle = showChatTitle;
        }
    }
}
