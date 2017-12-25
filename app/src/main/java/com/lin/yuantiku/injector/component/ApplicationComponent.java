package com.lin.yuantiku.injector.component;

import android.content.Context;

import com.lin.yuantiku.YTKApplication;
import com.lin.yuantiku.injector.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by zhanglin on 2017/7/25.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(YTKApplication mAppliation);

    Context getContext();

    Retrofit getRetrofit();

}
