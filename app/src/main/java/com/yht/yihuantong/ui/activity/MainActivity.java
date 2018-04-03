package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.fragment.CaseFragment;
import com.yht.yihuantong.ui.fragment.DocFragment;
import com.yht.yihuantong.ui.fragment.MsgFragment;
import com.yht.yihuantong.ui.fragment.MyFragment;

import custom.frame.ui.activity.BaseActivity;
import custom.widgets.ripples.RippleLinearLayout;

public class MainActivity extends BaseActivity
{
    private RippleLinearLayout tabMsg, tabDoc, tabCase, tabMy;
    private Fragment msgFragment, docFragment, caseFragment, myFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public int getLayoutID()
    {
        return R.layout.activity_main;
    }

    /**
     * 初始化tabs
     */
    private void initTab()
    {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        tabMsgView();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tabMsg = (RippleLinearLayout)findViewById(R.id.act_main_tab1);
        tabDoc = (RippleLinearLayout)findViewById(R.id.act_main_tab2);
        tabCase = (RippleLinearLayout)findViewById(R.id.act_main_tab3);
        tabMy = (RippleLinearLayout)findViewById(R.id.act_main_tab4);
        initTab();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tabMsg.setOnClickListener(this);
        tabDoc.setOnClickListener(this);
        tabCase.setOnClickListener(this);
        tabMy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_main_tab1:
                tabMsgView();
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
                tabMsgView();
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

    private void tabDocView()
    {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (docFragment == null)
        {
            docFragment = new DocFragment();
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
            caseFragment = new CaseFragment();
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
            myFragment = new MyFragment();
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
}
