package com.lin.yuantiku.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lin.yuantiku.R;


/**
 * 2016/8/17 11:29
 *
 * @author zhanglin
 * @email www.zhanglin01@100tal.com
 * todo
 */
public class RotateImageView extends ImageView {
    Animation animation;
    boolean isDialog; //

    public RotateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(Context context) {
        this(context, null);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startAnimation() {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
            animation.setInterpolator(new LinearInterpolator());
            setAnimation(animation);
        }
        startAnimation(animation);
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }

    public void stopAnaimtion() {
        clearAnimation();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isDialog) {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isDialog) {
            stopAnaimtion();
        }

    }
}
