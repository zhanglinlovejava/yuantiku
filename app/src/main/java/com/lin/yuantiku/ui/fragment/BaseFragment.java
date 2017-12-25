package com.lin.yuantiku.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lin.yuantiku.R;
import com.lin.yuantiku.YTKApplication;
import com.lin.yuantiku.injector.component.DaggerFragmentComponent;
import com.lin.yuantiku.injector.component.FragmentComponent;
import com.lin.yuantiku.injector.module.FragmentModule;
import com.lin.yuantiku.utils.MessageToast;
import com.lin.yuantiku.view.BaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lijinhua on 2016/5/19.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    protected FragmentComponent mFragmentComponent;
    protected Context mContext;
    private boolean isFirstLoad = true;//是否是第一次加载数据
    Unbinder unbinder;
    @Nullable
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(((YTKApplication) getActivity().getApplication()).getApplicationComponent())
                .build();
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initInjector();
        initUiAndData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutId(), container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isFirstLoad) {
                lazyLoadData();
                isFirstLoad = false;
            }
        }
    }

    /**
     * 在这里面进行延迟加载数据
     */
    public void lazyLoadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind(); // 在fragment里面需要unBind
    }


    public abstract void initUiAndData();

    public abstract int layoutId();

    public abstract void initInjector();

    @Override
    public void showToast(String msg) {
        MessageToast.showToast(msg);
    }

}
