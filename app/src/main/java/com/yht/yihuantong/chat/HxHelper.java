package com.yht.yihuantong.chat;

import android.app.Application;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.UserInfoCallback;
import com.hyphenate.easeui.domain.EaseUser;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.IRequest;
import custom.frame.http.Tasks;
import custom.frame.http.listener.ResponseListener;

/**
 * Created by dundun on 18/4/16.
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
    private static HxHelper instance;
    public Application app;
    public Opts mOpts;
    private IRequest iRequest;
    /**
     * 所有的会话集合
     */
    private Map<String, EMConversation> mConvMap;

    private HxHelper() {
        if (null != instance) {
            throw new IllegalStateException("Can not instantiate singleton class.");
        }
    }

    /**
     * 初始化
     *
     * @param application Application
     * @param opts        配置项
     * @param mIRequest
     */
    public void init(Application application, Opts opts, IRequest mIRequest) {
        app = application;
        mOpts = opts;
        iRequest = mIRequest;
    }

    /**
     * 单例模式
     *
     * @return 单例实例
     */
    public static HxHelper getInstance() {
        if (null == instance) {
            synchronized (HxHelper.class) {
                if (null == instance) {
                    instance = new HxHelper();
                }
            }
        }
        return instance;
    }

    public EaseUser getUser(String username, UserInfoCallback callback) {
        EaseUser user = new EaseUser(username);
        if (username.contains("p")) {
            List<PatientBean> list = DataSupport.where("patientId = ?", username)
                    .find(PatientBean.class);
            if (list != null && list.size() > 0) {
                PatientBean bean = list.get(0);
                if (!TextUtils.isEmpty(bean.getNickname()) &&
                        bean.getNickname().length() < 20) {
                    user.setNickname(bean.getNickname());
                } else {
                    user.setNickname(bean.getName());
                }
                user.setAvatar(bean.getPatientImgUrl());
                callback.onSuccess(user);
                return user;
            }
            iRequest.getPatientInfo(username, new ResponseListener<BaseResponse>() {
                @Override
                public void onResponseSuccess(Tasks task, BaseResponse response) {
                    PatientBean patientBean = response.getData();
                    if (patientBean != null) {
                        if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                                patientBean.getNickname().length() < 20) {
                            user.setNickname(patientBean.getNickname());
                        } else {
                            user.setNickname(patientBean.getName());
                        }
                        user.setAvatar(patientBean.getPatientImgUrl());
                    }
                    callback.onSuccess(user);
                }

                @Override
                public void onResponseError(Tasks task, Exception e) {
                }

                @Override
                public void onResponseCodeError(Tasks task, BaseResponse response) {
                }

                @Override
                public void onResponseStart(Tasks task) {
                }

                @Override
                public void onResponseEnd(Tasks task) {
                }

                @Override
                public void onResponseFile(Tasks task, File file) {
                }

                @Override
                public void onResponseLoading(Tasks task, boolean isUpload, long total,
                                              long current) {
                }

                @Override
                public void onResponseCancel(Tasks task) {
                }
            });
        } else {
            List<CooperateDocBean> list = DataSupport.where("doctorId = ?", username)
                    .find(CooperateDocBean.class);
            if (list != null && list.size() > 0) {
                CooperateDocBean bean = list.get(0);
                if (!TextUtils.isEmpty(bean.getNickname()) &&
                        bean.getNickname().length() < 20) {
                    user.setNickname(bean.getNickname());
                } else {
                    user.setNickname(bean.getName());
                }
                user.setAvatar(bean.getPortraitUrl());
                callback.onSuccess(user);
                return user;
            }
            iRequest.getDocInfo(username, new ResponseListener<BaseResponse>() {
                @Override
                public void onResponseSuccess(Tasks task, BaseResponse response) {
                    CooperateDocBean bean = response.getData();
                    if (bean != null) {
                        if (!TextUtils.isEmpty(bean.getNickname()) &&
                                bean.getNickname().length() < 20) {
                            user.setNickname(bean.getNickname());
                        } else {
                            user.setNickname(bean.getName());
                        }
                        user.setAvatar(bean.getPortraitUrl());
                    }
                    callback.onSuccess(user);
                }

                @Override
                public void onResponseError(Tasks task, Exception e) {
                }

                @Override
                public void onResponseCodeError(Tasks task, BaseResponse response) {
                }

                @Override
                public void onResponseStart(Tasks task) {
                }

                @Override
                public void onResponseEnd(Tasks task) {
                }

                @Override
                public void onResponseFile(Tasks task, File file) {
                }

                @Override
                public void onResponseLoading(Tasks task, boolean isUpload, long total,
                                              long current) {
                }

                @Override
                public void onResponseCancel(Tasks task) {
                }
            });
        }
        return user;
    }

    /**
     * 配置项
     */
    public static class Opts {
        public boolean showChatTitle;
    }
}
