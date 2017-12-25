package com.lin.yuantiku.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.adapter.BaseAdapter;
import com.lin.yuantiku.ui.adapter.ChooseItemsAdapter;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventUpdateResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ReadChooseFragment extends BaseFragment implements BaseAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private ChooseItemsAdapter adapter;
    private int index = 0;

    public static ReadChooseFragment getInstance(String title, int index, List<ChooseItem> list) {
        ReadChooseFragment cf = new ReadChooseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("title", title);
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
        cf.setArguments(bundle);
        return cf;
    }

    @Override
    public void initUiAndData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        List<ChooseItem> items = getArguments().getParcelableArrayList("list");
        tvTitle.setText(getArguments().getString("title"));
        index = getArguments().getInt("index");
        tvTitle.setVisibility(View.VISIBLE);
        adapter = new ChooseItemsAdapter(mContext, ChooseItemsAdapter.READ_TYPE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setNewData(items);
        adapter.setOnRecyclerViewItemClickListener(this);
    }

    @Override
    public int layoutId() {
        return R.layout.frag_choose_read;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void onItemClick(View view, int postion) {
        List<ChooseItem> list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (postion != i) {
                list.get(i).isChoosed = false;
            }
        }
        list.get(postion).isChoosed = !list.get(postion).isChoosed;
        adapter.getData().set(postion, list.get(postion));
        adapter.notifyDataSetChanged();

        ChooseItem ci = new ChooseItem();
        ci.index = index;
        ci.cat_name = String.valueOf(index + 1);
        ci.isChoosed = list.get(postion).isChoosed;
        RxBus.getDefault().post(new EventUpdateResult(ci));
    }
}
