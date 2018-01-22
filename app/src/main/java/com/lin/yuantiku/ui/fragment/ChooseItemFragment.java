package com.lin.yuantiku.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.BaseAdapter;
import com.lin.yuantiku.ui.adapter.ChooseItemsAdapter;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventUpdateChooseWXTK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ChooseItemFragment extends BaseFragment implements BaseAdapter.OnRecyclerViewItemClickListener {


    private int index = 0;
    private ChooseItemsAdapter adapter;

    public static ChooseItemFragment getInstance(ArrayList<ChooseItem> list, int index) {
        ChooseItemFragment cif = new ChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("item", list);
        bundle.putInt("index", index);
        cif.setArguments(bundle);
        return cif;
    }

    @Override
    public void initUiAndData() {
        ArrayList<ChooseItem> list = getArguments().getParcelableArrayList("item");
        index = getArguments().getInt("index");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        adapter = new ChooseItemsAdapter(mContext, ChooseItemsAdapter.CHOOSE_TYPE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setNewData(list);
    }

    @Override
    public void onItemClick(View view, int postion) {
        List<ChooseItem> list = adapter.getData();
        if (list.get(postion).isChoosed) {
            list.get(postion).isChoosed = false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).isChoosed = (postion == i);
            }
        }
        adapter.notifyDataSetChanged();
        RxBus.getDefault().post(new EventUpdateChooseWXTK(index, list.get(postion).isChoosed, list.get(postion).body));
    }

    @Override
    public int layoutId() {
        return R.layout.frag_choose_read;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }
}
