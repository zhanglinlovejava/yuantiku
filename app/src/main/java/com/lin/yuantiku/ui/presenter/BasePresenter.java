package com.lin.yuantiku.ui.presenter;

import android.support.annotation.Nullable;

import com.lin.yuantiku.view.BaseView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhanglin on 2017/7/25.
 */

public abstract class BasePresenter<T extends BaseView> {
    public T view;
    protected CompositeSubscription mSubscriptions;

    public void attachView(@Nullable T view) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
            this.view = view;
        }
    }

    public void detachView() {
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
        view = null;
    }
}
