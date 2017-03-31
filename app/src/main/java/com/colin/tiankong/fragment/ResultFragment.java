package com.colin.tiankong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ResultAdapter;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.utils.event.EventShowResult;
import com.colin.tiankong.utils.event.EventUpdateResult;
import com.colin.tiankong.utils.MyItemClickListener;
import com.colin.tiankong.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ResultFragment extends BaseFragment {
    public static ResultFragment getInstance(ArrayList<ResultEntity> dataList) {
        ResultFragment rf = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", dataList);
        rf.setArguments(bundle);
        return rf;
    }

    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private CompositeSubscription subscriptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_result, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        List<ResultEntity> dataList = getArguments().getParcelableArrayList("data");
        adapter = new ResultAdapter(dataList, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                RxBus.getDefault().post(new EventShowResult(postion));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        registerObersver();
        return view;
    }


    private void registerObersver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventUpdateResult.class)
                .subscribe(new Action1<EventUpdateResult>() {
                    @Override
                    public void call(EventUpdateResult eventUpdateResult) {
                        adapter.list.set(eventUpdateResult.resultEntity.index, eventUpdateResult.resultEntity);
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
}
