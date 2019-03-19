package com.zyc.doctor.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * viewpager适配器
 *
 * @author DUNDUN
 */
public class FragmentVpAdapter extends PagerAdapter {


    private List<Fragment> fragmentList;
    private Fragment currentFragment;

    public FragmentVpAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        if (fragmentList != null) {

            return fragmentList.size();
        }
        return 0;
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

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    /**
     * 得到当前正在展示的view
     */
    public View getCurrentView() {
        return currentFragment.getView();
    }
}
