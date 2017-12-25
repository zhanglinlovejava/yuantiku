package com.lin.yuantiku.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.HomePagerAdapter;
import com.lin.yuantiku.ui.presenter.GSWPresenter;
import com.lin.yuantiku.view.GSWView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class GSWActivity extends BaseActivity implements GSWView, ViewPager.OnPageChangeListener {
    private HomePagerAdapter adapter;
    private Map<Integer, ChooseItem> dataMap;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Inject
    GSWPresenter mPresenter;

    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, GSWActivity.class));
    }

    @Override
    public int setContentView() {
        return R.layout.act_gsw;
    }

    @Override
    public void initUIAndData(Bundle savedInstanceState) {
        String[] bodys = getResources().getStringArray(R.array.gsw_array);
        dataMap = new HashMap<>();
        for (int i = 0; i < bodys.length + 1; i++) {
            if (i == bodys.length) {
                dataMap.put(i, null);
                continue;
            }
            ChooseItem ci = new ChooseItem();
            ci.body = bodys[i];
            ci.index = i;
            ci.total = bodys.length;
            dataMap.put(i, ci);
        }
        adapter = new HomePagerAdapter(getSupportFragmentManager(), dataMap);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        mPresenter.attachView(this);
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }


    @Override
    public void updateAnswer(ChooseItem chooseItem) {
        dataMap.put(chooseItem.index, chooseItem);
    }

    @Override
    public void setCurrentItem(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        hideInputMethod();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
