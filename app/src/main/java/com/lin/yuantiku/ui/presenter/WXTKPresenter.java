package com.lin.yuantiku.ui.presenter;

import android.support.annotation.Nullable;

import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventUpdateChooseWXTK;
import com.lin.yuantiku.view.WXTKView;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by zhanglin on 2018/3/5.
 */

public class WXTKPresenter extends BasePresenter<WXTKView> {

    @Inject
    public WXTKPresenter() {
    }

    @Override
    public void attachView(@Nullable WXTKView view) {
        super.attachView(view);
        registerObserver();
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    private void registerObserver() {
        mSubscriptions.add(RxBus.getDefault().toObserverable(EventUpdateChooseWXTK.class).subscribe(new Action1<EventUpdateChooseWXTK>() {
            @Override
            public void call(EventUpdateChooseWXTK eventUpdateChoose) {
                view.updateChoose(eventUpdateChoose);

            }
        }));
    }

}
