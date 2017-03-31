package com.colin.tiankong.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by lijinhua on 2016/6/3.
 */
public abstract class BaseFragment extends Fragment {


    private boolean isFirstLoad = true;//是否是第一次加载数据


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isFirstLoad) {
                lazyLoadData();
                isFirstLoad = false;
            }
        }
    }

    /**
     * 在这里面进行延迟加载数据
     */
    public void lazyLoadData() {

    }

}
