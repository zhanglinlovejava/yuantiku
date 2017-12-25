package com.lin.yuantiku.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.entity.ChooseItemParent;
import com.lin.yuantiku.ui.fragment.ChooseItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class WXTKChoosePagerAdapter extends FragmentStatePagerAdapter {
    private List<ChooseItemParent> list;

    public WXTKChoosePagerAdapter(FragmentManager fm, List<ChooseItemParent> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return ChooseItemFragment.getInstance((ArrayList<ChooseItem>) list.get(position).list,position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
