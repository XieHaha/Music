package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ease.ChatActivity;
import com.yht.yihuantong.ui.fragment.CooperateDocFragment;
import com.yht.yihuantong.ui.fragment.MsgFragment;
import com.yht.yihuantong.ui.fragment.PatientsFragment;
import com.yht.yihuantong.ui.fragment.UserFragment;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.yht.yihuantong.version.view.VersionUpdateDialog;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.Version;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.ripples.RippleLinearLayout;

public class MainActivity extends BaseActivity
        implements EaseConversationListFragment.EaseConversationListItemClickListener,
                   VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener
{
    private RippleLinearLayout tabMsg, tabDoc, tabCase, tabMy;
    private Fragment msgFragment, docFragment, caseFragment, myFragment;
    private EaseConversationListFragment easeConversationListFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private MyConnectionListener connectionListener;
    /**
     * 消息监听
     */
    private EMMessageListener msgListener;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private VersionUpdateDialog versionUpdateDialog;
    /**
     * 是否通过广播检查版本更新
     */
    private boolean versionUpdateChecked = false;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_main;
    }

    /**
     * 初始化tabs
     */
    private void initTab()
    {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //        tabMsgView();
        tabEaseMsgView();
        setAlias();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        transparencyBar(this);
        tabMsg = (RippleLinearLayout)findViewById(R.id.act_main_tab1);
        tabDoc = (RippleLinearLayout)findViewById(R.id.act_main_tab2);
        tabCase = (RippleLinearLayout)findViewById(R.id.act_main_tab3);
        tabMy = (RippleLinearLayout)findViewById(R.id.act_main_tab4);
        initTab();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, mIRequest);
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tabMsg.setOnClickListener(this);
        tabDoc.setOnClickListener(this);
        tabCase.setOnClickListener(this);
        tabMy.setOnClickListener(this);
        //注册一个监听连接状态的listener
        connectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(connectionListener);
        msgListener = new EMMessageListener()
        {
            @Override
            public void onMessageReceived(List<EMMessage> messages)
            {
                //收到消息
                if (easeConversationListFragment != null)
                {
                    easeConversationListFragment.refresh();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages)
            {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages)
            {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message)
            {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages)
            {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change)
            {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onListItemClicked(EMConversation conversation)
    {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, conversation.conversationId());
        //                intent.putExtra(CommonData.KEY_CHAT_NAME,conversation.());
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_main_tab1:
                //                tabMsgView();
                tabEaseMsgView();
                break;
            case R.id.act_main_tab2:
                tabDocView();
                break;
            case R.id.act_main_tab3:
                tabCaseView();
                break;
            case R.id.act_main_tab4:
                tabMyView();
                break;
            default:
                //                tabMsgView();
                tabEaseMsgView();
                break;
        }
    }

    private void tabMsgView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (msgFragment == null)
        {
            msgFragment = new MsgFragment();
            transaction.add(R.id.act_main_tab_frameLayout, msgFragment);
        }
        else
        {
            transaction.show(msgFragment);
            msgFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    /**
     * 环信自带会话列表
     */
    private void tabEaseMsgView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (easeConversationListFragment == null)
        {
            easeConversationListFragment = new EaseConversationListFragment();
            transaction.add(R.id.act_main_tab_frameLayout, easeConversationListFragment);
        }
        else
        {
            transaction.show(easeConversationListFragment);
            easeConversationListFragment.onResume();
        }
        easeConversationListFragment.setConversationListItemClickListener(this);
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    private void tabDocView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (docFragment == null)
        {
            docFragment = new PatientsFragment();
            transaction.add(R.id.act_main_tab_frameLayout, docFragment);
        }
        else
        {
            transaction.show(docFragment);
            docFragment.onResume();
        }
        transaction.commit();
        selectTab(1);
    }

    private void tabCaseView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (caseFragment == null)
        {
            caseFragment = new CooperateDocFragment();
            transaction.add(R.id.act_main_tab_frameLayout, caseFragment);
        }
        else
        {
            transaction.show(caseFragment);
            caseFragment.onResume();
        }
        transaction.commit();
        selectTab(2);
    }

    private void tabMyView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (myFragment == null)
        {
            myFragment = new UserFragment();
            transaction.add(R.id.act_main_tab_frameLayout, myFragment);
        }
        else
        {
            transaction.show(myFragment);
            myFragment.onResume();
        }
        transaction.commit();
        selectTab(3);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction)
    {
        if (msgFragment != null) { transaction.hide(msgFragment); }
        if (docFragment != null) { transaction.hide(docFragment); }
        if (caseFragment != null) { transaction.hide(caseFragment); }
        if (myFragment != null) { transaction.hide(myFragment); }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index)
    {
        switch (index)
        {
            case 0:
                tabMsg.setSelected(true);
                tabDoc.setSelected(false);
                tabCase.setSelected(false);
                tabMy.setSelected(false);
                break;
            case 1:
                tabMsg.setSelected(false);
                tabDoc.setSelected(true);
                tabCase.setSelected(false);
                tabMy.setSelected(false);
                break;
            case 2:
                tabMsg.setSelected(false);
                tabDoc.setSelected(false);
                tabCase.setSelected(true);
                tabMy.setSelected(false);
                break;
            case 3:
                tabMsg.setSelected(false);
                tabDoc.setSelected(false);
                tabCase.setSelected(false);
                tabMy.setSelected(true);
                break;
            default:
                tabMsg.setSelected(true);
                tabDoc.setSelected(false);
                tabCase.setSelected(false);
                tabMy.setSelected(false);
                break;
        }
    }

    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading) {
        versionUpdateDialog = new VersionUpdateDialog(this);
        versionUpdateDialog.setCancelable(false);
        versionUpdateDialog.setUpdateMode(mode).
                setIsDownNewAPK(isDownLoading).setContent(version.getUpdateDescription());
        versionUpdateDialog.setOnEnterClickListener(this);
        versionUpdateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (versionUpdateDialog != null && versionUpdateDialog.isShowing()) {
            versionUpdateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
        versionUpdateChecked = true;
    }

    @Override
    public void onEnter(boolean isMustUpdate)
    {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, "开始下载");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //移除监听
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    public class MyConnectionListener implements EMConnectionListener
    {
        @Override
        public void onConnected()
        {
        }

        @Override
        public void onDisconnected(final int error)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (error == EMError.USER_REMOVED)
                    {
                        // 显示帐号已经被移除
                    }
                    else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE)
                    {
                        // 显示帐号在其他设备登录
                    }
                    else
                    {
                        if (NetUtils.hasNetwork(MainActivity.this))
                        {
                            //连接不到聊天服务器
                        }
                        else
                        {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置标签与别名
     */
    private void setAlias()
    {
        JPushInterface.setAlias(this, 100, loginSuccessBean.getDoctorId());
    }

    /**
     * 返回键 后台运行
     * 如果前一个activity未finish  会导致无法返回到后台
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
