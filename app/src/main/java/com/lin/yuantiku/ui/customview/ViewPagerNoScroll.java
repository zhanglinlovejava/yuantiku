package com.lin.yuantiku.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Colin.Zhang on 2017/3/27.
 * 自定义不可滑动的viewpager
 */
public class ViewPagerNoScroll extends CustomViewPager {

    public ViewPagerNoScroll(Context context) {
        super(context);
    }

    public ViewPagerNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
