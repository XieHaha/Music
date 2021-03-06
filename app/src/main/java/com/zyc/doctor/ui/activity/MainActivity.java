package com.zyc.doctor.ui.activity;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.chat.ChatActivity;
import com.zyc.doctor.chat.EaseConversationListFragment;
import com.zyc.doctor.chat.listener.AbstractEMContactListener;
import com.zyc.doctor.chat.listener.AbstractEMMessageListener;
import com.zyc.doctor.chat.receive.EaseMsgClickBroadCastReceiver;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.RegistrationTypeBean;
import com.zyc.doctor.data.bean.VersionBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;
import com.zyc.doctor.ui.fragment.CooperateDocFragment;
import com.zyc.doctor.ui.fragment.MainFragment;
import com.zyc.doctor.ui.fragment.UserFragment;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.ScreenUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.version.presenter.VersionPresenter;
import com.zyc.doctor.version.view.VersionUpdateDialog;
import com.zyc.doctor.widgets.ripples.RippleLinearLayout;
import com.zyc.shortcutbadge.ShortcutBadger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * @author dundun
 */
public class MainActivity extends BaseActivity
        implements EaseConversationListFragment.EaseConversationListItemClickListener,
                   VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener,
                   EaseConversationListFragment.EaseConversationListItemLongClickListener,
                   UserFragment.OnTransferCallbackListener, CooperateDocFragment.OnDocApplyCallbackListener,
                   MainFragment.OnMainFragmentCallbackListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.item_msg_num)
    TextView tvUnReadMsgCount;
    @BindView(R.id.message_red_point_small)
    RelativeLayout rlMsgPointSmallLayout;
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
     * message ????????????view
     */
    private View messagePop;
    private TextView tvDelete;
    /**
     * message ????????????
     */
    private PopupWindow popupWindow;
    /**
     * ??????????????????
     */
    private int[] location = new int[2];
    /**
     * ????????????
     */
    private int screenHeight;
    /**
     * popup
     */
    private int popupHeight;
    /**
     * ??????????????????
     */
    private EMConversation curConversation;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private MyConnectionListener connectionListener;
    /**
     * ????????????
     */
    private AbstractEMMessageListener msgListener;
    /**
     * ?????????????????????
     */
    private AbstractEMContactListener contactListener;
    /**
     * ????????????
     */
    private VersionPresenter mVersionPresenter;
    /**
     * ????????????
     */
    private VersionUpdateDialog versionUpdateDialog;
    /**
     * ????????????
     */
    private ArrayList<RegistrationTypeBean> registrationTypeBeans;
    /**
     * ????????????????????????????????????
     */
    private boolean versionUpdateChecked = false;
    /**
     * ??????????????????
     */
    private int msgUnReadCount = 0;
    private Bitmap largeIcon = null;
    private NotificationManager mNotificationManager;
    private int pendingCount = 1;
    /**
     * ????????????  ????????????
     */
    public static final int REQUEST_CODE = 0x0000c0de;
    /**
     * ????????????  ????????????
     */
    public static final int REQUEST_CODE_LOGIN = 200;

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    /**
     * ?????????tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
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
        }
        else if (type == 1) {
            tabEaseMsgView();
        }
        else if (type == 2) {
            tabCooperateDocView();
        }
        else if (type == 3) {
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
        largeIcon = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        initNotify();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //????????????
        mVersionPresenter = new VersionPresenter(this);
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
        //?????????????????????
        screenHeight = ScreenUtils.getScreenHeight(this);
        popupHeight = (int)BaseUtils.dipToPx(this, 48 * 2);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                getAllProduct();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        //?????????????????????????????????listener
        connectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(connectionListener);
        msgListener = new AbstractEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //????????????
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
                runOnUiThread(() -> updateUnReadCount());
                initNotify();
                sendChatMsg(messages.get(0));
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        contactListener = new AbstractEMContactListener() {
            @Override
            public void onContactDeleted(String username) {
                //???????????????????????????
                //????????????
                EMClient.getInstance().chatManager().deleteConversation(username, true);
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
                //??????????????????????????????
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
            }
        };
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        tvDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            HintDialog hintDialog = new HintDialog(this);
            hintDialog.setContentString(getString(R.string.dialog_txt_delete_hint));
            hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
                @Override
                public void onEnter() {
                    if (curConversation != null) {
                        //???????????????user?????????????????????????????????????????????false
                        EMClient.getInstance().chatManager().deleteConversation(curConversation.conversationId(), true);
                        //????????????
                        if (easeConversationListFragment != null) {
                            easeConversationListFragment.refresh();
                        }
                    }
                }
            });
            hintDialog.show();
        });
    }

    private void initNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = BaseData.BASE_CHAT_CHANNEL;
            String channelName = "????????????";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
        else {
            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * ??????????????????
     */
    private void getAllProduct() {
        RequestUtils.getAllProduct(this, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_ALL_PRODUCT:
                registrationTypeBeans = (ArrayList<RegistrationTypeBean>)response.getData();
                if (registrationTypeBeans == null) {
                    registrationTypeBeans = new ArrayList<>();
                }
                //????????????
                DataSupport.deleteAll(RegistrationTypeBean.class);
                DataSupport.saveAll(registrationTypeBeans);
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    public void updateUnReadCount() {
        msgUnReadCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (msgUnReadCount > 0) {
            rlMsgPointLayout2.setVisibility(View.VISIBLE);
            tvUnReadMsgCount2.setText(msgUnReadCount > BaseData.BASE_MEAASGE_DISPLAY_NUM ? "99+" : msgUnReadCount + "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //                sendSubscribeMsg(msgUnReadCount);
            }
            else {
                setShortcutBadge(msgUnReadCount);
            }
        }
        else {
            rlMsgPointLayout2.setVisibility(View.GONE);
            removeShortcutBadge();
        }
    }

    /**
     * ????????????
     */
    private void setShortcutBadge(int badgeCount) {
        ShortcutBadger.applyCount(this, badgeCount);
    }

    /**
     * ????????????
     */
    private void removeShortcutBadge() {
        ShortcutBadger.removeCount(this);
    }

    public void sendChatMsg(EMMessage message) {
        //???????????????????????????????????????????????????????????????????????????
        if (message.getFrom().equals(YihtApplication.getInstance().getChatId())) {
            return;
        }
        if (pendingCount > BaseData.BASE_PENDING_COUNT) {
            pendingCount = 1;
        }
        pendingCount++;
        Intent intent = new Intent(MainActivity.this, EaseMsgClickBroadCastReceiver.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, message.getFrom());
        intent.setAction("ease.msg.android.intent.CLICK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, pendingCount, intent,
                                                                 PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, BaseData.BASE_CHAT_CHANNEL);
            builder.setLargeIcon(largeIcon);
            builder.setChannelId(BaseData.BASE_CHAT_CHANNEL);
        }
        else {
            builder = new NotificationCompat.Builder(this, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.icon_alpha_logo);
            builder.setColor(ContextCompat.getColor(this, R.color.app_main_color));
        }
        else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentTitle("??????");
        builder.setContentText("??????????????????");
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        mNotificationManager.notify(message.getFrom(), pendingCount, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        mNotificationManager.createNotificationChannel(channel);
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
     * @param contentTv ????????????view
     * @param location  ????????????
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
        popupWindow.showAtLocation(contentTv, Gravity.NO_GRAVITY, location[0], location[1] + contentTv.getHeight());
    }

    /**
     * popup????????????
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
        }
        else {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - popupHeight - viewHeight / 2;
        }
        return location;
    }

    /**
     * ???popupwindow????????????????????????
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //?????????????????????
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
            mainFragment.setOnMainFragmentCallbackListener(this);
            transaction.add(R.id.act_main_tab_frameLayout, mainFragment);
        }
        else {
            transaction.show(mainFragment);
            mainFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    /**
     * ????????????????????????
     */
    private void tabEaseMsgView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (easeConversationListFragment == null) {
            easeConversationListFragment = new EaseConversationListFragment();
            transaction.add(R.id.act_main_tab_frameLayout, easeConversationListFragment);
        }
        else {
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
        }
        else {
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
        }
        else {
            transaction.show(userFragment);
            userFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(3);
    }

    /**
     * ??????????????????
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
     * ??????tab
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
    public void updateVersion(VersionBean version, int mode, boolean isDownLoading) {
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
     * ???????????????????????????
     */
    @Override
    public void updateNetWorkError() {
        versionUpdateChecked = true;
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.toast_download_start);
    }

    /**
     * ??????????????????
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
                tvUnReadMsgCount3.setText(num > BaseData.BASE_MEAASGE_DISPLAY_NUM ? "99+" : num + "");
            }
            else {
                rlMsgPointLayout3.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onTransferCallback() {
        try {
            boolean transfer = sharePreferenceUtil.getBoolean(CommonData.KEY_CHANGE_PATIENT_NUM);
            if (transfer) {
                rlMsgPointLayout4.setVisibility(View.VISIBLE);
                tvUnReadMsgCount4.setText("1");
            }
            else {
                rlMsgPointLayout4.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
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
                tvUnReadMsgCount.setText(num > BaseData.BASE_MEAASGE_DISPLAY_NUM ? "99+" : num + "");
            }
            else {
                rlMsgPointLayout.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    public void onOrderStatusCallback() {
        try {
            String transferNum = sharePreferenceUtil.getString(CommonData.KEY_NEW_TRANSFER_MESSAGE_REMIND);
            String orderNum = sharePreferenceUtil.getString(CommonData.KEY_NEW_ORDER_MESSAGE_REMIND);
            if (TextUtils.isEmpty(orderNum) && TextUtils.isEmpty(transferNum)) {
                rlMsgPointSmallLayout.setVisibility(View.GONE);
            }
            else {
                rlMsgPointSmallLayout.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //????????????
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
                    LogUtils.e("test", "???????????????");
                }
                else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    ToastUtil.toast(MainActivity.this, "???????????????????????????");
                }
            });
        }
    }

    /**
     * ?????????????????????
     */
    private void setAlias() {
        JPushInterface.setAlias(this, 100, loginSuccessBean.getDoctorId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE:
            case REQUEST_CODE_LOGIN:
                if (mainFragment != null) {
                    mainFragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ????????? ????????????
     * ???????????????activity???finish  ??????????????????????????????
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
