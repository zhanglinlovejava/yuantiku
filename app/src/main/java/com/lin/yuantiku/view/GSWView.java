package com.lin.yuantiku.view;

import com.lin.yuantiku.entity.ChooseItem;

/**
 * Created by zhanglin on 2017/12/22.
 */

public interface GSWView extends BaseView {
    void updateAnswer(ChooseItem chooseItem);

    void setCurrentItem(int index);
}
