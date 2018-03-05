package com.lin.yuantiku.ui.presenter;

import android.support.annotation.Nullable;

import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventShowResult;
import com.lin.yuantiku.view.YDLJView;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by zhanglin on 2018/3/5.
 */

public class YDLJPresenter extends BasePresenter<YDLJView> {


    @Inject
    public YDLJPresenter() {
    }

    @Override
    public void attachView(@Nullable YDLJView view) {
        super.attachView(view);
        registerObserver();
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    private void registerObserver() {
        mSubscriptions.add(RxBus.getDefault().toObserverable(EventShowResult.class)
                .subscribe(new Action1<EventShowResult>() {
                    @Override
                    public void call(EventShowResult eventShowResult) {
                        view.updateViewPagerIndex(0);
                    }
                }));
    }
}
