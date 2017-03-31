package com.colin.tiankong.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ReadPagerAdapter;
import com.colin.tiankong.customview.DragSlopLayout;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.utils.RxBus;
import com.colin.tiankong.utils.ScreenUtil;
import com.colin.tiankong.utils.event.EventShowResult;
import com.colin.tiankong.utils.event.EventUpdateChoose;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ReadFragment extends Fragment {
    public static ReadFragment getInstance() {
        return new ReadFragment();
    }

    private CompositeSubscription subscriptions;
    DragSlopLayout mDragLayout;
    private ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    private ScrollView scrollView;
    private TextView tvInde;
    String[] cats = {"A", "B", "C", "D"};
    String[] bodys = {"place ", "room", "floor", "ground "};
    String[] titles = {" In your class, if some students’ English is very good, but others is very poor,     What will you do?"
            , "In your opinion, what is a good teacher in English teaching? "
            , "Can you introduce yourself in English briefly?"
            , " Can you name some ways to compliment or to praise your students?  "
            , " Can you name some ways to compliment or to praise your students?  "
            , " Can you name some ways to compliment or to praise your students?  "};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_read, container, false);
        initView(view);
        initListener();
        registerObserver();
        return view;
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

    private void initView(View view) {
        mDragLayout = (DragSlopLayout) view.findViewById(R.id.drag_layout);
        viewPager = (ViewPager) view.findViewById(R.id.readViewPager);
        tvInde = (TextView) view.findViewById(R.id.tvInd);
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
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
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
}
