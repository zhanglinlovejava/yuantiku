package com.lin.yuantiku.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.ReadPagerAdapter;
import com.lin.yuantiku.ui.fragment.ReadFragment;
import com.lin.yuantiku.ui.fragment.ResultFragment;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventShowResult;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class YDLJActivity extends BaseActivity {
    private ViewPager viewPager;
    private ReadPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private CompositeSubscription subscriptions;


    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, YDLJActivity.class));
    }


    private void initView() {
        String[] titles = getResources().getStringArray(R.array.read_title_array);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments.add(ReadFragment.getInstance(titles));
        ArrayList<ChooseItem> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ChooseItem ci = new ChooseItem();
            ci.cat_name = String.valueOf(i+1);
            list.add(ci);
        }
        fragments.add(ResultFragment.getInstance(list));
        pagerAdapter = new ReadPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private void registerObserver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventShowResult.class)
                .subscribe(new Action1<EventShowResult>() {
                    @Override
                    public void call(EventShowResult eventShowResult) {
                        viewPager.setCurrentItem(0);
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
    public int setContentView() {
        return R.layout.act_read_test;
    }

    @Override
    public void initUIAndData(Bundle savedInstanceState) {
        initView();
        registerObserver();
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
