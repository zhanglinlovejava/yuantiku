package com.lin.yuantiku.injector.component;

import android.app.Activity;

import com.lin.yuantiku.injector.module.ActivityModule;
import com.lin.yuantiku.injector.scoped.PerActivity;
import com.lin.yuantiku.ui.activity.GSWActivity;
import com.lin.yuantiku.ui.activity.YDLJActivity;

import dagger.Component;

/**
 * Created by zhanglin on 2017/7/25.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivityContext();

    void inject(YDLJActivity activity);

    void inject(GSWActivity activity);


}

