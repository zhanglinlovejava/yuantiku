package com.colin.tiankong.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.colin.tiankong.R;
import com.colin.tiankong.adapter.ChooseItemsAdapter;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.utils.MyItemClickListener;
import com.colin.tiankong.utils.RxBus;
import com.colin.tiankong.utils.event.EventUpdateResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ReadChooseFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChooseItemsAdapter adapter;
    private TextView tvTitle;

    public static ReadChooseFragment getInstance(String title, int index, List<ChooseItem> list) {
        ReadChooseFragment cf = new ReadChooseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("title", title);
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
        cf.setArguments(bundle);
        return cf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_choose_read, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.chooseItemRecyclerView);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        List<ChooseItem> items = getArguments().getParcelableArrayList("list");
        tvTitle.setText(getArguments().getString("title"));
        tvTitle.setVisibility(View.VISIBLE);
        adapter = new ChooseItemsAdapter(items, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                for (int i = 0; i < adapter.list.size(); i++) {
                    if (postion != i) {
                        adapter.list.get(i).isChoosed = false;
                    }
                }
                adapter.list.get(postion).isChoosed = !adapter.list.get(postion).isChoosed;
                RxBus.getDefault().post(new EventUpdateResult(new ResultEntity(getArguments().getInt("index"),adapter.list.get(postion).isChoosed)));
                adapter.notifyDataSetChanged();
            }
        },ChooseItemsAdapter.READ_TYPE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }
}
