package com.zyc.doctor.ui.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
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
import com.zyc.shortcutbadge.ShortcutBadger;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.chat.ChatActivity;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.receive.EaseMsgClickBroadCastReceiver;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.ui.fragment.CooperateDocFragment;
import com.zyc.doctor.ui.fragment.MainFragment;
import com.zyc.doctor.ui.fragment.UserFragment;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.version.presenter.VersionPresenter;
import com.zyc.doctor.version.view.VersionUpdateDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.RegistrationTypeBean;
import custom.frame.bean.Version;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DensityUtil;
import custom.frame.utils.ScreenUtils;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.ripples.RippleLinearLayout;

import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

/**
 * @author dundun
 */
public class MainActivity extends BaseActivity implements EaseConversationListFragment.EaseConversationListItemClickListener, VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener, EaseConversationListFragment.EaseConversationListItemLongClickListener, UserFragment.OnTransferCallbackListener, CooperateDocFragment.OnDocApplyCallbackListener, MainFragment.OnPatientApplyCallbackListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.item_msg_num)
    TextView tvUnReadMsgCount;
    @BindView(R.id.message_red_point)
    RelativeLayout rlMsgPointLayout;
    @BindView(R.id.act_main_tab1)
    RippleLinearLayout tab1;
    @BindView(R.id.item_msg_num2)
    TextView tvUnReadMsgCount2;
    @BindView(R.id.message_red_point2)
    RelativeLayout rlMsgPointLayout2;
    @BindView(R.id.act_main_tab2)
    RippleLinearLayout tab2;
    @BindView(R.id.item_msg_num3)
    TextView tvUnReadMsgCount3;
    @BindView(R.id.message_red_point3)
    RelativeLayout rlMsgPointLayout3;
    @BindView(R.id.act_main_tab3)
    RippleLinearLayout tab3;
    @BindView(R.id.item_msg_num4)
    TextView tvUnReadMsgCount4;
    @BindView(R.id.message_red_point4)
    RelativeLayout rlMsgPointLayout4;
    @BindView(R.id.act_main_tab4)
    RippleLinearLayout tab4;
    private CooperateDocFragment cooperateDocFragment;
    private MainFragment mainFragment;
    private UserFragment userFragment;
    private EaseConversationListFragment easeConversationListFragment;
    /**
     * message 操作弹框view
     */
    private View messagePop;
    private TextView tvDelete;
    /**
     * message 操作弹框
     */
    private PopupWindow popupWindow;
    /**
     * 弹窗具体坐标
     */
    private int[] location = new int[2];
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
     * 商品集合
     */
    private ArrayList<RegistrationTypeBean> registrationTypeBeans;
    /**
     * 是否通过广播检查版本更新
     */
    private boolean versionUpdateChecked = false;
    /**
     * 未读消息总数
     */
    private int msgUnReadCount = 0;
    /**
     * 聊天渠道
     */
    public static final String CHANNEL_CHAT = "channel_chat";
    private Bitmap largeIcon = null;
    private NotificationManager mNotificationManager;
    private int pending_count = 1;

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        tabMainView();
        setAlias();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int type = -1;
        if (intent != null) {
            type = intent.getIntExtra(CommonData.KEY_PUBLIC, -1);
        }
        if (type == -1) {
            tabMainView();
        } else if (type == 1) {
            tabEaseMsgView();
        } else if (type == 2) {
            tabCooperateDocView();
        } else if (type == 3) {
            tabMyView();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnReadCount();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        transparencyBar(this);
        messagePop = LayoutInflater.from(this).inflate(R.layout.message_pop_menu, null);
        tvDelete = messagePop.findViewById(R.id.message_pop_menu_play);
        initTab();
        largeIcon = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotify();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, mIRequest);
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
        //弹窗参数初始化
        screenHeight = ScreenUtils.getScreenHeight(this);
        popupHeight = DensityUtil.dip2px(this, 48 * 2);
        getAllProduct();
    }

    @Override
    public void initListener() {
        super.initListener();
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        //注册一个监听连接状态的listener
        connectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(connectionListener);
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
                runOnUiThread(() -> updateUnReadCount());
                initNotify();
                sendChatMsg(messages.get(0));
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        contactListener = new EMContactListener() {
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
            }

            @Override
            public void onFriendRequestAccepted(String s) {
            }

            @Override
            public void onFriendRequestDeclined(String s) {
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                //删除会话
                EMClient.getInstance().chatManager().deleteConversation(username, true);
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
                //通知患者碎片刷新列表
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
            }

            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        };
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        tvDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            new SimpleDialog(this, "删除后，将清空该聊天的消息记录?", (dialog, which) -> {
                if (curConversation != null) {
                    //删除和某个user会话，如果需要保留聊天记录，传false
                    EMClient.getInstance().chatManager().deleteConversation(curConversation.conversationId(), true);
                    //收到消息
                    if (easeConversationListFragment != null) {
                        easeConversationListFragment.refresh();
                    }
                }
            }, (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void initNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = CHANNEL_CHAT;
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    /**
     * 获取所有商品
     */
    private void getAllProduct() {
        mIRequest.getAllProduct(this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_ALL_PRODUCT:
                registrationTypeBeans = response.getData();
                //数据存储
                DataSupport.deleteAll(RegistrationTypeBean.class);
                DataSupport.saveAll(registrationTypeBeans);
                break;
        }
    }

    /**
     * 未读消息
     */
    public void updateUnReadCount() {
        msgUnReadCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (msgUnReadCount > 0) {
            rlMsgPointLayout2.setVisibility(View.VISIBLE);
            tvUnReadMsgCount2.setText(msgUnReadCount > 99 ? "99+" : msgUnReadCount + "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //                sendSubscribeMsg(msgUnReadCount);
            } else {
                setShortcutBadge(msgUnReadCount);
            }
        } else {
            rlMsgPointLayout2.setVisibility(View.GONE);
            removeShortcutBadge();
        }
    }

    /**
     * 设置角标
     */
    private void setShortcutBadge(int badgeCount) {
        ShortcutBadger.applyCount(this, badgeCount);
    }

    /**
     * 移除角标
     */
    private void removeShortcutBadge() {
        ShortcutBadger.removeCount(this);
    }

    public void sendChatMsg(EMMessage message) {
        //当前消息发送者与正在聊天界面对象一致时，不显示通知
        if (message.getFrom().equals(YihtApplication.getInstance().getChatId())) {
            return;
        }
        if (pending_count > 10000) {
            pending_count = 1;
        }
        pending_count++;
        Intent intent = new Intent(MainActivity.this, EaseMsgClickBroadCastReceiver.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, message.getFrom());
        intent.setAction("ease.msg.android.intent.CLICK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, pending_count
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification =
                    new NotificationCompat.Builder(this, CHANNEL_CHAT).setContentText("收到新的消息").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(largeIcon).setDefaults(DEFAULT_VIBRATE | DEFAULT_SOUND).setAutoCancel(true).setContentIntent(pendingIntent).build();
            mNotificationManager.notify(message.getFrom(), 1, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setAutoCancel(true);
            builder.setContentText("收到新消息");
            builder.setContentIntent(pendingIntent);
            int defaults = Notification.DEFAULT_LIGHTS;
            defaults |= Notification.DEFAULT_VIBRATE;
            defaults |= Notification.DEFAULT_SOUND;
            builder.setDefaults(defaults);
            builder.setWhen(System.currentTimeMillis());
            mNotificationManager.notify(message.getFrom(), 1, builder.build());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onListItemClicked(EMConversation conversation) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, conversation.conversationId());
        startActivity(intent);
    }

    @Override
    public void onListItemLongClick(View view, EMConversation conversation) {
        curConversation = conversation;
        initPopwindow(view, popupLocation(view));
    }


    /**
     * @param contentTv 弹框依赖view
     * @param location  弹框坐标
     */
    public void initPopwindow(View contentTv, int[] location) {
        if (popupWindow == null) {
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
    private int[] popupLocation(View view) {
        view.getLocationOnScreen(location);
        int viewHeight = view.getHeight();
        int viewWidth = view.getWidth();
        if (screenHeight - location[1] > (popupHeight + popupHeight / 2)) {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - viewHeight / 2;
        } else {
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
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //设置阴影透明度
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_main_tab1:
                tabMainView();
                break;
            case R.id.act_main_tab2:
                tabEaseMsgView();
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

    private void tabMainView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            mainFragment.setOnPatientApplyCallbackListener(this);
            transaction.add(R.id.act_main_tab_frameLayout, mainFragment);
        } else {
            transaction.show(mainFragment);
            mainFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    /**
     * 环信自带会话列表
     */
    private void tabEaseMsgView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (easeConversationListFragment == null) {
            easeConversationListFragment = new EaseConversationListFragment();
            transaction.add(R.id.act_main_tab_frameLayout, easeConversationListFragment);
        } else {
            transaction.show(easeConversationListFragment);
            easeConversationListFragment.onResume();
        }
        easeConversationListFragment.setConversationListItemClickListener(this);
        easeConversationListFragment.setConversationListItemLongClickListener(this);
        transaction.commitAllowingStateLoss();
        selectTab(1);
    }

    private void tabCooperateDocView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (cooperateDocFragment == null) {
            cooperateDocFragment = new CooperateDocFragment();
            cooperateDocFragment.setOnDocApplyCallbackListener(this);
            transaction.add(R.id.act_main_tab_frameLayout, cooperateDocFragment);
        } else {
            transaction.show(cooperateDocFragment);
            cooperateDocFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(2);
    }

    private void tabMyView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (userFragment == null) {
            userFragment = new UserFragment();
            userFragment.setOnTransferCallbackListener(this);
            transaction.add(R.id.act_main_tab_frameLayout, userFragment);
        } else {
            transaction.show(userFragment);
            userFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(3);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (easeConversationListFragment != null) {
            transaction.hide(easeConversationListFragment);
        }
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (cooperateDocFragment != null) {
            transaction.hide(cooperateDocFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case 0:
                tab1.setSelected(true);
                tab2.setSelected(false);
                tab3.setSelected(false);
                tab4.setSelected(false);
                break;
            case 1:
                tab1.setSelected(false);
                tab2.setSelected(true);
                tab3.setSelected(false);
                tab4.setSelected(false);
                break;
            case 2:
                tab1.setSelected(false);
                tab2.setSelected(false);
                tab3.setSelected(true);
                tab4.setSelected(false);
                break;
            case 3:
                tab1.setSelected(false);
                tab2.setSelected(false);
                tab3.setSelected(false);
                tab4.setSelected(true);
                break;
            default:
                tab1.setSelected(true);
                tab2.setSelected(false);
                tab3.setSelected(false);
                tab4.setSelected(false);
                break;
        }
    }

    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            YihtApplication.getInstance().setVersionRemind(false);
            return;
        }
        YihtApplication.getInstance().setVersionRemind(true);
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
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, "开始下载");
    }

    /**
     * 合作医生申请
     */
    @Override
    public void onDocApplyCallback() {
        try {
            String pNum = sharePreferenceUtil.getString(CommonData.KEY_DOCTOR_APPLY_NUM);
            if (TextUtils.isEmpty(pNum)) {
                return;
            }
            int num = Integer.valueOf(pNum);
            if (num > 0) {
                rlMsgPointLayout3.setVisibility(View.VISIBLE);
                tvUnReadMsgCount3.setText(num > 99 ? "99+" : num + "");
            } else {
                rlMsgPointLayout3.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    /**
     * 转诊申请
     */
    @Override
    public void onTransferCallback() {
        try {
            boolean transfer = sharePreferenceUtil.getBoolean(CommonData.KEY_CHANGE_PATIENT_NUM);
            if (transfer) {
                rlMsgPointLayout4.setVisibility(View.VISIBLE);
                tvUnReadMsgCount4.setText("1");
            } else {
                rlMsgPointLayout4.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    public void onPatientApplyCallback() {
        try {
            String pNum = sharePreferenceUtil.getString(CommonData.KEY_PATIENT_APPLY_NUM);
            if (TextUtils.isEmpty(pNum)) {
                return;
            }
            int num = Integer.valueOf(pNum);
            if (num > 0) {
                rlMsgPointLayout.setVisibility(View.VISIBLE);
                tvUnReadMsgCount.setText(num > 99 ? "99+" : num + "");
            } else {
                rlMsgPointLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
    }

    public class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(() -> {

                if (error == EMError.USER_REMOVED) {
                    LogUtils.e("test", "账号被删除");
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    ToastUtil.toast(MainActivity.this, "账号在其他设备登录");
                }
            });
        }
    }

    /**
     * 设置标签与别名
     */
    private void setAlias() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
