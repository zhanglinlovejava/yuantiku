package com.colin.tiankong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ChooseItemsAdapter;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.utils.MyItemClickListener;
import com.colin.tiankong.utils.RxBus;
import com.colin.tiankong.utils.event.EventShowResult;
import com.colin.tiankong.utils.event.EventUpdateChoose;
import com.colin.tiankong.utils.event.EventUpdateResult;

import java.util.ArrayList;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ReadResultFragment extends Fragment {
    public static ReadResultFragment getInstance() {
        ReadResultFragment readResultFragment = new ReadResultFragment();
        return readResultFragment;
    }

    private RecyclerView recyclerView;
    private ChooseItemsAdapter adapter;
    private CompositeSubscription subscriptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_result, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        adapter = new ChooseItemsAdapter(new ArrayList<ChooseItem>(), new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                RxBus.getDefault().post(new EventShowResult(postion));
            }
        }, ChooseItemsAdapter.RESULT_TYPE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        registerObersver();
        return view;
    }


    private void registerObersver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventUpdateChoose.class)
                .subscribe(new Action1<EventUpdateChoose>() {
                    @Override
                    public void call(EventUpdateChoose eventUpdateChoose) {
                        adapter.updateData(eventUpdateChoose.list);
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventUpdateResult.class).subscribe(
                new Action1<EventUpdateResult>() {
                    @Override
                    public void call(EventUpdateResult eventUpdateResult) {
                        adapter.list.get(eventUpdateResult.resultEntity.index).isChoosed = eventUpdateResult.resultEntity.isAnswer;
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
