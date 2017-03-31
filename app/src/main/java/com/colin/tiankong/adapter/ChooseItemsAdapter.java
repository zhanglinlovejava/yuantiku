package com.colin.tiankong.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colin.tiankong.R;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.utils.MyItemClickListener;

import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ChooseItemsAdapter extends RecyclerView.Adapter<ChooseItemsAdapter.ViewHolder> {
    public static final int READ_TYPE = 1;
    public static final int RESULT_TYPE = 2;
    public static final int CHOOSE_TYPE = 3;
    private int type = 1;
    public List<ChooseItem> list;
    private MyItemClickListener myItemClickListener;

    public ChooseItemsAdapter(List<ChooseItem> list, MyItemClickListener myItemClickListener, int type) {
        this.list = list;
        this.myItemClickListener = myItemClickListener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (type == READ_TYPE||type==CHOOSE_TYPE) {
            holder.tvBody.setVisibility(View.VISIBLE);
            holder.tvBody.setText(list.get(position).body);
        }
        holder.tvCatName.setText(list.get(position).cat_name);
        if (list.get(position).isChoosed) {
            holder.tvCatName.setTextColor(Color.WHITE);
            holder.tvCatName.setBackgroundResource(R.drawable.choosed_bg);
        } else {
            holder.tvCatName.setTextColor(Color.BLACK);
            holder.tvCatName.setBackgroundResource(R.drawable.choosed_bg_trans);
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    public void updateData(List<ChooseItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCatName;
        public TextView tvBody;
        public View rootView;

        public ViewHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.root);
            tvCatName = (TextView) view.findViewById(R.id.tvCat);
            tvBody = (TextView) view.findViewById(R.id.tvBody);
        }
    }
}
