package com.lin.yuantiku;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lin.yuantiku.injector.component.ApplicationComponent;
import com.lin.yuantiku.injector.component.DaggerApplicationComponent;
import com.lin.yuantiku.injector.module.ApplicationModule;
import com.lin.yuantiku.utils.ScreenUtil;

/**
 * Created by Colin.Zhang on 2017/3/27.
 */

public class YTKApplication extends Application {
    private static Context context;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

        context = this;
        ScreenUtil.init(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
