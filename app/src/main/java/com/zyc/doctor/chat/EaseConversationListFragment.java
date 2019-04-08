package com.zyc.doctor.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * conversation list fragment
 * @author dundun
 */
public class EaseConversationListFragment extends EaseBaseFragment
{
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected TextView tvHintTxt;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected boolean isConflict;
    protected EMConversationListener convListener = new EMConversationListener()
    {
        @Override
        public void onCoversationUpdate()
        {
            refresh();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
        { return; }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView()
    {
        //获取状态栏高度，填充
        View mStateBarFixer = getView().findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                              getStateBarHeight(getActivity())));//填充状态栏
        inputMethodManager = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList)getView().findViewById(R.id.list);
        query = (EditText)getView().findViewById(R.id.query);
        tvHintTxt = getView().findViewById(R.id.fragment_conversation_hint);
        ((TextView)(getView().findViewById(R.id.public_title_bar_title))).setText("消息列表");
        // button to clear content in search bar
        clearSearch = (ImageButton)getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout)getView().findViewById(R.id.fl_error_item);
    }

    @Override
    protected void setUpView()
    {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);
        if (listItemClickListener != null)
        {
            conversationListView.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
            if (listItemLongClickListener != null)
            {
                conversationListView.setOnItemLongClickListener(
                        new AdapterView.OnItemLongClickListener()
                        {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id)
                            {
                                EMConversation conversation = conversationListView.getItem(
                                        position);
                                listItemLongClickListener.onListItemLongClick(view, conversation);
                                return true;
                            }
                        });
            }
        }
        EMClient.getInstance().addConnectionListener(connectionListener);
        conversationListView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     *
     * @param a
     * @return
     */
    public static int getStateBarHeight(Activity a)
    {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener()
    {
        @Override
        public void onDisconnected(int error)
        {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE ||
                error == EMError.SERVER_SERVICE_RESTRICTED ||
                error == EMError.USER_KICKED_BY_CHANGE_PASSWORD ||
                error == EMError.USER_KICKED_BY_OTHER_DEVICE)
            {
                isConflict = true;
            }
            else
            {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected()
        {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;
    private EaseConversationListItemLongClickListener listItemLongClickListener;
    protected Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;
                case MSG_REFRESH:
                {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected()
    {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected()
    {
        errorItemContainer.setVisibility(View.VISIBLE);
    }

    /**
     * refresh ui
     */
    public void refresh()
    {
        if (!handler.hasMessages(MSG_REFRESH))
        {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList()
    {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance()
                                                            .chatManager()
                                                            .getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        if (conversations != null && conversations.size() > 0)
        {
            tvHintTxt.setVisibility(View.GONE);
        }
        else
        {
            tvHintTxt.setVisibility(View.VISIBLE);
        }
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations)
        {
            for (EMConversation conversation : conversations.values())
            {
                if (conversation.getAllMessages().size() != 0)
                {
                    sortList.add(new Pair<Long, EMConversation>(
                            conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try
        {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList)
        {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList)
    {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>()
        {
            @Override
            public int compare(final Pair<Long, EMConversation> con1,
                    final Pair<Long, EMConversation> con2)
            {
                if (con1.first.equals(con2.first))
                {
                    return 0;
                }
                else if (con2.first.longValue() > con1.first.longValue())
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
        });
    }

    @Override
    protected void hideSoftKeyboard()
    {
        if (getActivity().getWindow().getAttributes().softInputMode !=
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        {
            if (getActivity().getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict)
        {
            refresh();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!hidden)
        {
            refresh();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (isConflict)
        {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener
    {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    public interface EaseConversationListItemLongClickListener
    {
        void onListItemLongClick(View view, EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(
            EaseConversationListItemClickListener listItemClickListener)
    {
        this.listItemClickListener = listItemClickListener;
    }

    public void setConversationListItemLongClickListener(
            EaseConversationListItemLongClickListener listItemLongClickListener)
    {
        this.listItemLongClickListener = listItemLongClickListener;
    }
}
