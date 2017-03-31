package com.colin.tiankong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ReadPagerAdapter;
import com.colin.tiankong.fragment.ReadFragment;
import com.colin.tiankong.fragment.ReadResultFragment;
import com.colin.tiankong.utils.RxBus;
import com.colin.tiankong.utils.event.EventShowResult;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class ReadTestActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ReadPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private CompositeSubscription subscriptions;

    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, ReadTestActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_read_test);
        initView();
        registerObserver();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments.add(ReadFragment.getInstance());
        fragments.add(ReadResultFragment.getInstance());
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
}
