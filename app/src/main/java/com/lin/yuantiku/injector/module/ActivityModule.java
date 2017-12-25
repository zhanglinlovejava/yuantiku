package com.lin.yuantiku.injector.module;

import android.app.Activity;

import com.lin.yuantiku.injector.scoped.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhanglin on 2017/7/25.
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }


}
