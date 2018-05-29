package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.yht.shortcutbadge.ShortcutBadger;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ease.ChatActivity;
import com.yht.yihuantong.ui.fragment.CooperateDocFragment;
import com.yht.yihuantong.ui.fragment.PatientsFragment;
import com.yht.yihuantong.ui.fragment.UserFragment;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.yht.yihuantong.version.view.VersionUpdateDialog;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.Version;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DensityUtil;
import custom.frame.utils.ScreenUtils;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.ripples.RippleLinearLayout;

public class MainActivity extends BaseActivity
        implements EaseConversationListFragment.EaseConversationListItemClickListener,
                   VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener,
                   EaseConversationListFragment.EaseConversationListItemLongClickListener
{
    private RippleLinearLayout tabMsg, tabDoc, tabCase, tabMy;
    private RelativeLayout rlMsgPointLayout;
    private TextView tvUnReadMsgCount;
    private Fragment patientFragment, cooperateDocFragment, myFragment;
    private EaseConversationListFragment easeConversationListFragment;
    /**
     * message 操作弹框view
     */
    public View messagePop;
    private TextView tvDelete;
    /**
     * message 操作弹框
     */
    public PopupWindow popupWindow;
    /**
     * 弹窗具体坐标
     */
    public int[] location = new int[2];
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * popup
     */
    private int popupHeight;
    /**
     * 当前选中会话
     */
    private EMConversation curConversation;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private MyConnectionListener connectionListener;
    /**
     * 消息监听
     */
    private EMMessageListener msgListener;
    /**
     * 联系人变化监听
     */
    private EMContactListener contactListener;
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
    /**
     * 未读消息总数
     */
    private int msgUnReadCount = 0;

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
        tabEaseMsgView();
        setAlias();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        int type = -1;
        if (intent != null)
        {
            type = intent.getIntExtra(CommonData.KEY_PUBLIC, -1);
        }
        if (type == -1)
        {
            tabEaseMsgView();
        }
        else if (type == 2)
        {
            tabCooperateDocView();
        }
        else if (type == 1)
        {
            tabPatientView();
        }
        else if (type == 3)
        {
            tabMyView();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateUnReadCount();
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
        rlMsgPointLayout = (RelativeLayout)findViewById(R.id.message_red_point);
        tvUnReadMsgCount = (TextView)findViewById(R.id.item_msg_num);
        messagePop = LayoutInflater.from(this).inflate(R.layout.message_pop_menu, null);
        tvDelete = messagePop.findViewById(R.id.message_pop_menu_play);
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
        //弹窗参数初始化
        screenHeight = ScreenUtils.getScreenHeight(this);
        popupHeight = DensityUtil.dip2px(this, 48 * 2);
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
                runOnUiThread(() -> updateUnReadCount());
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
        contactListener = new EMContactListener()
        {
            @Override
            public void onContactInvited(String username, String reason)
            {
                //收到好友邀请
            }

            @Override
            public void onFriendRequestAccepted(String s)
            {
            }

            @Override
            public void onFriendRequestDeclined(String s)
            {
            }

            @Override
            public void onContactDeleted(String username)
            {
                //被删除时回调此方法
                //删除会话
                EMClient.getInstance().chatManager().deleteConversation(username, true);
                if (easeConversationListFragment != null)
                {
                    easeConversationListFragment.refresh();
                }
                //通知患者碎片刷新列表
                NotifyChangeListenerServer.getInstance().notifyPatientStatusChange("");
            }

            @Override
            public void onContactAdded(String username)
            {
                //增加了联系人时回调此方法
            }
        };
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        tvDelete.setOnClickListener(v ->
                                    {
                                        if (curConversation != null)
                                        {
                                            //删除和某个user会话，如果需要保留聊天记录，传false
                                            popupWindow.dismiss();
                                            EMClient.getInstance()
                                                    .chatManager()
                                                    .deleteConversation(
                                                            curConversation.conversationId(), true);
                                            //收到消息
                                            if (easeConversationListFragment != null)
                                            {
                                                easeConversationListFragment.refresh();
                                            }
                                        }
                                    });
    }

    /**
     * 未读消息
     */
    public void updateUnReadCount()
    {
        msgUnReadCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (msgUnReadCount > 0)
        {
            rlMsgPointLayout.setVisibility(View.VISIBLE);
            tvUnReadMsgCount.setText(msgUnReadCount > 99 ? "99+" : msgUnReadCount + "");
            setShortcutBadge(msgUnReadCount);
        }
        else
        {
            rlMsgPointLayout.setVisibility(View.GONE);
            removeShortcutBadge();
        }
    }

    /**
     * 设置角标
     */
    private void setShortcutBadge(int badgeCount)
    {
        ShortcutBadger.applyCount(this, badgeCount);
    }

    /**
     * 移除角标
     */
    private void removeShortcutBadge()
    {
        ShortcutBadger.removeCount(this);
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
    public void onListItemLongClick(View view, EMConversation conversation)
    {
        curConversation = conversation;
        initPopwindow(view, popupLocation(view));
    }

    /**
     * @param contentTv 弹框依赖view
     * @param location  弹框坐标
     */
    public void initPopwindow(View contentTv, int[] location)
    {
        if (popupWindow == null)
        {
            popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setFocusable(true);
        popupWindow.setContentView(messagePop);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popOutShadow(popupWindow);
        popupWindow.showAtLocation(contentTv, Gravity.NO_GRAVITY, location[0],
                                   location[1] + contentTv.getHeight());
    }

    /**
     * popup位置计算
     *
     * @param view
     * @return
     */
    private int[] popupLocation(View view)
    {
        view.getLocationOnScreen(location);
        int viewHeight = view.getHeight();
        int viewWidth = view.getWidth();
        if (screenHeight - location[1] > (popupHeight + popupHeight / 2))
        {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - viewHeight / 2;
        }
        else
        {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - popupHeight - viewHeight / 2;
        }
        return location;
    }

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;//设置阴影透明度
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() ->
                                         {
                                             WindowManager.LayoutParams lp1 = getWindow().getAttributes();
                                             lp1.alpha = 1f;
                                             getWindow().setAttributes(lp1);
                                         });
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
                tabPatientView();
                break;
            case R.id.act_main_tab3:
                tabCooperateDocView();
                break;
            case R.id.act_main_tab4:
                tabMyView();
                break;
            default:
                tabEaseMsgView();
                break;
        }
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
        easeConversationListFragment.setConversationListItemLongClickListener(this);
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    private void tabPatientView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (patientFragment == null)
        {
            patientFragment = new PatientsFragment();
            transaction.add(R.id.act_main_tab_frameLayout, patientFragment);
        }
        else
        {
            transaction.show(patientFragment);
            patientFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(1);
    }

    private void tabCooperateDocView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (cooperateDocFragment == null)
        {
            cooperateDocFragment = new CooperateDocFragment();
            transaction.add(R.id.act_main_tab_frameLayout, cooperateDocFragment);
        }
        else
        {
            transaction.show(cooperateDocFragment);
            cooperateDocFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
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
        transaction.commitAllowingStateLoss();
        selectTab(3);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction)
    {
        if (easeConversationListFragment != null)
        {
            transaction.hide(easeConversationListFragment);
        }
        if (patientFragment != null) { transaction.hide(patientFragment); }
        if (cooperateDocFragment != null) { transaction.hide(cooperateDocFragment); }
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
    public void updateVersion(Version version, int mode, boolean isDownLoading)
    {
        if (mode == -1)
        {
            return;
        }
        versionUpdateDialog = new VersionUpdateDialog(this);
        versionUpdateDialog.setCancelable(false);
        versionUpdateDialog.setUpdateMode(mode).
                setIsDownNewAPK(isDownLoading).setContent(version.getUpdateDescription());
        versionUpdateDialog.setOnEnterClickListener(this);
        versionUpdateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current)
    {
        if (versionUpdateDialog != null && versionUpdateDialog.isShowing())
        {
            versionUpdateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError()
    {
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
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
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
            runOnUiThread(() ->
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
