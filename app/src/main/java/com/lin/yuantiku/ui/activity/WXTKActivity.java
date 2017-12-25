package com.lin.yuantiku.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lin.yuantiku.R;
import com.lin.yuantiku.ui.adapter.WXTKChoosePagerAdapter;
import com.lin.yuantiku.ui.customview.MTextView;
import com.lin.yuantiku.ui.customview.ViewPagerNoScroll;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.entity.ChooseItemParent;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.ScreenUtil;
import com.lin.yuantiku.utils.event.EventUpdateChooseWXTK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;

public class WXTKActivity extends FragmentActivity {
    private MTextView mTextView;
    private RelativeLayout relativeLayout;
    private ViewPagerNoScroll mViewPager;
    private WXTKChoosePagerAdapter pagerAdapter;
    private ScrollView scrollView;
    private List<TextView> textViews = new ArrayList<>();
    private Subscription subscription;
    private View loadingView;

    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, WXTKActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wxtk);
        mTextView = (MTextView) this.findViewById(R.id.mtextview);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlParent);
        mViewPager = (ViewPagerNoScroll) findViewById(R.id.viewPager);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        loadingView = findViewById(R.id.loadingView);
        initTextContent();
        registerObserver();
    }

    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private void initTextContent() {
        String source = getString(R.string.body_wxtk);
        SpannableString ss = new SpannableString(source);
        for (int i = 0; i < source.length() - 1; i++) {
            if (ss.charAt(i) == '*') {
                ImageSpan is = new ImageSpan(WXTKActivity.this, R.drawable.testbg_null_wxtk);
                ss.setSpan(is, i, i + 6, 0);
            }
        }
        mTextView.setMText(ss);
        mTextView.setTextSize(15);
        mTextView.setTextColor(Color.BLACK);
        mTextView.invalidate();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.isShown()) {
                    resetTextView();
                    mViewPager.setVisibility(View.GONE);
                }

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addTextView();
                hideLoading();
            }
        }, 200);
    }

    private void addTextView() {
        Map<Integer, List<Integer>> map = mTextView.getListMap();
        Log.e("test", map.toString());
        for (int i = 0; i < map.size(); i++) {
            List<Integer> list = map.get(i);
            final TextView textView = new TextView(WXTKActivity.this);
            textView.setBackgroundResource(R.drawable.testbg);
            textView.setText((i + 1) + "");
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (!mViewPager.isShown()) {
                        mViewPager.setVisibility(View.VISIBLE);
                    }
                    scrollBy(v);
                    resetTextView();
                    v.setBackgroundResource(R.drawable.testbg_low);
                    ((TextView) v).setTextColor(Color.WHITE);
                    mViewPager.setCurrentItem((int) v.getTag());
                }
            });
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(getResources().
                    getDimensionPixelSize(R.dimen.box_width_wxtk), getResources().getDimensionPixelSize(R.dimen.box_height_wxtk));
            rl.leftMargin = list.get(0);
            rl.topMargin = list.get(1);
            textViews.add(textView);
            relativeLayout.addView(textView, rl);
        }
        initViewPager(map.size());
    }

    private void scrollBy(final View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int y = location[1] + 10;
        if (y > mViewPager.getTop() - ScreenUtil.dip2px(10)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollBy(0, 30);
                    scrollBy(v);
                }
            }, 10);
        }
    }

    private void resetTextView() {
        for (int j = 0; j < textViews.size(); j++) {
            TextView tv = textViews.get(j);
            if (tv.getText().toString().length() > 3) {
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundResource(R.drawable.testbg_trans);
            } else {
                tv.setBackgroundResource(R.drawable.testbg);
            }
        }
    }

    public void initViewPager(int size) {
        String[] cats = getResources().getStringArray(R.array.read_item_cat);
        String[] bodys = getResources().getStringArray(R.array.read_item);
        List<ChooseItemParent> parents = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ChooseItemParent parent = new ChooseItemParent();
            parent.index = i;
            ArrayList<ChooseItem> list = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                ChooseItem ci = new ChooseItem();
                ci.cat_name = cats[j];
                ci.body = bodys[j] + "--" + (i + 1);
                ci.index = j;
                ci.isChoosed = false;
                list.add(ci);
            }
            parent.list = list;
            parents.add(parent);
        }
        pagerAdapter = new WXTKChoosePagerAdapter(getSupportFragmentManager(), parents);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void registerObserver() {
        subscription = RxBus.getDefault().toObserverable(EventUpdateChooseWXTK.class).subscribe(new Action1<EventUpdateChooseWXTK>() {
            @Override
            public void call(EventUpdateChooseWXTK eventUpdateChoose) {
                TextView tv = textViews.get(eventUpdateChoose.index);
                if (eventUpdateChoose.isChoosed) {
                    tv.setText((eventUpdateChoose.index + 1) + " " + eventUpdateChoose.body);
                    tv.setBackgroundResource(R.drawable.testbg_trans);
                    tv.setTextColor(Color.BLACK);
                    if (eventUpdateChoose.index + 1 < pagerAdapter.getCount()) {
                        if (textViews.get(eventUpdateChoose.index + 1).getText().length() <= 2) {
                            textViews.get(eventUpdateChoose.index + 1).setBackgroundResource(R.drawable.testbg_low);
                            mViewPager.setCurrentItem(eventUpdateChoose.index + 1);
                            scrollBy(textViews.get(eventUpdateChoose.index + 1));
                            return;
                        }
                        mViewPager.setVisibility(View.GONE);
                    }
                } else {
                    tv.setTextColor(Color.WHITE);
                    tv.setText((eventUpdateChoose.index + 1) + " ");
                    tv.setBackgroundResource(R.drawable.testbg);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
