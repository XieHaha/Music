package com.zyc.doctor.chat;

import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.zyc.doctor.YihtApplication;

import com.zyc.doctor.http.data.LoginSuccessBean;

/**
 * Created by dundun on 18/4/20.
 */
public class ChatHelper implements EaseChatFragment.EaseChatFragmentHelper {
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        LoginSuccessBean bean = YihtApplication.getInstance().getLoginSuccessBean();
        if (null == bean) {
            return;
        }
        //设置自己的头像和昵称到消息扩展中
        message.setAttribute(HxHelper.MSG_EXT_NICKNAME, bean.getName());
        message.setAttribute(HxHelper.MSG_EXT_AVATAR, bean.getPortraitUrl());
    }

    @Override
    public void onEnterToChatDetails() {
    }

    @Override
    public void onAvatarClick(String username) {
    }

    @Override
    public void onAvatarLongClick(String username) {
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
