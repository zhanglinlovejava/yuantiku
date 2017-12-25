package com.lin.yuantiku.ui.presenter;

import android.support.annotation.Nullable;

import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventShowResult;
import com.lin.yuantiku.utils.event.EventUpdateAnswer;
import com.lin.yuantiku.view.GSWView;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by zhanglin on 2017/12/22.
 */

public class GSWPresenter extends BasePresenter<GSWView> {

    @Inject
    public GSWPresenter() {
    }

    @Override
    public void attachView(@Nullable GSWView view) {
        super.attachView(view);
        registerSubscriptions();
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    private void registerSubscriptions() {
        mSubscriptions.add(RxBus.getDefault().toObserverable(EventUpdateAnswer.class)
                .subscribe(new Action1<EventUpdateAnswer>() {
                    @Override
                    public void call(EventUpdateAnswer event) {
                        view.updateAnswer(event.chooseItem);
                    }
                }));

        mSubscriptions.add(RxBus.getDefault().toObserverable(EventShowResult.class)
                .subscribe(new Action1<EventShowResult>() {
                    @Override
                    public void call(EventShowResult event) {
                        view.setCurrentItem(event.index);
                    }
                }));

    }

}
