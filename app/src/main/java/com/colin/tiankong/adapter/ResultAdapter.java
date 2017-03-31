package com.colin.tiankong.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colin.tiankong.R;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.utils.MyItemClickListener;

import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/30.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    public List<ResultEntity> list;
    private MyItemClickListener myItemClickListener;

    public ResultAdapter(List<ResultEntity> list, MyItemClickListener myItemClickListener) {
        this.list = list;
        this.myItemClickListener = myItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvCatName.setText(position + 1 + "");
        if (list.get(position).isAnswer) {
            holder.tvCatName.setBackgroundResource(R.drawable.choosed_bg);
            holder.tvCatName.setTextColor(Color.WHITE);
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


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCatName;
        public View rootView;

        public ViewHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.root);
            tvCatName = (TextView) view.findViewById(R.id.tvCat);
        }
    }
}
