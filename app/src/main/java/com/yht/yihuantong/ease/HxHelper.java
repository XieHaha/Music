package com.yht.yihuantong.ease;

import android.app.Application;

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
public class HxHelper
{
    //扩展消息-昵称
    public static final String MSG_EXT_NICKNAME = "hx_nickname";
    //扩展消息-头像
    public static final String MSG_EXT_AVATAR = "hx_avatar";
    private volatile static HxHelper instance;
    public Application app;
    public Opts mOpts;
    private IRequest iRequest;
    //所有的会话集合
    private Map<String, EMConversation> mConvMap;

    private HxHelper()
    {
        if (null != instance)
        { throw new IllegalStateException("Can not instantiate singleton class."); }
    }

    /**
     * 初始化
     *
     * @param application Application
     * @param opts        配置项
     * @param mIRequest
     */
    public void init(Application application, Opts opts, IRequest mIRequest)
    {
        app = application;
        mOpts = opts;
        iRequest = mIRequest;
    }

    /**
     * 单例模式
     *
     * @return 单例实例
     */
    public static HxHelper getInstance()
    {
        if (null == instance)
        {
            synchronized (HxHelper.class)
            {
                if (null == instance)
                {
                    instance = new HxHelper();
                }
            }
        }
        return instance;
    }

    public EaseUser getUser(String username,UserInfoCallback callback)
    {
        EaseUser user = new EaseUser(username);
//                //获取到所有会话
//                mConvMap = EMClient.getInstance().chatManager().getAllConversations();
//                if (null != mConvMap && mConvMap.size() > 0)
//                {
//                    List<EMMessage> msgList = null;
//                    for (Map.Entry<String, EMConversation> et : mConvMap.entrySet())
//                    {
//                        msgList = et.getValue().getAllMessages();
//                        //遍历消息列表，从消息扩展中获取昵称和头像
//                        if (null != msgList && !msgList.isEmpty())
//                        {
//                            for (EMMessage msg : msgList)
//                            {
//                                Log.e("test", "username:" + username + "  to:" + msg.getTo() + "  from:" +
//                                              msg.getFrom());
//                                try
//                                {
//                                    Log.e("test", "name:" + msg.getStringAttribute(MSG_EXT_NICKNAME) + "  url:" +msg.getStringAttribute(MSG_EXT_AVATAR));
//                                }
//                                catch (HyphenateException e)
//                                {
//                                    e.printStackTrace();
//                                }
//                                if (!TextUtils.equals(username, msg.getFrom()))
//                                {
//                                    //如果该条消息不是该用户的，就遍历下一条
//                                    continue;
//                                }
//                                //设置昵称和用户名
//
//                                try
//                                {
//                                    user.setNickname(msg.getStringAttribute(MSG_EXT_NICKNAME));
//                                    user.setAvatar(msg.getStringAttribute(MSG_EXT_AVATAR));
//                                }
//                                catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                }
        if (username.contains("p"))
        {
            List<PatientBean> list = DataSupport.where("patientId = ?", username).find(PatientBean.class);
            if(list!=null&& list.size()>0)
            {
                PatientBean bean = list.get(0);
                user.setNickname(bean.getName());
                user.setAvatar(bean.getPatientImgUrl());
                callback.onSuccess(user);
                return user;
            }
            iRequest.getPatientInfo(username, new ResponseListener<BaseResponse>()
            {
                @Override
                public void onResponseSuccess(Tasks task, BaseResponse response)
                {
                    PatientBean patientBean = response.getData();
                    user.setNickname(patientBean.getName());
                    user.setAvatar(patientBean.getPatientImgUrl());
                    callback.onSuccess(user);
                }

                @Override
                public void onResponseError(Tasks task, Exception e)
                {
                }

                @Override
                public void onResponseCodeError(Tasks task, BaseResponse response)
                {
                }

                @Override
                public void onResponseStart(Tasks task)
                {
                }

                @Override
                public void onResponseEnd(Tasks task)
                {
                }

                @Override
                public void onResponseFile(Tasks task, File file)
                {
                }

                @Override
                public void onResponseLoading(Tasks task, boolean isUpload, long total,
                        long current)
                {
                }

                @Override
                public void onResponseCancel(Tasks task)
                {
                }
            });
        }
        else
        {

            List<CooperateDocBean> list = DataSupport.where("doctorId = ?", username).find(CooperateDocBean.class);
            if(list!=null&& list.size()>0)
            {
                CooperateDocBean bean = list.get(0);
                user.setNickname(bean.getName());
                user.setAvatar(bean.getPortraitUrl());
                callback.onSuccess(user);
                return user;
            }
            iRequest.getDocInfo(username, new ResponseListener<BaseResponse>()
            {
                @Override
                public void onResponseSuccess(Tasks task, BaseResponse response)
                {
                    CooperateDocBean bean = response.getData();
                    user.setNickname(bean.getName());
                    user.setAvatar(bean.getPortraitUrl());
                    callback.onSuccess(user);
                }

                @Override
                public void onResponseError(Tasks task, Exception e)
                {
                }

                @Override
                public void onResponseCodeError(Tasks task, BaseResponse response)
                {
                }

                @Override
                public void onResponseStart(Tasks task)
                {
                }

                @Override
                public void onResponseEnd(Tasks task)
                {
                }

                @Override
                public void onResponseFile(Tasks task, File file)
                {
                }

                @Override
                public void onResponseLoading(Tasks task, boolean isUpload, long total,
                        long current)
                {
                }

                @Override
                public void onResponseCancel(Tasks task)
                {
                }
            });
        }
        return user;
    }

    /**
     * 配置项
     */
    public static class Opts
    {
        public boolean showChatTitle;
    }
}
