package com.lin.yuantiku.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;

import java.util.ArrayList;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ChooseItemsAdapter extends BaseAdapter<ChooseItem> {
    public static final int READ_TYPE = 1;
    public static final int RESULT_TYPE = 2;
    public static final int CHOOSE_TYPE = 3;
    private int type = 1;

    public ChooseItemsAdapter(Context context, int type) {
        super(context, R.layout.item_choose, new ArrayList<ChooseItem>());
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChooseItem item) {
        if (type == READ_TYPE || type == CHOOSE_TYPE) {
            TextView tvBody = helper.getView(R.id.tvBody);
            tvBody.setVisibility(View.VISIBLE);
            tvBody.setText(item.body);
        }
        helper.setText(R.id.tvCat, item.cat_name);
        helper.setTextColor(R.id.tvCat, item.isChoosed | item.isAnswer ? Color.WHITE : Color.BLACK);
        helper.setBackgroundRes(R.id.tvCat, item.isChoosed | item.isAnswer ? R.drawable.choosed_bg : R.drawable.choosed_bg_trans);

    }
}
