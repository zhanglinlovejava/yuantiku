package com.colin.tiankong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ChooseItemsAdapter;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.utils.MyItemClickListener;
import com.colin.tiankong.utils.RxBus;
import com.colin.tiankong.utils.event.EventUpdateChooseWXTK;

import java.util.ArrayList;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ChooseItemFragment extends Fragment {
    public static ChooseItemFragment getInstance(ArrayList<ChooseItem> list, int index) {
        ChooseItemFragment cif = new ChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("item", list);
        bundle.putInt("index", index);
        cif.setArguments(bundle);
        return cif;
    }

    private int index = 0;
    private RecyclerView recyclerView;
    private ChooseItemsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_choose_read, container, false);
        ArrayList<ChooseItem> list = getArguments().getParcelableArrayList("item");
        index = getArguments().getInt("index");
        recyclerView = (RecyclerView) view.findViewById(R.id.chooseItemRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        adapter = new ChooseItemsAdapter(list, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (adapter.list.get(postion).isChoosed) {
                    adapter.list.get(postion).isChoosed = false;
                } else {
                    for (int i = 0; i < adapter.list.size(); i++) {
                        adapter.list.get(i).isChoosed = postion == i;
                    }
                }
                adapter.notifyDataSetChanged();
                RxBus.getDefault().post(new EventUpdateChooseWXTK(index, adapter.list.get(postion).isChoosed, adapter.list.get(postion).body));
            }
        },ChooseItemsAdapter.CHOOSE_TYPE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }
}
