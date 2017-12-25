package com.lin.yuantiku.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.ReadPagerAdapter;
import com.lin.yuantiku.ui.customview.DragSlopLayout;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.ScreenUtil;
import com.lin.yuantiku.utils.event.EventShowResult;
import com.lin.yuantiku.utils.event.EventUpdateChoose;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ReadFragment extends BaseFragment {
    private String[] titles;
    List<Fragment> fragments = new ArrayList<>();
    private CompositeSubscription subscriptions;


    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @BindView(R.id.readViewPager)
    ViewPager viewPager;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tvInd)
    TextView tvInde;

    public static ReadFragment getInstance(String[] titles) {
        ReadFragment rf = new ReadFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("titles", titles);
        rf.setArguments(bundle);
        return rf;
    }

    private void initListener() {
        mDragLayout.setDragPositionListener(new DragSlopLayout.OnDragPositionListener() {
            @Override
            public void onDragPosition(final int visibleHeight, float percent, boolean isUp) {
                if (percent == 0.0 || percent == 1.0)
                    setTextViewHeight(visibleHeight);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvInde.setText((position + 1) + "/" + titles.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTextViewHeight(int height) {
        DragSlopLayout.LayoutParams rl = (DragSlopLayout.LayoutParams) scrollView.getLayoutParams();
        rl.height = ScreenUtil.screenHeight - height - 60;
        scrollView.setLayoutParams(rl);
    }

    private void registerObserver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventShowResult.class)
                .subscribe(new Action1<EventShowResult>() {
                    @Override
                    public void call(EventShowResult eventShowResult) {
                        viewPager.setCurrentItem(eventShowResult.index);
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

        titles = getArguments().getStringArray("titles");
        String[] cats = getResources().getStringArray(R.array.read_item_cat);
        String[] bodys = getResources().getStringArray(R.array.read_item);
        for (int i = 0; i < titles.length; i++) {
            List<ChooseItem> items = new ArrayList<>();
            for (int j = 0; j < cats.length; j++) {
                ChooseItem ci = new ChooseItem();
                ci.cat_name = cats[j];
                ci.body = bodys[j] + "-" + (i + 1);
                ci.isChoosed = false;
                items.add(ci);
            }
            fragments.add(ReadChooseFragment.getInstance(titles[i], i, items));
        }
        tvInde.setText("1/" + titles.length);
        ReadPagerAdapter readPagerAdapter = new ReadPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(readPagerAdapter);
        viewPager.setCurrentItem(0);
        setTextViewHeight(getResources().getDimensionPixelSize(R.dimen.drag_default_height));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ChooseItem> results = new ArrayList<>();
                for (int j = 0; j < titles.length; j++) {
                    ChooseItem ci = new ChooseItem();
                    ci.cat_name = String.valueOf(j + 1);
                    ci.isChoosed = false;
                    results.add(ci);
                }
                RxBus.getDefault().post(new EventUpdateChoose(results));
            }
        }, 200);
        initListener();
        registerObserver();
    }

    @Override
    public int layoutId() {
        return R.layout.frag_read;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }
}
