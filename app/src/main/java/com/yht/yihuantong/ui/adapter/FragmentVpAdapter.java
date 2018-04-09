package com.yht.yihuantong.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * viewpager适配器
 * @author DUNDUN
 */
public class FragmentVpAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> fragmentList;
    private Fragment currentFragment;

    public FragmentVpAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        if (fragmentList != null)
        {

            return fragmentList.size();
        }
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (Fragment) object;
    }

    /**
     * 得到当前正在展示的view
     */
    public View getCurrentView() {
        return currentFragment.getView();
    }
}
