package com.lin.yuantiku.injector.module;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.lin.yuantiku.injector.scoped.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhanglin on 2017/7/25.
 */
@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }
}
