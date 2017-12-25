package com.lin.yuantiku.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.BaseAdapter;
import com.lin.yuantiku.ui.adapter.ChooseItemsAdapter;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventShowResult;
import com.lin.yuantiku.utils.event.EventUpdateResult;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ResultFragment extends BaseFragment implements BaseAdapter.OnRecyclerViewItemClickListener {

    private ChooseItemsAdapter adapter;
    private CompositeSubscription subscriptions;

    public static ResultFragment getInstance(ArrayList<ChooseItem> list) {
        ResultFragment rf = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", list);
        rf.setArguments(bundle);
        return rf;
    }


    private void registerObersver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventUpdateResult.class)
                .subscribe(new Action1<EventUpdateResult>() {
                    @Override
                    public void call(EventUpdateResult event) {
                        adapter.getData().set(event.chooseItem.index,event.chooseItem);
                        adapter.notifyDataSetChanged();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void initUiAndData() {
        List<ChooseItem> data = getArguments().getParcelableArrayList("data");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        adapter = new ChooseItemsAdapter(mContext, ChooseItemsAdapter.RESULT_TYPE);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setOnRecyclerViewItemClickListener(this);
        registerObersver();
    }

    @Override
    public int layoutId() {
        return R.layout.frag_result;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        RxBus.getDefault().post(new EventShowResult(position));
    }
}
