package com.yht.yihuantong.ease;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/4/16.
 */
public class ChatActivity extends BaseActivity
{
    private String chatId, chatName;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_chat;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        if (getIntent() != null)
        {
            chatId = getIntent().getStringExtra(CommonData.KEY_CHAT_ID);
            chatName = getIntent().getStringExtra(CommonData.KEY_CHAT_NAME);
        }
//        ((TextView)findViewById(R.id.public_title_bar_title)).setText(chatName);
        EaseChatFragment easeChatFragment = new EaseChatFragment();  //环信聊天界面
        easeChatFragment.setChatFragmentHelper(new ChatHelper());
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, chatId);
        easeChatFragment.hideTitleBar();
        easeChatFragment.setArguments(args);
        replaceFragment(R.id.act_chat_root, easeChatFragment, ChatActivity.class.getName());
    }

    /**
     * 替换fragment
     *
     * @param containerResId 需要替换的资源布局
     * @param fragment       目标Fragment
     * @param tag            替换后Tag
     */
    public void replaceFragment(final int containerResId, final EaseChatFragment fragment,
            final String tag)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (TextUtils.isEmpty(tag))
                {
                    fragmentTransaction.replace(containerResId, fragment);
                }
                else
                {
                    fragmentTransaction.replace(containerResId, fragment, tag);
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }
}
