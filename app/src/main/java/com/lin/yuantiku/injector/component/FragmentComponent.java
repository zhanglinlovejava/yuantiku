package com.lin.yuantiku.injector.component;

import android.app.Activity;

import com.lin.yuantiku.injector.module.FragmentModule;
import com.lin.yuantiku.injector.scoped.PerFragment;
import com.lin.yuantiku.ui.fragment.ChildFragment;
import com.lin.yuantiku.ui.fragment.ChooseItemFragment;
import com.lin.yuantiku.ui.fragment.ReadChooseFragment;
import com.lin.yuantiku.ui.fragment.ReadFragment;
import com.lin.yuantiku.ui.fragment.ResultFragment;

import dagger.Component;

/**
 * Created by zhanglin on 2017/7/25.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(ChildFragment fragment);

    void inject(ResultFragment fragment);

    void inject(ChooseItemFragment fragment);

    void inject(ReadChooseFragment fragment);

    void inject(ReadFragment fragment);


}
