package com.lin.yuantiku.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lin.yuantiku.YTKApplication;
import com.lin.yuantiku.injector.component.ActivityComponent;
import com.lin.yuantiku.injector.component.DaggerActivityComponent;
import com.lin.yuantiku.injector.module.ActivityModule;
import com.lin.yuantiku.utils.MessageToast;
import com.lin.yuantiku.view.BaseView;

import butterknife.ButterKnife;

/**
 * Created by zhanglin on 2017/7/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected Context mContext;
    protected ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((YTKApplication) getApplication()).getApplicationComponent())
                .build();
        setContentView(setContentView());
        ButterKnife.bind(this);
        initInjector();
        initUIAndData(savedInstanceState);
    }

    public abstract int setContentView();

    public abstract void initUIAndData(Bundle savedInstanceState);

    public abstract void initInjector();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        MessageToast.showToast(msg);
    }

    public void hideInputMethod() {
        View view = getCurrentFocus();
        if (view == null) return;
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
