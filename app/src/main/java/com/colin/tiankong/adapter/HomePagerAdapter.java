package com.colin.tiankong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.fragment.ChildFragment;
import com.colin.tiankong.fragment.ResultFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Colin.Zhang on 2017/3/29.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private Map<Integer, ChooseItem> dataMap;

    public HomePagerAdapter(FragmentManager fm, Map<Integer, ChooseItem> dataMap) {
        super(fm);
        this.dataMap = dataMap;
    }

    @Override
    public Fragment getItem(int position) {
        if (position + 1 == dataMap.size()) {
            List<ResultEntity> list = new ArrayList<>();
            for (int i = 0; i < dataMap.size() - 1; i++) {
                ResultEntity resultEntity = new ResultEntity();
                resultEntity.index = i;
                if (dataMap.get(i).answer != null && dataMap.get(i).answer.size() > 0) {
                    for (int j = 0; j < dataMap.get(i).answer.size(); j++) {
                        if (dataMap.get(i).answer.get(j) != null && !TextUtils.isEmpty(dataMap.get(i).answer.get(j).trim())) {
                            resultEntity.isAnswer = true;
                        }
                    }
                }
                list.add(resultEntity);
            }
            return ResultFragment.getInstance((ArrayList<ResultEntity>) list);
        }
        return ChildFragment.getInstance(dataMap.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return dataMap.size();
    }
}
