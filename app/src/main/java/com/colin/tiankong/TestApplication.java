package com.colin.tiankong;

import android.app.Application;
import android.content.Context;

import com.colin.tiankong.utils.ScreenUtil;

/**
 * Created by Colin.Zhang on 2017/3/27.
 */

public class TestApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ScreenUtil.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
