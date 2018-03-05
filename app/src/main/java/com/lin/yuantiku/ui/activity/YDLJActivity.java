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
import com.lin.yuantiku.ui.presenter.YDLJPresenter;
import com.lin.yuantiku.view.YDLJView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class YDLJActivity extends BaseActivity implements YDLJView {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private ReadPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    @Inject
    YDLJPresenter mPresenter;

    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, YDLJActivity.class));
    }


    private void initView() {
        mPresenter.attachView(this);
        String[] titles = getResources().getStringArray(R.array.read_title_array);
        fragments.add(ReadFragment.getInstance(titles));
        ArrayList<ChooseItem> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ChooseItem ci = new ChooseItem();
            ci.cat_name = String.valueOf(i + 1);
            list.add(ci);
        }
        fragments.add(ResultFragment.getInstance(list));
        pagerAdapter = new ReadPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void updateViewPagerIndex(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public int setContentView() {
        return R.layout.act_read_test;
    }

    @Override
    public void initUIAndData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
